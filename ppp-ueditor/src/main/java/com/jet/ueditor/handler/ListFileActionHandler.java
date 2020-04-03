package com.jet.ueditor.handler;

import com.jet.ueditor.define.*;
import com.jet.ueditor.spi.SPIService;

/**
 * 在线文件管理动作处理器
 *
 * @author fangjiang
 * @date 2019年09月19日 13:08
 */
@SPIService(names = {Constants.LIST_FILE, Constants.LIST_IMAGE})
public class ListFileActionHandler implements UEditorActionHandler {
    @Override
    public UEditorRestResponse handler(ActionCallback actionCallback, ActionTypeEnum actionTypeEnum, Configuration configuration) {
        return null;
    }
}
