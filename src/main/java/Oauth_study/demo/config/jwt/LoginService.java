package Oauth_study.demo.config.jwt;

import Oauth_study.demo.member.application.MemberRepository;
import Oauth_study.demo.member.domain.Member;
import Oauth_study.demo.member.infrastructure.MemberRepositoryImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService{

    private final MemberRepository userRepository;

    public Member findUser(Long id){
        return userRepository.findById(id);
    }
}