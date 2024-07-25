package Oauth_study.demo.Member.service;

import Oauth_study.demo.Member.Member;
import Oauth_study.demo.Member.Role;
import Oauth_study.demo.Member.dto.MemberDto;
import Oauth_study.demo.Member.repository.MemberRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final KakaoOauthHelper kakaoOauthHelper;
    private final JwtUtil jwtUtil;
//    private final Aut\


    @Override
    public MemberDto.Response.SignIn login(String idToken){
        OauthInfo oauthInfo = kakaoOauthHelper.getOauthInfoByToken(idToken);
        Member member = memberRepository.findByOauthInfoOid(oauthInfo.getOid()).orElseGet(()-> forceJoin(oauthInfo));

//        String refreshToken = redisUtil.getData(RT + findMember.getId());
//        if (refreshToken == null) {
//            refreshToken = jwtUtil.createToken(findMember.getId().toString(), TokenType.REFRESH_TOKEN);
//            redisUtil.setData(RT + findMember.getId(), refreshToken, jwtUtil.getExpiration(TokenType.REFRESH_TOKEN));
//        }
        return MemberDto.Response.SignIn.of(
                jwtUtil.createAccessToken(member.getId(), "ROLE_USER"),
                member.getOauthInfo().getNickname());

    }

    @Override
    public Member forceJoin(OauthInfo oauthInfo) {
        Member newMember = Member.create(oauthInfo);
        return memberRepository.save(newMember);
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
