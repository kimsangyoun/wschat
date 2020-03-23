package com.ksy.wechat.interceptor;

import com.ksy.wechat.utill.CookieUtill;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class FirstInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final HttpSession session = Optional.ofNullable(request).map(HttpServletRequest::getSession).orElse(null);  //세션 가져오는 부분
        String token = CookieUtill.getValue(request, "accesstoken");
        System.out.println("First Interceptor pre Handler ::" + token);
        if (token == null || token.isEmpty()) { //토큰이 없을 경우 리다이렉트
            if (isAjaxRequest(request)) { // 만약 ajax 요청이라면
                response.sendError(-1);
                return false;
            } else { //비동기 요청이 아닐 경우
                response.sendRedirect(request.getContextPath() + "/sign");
                return false;
            }

        }
        //여기서 쿠키로 꺼내서 쓰까...

        return true;
    }

    private boolean isAjaxRequest(HttpServletRequest req) {
        String header = req.getHeader("AJAX");
        if ("true".equals(header)) {
            return true;
        } else {
            return false;
        }
    }

}
