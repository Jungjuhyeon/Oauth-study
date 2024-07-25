package Oauth_study.demo.oauth;

import Oauth_study.demo.oauth.dto.OIDCDecodePayload;
import Oauth_study.demo.oauth.dto.OIDCPublicKeysResponse;
import Oauth_study.demo.Member.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthHelper {
    private final OAuthOIDCHelper oauthOIDCHelper;
//    private final OauthProperties oauthProperties;
    private final KakaoOauthClient kakaoOauthClient;

//    @Value("${https://kauth.kakao.com}")
//    private String KakaoBaseUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KakaoAppId;

    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        OIDCPublicKeysResponse oidcPublicKeyResponse = kakaoOauthClient.getKakaoOIDCOpenKeys();

        return oauthOIDCHelper.getPayloadFromIdToken(
                token,
                "https://kauth.kakao.com",
                KakaoAppId,
                oidcPublicKeyResponse
        );
    }

    public OauthInfo getOauthInfoByToken(String idToken) {
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
//        System.out.println(oidcDecodePayload.getNickname());
        return OauthInfo.builder()
                .socialType(SocialType.KAKAO)
                .oid(oidcDecodePayload.getSub())
                .nickname(oidcDecodePayload.getNickname())
//                .profileUrl(oidcDecodePayload.getPicture())
                .build();
    }

}
