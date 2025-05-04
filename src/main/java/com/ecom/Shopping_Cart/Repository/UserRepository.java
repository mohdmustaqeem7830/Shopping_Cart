package com.ecom.Shopping_Cart.Repository;
import com.ecom.Shopping_Cart.Model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {


    UserDtls findByEmail(String username);

    List<UserDtls> findByRole(String role);

    UserDtls findByResetToken(String token);

    Boolean existsByEmail(String email);
}
