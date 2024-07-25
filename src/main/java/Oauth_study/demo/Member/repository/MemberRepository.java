package Oauth_study.demo.Member.repository;

import Oauth_study.demo.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauthInfoOid(String oid);
}
