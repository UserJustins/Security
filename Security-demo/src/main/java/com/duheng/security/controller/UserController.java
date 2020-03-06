package com.duheng.security.controller;

import com.duheng.security.entity.User;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/*************************
 Author: 杜衡
 Date: 2020/3/4
 Describe:
 *************************/
@RestController
public class UserController {

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable String id){
        System.out.println(id);
    }

    @PutMapping("user/{id}")
    public User update(@PathVariable("id") String id,@Valid @RequestBody User user,BindingResult errors){
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(err ->

                System.out.println(err.getField()+"===>"+err.getDefaultMessage())
            );
        }
        User obj = new User();
        obj.setId(id);
        obj.setName(user.getName());
        return obj;
    }
    /**
     * 不使用@JsonView，返回所有的字段
     * json格式数据在接受时使用@RequestBody
     * @param user
     * @return
     */
    @PostMapping("/user")
    public User create(@Valid @RequestBody User user, BindingResult errors) {
        if (errors.hasErrors()) {
            List<ObjectError> allErrors = errors.getAllErrors();
            for (ObjectError error : allErrors) {
                System.out.println(error.getDefaultMessage());
            }

        }
        System.out.println(user);
        User obj = new User();
        obj.setId("1");
        return obj;
    }
    @GetMapping("/user")
    @JsonView(User.simpleView.class)
    public List<User> getUsers(){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User("A", 12));
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
