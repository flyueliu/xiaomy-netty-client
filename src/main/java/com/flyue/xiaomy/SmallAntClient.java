package com.flyue.xiaomy;

import com.flyue.xiaomy.common.http.RestClient;
import com.flyue.xiaomy.common.http.vo.BaseResponse;
import com.flyue.xiaomy.common.http.vo.resp.login.TunnelInfo;
import com.flyue.xiaomy.common.utils.JsonUtils;
import com.flyue.xiaomy.handler.ClientChannelHandler;
import com.flyue.xiaomy.protocol.ProtocolMessage;
import com.flyue.xiaomy.protocol.RequestMessageHeader;
import com.flyue.xiaomy.starter.NetworkClientStarter;
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
    private NetworkClientStarter networkStarter;

    public SmallAntClient(String token) {
        this.restClient = new RestClient();
        this.token = token;
    }

    public TunnelInfo getTunnelInfo() {
        logger.info("开始获取隧道信息");
        try {
            BaseResponse resp = restClient.baseSend(String.format(loginUrl, token), HttpMethod.GET, "", BaseResponse.class);
            logger.debug("resp:{}", resp);
            logger.info("隧道信息:{}", JsonUtils.pojoToJson(resp.getData()));
            if (resp.getCode() != 0) {
                logger.error("login error!");
                logger.error("{}", JsonUtils.pojoToJson(resp.getData()));
                throw new RuntimeException("登录失败,请检查token");
            }
            this.tunnelInfo = JsonUtils.jsonToPojo(JsonUtils.pojoToJson(resp.getData()), TunnelInfo.class);
            if (tunnelInfo.getState() == -1) {
                logger.error("您的隧道未生效或已到期，请进入web控制台(i.xiaomy.net)处理。");
                throw new RuntimeException("隧道未生效或已到期");
            }
            logger.info("隧道信息有效,开始创建连接..");
            return tunnelInfo;
        } catch (Exception e) {
            logger.error("获取隧道信息失败\n\t具体原因:{}", e.getMessage());
            throw new RuntimeException("获取隧道信息失败");
        }
    }


    public void createNatChannel() {
        try {
            this.networkStarter = new NetworkClientStarter(tunnelInfo);
            ClientChannelHandler handler = new ClientChannelHandler(networkStarter);
            networkStarter.setClientHandler(handler);
            networkStarter.connect(tunnelInfo.getServer_ip(), 8888);
            RequestMessageHeader header = new RequestMessageHeader();
            header.setUserid(tunnelInfo.getUser_id());
            header.setToken(this.getToken());
            header.setId(tunnelInfo.getId());
            header.setVersion("3");
            header.setType("LOGIN");
            header.setChangeToken("false");
            ProtocolMessage message = new ProtocolMessage(JsonUtils.pojoToJson(header), "login".getBytes());
            networkStarter.getClientChannel().writeAndFlush(message);
            networkStarter.waitClose();
        } catch (Exception e) {
            logger.error("与服务器{}:{}建立连接失败!", tunnelInfo.getServer_ip(), 8888);
            throw new RuntimeException("与远程服务器建立连接失败!");
        }

    }


    public String getToken() {
        return token;
    }
}
