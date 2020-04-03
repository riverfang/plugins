package com.jet.ueditor.define;

/**
 * 动作类型
 *
 * @author fangjiang
 * @date 2019年09月20日 8:31
 */
public interface Constants {
    /******** 动作相关 ******/
    String CONFIG = "config";
    String UPLOAD_IMAGE = "uploadimage";
    String UPLOAD_SCRAWL = "uploadscrawl";
    String UPLOAD_VIDEO = "uploadvideo";
    String UPLOAD_FILE = "uploadfile";
    String UPLOAD_SNAPSCREE_IMAGE = "uploadsnapscreen";
    String CATCH_IMAGE = "catchimage";
    String LIST_FILE = "listfile";
    String LIST_IMAGE = "listimage";
    String UNKNOWN = "unknow";

    // 动作code后缀
    String ACTION_NAME_SUFFIX = "ActionName";
    // 上传文件字段名后缀
    String UPLOAD_FIELD_NAME_SUFFIX = "FieldName";
    // 文件最大限制配置后缀
    String MAX_SIZE_SUFFIX = "MaxSize";
    // 请求动作参数
    String ACTION_PARAM = "action";

    String SUCCESS = "SUCCESS";
    String ERROR = "ERROR";
}
