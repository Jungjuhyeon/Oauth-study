package Oauth_study.demo.jwt.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import Oauth_study.demo.jwt.JwtException;

import java.io.IOException;

@Slf4j

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        log.error("exception : " + exception);

        /**
         * 토큰 없는 경우
         */
        if (exception == null) {
            log.info("[NULL TOKEN]");
            JwtException.JWT_EMPTY.setResponse(response); // 토큰이 없음
            return;
        }

        else if ("로그아웃 되었습니다. 재 로그인하세요.".equals(exception)){
            log.info("[LOGOUT TOKEN]");
            JwtException.JWT_INVALID.setResponse(response);
        }
    }
}