package com.soak.sshframe.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionBindingListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.HashMap;

public class SessionListener implements HttpSessionActivationListener, HttpSessionBindingListener, HttpSessionAttributeListener, HttpSessionListener {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  ServletContext context;

  private static int sessionCounter = 0;

  HashMap userlist;

  String url = "jdbc:mysql://localhost:3306/mylinux";

  String name = "root";

  String password = "root";// "huazisoft0712";

  Connection conn = null;

  public Connection GetDBConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    try {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      conn =  DriverManager.getConnection(url, name, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  private void logout(String message) {
    PrintWriter out = null;
    try {
      out = new PrintWriter(new FileOutputStream("c:\\session.txt", true));
      out.println(new java.util.Date() + "::Form MySessionListener: " + message);
      out.close();
    } catch (Exception e) {
      out.close();
      e.printStackTrace();
    }
  }

  /**
   * session 创建 处理 HttpSessionListener
   */
  public void sessionCreated(HttpSessionEvent hse) {
    sessionCounter++;
    // logout("sessionCreated('" + event.getSession().getId() + "'),目前有"+ users
    // + "个用户");
    logger.debug("ApplicationListener  :  sessionCreated   ....................");
  }

  /**
   * session 超时 处理 HttpSessionListener
   */
  public void sessionDestroyed(HttpSessionEvent hse) {
    sessionCounter--;
    // logout("sessionDestroyed('" + event.getSession().getId() + "'),目前有"+
    // users + "个用户");

    // HttpSession session = hse.getSession();

    logger.debug("ApplicationListener  :  sessionDestroyed   ....................");

  }

  // HttpSessionActivationListener
  public void sessionDidActivate(HttpSessionEvent se) {
    // logout("sessionDidActivate(" + se.getSession().getId() + ")");
  }

  public void sessionWillPassivate(HttpSessionEvent se) {
    // logout("sessionWillPassivate(" + se.getSession().getId() + ")");
  }

  // HttpSessionActivationListener
  // HttpSessionBindingListener
  public void valueBound(HttpSessionBindingEvent event) {
    context = event.getSession().getServletContext();
    userlist = (HashMap) context.getAttribute("userlist");
    if (userlist == null) {
      userlist = new HashMap();
    } // String userID=user.getUserID();
    userlist.put(event.getSession().getId(), event.getSession());
    context.setAttribute("userlist", userlist);
    System.out.println(userlist.size());
    logout("valueBound(" + event.getSession().getId() + event.getValue() + ")");
  }

  public void valueUnbound(HttpSessionBindingEvent event) {

    context = event.getSession().getServletContext();
    userlist = (HashMap) context.getAttribute("userlist");
    String user = (String) event.getValue();
    if (user == null) {
      userlist.remove(event.getSession().getId());
      context.setAttribute("userlist", userlist);
    }
    System.out.println(userlist.size()); // 把对应的会员在线状态变成离线
    logout("valueUnbound(" + event.getSession().getId() + event.getValue() + ")");
  }

  // HttpSessionAttributeListener
  public void attributeAdded(HttpSessionBindingEvent event) {
    // logout("attributeAdded('" + event.getSession().getId() + "', '"+
    // event.getName() + "', '" + event.getValue() + "')");
  }

  public void attributeRemoved(HttpSessionBindingEvent event) {
    // 更改离线会员在数据库中的在线状态。
    if (event.getName() == "member") {
      try {
        conn = GetDBConnection();
        Statement sts = null;
        String str = "update memberlogin set state=0 where memberid='" + event.getValue() + "'";
        sts = conn.createStatement();
        sts.execute(str);
        try {
          sts.close();
        } catch (Exception e) {
        }
      } catch (InstantiationException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    // logout("attributeRemoved('" + event.getSession().getId() + "', '"+
    // event.getName() + "', '" + event.getValue() + "')");
  }

  public void attributeReplaced(HttpSessionBindingEvent se) {
    // logout("attributeReplaced('" + se.getSession().getId() + ",'"+
    // se.getName() + "','" + se.getValue() + "')");
  }// HttpSessionAttributeListener

  public static int getCount() {
    return sessionCounter;
  }
}