package com.producthor.Producthor.rest.controller;


import com.producthor.Producthor.domain.dto.UserDto;
import com.producthor.Producthor.rest.response.UserResponse;
import com.producthor.Producthor.rest.response.UsersResponse;
import com.producthor.Producthor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.producthor.Producthor.rest.response.BaseResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/private/update")
    public BaseResponse update(@RequestBody UserDto dto) {
        return userService.update(dto);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public BaseResponse delete(@PathVariable Long userId) {
        return userService.deleteById(userId);
    }

    @GetMapping("/private/get")
    public UserResponse getCurrent() {
        return userService.getCurrent();
    }

    @GetMapping("/admin/all")
    public UsersResponse getAll() {
        return userService.getAll();
    }
}
