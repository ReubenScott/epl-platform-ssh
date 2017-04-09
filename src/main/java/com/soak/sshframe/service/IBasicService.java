package com.soak.sshframe.service;

import java.io.Serializable;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;



public interface IBasicService {

  
  
  // Excel 下载
  public Workbook createExcelBySQL(String title ,String sql, Object... params);
  

  public void deleteEntityBySID(Serializable sid);
  

  public boolean deleteAnnotatedEntity(Object annoEntity);
  
  
}
