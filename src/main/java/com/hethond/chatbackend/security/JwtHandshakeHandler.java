package com.hethond.chatbackend.security;

import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.services.JwtService;
import com.hethond.chatbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeHandler extends HttpSessionHandshakeInterceptor {
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public JwtHandshakeHandler(final JwtService jwtService,
                               final UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        String token = extractToken(request);
        System.out.println("Extracted token: " + token);

        if (!jwtService.isTokenExpired(token)) {
            long userId = jwtService.extractUserId(token);
            User user = userService.findUserById(userId);
            Collection<GrantedAuthority> authorities = Collections.singletonList(user.getRole().getAuthority());
            var authentication = new UsernamePasswordAuthenticationToken(Long.toString(userId), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    private String extractToken(ServerHttpRequest request) {
        Map<String, List<String>> params = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams();
        return params.get("token").getFirst();
    }
}
