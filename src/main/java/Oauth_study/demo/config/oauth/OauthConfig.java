package Oauth_study.demo.config.oauth;


import Oauth_study.demo.config.oauth.client.GoogleOIDCClient;
import Oauth_study.demo.config.oauth.client.GoogleOauthClient;
import Oauth_study.demo.config.oauth.client.Helper.GoogleOauthHelper;
import Oauth_study.demo.config.oauth.client.Helper.KakaoOauthHelper;
import Oauth_study.demo.config.oauth.client.KakaoOauthClient;
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


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    // KakaoOauthHelper 빈으로 등록
    @Bean
    public KakaoOauthHelper kakaoOauthHelper(OAuthOIDCHelper oauthOIDCHelper, KakaoOauthClient kakaoOauthClient) {
        return new KakaoOauthHelper(oauthOIDCHelper, kakaoOauthClient, kakaoClientId, kakaoRedirectUri);
    }

    // GoogleOauthHelper 빈으로 등록 (추가적으로 필요한 경우)
    @Bean
    public GoogleOauthHelper googleOauthHelper(OAuthOIDCHelper oauthOIDCHelper, GoogleOIDCClient googleOIDCClient, GoogleOauthClient googleOauthClient) {
        return new GoogleOauthHelper(oauthOIDCHelper, googleOauthClient,googleOIDCClient, googleClientId, googleClientSecret, googleRedirectUri);
    }
}
