package com.flyue.xiaomy.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/18 23:22
 * @Description:
 */
public class ResponseMessageHeader {

    private String msg;

    private String domain;

    private String type;

    private String info;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
