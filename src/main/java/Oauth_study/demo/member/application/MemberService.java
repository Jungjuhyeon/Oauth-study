package Oauth_study.demo.member.application;

import Oauth_study.demo.member.domain.Member;
import Oauth_study.demo.member.dto.MemberDto;
import Oauth_study.demo.config.oauth.OauthInfo;

public interface MemberService {
    MemberDto.Response.SignIn login(String idToken);
    Member forceJoin(OauthInfo oauthInfo);

    void getToken(String code);

    MemberDto.Response.Reissue reissue(String refreshToken);

    void logout(String accessToken);
}
