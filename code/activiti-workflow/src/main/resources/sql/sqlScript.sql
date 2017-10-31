/*
Source Server         : mysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : activiti

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001
*/

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- 己废除
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_STU_INFO (
  USER_ID       INT(11)     NOT NULL AUTO_INCREMENT,
  USER_NAME     VARCHAR(20)          DEFAULT NULL,
  EMAIL_ADDRESS VARCHAR(30) NOT NULL,
  ABILITY_LEVEL VARCHAR(10)          DEFAULT NULL,
  COURSE_CODE   VARCHAR(50) NOT NULL,
  INDEX index_email_address (EMAIL_ADDRESS),
  PRIMARY KEY (USER_ID)
)
  ENGINE = MyISAM -- 采用MyISAM防止主键id自增出现断层现象
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for scheduleTime
-- 题目与工作流配置表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_SCHEDULE_TIME (
  COURSE_NAME         VARCHAR(50)  NOT NULL,
  COURSE_CODE         VARCHAR(20)  NOT NULL,
  JUDGE_START_TIME    DATETIME    DEFAULT NULL,
  JUDGE_END_TIME      DATETIME    DEFAULT NULL,
  AUDIT_START_TIME    DATETIME    DEFAULT NULL,
  AUDIT_END_TIME      DATETIME    DEFAULT NULL,
  PUBLISH_TIME        DATETIME    DEFAULT NULL,
  JUDGE_TIMES         INT(8)      DEFAULT 4,
  DISTRIBUTE_MAX_USER INT(8)      DEFAULT 100,
  TIMEOUT             VARCHAR(50) DEFAULT 'PT7D',
  IS_APPEAL           VARCHAR(10) DEFAULT 'no',
  GITHUB_ADDRESS      VARCHAR(100) NOT NULL,
  UNIQUE INDEX index_COURSE_CODE (COURSE_CODE),
  PRIMARY KEY (COURSE_CODE)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for record student`s workInfo
-- 学生作业表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_STU_WORK_INFO (
  COURSE_CODE       VARCHAR(20)   NOT NULL,
  EMAIL_ADDRESS     VARCHAR(30)   NOT NULL,
  WORK_DETAIL       VARCHAR(5000) NOT NULL,
  LAST_COMMIT_TIME  DATETIME      NOT NULL,
  GRADE             DOUBLE        DEFAULT NULL,
  JUDGE_TYPE        VARCHAR(10)   DEFAULT NULL,
  ASK_TO_VERIFY   VARCHAR(10)   DEFAULT 'no',
  USER_NAME         VARCHAR(50)   DEFAULT NULL,
  USER_TYPE         VARCHAR(50)   DEFAULT NULL,
  JUDGE_TIMES       INT(8)        DEFAULT 0,
  JOIN_JUDGE_TIME   DATETIME      DEFAULT NULL,
  DISTRIBUTE_STATUS VARBINARY(10) DEFAULT 'false',
  UNIQUE INDEX index_COURSE_CODE(COURSE_CODE, EMAIL_ADDRESS),
  INDEX index_JUDGE_TIMES(JUDGE_TIMES)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for record judgement_ls
-- 互评流水表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_JUDGEMENT_LS (
  COURSE_CODE      VARCHAR(20) NOT NULL,
  JUDGER_EMAIL     VARCHAR(30) NOT NULL,
  NON_JUDGER_EMAIL VARCHAR(30) NOT NULL,
  GRADE            DOUBLE      NOT NULL,
  JUDGE_TIME       DATETIME    NOT NULL,
  JUDGEMENT        VARCHAR(100) DEFAULT NULL,
  INDEX index_judge(COURSE_CODE, JUDGER_EMAIL),
  INDEX index_non_judge(COURSE_CODE, NON_JUDGER_EMAIL)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for record JUDGE_USER_ROLE
-- 管理员用户表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_USER_ROLE (
  ID      INT(8)      NOT NULL,
  EMAIL   VARCHAR(30) NOT NULL,
  ROLE    VARCHAR(30) DEFAULT 'teacher',
  REMARKS VARCHAR(50),
  PRIMARY KEY (EMAIL)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for record JUDGE_INVOKE_LOG
-- 接口调用日志表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_INVOKE_LOG (
  UUID        BIGINT,
  INVOKE_TIME BIGINT,
  PARAMS      VARCHAR(1000),
  RESULT      VARCHAR(20000),
  EMAIL       VARCHAR(50),
  REQUEST_URI VARCHAR(100),
  STATUS      VARBINARY(10),
  TIME        DATETIME
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for record JUDGE_EMAIL_LOG
-- 邮件日志表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_EMAIL_LOG (
  RECEIVE_ADDRESS VARCHAR(50),
  SEND_ADDRESS    VARCHAR(50),
  SUBJECT         VARCHAR(500),
  CONTENT         VARCHAR(20000),
  RSC_PATH        VARCHAR(100),
  RSC_ID          VARCHAR(100),
  SEND_TIME       DATETIME,
  STATUS          VARBINARY(10),
  INDEX index_mail_send(SEND_ADDRESS),
  INDEX index_mail_receive(RECEIVE_ADDRESS)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table for  JUDGE_VERIFY_TASK
-- 教师批改作业表
-- ----------------------------
CREATE TABLE IF NOT EXISTS JUDGE_VERIFY_TASK (
  EMAIL        VARCHAR(50),
  ANSWER       VARCHAR(5000),
  STATUS       VARCHAR(20) DEFAULT 'wait',
  COURSE_CODE  VARCHAR(50),
  GRADE        DOUBLE,
  JUDGE_TIMES  INT,
  JUDGER_EMAIL VARCHAR(50),
  UNIQUE INDEX index_mail_course(EMAIL, COURSE_CODE)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- insert default admin to JUDGE_USER_ROLE
-- ----------------------------

INSERT IGNORE INTO JUDGE_USER_ROLE (ID, EMAIL, REMARKS) VALUES (1, '1249055292@qq.com', 'admin');
INSERT IGNORE INTO JUDGE_USER_ROLE (ID, EMAIL, REMARKS) VALUES (1, 'jennyzhang8800@163.com', 'admin');