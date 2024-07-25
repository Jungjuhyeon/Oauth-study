package Oauth_study.demo.Member.controller;

import Oauth_study.demo.Member.dto.MemberDto;
import Oauth_study.demo.Member.service.AuthService;
import Oauth_study.demo.global.response.SuccessResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {

    private final AuthService authService;


    @GetMapping("/login/kakao")
    public SuccessResponse<MemberDto.Response.SignIn> loginByNaver(@RequestParam(name = "idToken") String idToken) {
        MemberDto.Response.SignIn response = authService.login(idToken);
        return SuccessResponse.success(response);
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
}