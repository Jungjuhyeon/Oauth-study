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

    @GetMapping("/kakao-login")
    public SuccessResponse<MemberDto.Response.SignIn> kakaoLogin(@RequestParam final String code){
        // Step 1: code로 idToken 가져오기
        String idToken = memberService.code(code);
        System.out.println(idToken);
        // Step 2: idToken으로 로그인 처리
        MemberDto.Response.SignIn response = memberService.login(idToken,"kakao");
        return SuccessResponse.success(response);

    }
    @GetMapping("/google-login")
    public SuccessResponse<MemberDto.Response.SignIn> googleLogin(@RequestParam final String code,
                                                                 @RequestParam String state){
        String idToken = memberService.googlecode(code);
        log.info(" Kakao idToken : {}", idToken);

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

}