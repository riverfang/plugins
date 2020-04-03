package com.jet.ueditor.handler;

import com.jet.ueditor.define.*;
import com.jet.ueditor.spi.SPIService;

/**
 * 图片捕获动作处理器
 *
 * @author fangjiang
 * @date 2019年09月19日 13:09
 */
@SPIService(names = Constants.CATCH_IMAGE)
public class CatchImageActionHandler implements UEditorActionHandler {
    @Override
    public UEditorRestResponse handler(ActionCallback actionCallback, ActionTypeEnum actionTypeEnum, Configuration configuration) {
        return null;
    }
}
