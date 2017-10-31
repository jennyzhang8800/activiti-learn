# 作业批改结果json格式的扩展

**在[原有的数据格式](https://github.com/Heaven1881/mooc-document/blob/master/%E7%BB%83%E4%B9%A0%E6%89%B9%E6%94%B9%E6%95%B0%E6%8D%AE%E8%AF%B4%E6%98%8E.md)基础上添加了`peerassessment`字段，用于记录互评的批改结果**

- 用于保存自动批改或者互评的得分结果
- 当前保存在gitlab上，库路径为teacher/answer

路径说明:
- 则数据的路径格式为`{email.md5[-2:]}/{username}/{q_number}/{q_number}.graded.json`
- `email.md5[-2:]`表示对email取md5编码的最后两位
- 对于一个学生（ email=test@test.com,username=student），对于题号为101的题目(q_number=101)的批改情况json的路径为`52/student/101/101.graded.json`

## Json格式说明

|字段|说明|备注|
|---|---|---|
|q_number|题号|必选|
|student|学生的相关信息|必选|
|graded_by|评分方法,"system"表示由系统自动评分（选择题）,"students"表示由学生互评(问答题)，"teacher"表示由老师批改|必选|
|student_answer|学生给出的答案|必选|
|standard_answer|该题的标准答案|必选|
|score|学生的得分，大于零的实数|必选|
|peer_assessment|互评批改结果|必选|


### peer_assessment 字段详细说明

 `peer_assessment`字段保存的是所有批改结果的list，list中的每一项是下表所示结构的字典
 
|字段|说明|备注|
|---|---|---|
|courseCode|题号|必选|
|grade|分数|必选|
|judgeEmail|打分人|必选|
|judgeTime|打分时间|必选|
|judgeTimeString|打分时间（可读字符串形式）|必选|
|judgement|评语|必选|
|nonJudgeEmail|被打分人|必选|

### 实例


```json
{
    "score":8.5,
    "peer_assessment":
    [
        {
            "courseCode":"1113",
            "grade":8.0,
            "judgeEmail":"os_course_tsinghua@126.com",
            "judgeTime":1509440885000,
            "judgeTimeString":"2017/10/31 17:08:05",
            "judgement":"test2",
            "nonJudgeEmail":"os_course_911_2@163.com"
        },
        {
            "courseCode":"1113",
            "grade":10.0,
            "judgeEmail":"os_course_tsingha@163.com",
            "judgeTime":1509441035000,
            "judgeTimeString":"2017/10/31 17:10:35",
            "judgement":"eee",
            "nonJudgeEmail":"os_course_911_2@163.com"
        },
        {
            "courseCode":"1113",
            "grade":8.0,
            "judgeEmail":"os_course_jenny@163.com",
            "judgeTime":1509441124000,
            "judgeTimeString":"2017/10/31 17:12:04",
            "judgement":"fff",
            "nonJudgeEmail":"os_course_911_2@163.com"
        },
        {
            "courseCode":"1113",
            "grade":9.0,
            "judgeEmail":"edx_tsinghua_os@163.com",
            "judgeTime":1509441236211,
            "judgement":"test",
            "nonJudgeEmail":"os_course_911_2@163.com"
        }

    ],
    "graded_by":"student",
    "student":
    {
        "is_staff":false,
        "email":"os_course_911_2@163.com",
        "username":"s0712"
    },
    "student_answer":"test",
    "q_number":1113,
    "standard_answer":
    {
        "explain":"UNIX系统中......",
        "degree_of_difficulty":1,
        "question":"在管道通信机制中,用信号量描述读进程和写进程访问管道文件的过程,假设管 道文件大小为10KB.\n",
        "answer":"UNIX系统中, 利用一个打开的共享文件来连接两个相互通信的进程.....",
        "q_number":1113,
        "source":"网络",
        "type":"question_answer",
        "status":"ok",
        "knowledge":
        [
            "实验环境准备实验"
        ]

    }

}
```
