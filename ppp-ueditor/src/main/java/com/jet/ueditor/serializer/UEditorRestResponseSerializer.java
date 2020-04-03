package com.jet.ueditor.serializer;

import static com.jet.ueditor.define.ActionTypeEnum.CONFIG;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.jet.ueditor.define.*;
import java.io.IOException;
import java.util.Objects;

/**
 *  UEditorRestResponse自定义序列化方式
 *
 * @author fangjiang
 * @date 2019年09月19日 15:05
 */
public class UEditorRestResponseSerializer extends JsonSerializer<UEditorRestResponse> {
    @Override
    public void serialize(UEditorRestResponse restResponse, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

       if(restResponse.getActionTypeEnum() == CONFIG) {
          jsonGenerator.writeObject(Configuration.getInstance());
          return;
       }

       // 判断执行状态
        jsonGenerator.writeStartObject();
       if(Objects.equals(restResponse.getState(), Constants.SUCCESS)) {
           switch (restResponse.getActionTypeEnum()) {
               case UPLOAD_FILE:
               case UPLOAD_SCRAWL:
               case UPLOAD_VIDEO:
               case UPLOAD_IMAGE:
               case UPLOAD_SNAPSCREE_IMAGE:
                   jsonGenerator.writeStringField("state", Constants.SUCCESS);
                   jsonGenerator.writeStringField("url", restResponse.getUrl());
                   jsonGenerator.writeStringField("title", restResponse.getTitle());
                   jsonGenerator.writeStringField("original", restResponse.getOriginal());
                   jsonGenerator.writeStringField("group", "");
                   break;
               case CATCH_IMAGE:
                   jsonGenerator.writeStringField("state", Constants.SUCCESS);
                   jsonGenerator.writeStringField("source", restResponse.getSource());
                   jsonGenerator.writeStringField("url", restResponse.getUrl());
                   break;
               case LIST_IMAGE:
               case LIST_FILE:
                   jsonGenerator.writeStringField("state", Constants.SUCCESS);
                   jsonGenerator.writeNumberField("start", restResponse.getStart());
                   jsonGenerator.writeNumberField("total", restResponse.getTotal());
                   jsonGenerator.writeArrayFieldStart("list");
                   if(CollectionUtils.isNotEmpty(restResponse.getList())) {
                      for(UEditorRestResponse restResponse2 : restResponse.getList()) {
                          jsonGenerator.writeStartObject();
                          jsonGenerator.writeStringField("state", Constants.SUCCESS);
                          jsonGenerator.writeStringField("url", restResponse2.getUrl());
                          jsonGenerator.writeStringField("title", restResponse2.getTitle());
                          jsonGenerator.writeStringField("original", restResponse2.getOriginal());
                          jsonGenerator.writeEndObject();
                      }
                   }
                   jsonGenerator.writeEndArray();
                   break;
               default:
                   jsonGenerator.writeStringField("state", AppInfo.getStateInfo(AppInfo.INVALID_ACTION));
           }

       } else {
           // NOTE：此处主要是为了适配前端(吐槽一下UEditor 代码真的是乱)
           jsonGenerator.writeStringField("state", restResponse.getInfo() == null ? "系统异常" : restResponse.getInfo());
       }
        jsonGenerator.writeEndObject();
    }
}
