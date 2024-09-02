package Oauth_study.demo.member.web;

import Oauth_study.demo.member.web.dto.TokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
public class GoogleOauthClient implements OauthClient {

    private final WebClient webClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    public GoogleOauthClient(String clientId, String clientSecret, String redirectUri, String tokenUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.webClient = WebClient.builder()
                .baseUrl(tokenUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }

    @Override
    public TokenRequest of(String code) {
        return new TokenRequest(clientId, redirectUri, code);
    }

    @Override
    public String getIdToken(TokenRequest tokenRequest) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("grant_type", tokenRequest.getGrantType())
                        .queryParam("client_id", tokenRequest.getClientId())
                        .queryParam("client_secret", clientSecret)
                        .queryParam("redirect_uri", tokenRequest.getRedirectUri())
                        .queryParam("code", tokenRequest.getCode())
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseBody -> (String) responseBody.get("id_token"))  // 수정된 부분
                .block();
    }
}
