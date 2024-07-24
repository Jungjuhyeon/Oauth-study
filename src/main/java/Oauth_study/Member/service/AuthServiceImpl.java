package Oauth_study.Member.service;

import Oauth_study.oauth.KakaoOauthHelper;
import Oauth_study.oauth.OauthInfo;
import Oauth_study.Member.Member;
import Oauth_study.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final KakaoOauthHelper kakaoOauthHelper;
//    private final Aut

    /*
    @Override
    public MemberDto.Response.SignIn login(String idToken){
        OauthInfo oauthInfo = kakaoOauthHelper.getOauthInfoByToken(idToken);
        Member member = memberRepository.findByOauthInfoOid(oauthInfo.getOid()).orElseGet(()-> forceJoin(oauthInfo));

    }


     */

    @Override
    public Member forceJoin(OauthInfo oauthInfo) {
        Member newMember = Member.create(oauthInfo);
        return memberRepository.save(newMember);
    }
}
