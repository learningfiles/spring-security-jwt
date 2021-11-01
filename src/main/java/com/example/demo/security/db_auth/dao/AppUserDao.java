package com.example.demo.security.db_auth.dao;

import com.example.demo.security.db_auth.AppUserDetails;

import java.util.Optional;

public interface AppUserDao {
    Optional<AppUserDetails> findByUserName(String userName);
}
