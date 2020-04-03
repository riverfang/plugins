package com.jet.ueditor.define;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 请求工具类
 *
 * @author fangjiang
 * @date 2019年09月24日 17:11
 */
public class RequestUtil {

  /**
   * 获取请求中的文件对象
   *
   * @param request          请求对象
   * @param fileFieldName   上传文件字段名称
   * @return MultipartFile
   */
 public static MultipartFile getFile(HttpServletRequest request, String fileFieldName) {
   if(StringUtils.isEmpty(fileFieldName)) {
     return null;
   }
   MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
   return multipartRequest.getFile(fileFieldName);
 }
}
