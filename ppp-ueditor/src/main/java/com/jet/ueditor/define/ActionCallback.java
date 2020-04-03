package com.jet.ueditor.define;

import org.springframework.web.multipart.MultipartFile;

/**
 *  动作回调函数
 *
 * @author fangjiang
 * @date 2019年09月19日 15:18
 */
@FunctionalInterface
public interface ActionCallback {
    /**
     * 执行函数
     *
     * @param file            文件对象
     * @param actionTypeEnum 枚举对象
     * @
     */
    UEditorRestResponse callback(MultipartFile file, ActionTypeEnum actionTypeEnum);
}
