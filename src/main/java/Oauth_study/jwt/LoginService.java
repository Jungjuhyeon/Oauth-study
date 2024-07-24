package Oauth_study.jwt;

import Oauth_study.Member.Member;
import Oauth_study.Member.repository.MemberRepository;
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
        Optional<Member> optionalUser = userRepository.findById(id);

        return optionalUser.orElse(null);
    }
}