package Oauth_study.demo.config.oauth.google;

import Oauth_study.demo.config.oauth.OAuthOIDCHelper;
import Oauth_study.demo.config.oauth.OauthInfo;
import Oauth_study.demo.config.oauth.dto.OIDCDecodePayload;
import Oauth_study.demo.config.oauth.dto.OIDCPublicKeysResponse;
import Oauth_study.demo.config.oauth.google.GoogleOauthClient;
import Oauth_study.demo.member.domain.Enum.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauthHelper {

    private final OAuthOIDCHelper oauthOIDCHelper;
    private final GoogleOauthClient googleOauthClient;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        OIDCPublicKeysResponse oidcPublicKeyResponse = googleOauthClient.getGoogleOIDCOpenKeys();

        return oauthOIDCHelper.getPayloadFromIdToken(
                token, //idToken
                "https://accounts.google.com",  // iss 와 대응되는 값
                googleClientId, // aud 와 대응되는값
                oidcPublicKeyResponse  // 공개키 목록
        );
    }

    public OauthInfo getOauthInfoByToken(String idToken) {
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
        return OauthInfo.builder()
                .socialType(SocialType.GOOGLE)
                .oid(oidcDecodePayload.getSub())
                .nickname(oidcDecodePayload.getNickname())  // 구글에서는 이름이 닉네임으로 사용될 수 있습니다.
                .email(oidcDecodePayload.getEmail())
                .build();
    }

}
