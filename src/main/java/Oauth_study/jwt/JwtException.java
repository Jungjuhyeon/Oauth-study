package Oauth_study.jwt;

import Oauth_study.global.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum JwtException {
    // 인증 처리
    JWT_EMPTY(HttpStatus.UNAUTHORIZED,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"JWT4101","다시 로그인 해주세요.(토큰이 유효하지 않습니다.)"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT4102","토큰이 만료되었습니다."),
    JWT_BAD(HttpStatus.UNAUTHORIZED,"JWT4103","JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"JWT4104","RefreshToken이 일치하지 않습니다."),
    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED,"JWT4105","권한이 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public void setResponse(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.valueOf(createJwtExceptionBody(this)));
    }

    private static ErrorResponse createJwtExceptionBody(JwtException exception) {
        return ErrorResponse.of(exception);
    }
}