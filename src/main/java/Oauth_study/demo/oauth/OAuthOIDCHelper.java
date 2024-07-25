package Oauth_study.demo.oauth;

import Oauth_study.demo.global.exception.BusinessException;
import Oauth_study.demo.jwt.util.JwtOIDCUtil;
import Oauth_study.demo.oauth.dto.OIDCPublicKeyDto;
import Oauth_study.demo.oauth.dto.OIDCPublicKeysResponse;
import Oauth_study.demo.oauth.dto.OIDCDecodePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static Oauth_study.demo.global.exception.errorcode.CommonErrorCode.JWT_INVALID;


@Component
@RequiredArgsConstructor
public class OAuthOIDCHelper {
    private final JwtOIDCUtil jwtOIDCUtil;

    public OIDCDecodePayload getPayloadFromIdToken(
            String token, String iss, String aud, OIDCPublicKeysResponse oidcPublicKeysResponse) {
        String kid = getKidFromUnsignedIdToken(token, iss, aud);

        OIDCPublicKeyDto oidcPublicKeyDto =
                oidcPublicKeysResponse.getKeys().stream()
                        .filter(o -> o.getKid().equals(kid))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(JWT_INVALID));

//        System.out.println(token);
        return jwtOIDCUtil.getOIDCTokenBody(token, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
    }

    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
        return jwtOIDCUtil.getKidFromUnsignedTokenHeader(token, iss, aud);
    }
}