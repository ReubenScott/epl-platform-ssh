package com.soak.sshframe.thread;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * 任务调度管理器
 * </p>
 * 
 * 2010-5-28 下午11:30:20
 */
public class ThreadManager {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static ThreadManager instance;

  private List<Thread> threadList;

  /**
   * 使用调度管理器构造
   * 
   * @param manager
   */
  private ThreadManager() {
    threadList = new ArrayList<Thread>();
  }

  public static ThreadManager getInstance() {
    if (instance == null) {
      synchronized (ThreadManager.class) {
        if (instance == null) {
          instance = new ThreadManager();
        }
      }
    }
    return instance;
  }

  /**
   * <p>
   * 启动该调度器
   * </p>
   */
  public void start() {
    boolean isFinished = false;
    while (!isFinished) {
      isFinished = true;
      for (Thread thread : threadList) {
        if (State.TERMINATED == thread.getState()) {
          threadList.remove(thread);
        }
        if (State.TERMINATED != thread.getState()) {
          isFinished = false;
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          logger.error("exception:", e);
        }
      }
    }

  }

  /**
   * 执行任务调度工作
   */
  public long push(Thread thread) {
    threadList.add(thread);
    return thread.getId();
  }

  /**
   * <p>
   * 强制停止该调度器
   * </p>
   */
  public void stop(String threadName) {
    for (Thread thread : threadList) {
      if(thread.getName().equals(threadName)){
        try {
          thread.interrupt();
          thread.join();
        } catch (InterruptedException e) {
          e.printStackTrace();
          System.out.println("333 "+  thread.getId() + "    " + thread.getName() + " "  + thread.isInterrupted() );
        }
      }
    }
    System.out.println("thread size : " + threadList.size());
  }
}