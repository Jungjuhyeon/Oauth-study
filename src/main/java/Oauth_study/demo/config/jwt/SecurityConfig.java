package Oauth_study.demo.config.jwt;

import Oauth_study.demo.config.jwt.filter.JwtAuthenticationFilter;
import Oauth_study.demo.config.jwt.handler.CustomAuthenticationEntryPoint;
import Oauth_study.demo.config.redis.util.RedisUtil;
import Oauth_study.demo.config.jwt.handler.CustomAccessDeniedHandler;
import Oauth_study.demo.config.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)  // 인증을 UI로 할 것이 아니라서 disable을 한 것
                .csrf(AbstractHttpConfigurer::disable) // 토큰을 위조하는 것을 방지하기 위함. 하지만 restful api에선 필요 업음.
                .cors(AbstractHttpConfigurer::disable) // CORS 삭젯
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/login/kakao").permitAll()
                        .requestMatchers("/api/v1/auth/kakao").permitAll()
                        .requestMatchers("/api/v1/auth/kakao-login").permitAll()
                        .requestMatchers("/api/v1/auth/google-login").permitAll()
                        .requestMatchers("/api/v1/auth/google").permitAll()
                        .requestMatchers("/api/v1/auth/kakao").permitAll()
                        .requestMatchers("/api/v1/auth/**").hasAnyRole("USER")

                        .requestMatchers("**").permitAll()
                        .anyRequest().authenticated())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT Filter 를 필터체인에 끼워넣어줌
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil,redisUtil), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .build();

    }
}
