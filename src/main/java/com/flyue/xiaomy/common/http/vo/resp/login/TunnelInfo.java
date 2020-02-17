package com.flyue.xiaomy.common.http.vo.resp.login;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/16 23:33
 * @Description:
 */
public class TunnelInfo {

    private Integer id;

    private String server_port;

    private Integer client_port;

    private String domain;

    private Integer server_id;

    private Integer time;

    private Integer state;

    private Long start_time;

    private Long stop_time;

    private Integer user_id;

    private String client_host;

    private String name;

    private String server_ip;


    private String conn;

    private String port_token;

    private Integer auto_conn;

    private String stop_time_1;

    private Integer free_tag;

    private Integer flag;

    private Long create_time;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServer_port() {
        return server_port;
    }

    public void setServer_port(String server_port) {
        this.server_port = server_port;
    }

    public Integer getClient_port() {
        return client_port;
    }

    public void setClient_port(Integer client_port) {
        this.client_port = client_port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getStop_time() {
        return stop_time;
    }

    public void setStop_time(Long stop_time) {
        this.stop_time = stop_time;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getClient_host() {
        return client_host;
    }

    public void setClient_host(String client_host) {
        this.client_host = client_host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    public String getPort_token() {
        return port_token;
    }

    public void setPort_token(String port_token) {
        this.port_token = port_token;
    }

    public Integer getAuto_conn() {
        return auto_conn;
    }

    public void setAuto_conn(Integer auto_conn) {
        this.auto_conn = auto_conn;
    }

    public String getStop_time_1() {
        return stop_time_1;
    }

    public void setStop_time_1(String stop_time_1) {
        this.stop_time_1 = stop_time_1;
    }

    public Integer getFree_tag() {
        return free_tag;
    }

    public void setFree_tag(Integer free_tag) {
        this.free_tag = free_tag;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return "TunnelInfo{" +
                "id=" + id +
                ", server_port='" + server_port + '\'' +
                ", client_port=" + client_port +
                ", domain='" + domain + '\'' +
                ", server_id=" + server_id +
                ", time=" + time +
                ", state=" + state +
                ", start_time=" + start_time +
                ", stop_time=" + stop_time +
                ", user_id=" + user_id +
                ", client_host='" + client_host + '\'' +
                ", name='" + name + '\'' +
                ", server_ip='" + server_ip + '\'' +
                ", conn='" + conn + '\'' +
                ", port_token='" + port_token + '\'' +
                ", auto_conn=" + auto_conn +
                ", stop_time_1='" + stop_time_1 + '\'' +
                ", free_tag=" + free_tag +
                ", flag=" + flag +
                ", create_time=" + create_time +
                '}';
    }
}
