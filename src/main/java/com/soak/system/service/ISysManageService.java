package com.soak.system.service;

import java.util.List;

import com.soak.sshframe.service.IBasicService;

public interface ISysManageService extends IBasicService {
  
  
  public List<?> getMenuListByUID(String userid);
  
 
}
