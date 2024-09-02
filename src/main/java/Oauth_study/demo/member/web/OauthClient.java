package Oauth_study.demo.member.web;

import Oauth_study.demo.member.web.dto.TokenRequest;

public interface OauthClient {
    TokenRequest of(String code);
    String getIdToken(TokenRequest tokenRequest);
}
