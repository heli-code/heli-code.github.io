package com.musicflow.interceptor;

import com.musicflow.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    
    private final JwtService jwtService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"未授权访问\",\"data\":null}");
            return false;
        }
        
        String token = authHeader.substring(7);
        
        try {
            if (!jwtService.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\",\"data\":null}");
                return false;
            }
            
            // 将用户名设置到请求属性中，供后续使用
            String username = jwtService.extractUsername(token);
            request.setAttribute("username", username);
            
            return true;
            
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":401,\"message\":\"Token验证失败\",\"data\":null}");
            return false;
        }
    }
}