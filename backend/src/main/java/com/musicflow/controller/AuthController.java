package com.musicflow.controller;

import com.musicflow.dto.ApiResponse;
import com.musicflow.dto.AuthRequest;
import com.musicflow.dto.AuthResponse;
import com.musicflow.dto.UserProfileDto;
import com.musicflow.entity.User;
import com.musicflow.service.AuthService;
import com.musicflow.service.JwtService;
import com.musicflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", response));
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userService.findByUsername(username);
        
        return ResponseEntity.ok(ApiResponse.success("获取成功", UserProfileDto.fromEntity(user)));
    }
    
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> updates) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userService.updateProfile(username, updates);
        
        return ResponseEntity.ok(ApiResponse.success("更新成功", UserProfileDto.fromEntity(user)));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String token) {
        jwtService.invalidateToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok(ApiResponse.success("登出成功", null));
    }
}