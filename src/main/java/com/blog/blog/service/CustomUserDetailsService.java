package com.blog.blog.service;

import com.blog.blog.model.User;
import com.blog.blog.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        // Ищем пользователя по полю userLogin
        User user = userRepository.findByUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userLogin: " + userLogin));

        // Возвращаем UserDetails (можно использовать ваш User или адаптировать его)
        return new org.springframework.security.core.userdetails.User(
                user.getUserLogin(),
                user.getPassword(),
                user.getAuthorities() // Если у вас есть роли или authorities
        );
    }
}