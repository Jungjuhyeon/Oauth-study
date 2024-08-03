package Oauth_study.demo.Member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDto {

    public static class Response{
        @Builder
        @Getter
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

        @Builder
        @Getter
        public static class Reissue{
            private String accessToken;
            private String refreshToken;

            public static Reissue of(String accessToken,String refreshToken){
                return Reissue.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
    }
}
