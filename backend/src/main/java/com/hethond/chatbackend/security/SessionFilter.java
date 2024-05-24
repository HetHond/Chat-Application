package com.hethond.chatbackend.security;

import com.hethond.chatbackend.entities.AccountStatus;
import com.hethond.chatbackend.entities.User;
import com.hethond.chatbackend.exceptions.InactiveAccountException;
import com.hethond.chatbackend.services.SessionService;
import com.hethond.chatbackend.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Component
public class SessionFilter extends OncePerRequestFilter {
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public SessionFilter(final SessionService sessionService,
                         final UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);

                final UUID userId = sessionService.getUserIdBySession(token);
                final User authenticatedUser = userService.findUserById(userId);

                if (authenticatedUser.getAccountStatus() == AccountStatus.BANNED) {
                    // FIXME maybe i need to switch to response.sendError
                    throw new AccessDeniedException("Access Denied");
                } else if (authenticatedUser.getAccountStatus() == AccountStatus.INACTIVE) {
                    throw new InactiveAccountException("Your account is inactive.", authenticatedUser.getPhone());
                }

                final Collection<GrantedAuthority> authorities = Collections.singletonList(authenticatedUser.getRole().getAuthority());
                final var authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
