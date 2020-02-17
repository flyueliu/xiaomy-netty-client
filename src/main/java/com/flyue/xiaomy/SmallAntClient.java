package com.flyue.xiaomy;

import com.flyue.xiaomy.common.http.RestClient;
import com.flyue.xiaomy.common.http.vo.BaseResponse;
import com.flyue.xiaomy.common.http.vo.resp.login.TunnelInfo;
import com.flyue.xiaomy.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/16 23:52
 * @Description:
 */
public class SmallAntClient {

    private static final Logger logger = LoggerFactory.getLogger(SmallAntClient.class);
    private final RestClient restClient;

    private String loginUrl = "http://nw.xiaomy.net:8080/port/get/token?portToken=%s";

    private String token;

    private TunnelInfo tunnelInfo;

    public SmallAntClient(String token) {
        this.restClient = new RestClient();
        this.token = token;
    }

    public SmallAntClient() {
        this.restClient = new RestClient();
        this.token = "t55uvwzy";
    }


    public TunnelInfo login() {
        BaseResponse resp = restClient.baseSend(String.format(loginUrl, token), HttpMethod.GET, "", BaseResponse.class);
        logger.info("resp:{}", resp);
        if (resp.getCode() != 0) {
            logger.error("login error!");
            logger.error("{}", JsonUtils.pojoToJson(resp.getData()));
            throw new RuntimeException("登录失败,请检查token");
        }
        TunnelInfo tunnelInfo = JsonUtils.jsonToPojo(JsonUtils.pojoToJson(resp.getData()), TunnelInfo.class);
        if (tunnelInfo.getState() == -1) {
            logger.error("您的隧道未生效或已到期，请进入web控制台(i.xiaomy.net)处理。");
            throw new RuntimeException("的隧道未生效或已到期");
        }
        logger.info("隧道信息有效,开始创建连接..");
        this.tunnelInfo = tunnelInfo;
        return tunnelInfo;
    }


}
