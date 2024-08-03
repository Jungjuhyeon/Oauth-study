package Oauth_study.demo.Member.service;

import Oauth_study.demo.Member.Member;
import Oauth_study.demo.Member.Role;
import Oauth_study.demo.Member.dto.MemberDto;
import Oauth_study.demo.Member.repository.MemberRepository;
import Oauth_study.demo.config.redis.util.RedisUtil;
import Oauth_study.demo.global.exception.BusinessException;
import Oauth_study.demo.jwt.util.JwtUtil;
import Oauth_study.demo.oauth.KakaoOauthHelper;
import Oauth_study.demo.oauth.OauthInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static Oauth_study.demo.global.exception.errorcode.CommonErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final KakaoOauthHelper kakaoOauthHelper;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private static final String RT = "RT:";
    private static final String LOGOUT = "LOGOUT:";
    private static final String ROLE_USER = "ROLE_USER";




    @Override
    public MemberDto.Response.SignIn login(String idToken){
        OauthInfo oauthInfo = kakaoOauthHelper.getOauthInfoByToken(idToken);
        Member member = memberRepository.findByOauthInfoOid(oauthInfo.getOid()).orElseGet(()-> forceJoin(oauthInfo));

        return MemberDto.Response.SignIn.of(
                jwtUtil.createAccessToken(member.getId(), ROLE_USER),
                getOrGenerateRefreshToken(member)
                ,member.getOauthInfo().getNickname());

    }

    @Override
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

    @Override
    public void logout(String accessToken){
        String resolveToken = jwtUtil.resolveToken(accessToken);
        Long userIdInToken = jwtUtil.getIdFromToken(resolveToken);
        String refreshTokenInRedis = redisUtil.getData(RT+userIdInToken);

        if (refreshTokenInRedis == null) throw new BusinessException(REFRESH_TOKEN_NOT_FOUND);

        redisUtil.deleteDate(RT+ userIdInToken);
        redisUtil.setData(LOGOUT+resolveToken, LOGOUT, jwtUtil.getExpiration(resolveToken));// 블랙리스트 처리
    }



    @Override
    public Member forceJoin(OauthInfo oauthInfo) {
        Member newMember = Member.create(oauthInfo);
        return memberRepository.save(newMember);
    }

    private String getOrGenerateRefreshToken(Member member){
        String refreshToken = redisUtil.getData(RT + member.getId());

        if (refreshToken == null) {
            refreshToken = jwtUtil.createRefreshToken(member.getId());
            redisUtil.setData(RT + member.getId(), refreshToken, jwtUtil.REFRESH_TOKEN_VALID_TIME);
        }
        return refreshToken;
    }



    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String reqURL;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirect_uri;



    //test를 위한 code를 줬을때 응답값
    @Override
    public void getToken(String code){
        String access_Token = "";
        String refresh_Token = "";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+client_id); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri="+redirect_uri); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

