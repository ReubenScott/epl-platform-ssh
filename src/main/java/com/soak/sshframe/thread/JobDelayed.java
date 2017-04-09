package com.soak.sshframe.thread;

/**
 * <p>
 * 需要进行调度工作的任务
 * </p>
 */
public class JobDelayed implements Runnable {

  private volatile Thread myThread;

  /**
   * 停止线程
   */
  public void stopMyThread() {
    Thread tmpThread = myThread;
    myThread = null;
    if (tmpThread != null) {
      tmpThread.interrupt();
    }
  }

  /**
   * 线程实现
   */
  public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        // 业务流程
      } catch (Exception e) {
        
      }
    }
  }
  
  public void run2() {
    if (myThread == null) {
      return; // stopped before started.
    }
    try {
      // all the run() method's code goes here
      // do some work
      Thread.yield(); // let another thread have some time perhaps to stop this one.
      if (Thread.currentThread().isInterrupted()) {
        throw new InterruptedException("Stopped by ifInterruptedStop()");
      }
      // do some more work
    } catch (Throwable t) {
      // log/handle all errors here
    }
  }

}
