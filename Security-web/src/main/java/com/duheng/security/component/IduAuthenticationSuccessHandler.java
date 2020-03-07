package com.duheng.security.component;

import com.duheng.security.enumeration.LoginResponseType;
import com.duheng.security.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    登录认证成功后，Security默认跳转到引发认证的请求
    自定义IduAuthenticationSuccessHandler实现AuthenticationSuccessHandler：登录成功后返回json成功信息


 Authentication：
    封装了认证通过后相关的信息，最主要的就是UserDetails
 ObjectMapper:
    将对象转换成Json对象
 SavedRequestAwareAuthenticationSuccessHandler
    AuthenticationSuccessHandler的一个实现类，SpringBoot默认认证成功处理
 *************************/
@Component
public class IduAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        logger.info("登录成功！");
        if ( LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())){

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else{
            super.onAuthenticationSuccess(request,response,authentication);
        }
    }
}
