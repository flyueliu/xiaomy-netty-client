package com.flyue.xiaomy.common.http;

import com.flyue.xiaomy.common.utils.JsonUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Liu Yuefei
 * @Date: Created in 2020/1/29 14:45
 * @Description:
 */
public class RestClient {

    protected RestTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    public RestClient() {
        this.template = new RestTemplate(new HttpComponentsClientRestfulHttpRequestFactory());
    }

    public RestClient(RestTemplate template) {
        this.template = template;
    }

    public void setTemplate(RestTemplate template) {
        this.template = template;
    }

    public String requestJson(String url, HttpMethod requestMethod, Object body, String... uriVariable) {
        String params = JsonUtils.pojoToJson(body);
        logger.info("请求地址:{}", url);
        logger.info("地址参数变量:{}", Arrays.toString(uriVariable));
        logger.info("请求参数:{}", params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> responseBody = template.exchange(url, requestMethod, httpEntity, String.class, uriVariable);
            if (responseBody.getStatusCode() != HttpStatus.OK) {
                logger.error("请求错误,{}", responseBody.getStatusCode());
                logger.error(JsonUtils.pojoToJson(responseBody.getBody()));
                return responseBody.getBody();
            }
            logger.info("请求成功,响应结果:{}", JsonUtils.pojoToJson(responseBody.getBody()));
            return responseBody.getBody();
        } catch (HttpClientErrorException httpException) {
            return this.getHttpClientErrorMessage(httpException);
        }

    }

    protected String getHttpClientErrorMessage(HttpClientErrorException httpException) {
        String errorMessage = httpException.getResponseBodyAsString();
        logger.error(httpException.getMessage(), httpException);
        if (StringUtils.isEmpty(errorMessage)) {
            errorMessage = httpException.getMessage();
            Map<String, String> respMap = new HashMap<>();
            respMap.put("functionResult", "ERROR");
            respMap.put("message", errorMessage);
            return JsonUtils.pojoToJson(respMap);
        }
        logger.error("http请求响应错误,异常原因:" + errorMessage);
        return errorMessage;

    }

    public <T> T baseSend(String url, HttpMethod requestMethod, Object body, Class<T> returnClazz, String... uriVariable) {
        String params = JsonUtils.pojoToJson(body);
        logger.info("请求地址:{}", url);
        logger.info("地址参数变量:{}", Arrays.toString(uriVariable));
        logger.info("请求参数:{}", params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<String> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<T> responseBody = template.exchange(url, requestMethod, httpEntity, returnClazz, uriVariable);
        if (responseBody.getStatusCode() != HttpStatus.OK) {
            logger.error("请求错误,{}", responseBody.getStatusCode());
            logger.error(JsonUtils.pojoToJson(responseBody.getBody()));
            return responseBody.getBody();
        }
        logger.info("请求成功,响应结果:{}", JsonUtils.pojoToJson(responseBody.getBody()));
        return responseBody.getBody();
    }


    public static class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
        public HttpGetRequestWithEntity(final URI uri) {
            super.setURI(uri);
        }

        @Override
        public String getMethod() {
            return HttpMethod.GET.name();
        }
    }

    public static class HttpComponentsClientRestfulHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
        @Override
        protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
            if (httpMethod == HttpMethod.GET) {
                return new HttpGetRequestWithEntity(uri);
            }
            return super.createHttpUriRequest(httpMethod, uri);
        }

        @Override
        public RequestConfig mergeRequestConfig(RequestConfig clientConfig) {
            return super.mergeRequestConfig(clientConfig);
        }
    }
}

