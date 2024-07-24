package Oauth_study.Member.repository;

import Oauth_study.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInfoOid(String oid);
}
