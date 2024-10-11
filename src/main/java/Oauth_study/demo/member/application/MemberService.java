package Oauth_study.demo.member.application;

import Oauth_study.demo.config.oauth.client.Helper.GoogleOauthHelper;
import Oauth_study.demo.config.oauth.client.Helper.KakaoOauthHelper;
import Oauth_study.demo.member.domain.Member;
import Oauth_study.demo.member.dto.MemberDto;
import Oauth_study.demo.config.redis.util.RedisUtil;
import Oauth_study.demo.global.exception.BusinessException;
import Oauth_study.demo.config.jwt.util.JwtUtil;
import Oauth_study.demo.config.oauth.OauthInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static Oauth_study.demo.global.exception.errorcode.CommonErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final KakaoOauthHelper kakaoOauthHelper;
    private final GoogleOauthHelper googleOauthHelper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private static final String RT = "RT:";
    private static final String LOGOUT = "LOGOUT:";
    private static final String ROLE_USER = "ROLE_USER";

    public String kakaoCode(String code){
        return kakaoOauthHelper.getKakaoIdToken(code);
    }

    public String googleCode(String code){
        return googleOauthHelper.getGoogleIdToken(code);
    }
    @Transactional
    public MemberDto.Response.SignIn login(String idToken,String type){
        OauthInfo oauthInfo;
        if(type.equals("kakao")){
            oauthInfo = kakaoOauthHelper.getOauthInfoByToken(idToken);
        }else{
            oauthInfo = googleOauthHelper.getOauthInfoByToken(idToken);
        }
        Member member = memberRepository.findByOauthInfoOid(oauthInfo.getOid(),oauthInfo);

        return MemberDto.Response.SignIn.of(
                jwtUtil.createAccessToken(member.getId(), ROLE_USER),
                getOrGenerateRefreshToken(member)
                ,member.getOauthInfo().getNickname());
    }


    @Transactional
    public MemberDto.Response.Reissue reissue(String refreshToken){
        String resolveToken = jwtUtil.resolveToken(refreshToken);
        Long userIdInToken = jwtUtil.getIdFromToken(resolveToken);

        String refreshTokenInRedis = redisUtil.getData(RT+userIdInToken);

        if(!resolveToken.equals(refreshTokenInRedis)){
            throw new BusinessException(JWT_REFRESHTOKEN_NOT_MATCH);
        }

        String newRefreshToken =jwtUtil.createRefreshToken(userIdInToken);
        String newAccessToken = jwtUtil.createAccessToken(userIdInToken, ROLE_USER);
        redisUtil.setData(RT+userIdInToken,newRefreshToken,jwtUtil.REFRESH_TOKEN_VALID_TIME);

        return MemberDto.Response.Reissue.of(newRefreshToken,newAccessToken);
    }

    @Transactional
    public void logout(String accessToken){
        String resolveToken = jwtUtil.resolveToken(accessToken);
        Long userIdInToken = jwtUtil.getIdFromToken(resolveToken);
        String refreshTokenInRedis = redisUtil.getData(RT+userIdInToken);

        if (refreshTokenInRedis == null) throw new BusinessException(REFRESH_TOKEN_NOT_FOUND);

        redisUtil.deleteDate(RT+ userIdInToken);
        redisUtil.setData(LOGOUT+resolveToken, LOGOUT, jwtUtil.getExpiration(resolveToken));// 블랙리스트 처리
    }


    @Transactional
    protected String getOrGenerateRefreshToken(Member member){
        String refreshToken = redisUtil.getData(RT + member.getId());

        if (refreshToken == null) {
            refreshToken = jwtUtil.createRefreshToken(member.getId());
            redisUtil.setData(RT + member.getId(), refreshToken, jwtUtil.REFRESH_TOKEN_VALID_TIME);
        }
        return refreshToken;
    }
}

