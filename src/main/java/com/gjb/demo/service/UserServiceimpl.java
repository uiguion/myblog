package com.gjb.demo.service;

import com.gjb.demo.dao.UserRepository;
import com.gjb.demo.model.User;
import com.gjb.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceimpl implements  UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user=userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
