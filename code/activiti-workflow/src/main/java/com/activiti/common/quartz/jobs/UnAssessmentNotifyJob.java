package com.activiti.common.quartz.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件提醒没有参加互评的人以及将它们的流程结束
 */
public class UnAssessmentNotifyJob extends JobCollection implements Job {
    private static final Logger logger = LoggerFactory.getLogger(UnAssessmentNotifyJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        commonUtil.unAssessmentNotifyJob(((JobDetailImpl) (jobExecutionContext).getJobDetail()).getGroup());
    }
}
