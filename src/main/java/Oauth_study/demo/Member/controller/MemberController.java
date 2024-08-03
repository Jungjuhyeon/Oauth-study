package Oauth_study.demo.Member.controller;

import Oauth_study.demo.Member.dto.MemberDto;
import Oauth_study.demo.Member.service.AuthService;
import Oauth_study.demo.global.response.SuccessResponse;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final AuthService authService;

    //로그인
    @GetMapping("/login/kakao")
    public SuccessResponse<MemberDto.Response.SignIn> loginByNaver(@RequestParam(name = "idToken") String idToken) {
        MemberDto.Response.SignIn response = authService.login(idToken);
        return SuccessResponse.success(response);
    }

    //
    @PostMapping("/reissue")
    public SuccessResponse<MemberDto.Response.Reissue> reissue(@RequestHeader("Authorization") String refreshToken){
        MemberDto.Response.Reissue response = authService.reissue(refreshToken);
        return SuccessResponse.success(response);
    }

    @PostMapping("/logout")
    public SuccessResponse<String> logout(@RequestHeader("Authorization") String accessToken){
        authService.logout(accessToken);
        return SuccessResponse.success("로그아웃 성공");
    }

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) {
        authService.getToken(code);
//        System.out.println(code);
    }

    @PostMapping("/test")
    public void test(){
        System.out.println("Gg");
    }
}