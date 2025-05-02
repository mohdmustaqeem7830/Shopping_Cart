package com.ecom.Shopping_Cart.Services;

import com.ecom.Shopping_Cart.Model.UserDtls;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public UserDtls saveUser(UserDtls user);
    public UserDtls updateUser(UserDtls user);

    public UserDtls getUserByEmail(String email);

    public List<UserDtls> getAllUser(String role);

    Boolean updateAccountStatus(int id);

    public void increaseFailedAttempts(UserDtls user);
    public void userAccountLock(UserDtls user);
    public Boolean unlockAccountTimeExpired(UserDtls user);
    public void resetAttempt(int userId);
    public void updateUserResetToken(String email, String resetToken);

    UserDtls getUserByToken(String token);

    public UserDtls updateUserProfile(UserDtls user, MultipartFile img);


}
