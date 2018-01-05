package com.activiti.common.quartz.jobs;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通知参加互评，并且打乱学生提交顺序
 * Created by 12490 on 2017/8/19.
 */
public class AssessmentStartJob extends JobCollection implements Job {
    private static final Logger logger = LoggerFactory.getLogger(AssessmentStartJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        commonUtil.assessmentStartJob(((JobDetailImpl) (jobExecutionContext).getJobDetail()).getGroup());
    }
}
