package com.soak.sshframe.task;

import java.util.Date;

/**
 * 任务处理器接口
 * @author ke_xiong
 *
 */
public interface JobProcesser {

	/**	任务处理  **/
	public void processer();
	
	/** 设置跑批日期 **/
	public void setTaskTime(Date date);
	
}
