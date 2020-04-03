package com.jet.ueditor.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceConfigurationError;


/**
 *  一个简单的服务提供者装载设施
 * <p>
 *     建立在{@link java.util.ServiceLoader}之上 去除了惰性迭代
 *     服务提供者暴露文件配置和原文件一致
 *     NOTE: 所有接口或者抽象类的实现类必须添加{@link SPIService}注解 否则会被忽略
 * </p>
 * @author fangjiang
 * @date 2019年09月20日 16:07
 */

public final class ServiceLoader<S> {
    // 服务发现前缀路径
    private static final String PREFIX = "META-INF/services/";

    // 需要被加载的类或者接口
    private Class<S> service;

    // 用于定位、装载class和实例化服务提供者的类装载器
    private ClassLoader loader;

    // 缓存服务提供者
    private LinkedHashMap<String, List<S>> providers = new LinkedHashMap<>();

    private ServiceLoader(Class<S> service, ClassLoader classLoader) {
        this.service = service;
        this.loader = classLoader;
        this.init();
    }

    private void init() {
        try {
            String fullName = PREFIX + service.getName();
            Enumeration<URL> configs = loader == null ? ClassLoader.getSystemResources(fullName) : loader.getResources(fullName);
            while (configs.hasMoreElements()) {
              this.parse(configs.nextElement());
            }
        } catch (IOException x) {
            ServiceLoader.fail(service, "错误的定位配置文件", x);
        }
    }

    /**
     * 读取单行进行解析
     *
     * @param line  解析文件中的一行
     */
    private void parseLine(String line) {
        line = line.trim();
        if(line.indexOf("##") == 0 || Objects.equals(line, "")) {
          return;
        }
        this.instantiationAndLoadService(line);
    }

    /**
     * 根据指定
     *
     * @param url      服务配置文件资源符
     */
    private void parse(URL url) {
        try(InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null) {
                this.parseLine(line);
            }
        } catch (IOException e) {
            fail(service, "服务配置文件读取失败！", e);
        }
    }

    /**
     * 实例化并装载服务
     *
     * @param classFullPath  类的全路径
     */
    private void instantiationAndLoadService(String classFullPath) {
        Class <?> clazz = null;
        try {
            clazz = Class.forName(classFullPath, false, loader);
        } catch (ClassNotFoundException e) {
            ServiceLoader.fail(service, "服务提供者: " + classFullPath + " 没有找到");
        }
        // 判断c是否是service的派生类
        if (!service.isAssignableFrom(clazz)) {
            ServiceLoader.fail(service, "服务提供者: " + classFullPath  + " 不是'" + service.getName() + "'接口的派生类");
        }
        SPIService spiService = clazz.getAnnotation(SPIService.class);
        // 如果没有添加@SPIService注解直接忽略
        if(spiService == null) {
            return;
        }


        try {
            S s = service.cast(clazz.newInstance());
            Arrays.asList(spiService.names()).forEach(serviceName -> {
                if(this.providers.containsKey(serviceName)) {
                    this.providers.get(serviceName).add(s);
                } else {
                    List<S> services = new ArrayList <>();
                    services.add(s);
                    this.providers.put(serviceName, services);
                }
            });
        } catch (InstantiationException | IllegalAccessException e) {
            ServiceLoader.fail(service, "服务提供者: " + classFullPath + "不可以被实例化", e);
        }
    }

    /**
     * 通过给定的服务类型和类装载器创建一个新的服务装载器
     * @param  service   代表服务提供者的一个接口或者抽象类
     * @param  loader   资源装载器
     * @return 一个新的服务装载器
     */
    public static <S> ServiceLoader<S> load(Class<S> service, ClassLoader loader) {
        return new ServiceLoader<>(service, loader);
    }

    /**
     * 通过指定的服务服务类型创建一个服务装载器
     * @param  service  代表服务提供者的一个接口或者抽象类
     * @return 一个新的服务装载器
     */
    public static <S> ServiceLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ServiceLoader.load(service, cl);
    }

    /**
     * 获取默认的服务提供者
     *
     * @param <S> 泛型参数
     * @return 服务提供者
     */
    public static <S> S defaultServiceProvider(Class<S> service) {
       Objects.requireNonNull(service, "参数service不能为null");
       SPI spi = service.getAnnotation(SPI.class);
       Objects.requireNonNull(spi, "接口:" + service.getSimpleName() + "  没有设置默认的实现类");
       return getService(service, spi.value());
    }


    /**
     * 通过serviceName获取服务提供者
     *
     * @param clazz         服务对应的类型
     * @param serviceName  服务名称
     * @param <S>          泛型参数
     * @return 服务实例对象
     */
    public static <S> S getService(Class<S> clazz, String serviceName) {
        return load(clazz).providers.get(serviceName).get(0);
    }

    /**
     * 通过serviceName获取服务提供者
     *
     * @param clazz         服务对应的类型
     * @param serviceName  服务名称
     * @param <S>          泛型参数
     * @return 获取所有服务实例对象
     */
    public static <S> List<S> getServices(Class<S> clazz, String serviceName) {
        return load(clazz).providers.get(serviceName);
    }

    /**
     * 通过serviceName获取主要服务提供者(依据{@link SPIService}注解)
     *
     * @param clazz         服务对应的类型
     * @param serviceName  服务名称
     * @param <S>          泛型参数
     * @return 服务实例对象
     */
    public static <S> S getPrimaryService(Class<S> clazz, String serviceName) {
        List<S> services = load(clazz).providers.get(serviceName);
        if(services == null) {
            ServiceLoader.fail(clazz, "没有找到服务提供者:" + serviceName);
        }
        if(services.size() == 1) {
            return services.get(0);
        }
        // 根据order排序
        services.sort(Comparator.comparingInt((S o) -> Optional.ofNullable(
                o.getClass().getAnnotation(SPIService.class)).map(SPIService::order).orElse(999)));
        return services.get(0);
    }

    private static void fail(Class service, String msg, Throwable cause) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg, cause);
    }

    private static void fail(Class service, String msg) throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class service, URL u, int line, String msg) {
        fail(service, u + ":" + line + ": " + msg);
    }

    /**
     * 返回一个描述服务的字符串
     *
     * @return  一个具有描述性的字符串
     */
    @Override
    public String toString() {
        return "java.util.ServiceLoader[" + service.getName() + "]";
    }
}
