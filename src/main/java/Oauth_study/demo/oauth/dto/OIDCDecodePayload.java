package Oauth_study.demo.oauth.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class OIDCDecodePayload {
    private String iss;
    private String aud;
    private String sub;
    private String nickname;
    private String email;
//    private String picture;
}
