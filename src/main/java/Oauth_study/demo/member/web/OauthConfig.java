package Oauth_study.demo.member.web;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Getter
public class OauthConfig {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String kakaoTokenUrl;


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUrl;
    @Bean
    public KakaoOauthClient kakaoOauthClient() {
        return new KakaoOauthClient(kakaoClientId, kakaoRedirectUri, kakaoTokenUrl);
    }
    @Bean
    public GoogleOauthClient googleOauthClient() {
        return new GoogleOauthClient(googleClientId, googleClientSecret, googleRedirectUri, googleTokenUrl);
    }
}
