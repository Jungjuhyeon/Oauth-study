package Oauth_study.demo.jwt.filter;

import Oauth_study.demo.jwt.JwtException;
import Oauth_study.demo.jwt.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = resolveToken(request);

        if (token != null) {
            try {
                jwtUtil.isTokenValid(token);
                //블랙리스트 처리 x
                Authentication authentication = jwtUtil.getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch (TokenExpiredException e) {
                //토큰의 유효기간 만료
                log.error("만료된 토큰입니다");
                JwtException.JWT_EXPIRED.setResponse(response);
                return;
            }catch (JWTVerificationException e){
                log.error("유효하지 않은 토큰입니다");
                JwtException.JWT_INVALID.setResponse(response);

            }

        }
        filterChain.doFilter(request, response);

    }

    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
