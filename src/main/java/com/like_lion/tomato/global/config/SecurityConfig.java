package com.like_lion.tomato.global.config;

import com.like_lion.tomato.global.auth.filter.JwtExceptionFilter;
import com.like_lion.tomato.global.auth.filter.JwtVerificationFilter;
import com.like_lion.tomato.global.auth.handler.JwtAccessDeniedHandler;
import com.like_lion.tomato.global.auth.handler.JwtLogoutSuccessHandler;
import com.like_lion.tomato.global.auth.handler.OAuth2EntryPorint;
import com.like_lion.tomato.global.auth.handler.OAuth2LoginSuccessHandler;
import com.like_lion.tomato.global.auth.service.LikeLionOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final LikeLionOauth2UserService likeLionOauth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2EntryPorint oAuth2EntryPorint;
    private final JwtLogoutSuccessHandler jwtLogoutSuccessHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtVerificationFilter jwtVerificationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("MASTER").implies("ADMIN")
                .role("ADMIN").implies("MEMBER")
                .role("MEMBER").implies("GUEST")
                .build();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 경로별 인가
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1").permitAll()
                        .requestMatchers("/api/v1/master/**").hasAnyRole("MASTER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/member/**").hasRole("MEMBER")
                        .anyRequest().authenticated());


        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);
        http
                .sessionManagement((session)-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig
                                        .userService(likeLionOauth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .logout(logout ->
                        logout
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .logoutUrl("/api/v1/auth/logout")
                                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                )
                .addFilterBefore(jwtVerificationFilter, OAuth2AuthorizationRequestRedirectFilter.class)

                .addFilterBefore(jwtExceptionFilter, JwtVerificationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(oAuth2EntryPorint)
                                .accessDeniedHandler(jwtAccessDeniedHandler));



        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
