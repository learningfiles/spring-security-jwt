package com.example.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtTokenVerifier(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = authorizationHeader.substring(7);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");

            List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(authorityMap -> new SimpleGrantedAuthority(authorityMap.get("authority")))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
         catch (JwtException e) {
            throw new IllegalStateException("Token cannot be trusted");
         }

        filterChain.doFilter(request, response);
    }
}
