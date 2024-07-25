package Oauth_study.demo.Member;

import Oauth_study.demo.oauth.OauthInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private OauthInfo oauthInfo;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Member(OauthInfo oauthInfo){
        this.role = Role.USER;
        this.oauthInfo = oauthInfo;
    }

    public static Member create(OauthInfo oauthInfo){
        return new Member(oauthInfo);
    }


}