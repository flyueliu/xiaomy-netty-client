package com.flyue.xiaomy;

import java.util.Scanner;

public class NatServerApplication {

    // 使用命令 wsimport -keep -p com.pachira.boot.webservice.model -encoding utf-8 http://127.0.0.1:8020/HelloHBuilder/SaveQualityResults.xml
    public static void main(String[] args) {
        System.out.println("请输入隧道token信息:");
        Scanner scanner = new Scanner(System.in);
        String token = scanner.next();
        run(token);
    }

    public static void run(String token) {
        SmallAntClient client = new SmallAntClient(token);
        client.getTunnelInfo();
        client.createNatChannel();
    }


}
