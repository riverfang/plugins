package com.jet.ueditor.define;

import java.util.Arrays;
import java.util.Objects;

/**
 * 动作类型枚举类
 *
 * @author fangjiang
 * @date 2019年09月19日 12:34
 */
public enum ActionTypeEnum{
    // 请求配置文件动作
    CONFIG(Constants.CONFIG, "配置文件", null),
    // 上传图片动作
    UPLOAD_IMAGE(Constants.UPLOAD_IMAGE, "上传图片", "image"),
    // 上传涂鸦动作
    UPLOAD_SCRAWL(Constants.UPLOAD_SCRAWL, "上传涂鸦", "scrawl"),
    // 上传视频动作
    UPLOAD_VIDEO(Constants.UPLOAD_VIDEO, "上传视频", "video"),
    // 上传文件动作
    UPLOAD_FILE(Constants.LIST_FILE, "上传文件", "file"),
    // 上传截图动作
    UPLOAD_SNAPSCREE_IMAGE(Constants.UPLOAD_SNAPSCREE_IMAGE, "上传截图", "snapscreen"),
    // 捕获图片动作
    CATCH_IMAGE(Constants.CATCH_IMAGE, "捕获图片", "catcher"),
    // 在线文件管理动作
    LIST_FILE(Constants.LIST_FILE, "文件列表", "fileManager"),
    // 在线图片管理动作
    LIST_IMAGE(Constants.LIST_IMAGE, "图片管理", "imageManager"),
    // 未知的动作
    UNKNOWN(Constants.UNKNOWN, "未知动作", null);

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    // 动作名称
    private String actionName;

    // 动作code
    private String actionCode;

    // 配置文件项前缀
    private String prefix;

    ActionTypeEnum(String actionCode, String actionName, String prefix) {
       this.actionName = actionName;
       this.actionCode = actionCode;
       this.prefix = prefix;
    }

    /**
     * 通过动作code获取动作类型枚举
     * NOTE: 不要通过<code>ActionTypeEnum.valueOf(xxx)</code>获取
     *
     * @param actionCode  动作名称
     * @return actionName对应的动作类型枚举，如果未找到对应的则返回未知动作类型枚举
     */
    public static ActionTypeEnum valueOf2(String actionCode) {
       return Arrays.stream(ActionTypeEnum.values())
                      .filter(action -> Objects.equals(actionCode, action.actionCode))
                      .findFirst()
                      .orElseGet(() -> ActionTypeEnum.UNKNOWN);
    }
}
