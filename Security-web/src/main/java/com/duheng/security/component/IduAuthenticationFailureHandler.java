package com.duheng.security.component;

import com.duheng.security.enumeration.LoginResponseType;
import com.duheng.security.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    自定义登录认证失败处理器 实现 AuthenticationFailureHandler
    SpringBoot默认的登录失败处理器类 SimpleUrlAuthenticationFailureHandler

 *************************/
@Component
public class IduAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        logger.info("登录失败！");
        if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())) {
            //自定义的登录失败处理
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(exception));
        }else{
            //SpringBoot默认的登录认证失败处理
            super.onAuthenticationFailure(request,response,exception);
        }
    }
}
