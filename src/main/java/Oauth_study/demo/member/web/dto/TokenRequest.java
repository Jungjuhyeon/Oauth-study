package Oauth_study.demo.member.web.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private final String clientId;
    private final String redirectUri;
    private final String code;
    private String grantType = "authorization_code";

    public TokenRequest(String clientId, String redirectUri, String code) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
    }

}
