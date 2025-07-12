package com.like_lion.tomato;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTestController {
    @GetMapping("/redirect")
    public String test(@RequestHeader(value = "Authorization", required = false) String accessToken) {
        return "redirect-page, Header Access Token: " + (accessToken != null ? accessToken : "없음");
    }
}
