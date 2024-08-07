package Oauth_study.demo.member.infrastructure;

import Oauth_study.demo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInfoOid(String oid);
}
