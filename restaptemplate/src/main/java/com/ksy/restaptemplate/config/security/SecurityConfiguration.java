package com.ksy.restaptemplate.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable() // rest api �씠誘�濡� 湲곕낯�꽕�젙 �궗�슜�븞�븿. 湲곕낯�꽕�젙�� 鍮꾩씤利앹떆 濡쒓렇�씤�뤌 �솕硫댁쑝濡� 由щ떎�씠�젆�듃 �맂�떎.
                .csrf().disable() // rest api�씠誘�濡� csrf 蹂댁븞�씠 �븘�슂�뾾�쑝誘�濡� disable泥섎━.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token�쑝濡� �씤利앺븯誘�濡� �꽭�뀡�� �븘�슂�뾾�쑝誘�濡� �깮�꽦�븞�븿.
                .and()
                .authorizeRequests() // �떎�쓬 由ы�섏뒪�듃�뿉 ���븳 �궗�슜沅뚰븳 泥댄겕
                .antMatchers("/*/signin", "/*/signup").permitAll() // 媛��엯 諛� �씤利� 二쇱냼�뒗 �늻援щ굹 �젒洹쇨��뒫
                .antMatchers(HttpMethod.GET, "/exception/**", "helloworld/**").permitAll() // exception異붽� �떆�옉�븯�뒗 GET�슂泥� 由ъ냼�뒪�뒗 �늻援щ굹 �젒洹쇨��뒫
                .antMatchers(HttpMethod.POST, "/*/user").permitAll() // �쉶�썝 媛��엯 �슂泥� �젒洹� 媛��뒫
                .antMatchers(HttpMethod.POST, "/*/auth/signin").permitAll() // �쉶�썝 媛��엯 �슂泥� �젒洹� 媛��뒫
                .anyRequest().hasRole("USER") // 洹몄쇅 �굹癒몄� �슂泥��� 紐⑤몢 �씤利앸맂 �쉶�썝留� �젒洹� 媛��뒫
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()) //�넗�겙�� �젙�긽�씠�굹 �빐�떦 沅뚰븳�쑝濡� �젒洹� 遺덇��뒫�떆 �빖�뱾�윭濡� 蹂대깂.
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token �븘�꽣瑜� id/password �씤利� �븘�꽣 �쟾�뿉 �꽔�뒗�떎
    }

    @Override // ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");

    }
}