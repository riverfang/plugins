package com.jet.ueditor.handler;

import com.jet.ueditor.define.*;

/**
 * 百度富文本框动作处理器
 *
 * @author fangjiang
 * @date 2019年09月19日 13:02
 */
public interface UEditorActionHandler {

    /**
     * 事件动作处理
     *
     * @param actionCallback   动作回调
     * @param actionTypeEnum   动作类型枚举
     * @param configuration    配置
     * @return UEditorRestResponse
     */
     UEditorRestResponse handler(ActionCallback actionCallback, ActionTypeEnum actionTypeEnum,
         Configuration configuration);
}
