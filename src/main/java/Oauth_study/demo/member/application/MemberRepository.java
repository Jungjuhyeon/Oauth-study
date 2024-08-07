package Oauth_study.demo.member.application;

import Oauth_study.demo.config.oauth.OauthInfo;
import Oauth_study.demo.member.domain.Member;

public interface MemberRepository {
    public Member findByOauthInfoOid(String Oid, OauthInfo oauthInfo);
    public Member save(Member member);

    public Member findById(Long id);

}
