package Oauth_study.demo.config.oauth.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class OIDCPublicKeysResponse {
    List<OIDCPublicKeyDto> keys;
}

