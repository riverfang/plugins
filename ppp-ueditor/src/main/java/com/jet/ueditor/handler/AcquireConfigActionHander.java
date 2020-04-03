package com.jet.ueditor.handler;

import com.jet.ueditor.define.*;
import com.jet.ueditor.spi.SPIService;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取配置动作处理器
 *
 * @author fangjiang
 * @date 2019年09月19日 16:45
 */
@Slf4j
@SPIService(names = Constants.CONFIG)
public class AcquireConfigActionHander implements UEditorActionHandler {

    @Override
    public UEditorRestResponse handler(ActionCallback actionCallback, ActionTypeEnum actionTypeEnum, Configuration configuration) {
        return UEditorRestResponse.builder().state(Constants.SUCCESS).build();
    }
}
