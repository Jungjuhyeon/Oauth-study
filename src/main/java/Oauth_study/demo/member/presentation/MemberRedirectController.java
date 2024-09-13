package Oauth_study.demo.member.presentation;

import Oauth_study.demo.member.web.OauthConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberRedirectController {
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
    //랜던 값 생성
    private String generateRandomState() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] state = new byte[16];
        secureRandom.nextBytes(state);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(state);
    }
}
