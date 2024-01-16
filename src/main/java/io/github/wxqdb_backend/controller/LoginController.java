package io.github.wxqdb_backend.controller;

import io.github.wxqdb_backend.function.CreateUser;
import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/user")
public class LoginController {


    @PostMapping("/new")
    public Boolean insert_new(@RequestParam String name, @RequestParam String password) throws DocumentException, IOException {

        return CreateUser.createUserWithReturn(name, password);
    }

    @PostMapping("/login")
    public Boolean login(@RequestParam String name, @RequestParam String password) throws DocumentException, IOException {
        //输入为空预判断
        if (name == "" || name == null || password == "" || password == null) {
            return false;
        }
        //遍历查询是否有相同的name,如果name相同则判断密码是否相同
        return CreateUser.Login(name, password);
    }

}
