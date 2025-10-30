package com.musicflow.util;

import com.musicflow.constant.AppConstants;
import com.musicflow.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    
    private final Key secretKey = Keys.hmacShaKeyFor(AppConstants.JWT_SECRET.getBytes());
    
    /**
     * 生成JWT Token
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + AppConstants.JWT_EXPIRATION))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 解析Token
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证Token
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 从Token中获取用户名
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    /**
     * 从Token中获取用户ID
     */
    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }
    
    /**
     * 检查Token是否过期
     */
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
    
    /**
     * 获取Token剩余有效时间（毫秒）
     */
    public long getRemainingTime(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}