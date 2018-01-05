package com.activiti.pojo.restApiDto;

import java.io.Serializable;

/**
 * 接口统一信息返回
 * Created by liulinhui on 2017/8/5.
 */
public class RestApiResponse implements Serializable {
    private static final long serialVersionUID = 2120869894112984147L;
    private String errorMessage;  //错误信息
    private int resCode;    //返回码
    private boolean success;
    private Object data;

    public RestApiResponse(int resCode, String errorMessage, boolean success) {
        this.errorMessage = errorMessage;
        this.resCode = resCode;
        this.success = success;
    }

    public RestApiResponse(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public RestApiResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    @Override
    public String toString() {
        return "RestApiResponse{" +
                "errorMessage='" + errorMessage + '\'' +
                ", resCode=" + resCode +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
