package Oauth_study.demo.Member.service;

import Oauth_study.demo.Member.Member;
import Oauth_study.demo.Member.dto.MemberDto;
import Oauth_study.demo.oauth.OauthInfo;

public interface AuthService {
    MemberDto.Response.SignIn login(String idToken);
    Member forceJoin(OauthInfo oauthInfo);

    void getToken(String code);

    MemberDto.Response.Reissue reissue(String refreshToken);

    void logout(String accessToken);
}
