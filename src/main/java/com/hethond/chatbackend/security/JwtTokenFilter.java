package com.hethond.chatbackend.security;

import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.services.JwtService;
import com.hethond.chatbackend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtTokenFilter(final JwtService jwtService,
                          final UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (!jwtService.isTokenExpired(token)) {
                    long userId = jwtService.extractUserId(token);
                    User user = userService.findUserById(userId);
                    Collection<GrantedAuthority> authorities = Collections.singletonList(user.getRole().getAuthority());
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
