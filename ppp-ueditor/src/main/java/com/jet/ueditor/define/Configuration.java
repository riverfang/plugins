package com.jet.ueditor.define;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *  配置文件
 *
 * @author fangjiang
 * @date 2019年09月19日 16:49
 */
@Data
@Slf4j
public class Configuration {
    @ConfigGroup(prefix = "image",value = ActionTypeEnum.UPLOAD_IMAGE)
    private String imageActionName;
    @ConfigGroup(prefix = "image", value = {ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private String imageFieldName;
    @ConfigGroup(prefix = "image", value = {ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private long imageMaxSize;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private List<String> imageAllowFiles;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private boolean imageCompressEnable;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private long imageCompressBorder;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private String imageInsertAlign;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private String imageUrlPrefix;
    @ConfigGroup(prefix = "image", value = { ActionTypeEnum.UPLOAD_IMAGE, ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE})
    private String imagePathFormat;
    
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private String scrawlActionName;
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private String scrawlFieldName;
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private String scrawlPathFormat;
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private long scrawlMaxSize;
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private String scrawlUrlPrefix;
    @ConfigGroup(prefix = "scraw", value = ActionTypeEnum.UPLOAD_SCRAWL)
    private String scrawlInsertAlign;
    
    @ConfigGroup(prefix = "snapscreen", value = ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE)
    private String snapscreenActionName;
    @ConfigGroup(prefix = "snapscreen", value = ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE)
    private String snapscreenPathFormat;
    @ConfigGroup(prefix = "snapscreen", value = ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE)
    private String snapscreenUrlPrefix;
    @ConfigGroup(prefix = "snapscreen", value = ActionTypeEnum.UPLOAD_SNAPSCREE_IMAGE)
    private String snapscreenInsertAlign;
    
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private List<String> catcherLocalDomain;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private String catcherActionName;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private String catcherFieldName;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private String catcherPathFormat;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private String catcherUrlPrefix;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private long catcherMaxSize;
    @ConfigGroup(prefix = "catcher", value = ActionTypeEnum.CATCH_IMAGE)
    private List<String> catcherAllowFiles;
    
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private String videoActionName;
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private String videoFieldName;
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private String videoPathFormat;
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private String videoUrlPrefix;
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private long videoMaxSize;
    @ConfigGroup(prefix = "video", value = ActionTypeEnum.UPLOAD_VIDEO)
    private List<String> videoAllowFiles;

    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private String fileActionName;
    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private String fileFieldName;
    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private String filePathFormat;
    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private String fileUrlPrefix;
    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private long fileMaxSize;
    @ConfigGroup(prefix = "file", value = ActionTypeEnum.UPLOAD_FILE)
    private List<String> fileAllowFiles;
    
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private String imageManagerActionName;
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private String imageManagerListPath;
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private long imageManagerListSize;
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private String imageManagerUrlPrefix;
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private String imageManagerInsertAlign;
    @ConfigGroup(prefix = "imageManager", value = ActionTypeEnum.LIST_IMAGE)
    private List<String> imageManagerAllowFiles;
    
    @ConfigGroup(prefix = "fileManager", value = ActionTypeEnum.LIST_FILE)
    private String fileManagerActionName;
    @ConfigGroup(prefix = "fileManager", value = ActionTypeEnum.LIST_FILE)
    private String fileManagerListPath;
    @ConfigGroup(prefix = "fileManager", value = ActionTypeEnum.LIST_FILE)
    private String fileManagerUrlPrefix;
    @ConfigGroup(prefix = "fileManager", value = ActionTypeEnum.LIST_FILE)
    private long fileManagerListSize;
    @ConfigGroup(prefix = "fileManager", value = ActionTypeEnum.LIST_FILE)
    private List<String> fileManagerAllowFiles;
    @JsonIgnore
    private static Configuration config = null;
    @JsonIgnore
    private static Map<ActionTypeEnum, Map<String,Object>> actionConfigMapping = new HashMap<>();
    @JsonIgnore
    private HttpServletRequest request;

    public static Configuration getInstance() {
        if(config != null) {
            return config;
        }
        synchronized (Configuration.class) {
            if(config == null) {
                try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.json")) {
                    String configJsonString = IOUtils.toString(inputStream).replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
                    ObjectMapper objectMapper = new ObjectMapper();
                    config = objectMapper.readerFor(Configuration.class).readValue(configJsonString);
                    initActionConfigMapping();
                } catch (IOException e) {
                    log.error("获取配置文件发生异常", e);
                    return null;
                }
            }
            return config;
        }
    }

    private static void initActionConfigMapping() {
        Arrays.asList(config.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            ConfigGroup configGroupAnnotation = field.getAnnotation(ConfigGroup.class);
            if(configGroupAnnotation != null) {
                Arrays.asList(configGroupAnnotation.value()).forEach(actionTypeEnum -> {
                   try {
                       String filedName = field.getName();
                       if(!filedName.startsWith(configGroupAnnotation.prefix())) {
                           filedName = filedName.replace(configGroupAnnotation.prefix(), actionTypeEnum.getPrefix());
                       }
                       if (actionConfigMapping.containsKey(actionTypeEnum)) {
                           actionConfigMapping.get(actionTypeEnum).put(filedName, field.get(config));
                       } else {
                           Map<String, Object> cfg = Maps.newHashMap();
                           cfg.put(filedName, field.get(config));
                           actionConfigMapping.put(actionTypeEnum, cfg);
                       }
                   } catch (IllegalAccessException e) {
                       // ignore
                   }
                });
            }
        });
    }

    /**
     * 获取动作枚举类型
     *
     * @param actionCode  动作code
     * @return ActionTypeEnum
     */
    public ActionTypeEnum getActionTypeEnum(String actionCode) {
       if(Objects.equals(actionCode, Constants.CONFIG)) {
           return ActionTypeEnum.CONFIG;
       }
       for(Map.Entry<ActionTypeEnum, Map<String, Object>> entry : actionConfigMapping.entrySet()) {
           for(Object o : entry.getValue().values()) {
               if(o instanceof String && Objects.equals(actionCode, o)) {
                   return entry.getKey();
               }
           }
       }
       return ActionTypeEnum.UNKNOWN;
    }

    /**
     * 获取上传字段名称
     *
     * @param actionTypeEnum  事件动作类枚举
     * @return 获取上传字段名称
     */
    public String getUploadFieldName(ActionTypeEnum actionTypeEnum) {
       return Optional.ofNullable(actionConfigMapping.get(actionTypeEnum))
               .map(value -> value.get(actionTypeEnum.getPrefix() + Constants.UPLOAD_FIELD_NAME_SUFFIX).toString())
               .orElse("");
    }

    public <T> T getConfigValue(ActionTypeEnum actionTypeEnum, String key) {
        try {
            return (T)actionConfigMapping.get(actionTypeEnum).get(key);
        } catch (Exception e) {
            log.warn("获取配置项发生错误", e);
            return null;
        }
    }

    /**
     * 获取当前上传文件
     *
     * @param fileFieldName   文件字段名称
     * @return MultipartFile
     */
    public MultipartFile currentUploadFile(String fileFieldName) {
      return RequestUtil.getFile(request, fileFieldName);
    }
}
