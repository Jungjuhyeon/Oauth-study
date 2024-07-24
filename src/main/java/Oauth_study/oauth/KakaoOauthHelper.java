package Oauth_study.oauth;

import Oauth_study.oauth.dto.OIDCDecodePayload;
import Oauth_study.oauth.dto.OIDCPublicKeysResponse;
import Oauth_study.Member.SocialType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthHelper {
    private final OAuthOIDCHelper oauthOIDCHelper;
    private final OauthProperties oauthProperties;
    private final KakaoOauthClient kakaoOauthClient;



    public OIDCDecodePayload getOIDCDecodePayload(String token) {
        OIDCPublicKeysResponse oidcPublicKeyResponse = kakaoOauthClient.getKakaoOIDCOpenKeys();

        return oauthOIDCHelper.getPayloadFromIdToken(
                token,
                oauthProperties.getKakaoBaseUrl(),
                oauthProperties.getKakaoAppId(),
                oidcPublicKeyResponse
        );
    }

    public OauthInfo getOauthInfoByToken(String idToken) {
        OIDCDecodePayload oidcDecodePayload = getOIDCDecodePayload(idToken);
        return OauthInfo.builder()
                .socialType(SocialType.KAKAO)
                .oid(oidcDecodePayload.getSub())
                .nickname(oidcDecodePayload.getNickname())
//                .profileUrl(oidcDecodePayload.getPicture())
                .build();
    }

}
