package com.flyue.xiaomy.configuration;

import com.flyue.xiaomy.common.http.RestClient;
import org.apache.http.client.config.RequestConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2019/6/12 10:27
 * @Description:
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        RestClient.HttpComponentsClientRestfulHttpRequestFactory factory = new RestClient.HttpComponentsClientRestfulHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(30000)
                .build();
        factory.mergeRequestConfig(requestConfig);
        return factory;
    }


}




