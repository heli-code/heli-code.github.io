package com.musicflow.service;

import com.musicflow.dto.AuthRequest;
import com.musicflow.dto.AuthResponse;
import com.musicflow.dto.PasswordResetRequest;
import com.musicflow.dto.ProfileUpdateRequest;
import com.musicflow.entity.User;
import com.musicflow.entity.UserPreference;
import com.musicflow.repository.UserPreferenceRepository;
import com.musicflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    @Transactional
    public AuthResponse register(AuthRequest request) {
        // 验证用户名格式
        if (!isValidUsername(request.getUsername())) {
            throw new RuntimeException("用户名格式不正确，只能包含字母、数字和下划线，长度3-20位");
        }
        
        // 验证邮箱格式
        if (!isValidEmail(request.getEmail())) {
            throw new RuntimeException("邮箱格式不正确");
        }
        
        // 验证密码强度
        if (!isValidPassword(request.getPassword())) {
            throw new RuntimeException("密码强度不足，需要包含字母和数字，长度6-20位");
        }
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getUsername());
        user.setAvatar("👤");
        
        User savedUser = userRepository.save(user);
        
        // 创建默认用户偏好设置
        createDefaultUserPreference(savedUser);
        
        log.info("新用户注册成功: {}", savedUser.getUsername());
        
        return createAuthResponse(savedUser);
    }
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // 记录登录失败尝试
            log.warn("用户 {} 登录失败: 密码错误", request.getUsername());
            throw new RuntimeException("密码错误");
        }
        
        // 更新最后登录时间
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("用户 {} 登录成功", user.getUsername());
        
        return createAuthResponse(user);
    }
    
    @Transactional
    public void updateProfile(String token, ProfileUpdateRequest request) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 更新昵称
        if (request.getNickname() != null && !request.getNickname().trim().isEmpty()) {
            user.setNickname(request.getNickname());
        }
        
        // 更新头像
        if (request.getAvatar() != null && !request.getAvatar().trim().isEmpty()) {
            user.setAvatar(request.getAvatar());
        }
        
        // 更新邮箱（需要验证）
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (!isValidEmail(request.getEmail())) {
                throw new RuntimeException("邮箱格式不正确");
            }
            if (userRepository.existsByEmailAndIdNot(request.getEmail(), user.getId())) {
                throw new RuntimeException("邮箱已被其他用户使用");
            }
            user.setEmail(request.getEmail());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("用户 {} 更新了个人资料", username);
    }
    
    @Transactional
    public void changePassword(String token, PasswordResetRequest request) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }
        
        // 验证新密码强度
        if (!isValidPassword(request.getNewPassword())) {
            throw new RuntimeException("新密码强度不足，需要包含字母和数字，长度6-20位");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        log.info("用户 {} 修改了密码", username);
    }
    
    public AuthResponse.UserInfo getUserInfo(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setNickname(user.getNickname());
        userInfo.setCreatedAt(user.getCreatedAt());
        
        return userInfo;
    }
    
    public boolean validateToken(String token) {
        try {
            String username = jwtService.extractUsername(token.replace("Bearer ", ""));
            return userRepository.existsByUsername(username);
        } catch (Exception e) {
            return false;
        }
    }
    
    private AuthResponse createAuthResponse(User user) {
        String token = jwtService.generateToken(user);
        
        AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setNickname(user.getNickname());
        userInfo.setCreatedAt(user.getCreatedAt());
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUser(userInfo);
        
        return response;
    }
    
    /**
     * 验证用户名格式
     */
    private boolean isValidUsername(String username) {
        return username != null && 
               username.matches("^[a-zA-Z0-9_]{3,20}$") &&
               !username.matches("^\\d+$"); // 不能全是数字
    }
    
    /**
     * 验证邮箱格式
     */
    private boolean isValidEmail(String email) {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    }
    
    /**
     * 验证密码强度
     */
    private boolean isValidPassword(String password) {
        return password != null && 
               password.length() >= 6 && 
               password.length() <= 20 &&
               password.matches(".*[a-zA-Z].*") && // 包含字母
               password.matches(".*\\d.*"); // 包含数字
    }
    
    /**
     * 创建默认用户偏好设置
     */
    private void createDefaultUserPreference(User user) {
        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setPreferredGenres(new String[]{"流行", "轻音乐", "电子"});
        preference.setPreferredMoods(new String[]{"快乐", "放松", "专注"});
        preference.setPreferredBpmRange("60-120");
        preference.setScenePreferences("work");
        preference.setVolumeLevel(80);
        preference.setAutoPlay(true);
        preference.setRecommendationDiversity(0.7);
        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        
        userPreferenceRepository.save(preference);
        log.info("为用户 {} 创建了默认偏好设置", user.getUsername());
    }
    
    /**
     * 刷新Token
     */
    public AuthResponse refreshToken(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证Token是否即将过期
        if (jwtService.isTokenExpiringSoon(token.replace("Bearer ", ""))) {
            String newToken = jwtService.generateToken(user);
            
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setEmail(user.getEmail());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setNickname(user.getNickname());
            userInfo.setCreatedAt(user.getCreatedAt());
            
            AuthResponse response = new AuthResponse();
            response.setToken(newToken);
            response.setUser(userInfo);
            
            log.info("用户 {} 的Token已刷新", username);
            return response;
        }
        
        throw new RuntimeException("Token尚未过期，无需刷新");
    }
    
    /**
     * 注销用户
     */
    @Transactional
    public void logout(String token) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        
        // 在实际应用中，这里会将Token加入黑名单
        // 简化实现 - 只记录日志
        log.info("用户 {} 已注销", username);
    }
    
    /**
     * 强制用户下线
     */
    @Transactional
    public void forceLogout(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 在实际应用中，这里会强制使该用户的所有Token失效
        log.info("已强制用户 {} 下线", username);
    }
    
    /**
     * 检查用户状态
     */
    public Map<String, Object> getUserStatus(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Map<String, Object> status = new HashMap<>();
        status.put("username", user.getUsername());
        status.put("status", "active");
        status.put("lastLogin", user.getUpdatedAt());
        status.put("createdAt", user.getCreatedAt());
        
        return status;
    }
    
    /**
     * 批量用户状态检查
     */
    public List<Map<String, Object>> batchCheckUserStatus(List<String> usernames) {
        return usernames.stream()
                .map(username -> {
                    try {
                        return getUserStatus(username);
                    } catch (Exception e) {
                        return Map.of(
                                "username", username,
                                "status", "not_found",
                                "error", e.getMessage()
                        );
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 检查用户名是否可用
     */
    public boolean checkUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    /**
     * 检查邮箱是否可用
     */
    public boolean checkEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}