package com.ecom.Shopping_Cart.config;

import com.ecom.Shopping_Cart.Model.UserDtls;
import com.ecom.Shopping_Cart.Repository.UserRepository;
import com.ecom.Shopping_Cart.Services.UserService;
import com.ecom.Shopping_Cart.Utils.AppConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String  email = request.getParameter("username");
        UserDtls user = userRepository.findByEmail(email);

        if (user.getIsEnable()){
            if (user.getAccountNonLocked()){
                     if (user.getFailedAttempt()<AppConstant.Attempt_Time){
                         userService.increaseFailedAttempts(user);
                     }else{
                         userService.userAccountLock(user);
                         exception = new LockedException("Your Account is Locked !! failed Attempt 3");
                     }
            }else{

                if (userService.unlockAccountTimeExpired(user)){
                    exception = new LockedException("Your Account is Unlocked !! Please try to Login");
                }else{
                    exception = new LockedException("Your Account is Locked !! Please try after Sometime");
                }

            }
        }else{
            exception = new LockedException("Your Account is InActive");
        }
        super.setDefaultFailureUrl("/signin?error");
        super.onAuthenticationFailure(request, response, exception);

    }
}
