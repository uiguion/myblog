package com.gjb.demo.dao;

import com.gjb.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
         User findByUsernameAndPassword(String username,String password);
}
