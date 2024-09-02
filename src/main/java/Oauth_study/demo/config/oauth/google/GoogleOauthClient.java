package Oauth_study.demo.config.oauth.google;

import Oauth_study.demo.config.oauth.dto.OIDCPublicKeysResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "GoogleAuthClient",
        url = "https://www.googleapis.com")
public interface GoogleOauthClient {

    @GetMapping("/oauth2/v3/certs")
    OIDCPublicKeysResponse getGoogleOIDCOpenKeys();
}

