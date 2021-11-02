package com.example.demo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtUserNameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtUserNameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtConfig jwtConfig,
                                                      SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    // grab client credentials and validate them
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // grab userName and password from request
            ObjectMapper mapper = new ObjectMapper();
            UserNameAndPasswordAuthenticationRequest userNameAndPasswordAuthenticationRequest =
                    mapper.readValue(request.getInputStream(), UserNameAndPasswordAuthenticationRequest.class);

            //create Authentication object and trigger the actual authentication
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userNameAndPasswordAuthenticationRequest.getUsername(),
                    userNameAndPasswordAuthenticationRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // on successful authenticate generate the token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationDays())))
                .signWith(secretKey)
                .compact();

        response.addHeader("Authorization", jwtConfig.getTokenPrefix() + token);
    }
}
