||方法 ||接口 ||说明 ||
||GET ||/api/common/getQAContent ||Github请求题目和答案 ||
||GET ||/api/common/selectScheduleTime ||查询指定题目的流程配置||
||GET ||/api/common/selectAllScheduleTime  ||查询所有题目的流程配置||
||GET ||/api/common/selectInvokeLog  ||查询接口调用日志||
||GET ||/api/common/selectEmailLog ||邮件日志查询||
||GET ||/api/user/selectStudentWorkInfo  ||查询学生已经提交的作业||
||GET ||/api/user/selectWorkListToJudge  ||查询需要评论的任务||
||GET ||/api/user/selectStudentGrade ||&学生成绩查询||
||GET ||/api/user/selectWhoJudgeMe ||查询哪些人评论了我的作业||
||GET ||/api/user/selectAllTeacherTask ||查询教师需要完成的任务||
||POST || /api/common/loginAbutment  ||登陆对接||
||POST || /api/user/commitWork ||学生提交作业||
||POST || /api/user/commitJudgementInfo  ||提交互评结果||
||POST || /api/user/ackTeacherVerify ||申请让老师批改作业||
||POST || /api/user/finishTeacherVerifyTask  ||教师完成任务并提交||
