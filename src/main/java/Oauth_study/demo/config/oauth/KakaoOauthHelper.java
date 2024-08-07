package Oauth_study.demo.config.oauth;

import Oauth_study.demo.config.oauth.dto.OIDCDecodePayload;
import Oauth_study.demo.config.oauth.dto.OIDCPublicKeysResponse;
import Oauth_study.demo.member.domain.Enum.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthHelper {
    private final OAuthOIDCHelper oauthOIDCHelper;
    private final KakaoOauthClient kakaoOauthClient;

//    @Value("${https://kauth.kakao.com}")
//    private String KakaoBaseUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KakaoAppId;

    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        OIDCPublicKeysResponse oidcPublicKeyResponse = kakaoOauthClient.getKakaoOIDCOpenKeys();

        return oauthOIDCHelper.getPayloadFromIdToken(
                token, //idToken
                "https://kauth.kakao.com",  // iss 와 대응되는 값
                KakaoAppId, // aud 와 대응되는값
                oidcPublicKeyResponse  // 공개키 목록
        );
    }

    public OauthInfo getOauthInfoByToken(String idToken) {
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
        return OauthInfo.builder()
                .socialType(SocialType.KAKAO)
                .oid(oidcDecodePayload.getSub())
                .nickname(oidcDecodePayload.getNickname())
                .build();
    }

}
