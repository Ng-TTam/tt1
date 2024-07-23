package com.manage.userManagement.controller;

import com.manage.userManagement.dto.request.UserRequest;
import com.manage.userManagement.dto.response.ApiResponse;
import com.manage.userManagement.dto.response.UserResponse;
import com.manage.userManagement.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(userRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUsers(){
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable int id){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }

    @GetMapping("/info")
    ApiResponse<UserResponse> getUserInfo(){
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByUsername())
                .build();
    }

    @GetMapping("/search")
    ApiResponse<List<UserResponse>> searchByKeyword(@RequestParam String keyword) {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.searchByKeyword(keyword))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> editUser(@PathVariable int id, @RequestBody @Valid UserRequest userRequest){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, userRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable int id){
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("Delete successful")
                .build();
    }
}
