package com.github.service;

/**
 * @author fangjiang
 */

public interface IDemoService {

  /**
   * 适合.doc, word模板导出转PDF
   *
   * @param fileName 文件名称
   * @return 返回PDF二进制数据
   */
  byte[] word2pdfWithDoc(String fileName);

  /**
   * 适合docx
   *
   * @param fileName 文件名称
   * @return 返回PDF二进制数据
   */
  byte[] word2pdfWithDocx(String fileName);
}
