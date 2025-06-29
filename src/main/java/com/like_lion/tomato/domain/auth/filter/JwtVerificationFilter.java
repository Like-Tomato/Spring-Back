package com.like_lion.tomato.domain.auth.filter;

import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.global.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerTocken= request.getHeader("Authorization");
        String accessTocken = jwtService.getTockenFromBearer(bearerTocken);

        if(!StringUtils.hasText(accessTocken)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtService.validate(accessTocken);
        setAuthenticationcontext(accessTocken);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return super.shouldNotFilter(request);
    }

    private void setAuthenticationcontext(String tocken) {
        LikeLionOAuth2User likeLionOAuth2User = jwtService.getPrincipal(tocken);
        OAuth2AuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(
                        likeLionOAuth2User,
                        likeLionOAuth2User.getAuthorities(),
                        likeLionOAuth2User.getProvider()
                );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
