package com.activiti.common.utils;

/**
 * 常量池
 * Created by 12490 on 2017/8/15.
 */
public class ConstantsUtils {

    //GitLab题目答案地址
    //https://api.github.com/repos/chyyuu/os_course_exercise_library/contents/data/json/16/1502.json
    public static  final String emailTopic="EMAIL";

    public static final String sessionEmail="userEmail";


    //定时任务相关的数据字典
    public static final String NOTIFY_TO_ASSESSMENT="NOTIFY_TO_ASSESSMENT";  //邮件通知互评，打乱学生顺序
    public static final String NOTIFY_HAVE_NOT_JOIN_ASSESSMENT="NOTIFY_HAVE_NOT_JOIN_ASSESSMENT";  //邮件通知没有参加互评
    public static final String NOTIFY_PUBLISH_GRADE="NOTIFY_PUBLISH_GRADE";  //邮件通知成绩发布了
    public static final String TRIGGER_NAME = "动态任务触发器";
    public static final String TRIGGER_GROUP_NAME = "ASSESSMENT_JOB_GROUP";

    public static final String tablePrefixName="JUDGE_STU_INFO_";

    public static final int minJudgeTimes=3;  //最低互评次数

    //页面地址
    public static final String successAnswerFtl="mail/successAnswer.ftl";

}
