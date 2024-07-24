package Oauth_study.oauth;

import Oauth_study.global.exception.BusinessException;
import Oauth_study.jwt.util.JwtOIDCUtil;
import Oauth_study.oauth.dto.OIDCDecodePayload;
import Oauth_study.oauth.dto.OIDCPublicKeyDto;
import Oauth_study.oauth.dto.OIDCPublicKeysResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static Oauth_study.global.exception.errorcode.CommonErrorCode.JWT_INVALID;


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

        return jwtOIDCUtil.getOIDCTokenBody(token, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
    }

    private String getKidFromUnsignedIdToken(String token, String iss, String aud) {
        return jwtOIDCUtil.getKidFromUnsignedTokenHeader(token, iss, aud);
    }
}