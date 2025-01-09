package com.blog.blog.service;

import com.blog.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User  not found: " + username);
        }
        // Возвращаем объект UserDetails, который будет использоваться для аутентификации
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }
}
