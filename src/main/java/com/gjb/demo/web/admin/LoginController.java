package com.gjb.demo.web.admin;

import com.gjb.demo.model.User;
import com.gjb.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginpage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes attributes){
        //查找有无该用户
        User user=userService.checkUser(username,password);
        //判断是否有该用户
        if(user!=null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }else{
            attributes.addFlashAttribute("message","用户名和密码错误");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public  String logout(HttpSession session){
       session.removeAttribute("user");
        return "redirect:/admin";
    }
}
