package Oauth_study.demo.member.infrastructure;

import Oauth_study.demo.config.oauth.OauthInfo;
import Oauth_study.demo.member.application.MemberRepository;
import Oauth_study.demo.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    @Transactional
    public Member findByOauthInfoOid(String Oid, OauthInfo oauthInfo) {
        return memberJpaRepository.findByOauthInfoOid(Oid).orElseGet(()-> forceJoin(oauthInfo));
    }

    @Override
    @Transactional
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Transactional
    public Member forceJoin(OauthInfo oauthInfo) {
        Member newMember = Member.create(oauthInfo);
        return save(newMember);
    }
    @Override
    public Member findById(Long id) {
        return memberJpaRepository.findById(id).orElse(null);
    }

}
