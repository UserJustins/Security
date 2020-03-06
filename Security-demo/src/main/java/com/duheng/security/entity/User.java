package com.duheng.security.entity;

import com.duheng.security.annotation.MyValidAnnotation;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Past;
import java.util.Date;

/*************************
 Author: 杜衡
 Date: 2020/3/4
 Describe:
 *************************/
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    public interface simpleView{}

    public interface detailView  extends simpleView{}

    private String id;
    @NotBlank(message = "用户名不能为空")
    @JsonView(simpleView.class)
    private String name;

    @MyValidAnnotation(message = "放心吧！该字段使用自定义校验注解永远都不会成功")
    @JsonView(simpleView.class)
    private Integer age;

    @Length(max = 6,message = "身份证号长度不能超过18")
    @JsonView(detailView.class)
    private String cardID;

    @Past(message = "生日的时间需要是个过去的时间")
    private Date birthday;
    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }


}
