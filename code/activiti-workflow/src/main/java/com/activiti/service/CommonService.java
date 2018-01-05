package com.activiti.service;

import com.activiti.pojo.tools.Analysis;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 12490 on 2017/8/14.
 */
public interface CommonService {

    JSONObject getQAFromGitHub(String githubUrl) throws UnsupportedEncodingException;

    JSONObject getStudentCommitTimeAnalysis(String courseCode);

    JSONObject getStudentCommitGradeAnalysis(String courseCode);

    List<Analysis> selectAllStudentWorkInfo(String courseCode);

    JSONObject getStudentGradeAnalysis(String courseCode);
}
