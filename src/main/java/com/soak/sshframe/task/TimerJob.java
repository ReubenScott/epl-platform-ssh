package com.soak.sshframe.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TimerJob {

	private List <JobProcesser>jobList;
	
	public List<JobProcesser> getJobList() {
		return jobList;
	}

	public void setJobList(List<JobProcesser> jobList) {
		this.jobList = jobList;
	}

	/**
	 * 执行调度任务列表
	 */
	public void work(){
		Date date=new Date();
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -1);//日期减1，导出前一天报表
		date=c.getTime();
		for(JobProcesser job:jobList){
			job.setTaskTime(date);
			job.processer();
		}
	}
}
