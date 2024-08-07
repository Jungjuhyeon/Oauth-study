package Oauth_study.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableJpaAuditing
public class OauthStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthStudyApplication.class, args);
    }

}
