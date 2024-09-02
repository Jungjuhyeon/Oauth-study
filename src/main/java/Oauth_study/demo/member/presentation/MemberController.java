package Oauth_study.demo.member.presentation;

import Oauth_study.demo.member.dto.MemberDto;
import Oauth_study.demo.member.application.MemberService;
import Oauth_study.demo.global.response.SuccessResponse;
import Oauth_study.demo.member.web.OauthConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final OauthConfig oauthConfig;

    @GetMapping("/kakao")
    public void initiateKakaoLogin(HttpServletResponse response) throws IOException {
        String authUrl = "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + oauthConfig.getKakaoClientId() +
                "&redirect_uri=" + oauthConfig.getKakaoRedirectUri() +
                "&response_type=code";

        log.info("Redirecting to Kakao OAuth2 URL: {}", authUrl);
        response.sendRedirect(authUrl);
    }

    @GetMapping("/kakao-login")
    public SuccessResponse<MemberDto.Response.SignIn> kakaoLogin(@RequestParam final String code){
        // Step 1: code로 idToken 가져오기
        String idToken = memberService.code(code);
        // Step 2: idToken으로 로그인 처리
        MemberDto.Response.SignIn response = memberService.login(idToken,"kakao");
        return SuccessResponse.success(response);

    }
    @GetMapping("/google")
    public void initiateGoogleLogin(HttpServletResponse response) throws IOException {
        String state = generateRandomState(); // 랜덤 state 값 생성

        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + oauthConfig.getGoogleClientId() +
                "&redirect_uri=" + oauthConfig.getGoogleRedirectUri() +
                "&response_type=code" +
                "&scope=openid%20email%20profile" +
                "&state=" + state;

        log.info("Redirecting to Google OAuth2 URL: {}", authUrl);
        response.sendRedirect(authUrl);
    }
    @GetMapping("/google-login")
    public SuccessResponse<MemberDto.Response.SignIn> googleLogin(@RequestParam final String code,
                                                                 @RequestParam String state){
        String idToken = memberService.googlecode(code);
        MemberDto.Response.SignIn response = memberService.login(idToken,"google");
        return SuccessResponse.success(response);

    }

    //
    @PostMapping("/reissue")
    public SuccessResponse<MemberDto.Response.Reissue> reissue(@RequestHeader("Authorization") String refreshToken){
        MemberDto.Response.Reissue response = memberService.reissue(refreshToken);
        return SuccessResponse.success(response);
    }

    @PostMapping("/logout")
    public SuccessResponse<String> logout(@RequestHeader("Authorization") String accessToken){
        memberService.logout(accessToken);
        return SuccessResponse.success("로그아웃 성공");
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal Long userId){
        System.out.println("ji");
        System.out.println(userId);
    }

    //랜던 값 생성
    private String generateRandomState() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] state = new byte[16];
        secureRandom.nextBytes(state);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(state);
    }
}