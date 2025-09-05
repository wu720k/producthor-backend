package com.producthor.Producthor.rest.controller;

import com.producthor.Producthor.domain.dto.AuthenticationDto;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.LoginResponse;
import com.producthor.Producthor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/public/register")
    public BaseResponse register(@RequestBody AuthenticationDto dto) {
        return userService.registration(dto);
    }

    @PostMapping("/public/login")
    public LoginResponse login(@RequestBody AuthenticationDto dto) {
        return userService.login(dto);
    }
}
