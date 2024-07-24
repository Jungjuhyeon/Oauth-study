package Oauth_study.oauth;

import Oauth_study.Member.SocialType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.*;


@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthInfo {

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
