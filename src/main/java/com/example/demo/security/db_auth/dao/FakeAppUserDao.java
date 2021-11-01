package com.example.demo.security.db_auth.dao;

import com.example.demo.security.ApplicationUserRole;
import com.example.demo.security.db_auth.AppUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeAppUserDao implements AppUserDao{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<AppUserDetails> findByUserName(String userName) {
        return getUsers().stream()
                .filter(user -> user.getUsername().equals(userName))
                .findFirst();
    }

    private List<AppUserDetails> getUsers() {
        return List.of(
                new AppUserDetails(
                        "foo",
                        passwordEncoder.encode("foo"),
                        ApplicationUserRole.STUDENT.getAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),new AppUserDetails(
                        "bar",
                        passwordEncoder.encode("bar"),
                        ApplicationUserRole.ADMIN.getAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),new AppUserDetails(
                        "tom",
                        passwordEncoder.encode("tom"),
                        ApplicationUserRole.ADMINTRAINEE.getAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
