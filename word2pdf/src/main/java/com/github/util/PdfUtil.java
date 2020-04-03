package com.github.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.LibraryLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author riverfang
 */

@Slf4j
public class PdfUtil {

  public static String COMMAND_OPEN = "Open";

  public static String COMMAND_SAVE_AS = "SaveAs";

  public static String COMMAND_CLOSE = "Close";

  public static int PDF_FILE_FORMAT = 17;

  static {
    // 设置jacob的dll文件所在目录的环境变量
    String dllName = LibraryLoader.getPreferredDLLName() + ".dll";
    String dllPath = PdfUtil.class.getResource("/" + dllName).getPath();
    System.setProperty(LibraryLoader.JACOB_DLL_PATH, dllPath);
  }

  public static byte[] wordToPdf(String wordContent) {
    return word2Pdf(wordContent);
  }

  public static byte[] wordToPdf(byte[] wordContent) {
    return word2Pdf(wordContent);
  }

  /**
   * word转PDF
   *
   * @param wordContent  word内容
   * @return 返回PDF二进制数据
   */
  private static byte[] word2Pdf(Object wordContent) {
    if(wordContent == null) {
      return null;
    }
    // 调用word的com组件
    ActiveXComponent activeXComponent = ActiveXComponent.createNewInstance("WORD.Application");
    Dispatch wordDoc = null;
    File wordFile = createFile(wordContent, "temp.doc");
    File pdfFile = createFile(null, "temp.pdf");
    try {
      log.info("当前正在使用的{}版本为20{}",
          Dispatch.get(activeXComponent, "Name").getString(),
          Dispatch.get(activeXComponent, "Version").getString());

      Objects.requireNonNull(activeXComponent, "当前系统未安装word软件，无法调用word com组件");
      // 设置为不可见
      activeXComponent.setProperty("Visible", Boolean.FALSE);
      Dispatch documents = activeXComponent.getProperty("Documents").toDispatch();
      // 打开Word页面, Open方法详细见: https://docs.microsoft.com/zh-cn/office/vba/api/word.documents.open
      wordDoc = Dispatch
          .invoke(documents, COMMAND_OPEN, Dispatch.Method, new Object[]{
              wordFile.getAbsolutePath(), false, true
          }, new int[3]).toDispatch();
      // 另存为
      Dispatch.call(wordDoc, COMMAND_SAVE_AS, pdfFile.getAbsolutePath(),
          PDF_FILE_FORMAT);
      try (FileInputStream fileInputStream = new FileInputStream(pdfFile)) {
        return IOUtils.toByteArray(fileInputStream);
      }
    } catch (Exception e) {
      log.error("word转pdf时发生异常, cause by:", e);
    } finally {
      log.info("PDF转换完成，正在进行垃圾清理和进程释放。");
      if (wordDoc != null) {
        Dispatch.call(wordDoc, COMMAND_CLOSE, Boolean.FALSE);
      }
      if (activeXComponent != null) {
        activeXComponent.invoke("Quit");
      }
      ComThread.Release();
      pdfFile.deleteOnExit();
      wordFile.deleteOnExit();
    }
    return null;
  }

  private static File createFile(Object content, String fileName) {
    File file = null;
    try {
      file = File.createTempFile("word2pdf", fileName);
      try (OutputStream outputStream = new FileOutputStream(file)) {
        if(content instanceof String) {
          String wordContent = (String) content;
          IOUtils.write(wordContent, outputStream);
        } else if(content instanceof byte[]) {
          byte[] bytes = (byte[]) content;
          IOUtils.write(bytes, outputStream);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return file;
  }
}
