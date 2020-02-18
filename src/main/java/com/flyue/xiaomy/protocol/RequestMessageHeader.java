package com.flyue.xiaomy.protocol;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/18 22:11
 * @Description:
 */
public class RequestMessageHeader {

    private Integer userid;


    private String token;

    private Integer id;

    private String version;

    private String type;

    private String ChangeToken;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChangeToken() {
        return ChangeToken;
    }

    public void setChangeToken(String changeToken) {
        ChangeToken = changeToken;
    }
}
