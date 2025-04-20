package com.ecom.Shopping_Cart.Service.Impl;
import com.ecom.Shopping_Cart.Services.CommonServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Service
public class CommonServiceImpl implements CommonServices {
    @Override
    public void removeSessionMsg() {
       HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
       HttpSession session = request.getSession();
       session.removeAttribute("succMsg");
       session.removeAttribute("errorMsg");
    }
}
