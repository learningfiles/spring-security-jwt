package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("foo")
                .password(passwordEncoder.encode("foo"))
                //.roles(ApplicationUserRole.STUDENT.name())
                .authorities(ApplicationUserRole.STUDENT.getAuthorities())
                .and()
                .withUser("bar")
                .password(passwordEncoder.encode("bar"))
                //.roles(ApplicationUserRole.ADMIN.name())
                .authorities(ApplicationUserRole.ADMIN.getAuthorities())
                .and()
                .withUser("tom")
                .password(passwordEncoder.encode("tom"))
//                .roles(ApplicationUserRole.ADMINTRAINEE.name());
                .authorities(ApplicationUserRole.ADMINTRAINEE.getAuthorities());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
                // using http with role based
                /*.antMatchers(HttpMethod.POST, "/management/**").hasRole(ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/management/**").hasRole(ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/management/**").hasRole(ApplicationUserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/management/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())*/
                // using http with authority based
                /*.antMatchers(HttpMethod.POST, "/management/**").hasAuthority(ApplicationUserPermission.STUDENT_WRITE.name())
                .antMatchers(HttpMethod.PUT, "/management/**").hasAuthority(ApplicationUserPermission.STUDENT_WRITE.name())
                .antMatchers(HttpMethod.DELETE, "/management/**").hasAuthority(ApplicationUserPermission.STUDENT_WRITE.name())
                .antMatchers(HttpMethod.GET, "/management/**").hasAuthority(ApplicationUserPermission.STUDENT_READ.name())*/
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
