package com.ecom.Shopping_Cart.Service.Impl;

import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Repository.UserRepository;
import com.ecom.Shopping_Cart.Services.UserService;
import com.ecom.Shopping_Cart.Utils.AppConstant;
import com.ecom.Shopping_Cart.config.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailService;

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
    public UserDtls updateUser(UserDtls user) {
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
    @Override
    public void updateUserResetToken(String email, String resetToken){
        UserDtls findByEmail = userRepository.findByEmail(email);
        findByEmail.setResetToken(resetToken);
        userRepository.save(findByEmail);
    }

    @Override
    public UserDtls getUserByToken(String token) {
        return userRepository.findByResetToken(token);
    }

    @Override
    public UserDtls updateUserProfile(UserDtls user, MultipartFile img) {
        UserDtls dbUser = userRepository.findById(user.getId()).get();

        if (!img.isEmpty()){
            dbUser.setProfileImage(img.getOriginalFilename());
        }
        if (ObjectUtils.isEmpty(dbUser)){
            return null;
        }else {


            dbUser.setName(user.getName());
            dbUser.setMobileNumber(user.getMobileNumber());
            dbUser.setAddress(user.getAddress());
            dbUser.setCity(user.getCity());
            dbUser.setState(user.getState());
            dbUser.setPincode(user.getPincode());

            userRepository.save(dbUser);


        }
         try {
             if (!img.isEmpty()){
                 File saveFile = new ClassPathResource("static/img").getFile();
                 Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+"profile_img"+File.separator+img.getOriginalFilename());

                 System.out.println(path);
                 Files.copy(img.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
             }
         }catch (Exception e){
             e.printStackTrace();
         }
        return dbUser;

    }


}
