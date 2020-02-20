package com.flyue.xiaomy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class NatServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(NatServerApplication.class);

    // 使用命令 wsimport -keep -p com.pachira.boot.webservice.model -encoding utf-8 http://127.0.0.1:8020/HelloHBuilder/SaveQualityResults.xml
    public static void main(String[] args) {
        String token = readTokenFromConf("xiaomy.properties");
        if (Objects.isNull(token) || token.isEmpty()) {
            System.out.println("please input token:");
            Scanner scanner = new Scanner(System.in);
            token = scanner.next();
        } else {

        }
        run(token);
    }

    private static String readTokenFromConf(String fileName) {
        Properties p = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                logger.warn("config file not exist, need input token!");
                return null;
            }
            p = new Properties();
            p.load(new FileReader(file));
            String value = p.getProperty("token", null);
            if (Objects.nonNull(value)) {
                logger.info("find token in properties file");
            }
            return value;
        } catch (IOException e) {
            logger.error("read config file error!");
            return null;
        }
    }

    public static void run(String token) {
        SmallAntClient client = new SmallAntClient(token);
        client.getTunnelInfo();
        client.createNatChannel();
    }


}
