package com.ksy.wechat.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.ksy.wechat.handler.CResponseErrorHandler;

@Configuration
public class RestfulConfig {

    @Value("${restTemplate.factory.readTimeout}")
    private int READ_TIMEOUT;

    @Value("${restTemplate.factory.connectTimeout}")
    private int CONNECT_TIMEOUT;

    @Value("${restTemplate.httpClient.maxConnTotal}")
    private int MAX_CONN_TOTAL;

    @Value("${restTemplate.httpClient.maxConnPerRoute}")
    private int MAX_CONN_PER_ROUTE;

    @Bean
    public RestTemplate restTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		/*
		 * HttpComponentsClientHttpRequestFactory factory = new
		 * HttpComponentsClientHttpRequestFactory();
		 * factory.setReadTimeout(READ_TIMEOUT);
		 * factory.setConnectTimeout(CONNECT_TIMEOUT);
		 * 
		 * HttpClient httpClient = HttpClientBuilder.create()
		 * .setMaxConnTotal(MAX_CONN_TOTAL) .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)
		 * .build();
		 * 
		 * factory.setHttpClient(httpClient); RestTemplate restTemplate = new
		 * RestTemplate(factory); restTemplate.setErrorHandler(new
		 * CResponseErrorHandler());
		 * 
		 * return restTemplate;
		 */
        
    	TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContextBuilder sslcontext = new SSLContextBuilder();
        sslcontext.loadTrustMaterial(null, new TrustSelfSignedStrategy());

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                                                               .loadTrustMaterial(null, acceptingTrustStrategy)
                                                               .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslcontext.build()).setSSLHostnameVerifier(
                NoopHostnameVerifier.INSTANCE)
                                                    .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;


        
    }
    
}