package io.github.wxqdb_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {


    @PostMapping("/new")
    public Boolean insert_new(@RequestParam String id,@RequestParam String name,@RequestParam String password){

        return true;
    }

    @PostMapping("/login")
    public Boolean login(@RequestParam String name,@RequestParam String password){
        //输入为空预判断
        if(name == ""||name == null||password==""||password==null){return false;}
        //遍历查询是否有相同的name,如果name相同则判断密码是否相同
        if(name == "xxxx" && password == "xxxx"){return true;}
        return false;
    }

}
