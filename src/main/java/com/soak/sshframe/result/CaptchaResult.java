package com.soak.sshframe.result;

import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片验证码
 */
public class CaptchaResult extends StrutsResultSupport {

  private static final long serialVersionUID = 1L;

  private int wordLength;

  private int imageWidth;

  private int imageHeight;

  public int getWordLength() {
    return wordLength;
  }

  public void setWordLength(int wordLength) {
    this.wordLength = wordLength;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

  /**
   * // 生成随机数
   */
  public String getRandomWord(int wordLength) {// 生成随机数

    StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    StringBuffer sb = new StringBuffer();

    Random r = new Random();

    int range = buffer.length();

    for (int i = 0; i < wordLength; i++) {

      sb.append(buffer.charAt(r.nextInt(range)));

    }

    return sb.toString();

  }

  /**
   * 生成图片验证码
   * 
   * @param word
   * @param imageWidth
   * @param imageHeight
   * @return
   */
  public BufferedImage getCaptchaImage(String word, int imageWidth, int imageHeight) {

    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();

    g.fillRect(0, 0, imageWidth, imageHeight); // 画一个矩形

    g.setFont(new java.awt.Font("宋体", java.awt.Font.BOLD, 18));// 设置字体

    g.setColor(Color.gray);// 设置背景颜色

    g.drawString(word, 0, 20);// 写入所给字符串

    g.dispose();// 释放此图形的上下文以及它使用的所有系统资源。调用 dispose 之后，就不能再使用 Graphics 对象。

    return image;// 返回 BufferedImage 对象

  }

  
  
  // overwrite
  protected void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
    ActionContext actionContext = invocation.getInvocationContext();
    HttpServletRequest request = (HttpServletRequest) actionContext.get(HTTP_REQUEST);
    HttpServletResponse response = (HttpServletResponse) actionContext.get(HTTP_RESPONSE);
    response.setContentType("image/jpeg");

    String randomWord = (String)request.getAttribute("randomWord");
    if(randomWord==null){
      randomWord = this.getRandomWord(wordLength);
    }

    // Cookie newCookie = new Cookie("CookieName", word);
    // response.addCookie(newCookie);

    BufferedImage bimg = this.getCaptchaImage(randomWord, imageWidth, imageHeight);
    // 输入图片方式二
     ImageIO.write(bimg, "jpeg", response.getOutputStream());
    

//    // 输入图片方式一 encode:
//    OutputStream os = response.getOutputStream();
//    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
//    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimg);
//    param.setQuality(1.0f, false);
//    encoder.setJPEGEncodeParam(param);
//    encoder.encode(bimg);

//    bimg.flush();
//    os.flush();
//    os.close();

  }

}