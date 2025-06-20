package com.like_lion.tomato.member.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/api/v1/auth")

@RestController
public class MemberController {

    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin() {
        String authUrl = "https://accounts.google.com/o/oauth2/auth";
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(authUrl))
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return null;
    }


    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<Void> me() {
        return null;
    }



}
