package com.flyue.xiaomy.common.http.vo;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/16 23:32
 * @Description:
 */
public class BaseResponse<T extends Object> {

    private Integer code;

    private String msg;

    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
