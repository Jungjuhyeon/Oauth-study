package Oauth_study.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Configuration
public class OauthProperties {
    private OauthSecret kakao;

    @Getter
    @Setter
    public static class OauthSecret {
        private String baseUrl;
        private String appId;
    }
    public String getKakaoBaseUrl() {
        return kakao.getBaseUrl();
    }
    public String getKakaoAppId() {
        return kakao.getAppId();
    }
}
