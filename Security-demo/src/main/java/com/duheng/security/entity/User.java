package com.duheng.security.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

/*************************
 Author: 杜衡
 Date: 2020/3/4
 Describe:
 *************************/
@Getter
@Setter
public class User {

    public interface simpleView{}

    public interface detailView  extends simpleView{}


    @JsonView(simpleView.class)
    private String name;

    @JsonView(simpleView.class)
    private Integer age;

    @JsonView(detailView.class)
    private String cardID;

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }


}
