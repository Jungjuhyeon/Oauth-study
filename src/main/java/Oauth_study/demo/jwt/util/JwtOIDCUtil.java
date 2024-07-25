package Oauth_study.demo.jwt.util;

import Oauth_study.demo.global.exception.BusinessException;
import Oauth_study.demo.oauth.dto.OIDCDecodePayload;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import static Oauth_study.demo.global.exception.errorcode.CommonErrorCode.*;

@Service
@Getter
@Slf4j
public class JwtOIDCUtil {
    private final String KID = "kid";

    /**
     * 미인증 토큰에서 Kid를 가져옴
     */
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        DecodedJWT decodedJWT = getUnsignedTokenClaims(token, iss, aud);
        return decodedJWT.getHeaderClaim(KID).asString();
    }

    //Header, Payload, Signature 를 분리하고 검증한다.
    private String getUnsignedToken(String token) {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new BusinessException(JWT_BAD);
        return splitToken[0] + "." + splitToken[1] + ".";
    }


    // JwtOIDCProvider
    private DecodedJWT getUnsignedTokenClaims(String token, String iss, String aud) {
        try {
            DecodedJWT decodedJWT = JWT.decode(getUnsignedToken(token));
            if (!decodedJWT.getAudience().contains(aud)) {
                log.error("[2]");
                throw new BusinessException(JWT_INVALID);
            }
            if (!decodedJWT.getIssuer().equals(iss)) {
                log.error("[3]");
                throw new BusinessException(JWT_INVALID);
            }
            return decodedJWT;
        } catch (JWTDecodeException e) {

            throw new BusinessException(JWT_BAD);
        }
    }

    public OIDCDecodePayload getOIDCTokenBody(String token, String modulus, String exponent) {
        DecodedJWT decodedJWT = getOIDCTokenJws(token, modulus, exponent);
//        System.out.println(decodedJWT.getClaim("nickname").asString());
        return new OIDCDecodePayload(
                decodedJWT.getIssuer(),
                decodedJWT.getAudience().get(0),
                decodedJWT.getSubject(),
                decodedJWT.getClaim("nickname").asString(),
                decodedJWT.getClaim("email").asString());
    }
    public DecodedJWT getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) getRSAPublicKey(modulus, exponent), null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (TokenExpiredException e) {
            throw new BusinessException(JWT_EXPIRED);
        } catch (Exception e) {
            throw new BusinessException(JWT_BAD);
        }
    }

    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }
}
