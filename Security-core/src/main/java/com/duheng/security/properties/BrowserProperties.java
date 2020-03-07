package com.duheng.security.properties;

import com.duheng.security.enumeration.LoginResponseType;
import lombok.Getter;
import lombok.Setter;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    封装idu.security.browser.loginPage属性
    封装idu.security.browser.loginResponseType属性
 *************************/
@Getter
@Setter
public class BrowserProperties {

    //认证登录页面
    private String loginPage = "/defaultLogin.html";

    //登录认证后响应的方式,默认是json方式
    private LoginResponseType loginResponseType = LoginResponseType.JSON;
}
