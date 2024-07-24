package Oauth_study.oauth.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class OIDCDecodePayload {
    private String iss;
    private String aud;
    private String sub;
    private String email;
    private String nickname;
//    private String picture;
}
