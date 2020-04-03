package com.github.controller;

import com.github.service.IDemoService;
import com.github.util.ObjectRestResponse;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fangjiang
 */

@RestController
@RequestMapping("/demo")
public class DemoController {

  private IDemoService demoService;

  @Autowired
  public DemoController(IDemoService demoService) {
    this.demoService = demoService;
  }

  @GetMapping("/word2pdf")
  public ObjectRestResponse<byte[]> word2pdf(String fileName, String type) {
    if(Objects.equals(type, "doc")) {
      return ObjectRestResponse.success(this.demoService.word2pdfWithDoc(fileName + ".doc"));
    } else if(Objects.equals(type, "docx")) {
      return ObjectRestResponse.success(this.demoService.word2pdfWithDocx(fileName + ".docx"));
    } else {
      return ObjectRestResponse.fail("类型[" + type + "]无法处理。");
    }
  }
}
