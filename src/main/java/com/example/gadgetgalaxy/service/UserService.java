package com.example.gadgetgalaxy.service;
import com.example.gadgetgalaxy.entity.User;
import com.example.gadgetgalaxy.rdb.UserCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class UserService {

    private final UserCustomRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserCustomRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User saveUser(User user) {
        // Хеширование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public boolean register(String username, String password, String confirmPassword) {
        // Проверка, существует ли пользователь с таким именем пользователя
        if (getUserByUsername(username) != null) {
            return false; // Пользователь уже существует
        }

        // Проверка, что пароль и подтверждение пароля совпадают
        if (!password.equals(confirmPassword)) {
            return false; // Пароли не совпадают
        }

        // Создание нового пользователя и сохранение его
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Пароль будет хеширован при сохранении
        saveUser(newUser);

        return true; // Успешная регистрация
    }

    public User getCurrentLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username);
        }

        return null;
    }

}
