package Oauth_study.demo.config.oauth.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

// 토큰을 가져오는 클라이언트
@FeignClient(name = "GoogleOauthClient", url = "https://oauth2.googleapis.com")
public interface GoogleOauthClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    Map<String, Object> getIdToken(@RequestParam("grant_type") String grantType,
                                   @RequestParam("client_id") String clientId,
                                   @RequestParam("redirect_uri") String redirectUri,
                                   @RequestParam("code") String code,
                                   @RequestParam("client_secret") String clientSecret);
}