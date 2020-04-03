package com.jet.ueditor;

import com.google.common.collect.Maps;
import com.jet.ueditor.define.*;
import com.jet.ueditor.handler.UEditorActionHandler;
import com.jet.ueditor.spi.ServiceLoader;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 百度富文本框动作
 *
 * @author fangjiang
 * @date 2019年09月19日 12:31
 */
@Slf4j
public class UEditorAction {

    /**
     * 动作执行
     * @param actionCallback   事件动作回调函数
     * @param request           请求对象
     */
    public static UEditorRestResponse invoker(ActionCallback actionCallback, HttpServletRequest request) {
      Map<String, String> requestParams = requestParams(request);
      Configuration configuration = Configuration.getInstance();
      ActionTypeEnum actionTypeEnum = Objects.requireNonNull(configuration).getActionTypeEnum(requestParams.get(Constants.ACTION_PARAM));
      if(ActionTypeEnum.UNKNOWN == actionTypeEnum) {
          return UEditorRestResponse.fail(AppInfo.INVALID_ACTION);
      }
      UEditorActionHandler actionHandler = ServiceLoader.getPrimaryService(UEditorActionHandler.class, actionTypeEnum.getActionCode());
      if(actionHandler == null) {
        log.error("百度富文本动作[{}]没有对应的事件处理器", actionTypeEnum.getActionCode());
        return UEditorRestResponse.fail("事件动作没有找到对应的事件处理器");
      }
      configuration.setRequest(request);
      // 使用动作处理器处理动作事件
      UEditorRestResponse restResponse = actionHandler.handler(actionCallback, actionTypeEnum, configuration);
      if(restResponse != null) {
          // 设置响应的动作类型
          restResponse.setActionTypeEnum(actionTypeEnum);
      }
      return restResponse == null ? UEditorRestResponse.fail("服务内部发生错误") : restResponse;
    }

    /**
     * 获取请求参数
     *
     * @param request   请求对象
     * @return 返回请求参数
     */
    private static Map<String, String> requestParams(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        request.getParameterMap().forEach((key ,value) -> {
            params.put(key, value != null && value.length > 0 ? value[0] : null);
        });
        return params;
    }
}
