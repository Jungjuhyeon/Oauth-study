package Oauth_study.oauth;

import Oauth_study.oauth.dto.OIDCPublicKeysResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(
        name = "KakaoAuthClient",
        url = "https://kauth.kakao.com")
//        configuration = KakaoKauthConfig.class)
public interface KakaoOauthClient {
//    @Cacheable(cacheNames = "KakaoOICD", cacheManager = "oidcCacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeysResponse getKakaoOIDCOpenKeys();
}
