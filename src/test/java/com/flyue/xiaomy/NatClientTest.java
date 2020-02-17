package com.flyue.xiaomy;

import com.flyue.xiaomy.common.http.vo.resp.login.TunnelInfo;
import com.flyue.xiaomy.handler.ClientChannelHandler;
import com.flyue.xiaomy.tcp.NetworkClientStarter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.NetworkClient;
import sun.nio.ch.Net;

import java.io.InputStream;
import java.net.Socket;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/2/16 23:25
 * @Description:
 */
public class NatClientTest {


    private static final Logger logger = LoggerFactory.getLogger(NatClientTest.class);

    @Test
    public void test01() throws Exception {
        SmallAntClient client = new SmallAntClient();
        TunnelInfo tunnelInfo = client.login();
        logger.info("tunnel:{}", tunnelInfo);
        NetworkClientStarter networkStarter = new NetworkClientStarter(new ClientChannelHandler());
        networkStarter.connect("127.0.0.1", 8888);
        Thread.sleep(100000);
        networkStarter.close();
    }
}
