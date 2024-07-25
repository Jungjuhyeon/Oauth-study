package Oauth_study.demo.jwt.util;


import Oauth_study.demo.jwt.LoginService;
import Oauth_study.demo.Member.Member;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    // 토큰 유효시간 30분
    public static final long TOKEN_VALID_TIME = 1000L * 60 * 5 ; // 5분(밀리초)
    public static final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 144; // 일주일(밀리초)
    public static final long REFRESH_TOKEN_VALID_TIME_IN_REDIS = 60 * 60 * 24 * 7; // 일주일 (초)

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String ROLE_CLAIM = "roles";

    private final LoginService loginService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT access 토큰 생성
    public String createAccessToken(Long id ,String roles) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis()+ TOKEN_VALID_TIME))
                .withIssuedAt(new Date(System.currentTimeMillis()))// 토큰 발행 시간 정보

                .withClaim(EMAIL_CLAIM, id)
                .withClaim(ROLE_CLAIM,roles)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }

    // JWT refresh 토큰 생성
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_VALID_TIME))
                .withIssuedAt(new Date(System.currentTimeMillis()))// 토큰 발행 시간 정보
                .sign(Algorithm.HMAC512(secretKey));
    }
    //토큰 파싱
    // 토큰에서 클레임 추출
    private DecodedJWT extractAllClaims(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(secretKey);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    // 토큰에서 id 추출
    public Long getIdFromToken(String token) {
        DecodedJWT decodedJWT = extractAllClaims(token);
        return decodedJWT.getClaim("id").asLong();
    }

    // JWT 토큰에서 인증 정보(권한) 조회
    public Authentication getAuthentication(String token) {
        Long id = getIdFromToken(token);
        Member user = loginService.findUser(id);
        return new UsernamePasswordAuthenticationToken(id, null, List.of(new SimpleGrantedAuthority(user.getRole().getKey())));
    }

    public void isTokenValid(String token){
        JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
    }



}
