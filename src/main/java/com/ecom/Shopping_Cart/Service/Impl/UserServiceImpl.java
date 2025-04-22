package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Repository.UserRepository;
import com.ecom.Shopping_Cart.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_ADMIN");
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }
}
