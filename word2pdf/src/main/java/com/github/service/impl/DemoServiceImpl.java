package com.github.service.impl;

import com.github.service.IDemoService;
import com.github.util.PdfUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

/**
 * @author fangjiang
 */

@Service
@Slf4j
public class DemoServiceImpl implements IDemoService {

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] word2pdfWithDoc(String fileName) {
    try(InputStream inputStream = this.getClass().getResourceAsStream("/example/" + fileName)) {
      String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      return PdfUtil.wordToPdf(content);
    } catch (IOException e) {
     log.error("转换发生异常，cause by:", e);
    }
    return null;
  }

  @Override
  public byte[] word2pdfWithDocx(String fileName) {
    try(InputStream inputStream = this.getClass().getResourceAsStream("/example/" + fileName)) {
      byte[] content = IOUtils.toByteArray(inputStream);
      return PdfUtil.wordToPdf(content);
    } catch (IOException e) {
      log.error("转换发生异常，cause by:", e);
    }
    return null;
  }
}
