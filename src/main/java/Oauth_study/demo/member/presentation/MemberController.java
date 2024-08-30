package Oauth_study.demo.member.presentation;

import Oauth_study.demo.member.dto.MemberDto;
import Oauth_study.demo.member.application.MemberService;
import Oauth_study.demo.global.response.SuccessResponse;
import lombok.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;

    //code로 idtoken 가져오기
    @GetMapping("/kakao-login")
    public SuccessResponse<MemberDto.Response.SignIn> codelogin(@RequestParam final String code){
        // Step 1: code로 idToken 가져오기
        String idToken = memberService.code(code);
        // Step 2: idToken으로 로그인 처리
        MemberDto.Response.SignIn response = memberService.login(idToken);
        return SuccessResponse.success(response);

    }


    //로그인
    @GetMapping("/login/kakao")
    public SuccessResponse<MemberDto.Response.SignIn> loginByNaver(@RequestParam(name = "idToken") String idToken) {
        MemberDto.Response.SignIn response = memberService.login(idToken);
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