package com.activiti.common.utils;

import com.activiti.pojo.user.JudgementLs;
import com.activiti.pojo.user.StudentWorkInfo;
import com.activiti.service.CommonService;
import com.activiti.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

@Component
public class HttpClientUtil {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    @Autowired
    private CommonService commonService;
    @Autowired
    private CommonUtil commonUtil;
    @Value("${spring.profiles.active}")
    private String env;
    @Autowired
    private UserService userService;

    /**
     * 提交作业到gitlab
     *
     * @param studentWorkInfo
     * @throws UnsupportedEncodingException
     */
    public void commitWorkToGitlab(StudentWorkInfo studentWorkInfo) throws UnsupportedEncodingException {
        String userName = studentWorkInfo.getUserName();
        String userType = studentWorkInfo.getUserType();
        String uri = "http://192.168.1.136/api/v3/projects/287/repository/files";
        String courseCode = studentWorkInfo.getCourseCode();
        String email = studentWorkInfo.getEmailAddress();
        String md5 = getMD5(email).toLowerCase();
        String file_path = md5.substring(md5.length() - 2, md5.length()) + "/" + userName + "/" + courseCode + "/" + courseCode + ".json";
        JSONObject jsonObject = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject student = new JSONObject();
        JSONArray answer = new JSONArray();
        JSONObject answerDetail = new JSONObject();
        answerDetail.put("time", new DateTime(new Date()).toString("yyyy-MM-dd HH:mm:ss"));
        answerDetail.put("answer", studentWorkInfo.getWorkDetail());
        answer.add(answerDetail);
        student.put("email", email);
        student.put("username", userName);
        student.put("is_staff", "staff".equals(userType));
        content.put("student", student);
        content.put("tried", 1);
        content.put("maxTry", 1);
        content.put("answer", answer);
        content.put("question", commonService.getQAFromGitHub(commonUtil.generateGitHubUrl(Integer.valueOf(courseCode))));
        jsonObject.put("private_token", "L7Zxq6V_WXvG36wyrxt6");
        jsonObject.put("ref", "master");
        jsonObject.put("commit_message", "commitWorkToGitlab>>>>" + file_path);
        jsonObject.put("branch_name", "master");
        jsonObject.put("content", JsonFormatTool.formatJson(content.toJSONString()));
        jsonObject.put("file_path", file_path);
        if ("pro".equals(env))
            doPost(uri, jsonObject);
    }

    /**
     * 向gitlab提交成绩
     *
     * @param studentWorkInfo
     * @param judgementLs
     * @throws UnsupportedEncodingException
     */
    public void updateGradeToGitlab(StudentWorkInfo studentWorkInfo, List<JudgementLs> judgementLs, boolean put) throws UnsupportedEncodingException {
        String userName = studentWorkInfo.getUserName();
        String userType = studentWorkInfo.getUserType();
        String uri = "http://192.168.1.136/api/v3/projects/287/repository/files";
        String courseCode = studentWorkInfo.getCourseCode();
        String email = studentWorkInfo.getEmailAddress();
        String md5 = getMD5(email).toLowerCase();
        String file_path = md5.substring(md5.length() - 2, md5.length()) + "/" + userName + "/" + courseCode + "/" + courseCode + "_graded.json";
        JSONObject jsonObject = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject student = new JSONObject();
        student.put("email", email);
        student.put("username", userName);
        student.put("is_staff", "staff".equals(userType));
        content.put("q_number", Integer.valueOf(studentWorkInfo.getCourseCode()));
        content.put("student", student);
        content.put("student_answer", userService.selectStudentWorkInfo(studentWorkInfo).getWorkDetail());
        content.put("standard_answer", commonService.getQAFromGitHub(commonUtil.generateGitHubUrl(Integer.valueOf(courseCode))));
        content.put("score", studentWorkInfo.getGrade());
        content.put("graded_by", studentWorkInfo.getJudgeType());
        content.put("peer_assessment", judgementLs);
        jsonObject.put("private_token", "L7Zxq6V_WXvG36wyrxt6");
        jsonObject.put("ref", "master");
        jsonObject.put("commit_message", "updateGradeToGitlab>>>>" + file_path);
        jsonObject.put("branch_name", "master");
        jsonObject.put("content", JsonFormatTool.formatJson(content.toJSONString()));
        jsonObject.put("file_path", file_path);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>===============================begin updateGradeToGitlab+++++++++++++++++++++++++++++++");
        if ("pro".equals(env)) {
            if (put)
                doPut(uri, jsonObject);
            else
                doPost(uri, jsonObject);
        }
    }

    public String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        int num;
        for (byte aByte : bytes) {
            num = aByte;
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */
    private void doPost(String url, JSONObject json) {
        logger.info("调用gitlab接口" + url + "(POST)parameter======：" + json.toJSONString());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString(), Charset.forName("UTF-8"));
            s.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSON.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("调用gitlab接口" + url + "(POST)return message======：" + response);
    }

    /**
     * put 请求
     *
     * @param url
     * @param json
     * @return
     */
    private void doPut(String url, JSONObject json) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut put = new HttpPut(url);
        JSONObject response = null;
        try {
            StringEntity s = new StringEntity(json.toString(), Charset.forName("UTF-8"));
            s.setContentType("application/x-www-form-urlencoded");//发送json数据需要设置contentType
            s.setContentType("application/json");
            put.setEntity(s);
            HttpResponse res = client.execute(put);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                response = JSON.parseObject(result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("调用gitlab接口" + url + "(PUT)返回数据：" + response);
    }
}
