package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Repository.UserRepository;
import com.ecom.Shopping_Cart.Services.UserService;
import com.ecom.Shopping_Cart.Utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDtls saveUser(UserDtls user) {
        user.setRole("ROLE_ADMIN");
        user.setIsEnable(true);
        user.setAccountNonLocked(true);
        user.setFailedAttempt(0);
        user.setLockTime(null);
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        return userRepository.save(user);
    }

    @Override
    public UserDtls getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDtls> getAllUser(String role) {

        return userRepository.findByRole(role);
    }

    @Override
    public Boolean updateAccountStatus(int id) {
        UserDtls user = userRepository.findById(id).get();
        if (user!=null){
            Boolean oldstatus = user.getIsEnable();
            user.setIsEnable(!oldstatus);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void increaseFailedAttempts(UserDtls user) {
        int attempt = user.getFailedAttempt()+1;
        user.setFailedAttempt(attempt);
        userRepository.save(user);
    }

    @Override
    public void userAccountLock(UserDtls user) {
             user.setAccountNonLocked(false);
             user.setLockTime(new Date());
             userRepository.save(user);
    }

    @Override
    public Boolean unlockAccountTimeExpired(UserDtls user) {

        long lockTime = user.getLockTime().getTime();
        long UnlockTime = lockTime+ AppConstant.UNLOCK_DURATION_TIME;

        long currentTime = System.currentTimeMillis();

        if (UnlockTime < currentTime) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void resetAttempt(int userId) {

    }


}
