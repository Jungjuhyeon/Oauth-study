package Oauth_study.Member.service;

import Oauth_study.oauth.OauthInfo;
import Oauth_study.Member.Member;
import Oauth_study.Member.dto.MemberDto;

public interface AuthService {
    MemberDto.Response.SignIn login(String idToken);
    Member forceJoin(OauthInfo oauthInfo);
}
