package Oauth_study.demo.oauth;

import Oauth_study.demo.Member.SocialType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.*;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String oid;

    private String nickname;

    @Builder
    public OauthInfo(SocialType socialType, String oid, String nickname){
        this.socialType =socialType;
        this.oid =oid;
        this.nickname = nickname;
    }
}
