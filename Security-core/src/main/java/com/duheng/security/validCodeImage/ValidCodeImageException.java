package com.duheng.security.validCodeImage;


import org.springframework.security.core.AuthenticationException;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe: 自定义异常，用于图片校验的异常信息
    SpringSecurity中用到的自定义异常继承
 org.springframework.security.core.AuthenticationException;
 *************************/
public class ValidCodeImageException extends AuthenticationException {

    public ValidCodeImageException(String msg) {
        super(msg);
    }
}
