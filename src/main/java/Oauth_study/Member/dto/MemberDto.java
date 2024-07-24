package Oauth_study.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class MemberDto {

    public static class Response{

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class SignIn{
            private String accessToken;
            private String refreshToken;
            private String nickName;

            public static SignIn of(String accessToken,String refreshToken,String nickName){
                return SignIn.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .nickName(nickName)
                        .build();
            }
        }
    }
}
