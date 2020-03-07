package com.duheng.security.controller;

import com.duheng.security.properties.SecurityProperties;
import com.duheng.security.support.SimpleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Author: 杜衡
 Date: 2020/3/6
 Describe:
 
 HttpSessionRequestCache：
    资源是否需要认证由SpringSecurity决定，如果需要进行认证就会根据loginPage进行跳转；
 跳转之前SpringSecurity将当前请求封装到HttpSessionRequestCache。
 DefaultRedirectStrategy:
    进行页面跳转的工具类

 *************************/
@RestController
public class SecurityController {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;
    /**
     *  认证就来到该请求,判断请求来自浏览器还是其他的客户端，浏览器
     *  就跳转到配置或默认的登录热证页面，其他的客户端响应状态码和错
     *  误消息
     *  
     * 1、判断请求来自HTML页面
     * 2、如何获取引发跳转的请求？
     * @param request
     * @param response
     */
    @RequestMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResult requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest saveRequest = requestCache.getRequest(request, response);
        if(saveRequest != null){
            String redirectUrl = saveRequest.getRedirectUrl();
            logger.info("引发跳转的请求URL===>{}",redirectUrl);
            if(StringUtils.endsWithIgnoreCase(redirectUrl,".html")){
                //请求来自HTML页面，那么照旧给跳转到一个HTML
                //url需要从配置文件中读取
                String url = securityProperties.getBrowser().getLoginPage();
                logger.info("配置文件idu.security.browser.logPage===>{}",url);
                redirectStrategy.sendRedirect(request,response,url);
            }
        }
        return new SimpleResult("访问的资源需要身份认证，请引导用户到登录页面");
    }
}
