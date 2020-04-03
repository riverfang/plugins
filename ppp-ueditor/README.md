# 百度富文本框部署
## 前端部署
- 将UEditor静态文件放到/static目录下， 如果没有static目录，
新建一个static目录如下图所示：<br/>
![](http://riverfang.oss-cn-hangzhou.aliyuncs.com/ueditor/static-ueditor.png)
- 将vue封装的UEditor组件复制到如下目录<br/>
![](http://riverfang.oss-cn-hangzhou.aliyuncs.com/ueditor/ueditor-component.png)
- 将vue混入文件ueditorWrapMixin.js放入如下目录<br/>
![](http://riverfang.oss-cn-hangzhou.aliyuncs.com/ueditor/ueditor-mxins.png)
- 在页面中使用案例如下

```
// 在vue页面中使用
// ...省略若干前缀代码
<vue-ueditor-wrap v-model="appData.issueContent" :config="ueditorConfig"/>
// ... 省略若干后缀代码

// 引入方式
// ...
import ueditorWrapMixin from '@/mixins/ueditorWrapMixin';
export default {
  mixins: [ueditorWrapMixin()]
  // ...
  };
```
- 修改文件上传请求路径，在VueUeditorWrap组件config文件夹的index.js修改serverUrl的地址
其他UEditor的配置参数可以参考百度富文本框官网说明文档。

## 后端部署 ##
- 将ppp-ueditor模块项目复制，如果jar在私服忽略该操作
- 如果模块需要更改报名，请同步更改/META-INF/services文件的服务发现配置文件, 默认配置如下

```
## 获取配置事件处理器
com.jet.ueditor.handler.AcquireConfigActionHander
## 捕获远程图片事件处理器
com.jet.ueditor.handler.CatchImageActionHandler
## 文件管理事件处理器
com.jet.ueditor.handler.ListFileActionHandler
## 上传文件事件处理器
com.jet.ueditor.handler.UploadActionHandler
```
- 使用案例代码如下

```
/**
   * 百度富文本框文件上传
   *
   * @param request 请求对象
   * @return UEditorRestResponse
   */
  @ApiOperation("百度富文本框文件上传")
  @RequestMapping(value = "/ueditor", method = {RequestMethod.GET, RequestMethod.POST})
  public UEditorRestResponse ueditor(HttpServletRequest request, HttpServletResponse response) {
    return UEditorAction.invoker((file, actionTypeEnum) -> {
      try {
        ObjectRestResponse<FileInfoDTO> restResponse = fileApi.upload("mechanical",file);
        if (restResponse.isRel() || Objects.equals("文件已存在", restResponse.getMessage())) {
          return UEditorRestResponse.builder().url(Constants.FILE_CONFIG + restResponse.getData().getPath()).build();
        } else {
          logger.error("文件存储至文件中心失败, cause by:" + restResponse.getMessage());
          return UEditorRestResponse.fail(restResponse.getMessage());
        }
      } catch (Exception e) {
        logger.error("文件存储至文件中心失败", e);
        return UEditorRestResponse.fail("文件存储文件中心失败");
      }
    }, request);
  }
```
- 重写默认的事件动作处理器
// 描述略
该需求请直接更改对应动作类型事件处理实现类
