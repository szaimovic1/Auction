package com.ABH.Auction.filters;

import com.ABH.Auction.services.UserService;
import com.ABH.Auction.utility.JWTUtility;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JWTUtility jwtUtility;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token =  null;
        String userName = null;

        if(null != authorization && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            userName = jwtUtility.getUsernameFromToken(token);
        }
        if(null != userName && SecurityContextHolder.getContext().getAuthentication() == null)
            authenticateUser(userName, token, request);

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(String userName, String token, HttpServletRequest request) {
        UserDetails userDetails = userService.loadUserByUsername(userName);

        if(jwtUtility.validateToken(token,userDetails)) {
            UsernamePasswordAuthenticationToken usernamePAToken = new UsernamePasswordAuthenticationToken
                    (userDetails, null, userDetails.getAuthorities());

            usernamePAToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(usernamePAToken);
        }
    }
}
