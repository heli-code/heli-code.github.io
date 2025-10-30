package com.musicflow.service;

import com.musicflow.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTime = 86400000; // 24小时
    
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }
    
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    public void invalidateToken(String token) {
        // 简单的令牌失效实现 - 在实际应用中可能需要更复杂的逻辑
        // 例如将令牌加入黑名单或设置过期时间
        // 这里暂时不实现具体逻辑
    }
    
    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
            
            // 如果令牌在30分钟内过期，则认为即将过期
            return timeUntilExpiration <= 30 * 60 * 1000;
        } catch (Exception e) {
            return false;
        }
    }
}