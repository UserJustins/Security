package com.duheng.security.config;

import com.duheng.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/*************************
 Author: 杜衡
 Date: 2020/3/5
 Describe:
    资源是否需要认证由SpringSecurity决定，如果需要进行认证就会根据loginPage进行跳转；
 跳转之前SpringSecurity将当前请求封装到HttpSessionRequestCache。


 *************************/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private SecurityProperties securityProperties;

    //自定义登录成功处理器
    @Autowired
    private AuthenticationSuccessHandler iduAuthenticationSuccessHandler; //自定义登录成功处理器
    //自定义登录失败处理器
    @Autowired
    private AuthenticationFailureHandler iduAuthenticationFailureHandler;
    /**
     * 密码的加密算法
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 1、指定登录认证方式 eg: formLogin()
     * 2、对所有的资源进行权限检查
     *      http.formLogin()
     *                 .and()
     *                 .authorizeRequests()
     *                 .anyRequest()
     *                 .authenticated();
     * 3、自定义登录页面 ,可以是Handler也可以是pageName;记得要要对资源放行
     *    否则login.html被拦截重新去到login.html,死循环;导致页面重定向次数
     *    过多而报错
     *      eg:  loginPage("/login.html")
     *           .antMatchers("/login.html").permitAll()
     * 4、登录请求处理
     *      UsernamePasswordAuthenticationFilter中默认处理的是"/login"
     *      和"post"的请求,自定义action怎么办？
     *        eg: .loginProcessingUrl("/authentication/form")
     * 5、暂时关闭CSRF
     *      eg:http.csrf().disable();
     *
     *
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()//指定使用表单的认证方式 UsernamePasswordAuthenticationFilter
                .loginPage("/authentication/require")//认证去Handler进行处理
                .loginProcessingUrl("/authentication/form")
                .successHandler(iduAuthenticationSuccessHandler)
                .failureHandler(iduAuthenticationFailureHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage()).permitAll()//指定放行的资源
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();

    }
}
