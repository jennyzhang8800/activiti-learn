package com.activiti.common.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件通知成绩
 */
public class PublishGradeEmailNotifyJob extends JobCollection implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PublishGradeEmailNotifyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        commonUtil.publishGradeEmailNotifyJob(((JobDetailImpl) (jobExecutionContext).getJobDetail()).getGroup());
    }
}
