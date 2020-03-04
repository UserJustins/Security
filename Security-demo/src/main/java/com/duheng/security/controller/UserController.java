package com.duheng.security.controller;

import com.duheng.security.entity.User;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*************************
 Author: 杜衡
 Date: 2020/3/4
 Describe:
 *************************/
@RestController
public class UserController {
    @GetMapping("/user")
    @JsonView(User.simpleView.class)
    public List<User> getUsers(@RequestParam String name){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(name, 12));
        list.add(new User("B", 13));
        list.add(new User("C", 14));
        return list;
    }


    @GetMapping("/user/{id}")
    @JsonView(User.detailView.class)
    public User getUserInfo(@PathVariable("id") Integer id){
        return new User("tom", id);
    }
}
