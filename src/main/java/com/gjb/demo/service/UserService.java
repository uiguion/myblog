package com.gjb.demo.service;

import com.gjb.demo.model.User;

public interface UserService {
    //按照用户密码查找是否有该用户
    User checkUser(String username,String password);
}
