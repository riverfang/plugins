package com.jet.ueditor.define;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jet.ueditor.serializer.UEditorRestResponseSerializer;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 百度富文本框rest响应
 *
 * @author fangjiang
 * @date 2019年09月19日 13:47
 */
@Data
@JsonSerialize(using = UEditorRestResponseSerializer.class)
@Builder
public class UEditorRestResponse implements Serializable {
    // 事件动作类型(作为序列化的依据)
    @JsonIgnore
    private ActionTypeEnum actionTypeEnum;
    // 执行状态
    private String state;
    @JsonManagedReference
    List<UEditorRestResponse> list;
    /*******************文件信息相关 start****************************/
    // 错误信息
    private String info = "未知的错误";
    // 文件title
    private String title;
    // 文件url
    private String url;
    // 文件original
    private String original;
    /*******************文件信息相关 end****************************/
    // 起始位置
    private int start;
    // 总数
    private int total;
    // 远程图片抓取的url资源地址
    private String source;

    /**
     * 操作失败
     *
     * @param errorCode  错误码
     * @return 百度富文本框rest响应
     */
    public static UEditorRestResponse fail(int errorCode) {
        return fail(AppInfo.getStateInfo(errorCode));
    }

    /**
     * 操作失败
     *
     * @param message  提示信息
     * @return 百度富文本框rest响应
     */
    public static UEditorRestResponse fail(String message) {
        return UEditorRestResponse.builder().state(Constants.ERROR).info(message).build();
    }
}
