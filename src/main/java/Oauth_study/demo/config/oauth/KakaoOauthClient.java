package Oauth_study.demo.config.oauth;

import Oauth_study.demo.config.oauth.dto.OIDCPublicKeysResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoAuthClient",
        url = "https://kauth.kakao.com")
//=        configuration = KakaoKauthConfig.class)
public interface KakaoOauthClient {
//    @Cacheable(cacheNames = "KakaoOICD", cacheManager = "oidcCacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeysResponse getKakaoOIDCOpenKeys();
}
