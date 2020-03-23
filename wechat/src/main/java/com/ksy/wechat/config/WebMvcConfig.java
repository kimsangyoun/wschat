package com.ksy.wechat.config;

import com.ksy.wechat.interceptor.FirstInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override  //인터셉터 등록
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> URL_PATTERNS = Arrays.asList("/sign", "/signin", "/signup");
        registry.addInterceptor(new FirstInterceptor())  //만들어준 인터셉터
                .addPathPatterns("/**") // 모든 요청 다 포함.
                .excludePathPatterns(URL_PATTERNS) // sign 관련 된 부분의 경우 제외.
                .excludePathPatterns("/template/**"); // sign의 경우 제외.

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
