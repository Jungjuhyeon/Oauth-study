package Oauth_study.Member.controller;

import Oauth_study.global.response.SuccessResponse;
import Oauth_study.Member.dto.MemberDto;
import Oauth_study.Member.service.AuthService;
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
    @Value("${kakao.client.id}")
    String clientId;
    @Value("${kakao.redirect.uri}")
    String redirectUri;
    @Value("${kakao.client.secret}")
    String clientSecret;

    private final AuthService authService;


    @GetMapping("/login/kakao")
    public SuccessResponse<MemberDto.Response.SignIn> loginByNaver(@RequestParam(name = "idToken") String idToken) {
        MemberDto.Response.SignIn response = authService.login(idToken);
        return SuccessResponse.success(response);
    }
}