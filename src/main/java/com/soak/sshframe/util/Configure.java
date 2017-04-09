package com.soak.sshframe.util;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 12-6-20
 * Time: 下午2:49
 * To change this template use File | Settings | File Templates.
 */
public class Configure {
    private static Log log = LogFactory.getLog(Configure.class);

    private static Configure instance = new Configure();
    private ResourceBundle config ;

    private Configure() {
        config = ResourceBundle.getBundle("config"); 
    }

    public static Configure getInstance() {
        return instance;
    }

    public String getValue (String key){
      return config.getString(key);
    }
}
