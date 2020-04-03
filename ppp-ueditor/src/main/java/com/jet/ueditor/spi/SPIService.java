package com.jet.ueditor.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为SPI服务
 *
 * @author fangjiang
 * @date 2019年09月20日 8:20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPIService {
    // 优先级
    int order() default 999;
    // 服务名(用于发现服务的key)
    String[] names();
}
