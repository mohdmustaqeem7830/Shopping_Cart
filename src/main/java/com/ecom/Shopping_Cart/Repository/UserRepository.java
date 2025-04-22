package com.ecom.Shopping_Cart.Repository;
import com.ecom.Shopping_Cart.Model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDtls, Integer> {


    UserDtls findByEmail(String username);
}
