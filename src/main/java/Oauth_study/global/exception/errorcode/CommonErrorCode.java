package Oauth_study.global.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    // 공용 처리
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "4000", "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "4040", "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "알수없는 에러 관리자에게 문의"),

    // 인증 처리
    JWT_EMPTY(HttpStatus.UNAUTHORIZED,"JWT4100","JWT 토큰을 넣어주세요."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"JWT4101","다시 로그인 해주세요.(토큰이 유효하지 않습니다.)"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT4102","토큰이 만료되었습니다."),
    JWT_BAD(HttpStatus.UNAUTHORIZED,"JWT4103","JWT 토큰이 잘못되었습니다."),
    JWT_REFRESHTOKEN_NOT_MATCH(HttpStatus.UNAUTHORIZED,"JWT4104","RefreshToken이 일치하지 않습니다."),
    JWT_AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED,"JWT4105","권한이 없습니다."),

    //user error (4001~
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"4001","해당 유저를 찾을 수 없습니다."),


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
