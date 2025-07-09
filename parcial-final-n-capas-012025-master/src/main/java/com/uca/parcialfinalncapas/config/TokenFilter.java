package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenUtils  tokenUtils;
    private final UserService UserServices;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return path.startsWith("/user/auth") || path.equals("/home");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if(authorization != null && authorization.startsWith("Bearer ") && authorization.length() > 7) {
            token = authorization.substring(7);

            try{
                username = tokenUtils.getUsernameFromToken(token);
            }catch (IllegalArgumentException e){
                System.out.println("Unable to get JWT token");
            }catch (ExpiredJwtException e){
                System.out.println("JWT token expired");
            }catch (MalformedJwtException e){
                System.out.println("JWT token malformed");
            }
        }else{
            System.out.println("Bearer string not found");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = (User) UserServices.loadUserByUsername(username);

            if(user != null) {
                Boolean tokenValidity = tokenUtils.validateToken(token);

                if(tokenValidity) {
                    UsernamePasswordAuthenticationToken authToken
                            = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}