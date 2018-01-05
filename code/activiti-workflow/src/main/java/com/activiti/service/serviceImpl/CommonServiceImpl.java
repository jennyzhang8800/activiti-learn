package com.activiti.service.serviceImpl;

import com.activiti.common.utils.CommonUtil;
import com.activiti.mapper.AnalysisMapper;
import com.activiti.pojo.tools.Analysis;
import com.activiti.service.CommonService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 12490 on 2017/8/14.
 */
@Service
@EnableCaching
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private AnalysisMapper analysisMapper;

    /**
     * 从gitlab取题目数据
     *
     * @param githubUrl 题目地址
     * @return
     */
    @Override
    @Cacheable(value = "ehCache300", key = "'QAFromGitLab_'+#githubUrl")
    public JSONObject getQAFromGitHub(String githubUrl) throws UnsupportedEncodingException {
        return JSONObject.parseObject(new String(
                Base64.decodeBase64(
                        commonUtil.getQAFromGitHub(githubUrl).get("content").toString().getBytes()),
                "utf-8"));
    }

    /**
     * 学生提交时间分析
     *
     * @param courseCode
     * @return
     */
    @Override
    public JSONObject getStudentGradeAnalysis(String courseCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", selectAllStudentWorkInfo(courseCode));
        return jsonObject;
    }

    @Override
//    @Cacheable(value = "ehCache300", keyGenerator = "keyGenerator")
    public List<Analysis> selectAllStudentWorkInfo(String courseCode) {
        return analysisMapper.selectAllStudentWorkInfo(courseCode);
    }

    /**
     * 学生成绩分析
     *
     * @param courseCode
     * @return
     */
    @Override
//    @Cacheable(value = "ehCache300", keyGenerator = "keyGenerator")
    public JSONObject getStudentCommitTimeAnalysis(String courseCode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject list = new JSONObject();
        JSONArray dateArray = new JSONArray();
        JSONArray dataArray = new JSONArray();
        List<Analysis> analysisList=selectAllStudentWorkInfo(courseCode);
        List<String>list1=new LinkedList<>();
        analysisList.forEach(analysis -> {
            String date = new DateTime(analysis.getLastCommitTime()).toString("yyyy/MM/dd");
            list1.add(date);
            if (list.containsKey(date))
                list.put(date, (int) list.get(date) + 1);
            else
                list.put(date, 1);
        });
        list1.forEach(key -> {
            dateArray.add(key);
            dataArray.add(list.get(key));
        });
        jsonObject.put("date", dateArray);
        jsonObject.put("data", dataArray);
        return jsonObject;
    }

    /**
     * 学生成绩分析
     *
     * @param courseCode
     * @return
     */
    @Override
    public JSONObject getStudentCommitGradeAnalysis(String courseCode) {
        JSONObject jsonObject = new JSONObject();
        selectAllStudentWorkInfo(courseCode).forEach(analysis -> {
            String grade = String.valueOf((int) analysis.getGrade());
            if (jsonObject.containsKey(grade)) {
                jsonObject.put(grade, (int) jsonObject.get(grade) + 1);
            } else {
                jsonObject.put(grade, 1);
            }
        });
        return jsonObject;
    }
}
