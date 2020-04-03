package com.github.util;

import java.io.Serializable;
import lombok.Data;

/**
 * @author fangjiang
 */
@Data
public class ObjectRestResponse<T> implements Serializable {

  private String message;

  private Integer code;

  private T data;

  private Boolean rel;

  public static <T> ObjectRestResponse<T> fail(String message) {
    ObjectRestResponse<T> restResponse = new ObjectRestResponse<>();
    restResponse.setRel(Boolean.FALSE);
    restResponse.setMessage(message);
    return restResponse;
  }

  public static <T> ObjectRestResponse<T> success(T data) {
    ObjectRestResponse<T> restResponse = new ObjectRestResponse<>();
    restResponse.setRel(Boolean.TRUE);
    restResponse.setData(data);
    restResponse.setCode(200);
    return restResponse;
  }
}
