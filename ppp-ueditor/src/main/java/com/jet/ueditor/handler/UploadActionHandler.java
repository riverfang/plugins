package com.jet.ueditor.handler;

import com.jet.ueditor.define.*;
import com.jet.ueditor.spi.SPIService;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传动作处理器
 *
 * @author fangjiang
 * @date 2019年09月19日 13:05
 */
@SPIService(names = {Constants.UPLOAD_FILE, Constants.UPLOAD_IMAGE,
        Constants.UPLOAD_SCRAWL, Constants.UPLOAD_VIDEO, Constants.UPLOAD_SNAPSCREE_IMAGE})
public class UploadActionHandler implements UEditorActionHandler {

    @Override
    public UEditorRestResponse handler(ActionCallback actionCallback, ActionTypeEnum actionTypeEnum, Configuration configuration) {
        MultipartFile multipartFile =configuration.currentUploadFile(
                Objects.requireNonNull(Configuration.getInstance()).getUploadFieldName(actionTypeEnum));
        if(multipartFile == null) {
            return UEditorRestResponse.fail(AppInfo.NOT_MULTIPART_CONTENT);
        }
        // 判断文件是否超过大小
        Long maxSize = configuration.getConfigValue(actionTypeEnum, actionTypeEnum.getPrefix() + Constants.MAX_SIZE_SUFFIX);
        if(multipartFile.getSize() > maxSize) {
            return UEditorRestResponse.fail(AppInfo.MAX_SIZE);
        }
        // 调用回调函数
        UEditorRestResponse uEditorRestResponse = actionCallback.callback(multipartFile, actionTypeEnum);
        // 如果回调函数执行失败则快速短路
        if(Objects.equals(Constants.ERROR,uEditorRestResponse.getState())) {
           return uEditorRestResponse;
        }
        uEditorRestResponse.setState(Constants.SUCCESS);
        uEditorRestResponse.setTitle(multipartFile.getOriginalFilename());
        return uEditorRestResponse;
    }
}
