package com.activiti;

import com.activiti.common.quartz.QuartzManager;
import com.activiti.common.quartz.jobs.DistributeWork;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 12490 on 2017/8/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzTest {
    public static String JOB_NAME = "动态任务调度";
    public static String TRIGGER_NAME = "动态任务触发器";
    public static String JOB_GROUP_NAME = "XLXXCC_JOB_GROUP";
    public static String TRIGGER_GROUP_NAME = "XLXXCC_JOB_GROUP";

    @Autowired
    private QuartzManager quartzManager;
    @Test
    public void QuartzTest() {
        try {
            System.out.println("【系统启动】开始(每1秒输出一次)...");
            quartzManager.addJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, DistributeWork.class, "0/1 * * * * ?");

            Thread.sleep(10000);
            System.out.println("【修改时间】开始(每5秒输出一次)...");
            quartzManager.modifyJobTime(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME, "0/5 * * * * ?");

            Thread.sleep(30000);
            System.out.println("【移除定时】开始...");
            quartzManager.removeJob(JOB_NAME, JOB_GROUP_NAME, TRIGGER_NAME, TRIGGER_GROUP_NAME);
            System.out.println("【移除定时】成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
