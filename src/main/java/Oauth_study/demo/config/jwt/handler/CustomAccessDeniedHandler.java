package Oauth_study.demo.config.jwt.handler;

import Oauth_study.demo.config.jwt.filter.JwtAuthenticationFilter;
import Oauth_study.demo.global.exception.errorcode.CommonErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    /***
     * '인가'가 실패했을 때의 메서드.
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[ACCESS DENIED]");
        JwtAuthenticationFilter.setErrorResponse(response, CommonErrorCode.JWT_AUTHORIZATION_FAILED);
    }
}
