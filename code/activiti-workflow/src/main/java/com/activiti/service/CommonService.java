package com.activiti.service;

import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by 12490 on 2017/8/14.
 */
public interface CommonService {

    JSONObject getQAFromGitHub(String githubUrl) throws UnsupportedEncodingException;
}
