package com.example.demo.security.db_auth;

import com.example.demo.security.db_auth.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    @Qualifier("fake")
    private AppUserDao appUserDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return appUserDao.findByUserName(userName)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("username:- %s not found", userName)));
    }
}
