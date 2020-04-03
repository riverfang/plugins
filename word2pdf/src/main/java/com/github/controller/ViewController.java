package com.github.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author fangjiang
 */
@Controller
public class ViewController {

  @GetMapping("/")
  public String index() {
    return "index";
  }

  @GetMapping("/view/{path}")
  public String view(@RequestParam(required = false, defaultValue = "views", value = "root") String root,
      @PathVariable String path) {
    return root + "/" + path;
  }
}
