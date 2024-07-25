package Oauth_study.demo.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_ADMIN"), USER("ROLE_USER");

    private final String key;

}
