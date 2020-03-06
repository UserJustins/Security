package com.duheng.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*************************
 Author: 杜衡
 Date: 2020/3/5
 Describe:
    认证代码片段存在两个问题
 1、Restful请求需要认证不应该返回一个登录页面的地址，应该是响应的状态码
 2、登录页面需要进行处理，开发者可以对登录页面随意设置，没有的话使用默认的
 *************************/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
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
                .loginPage("/login.html")//指定基于表单的登录页面(页面名称)
                .loginProcessingUrl("/authentication/form")
                .and()
                .authorizeRequests()
                .antMatchers("/login.html").permitAll()//指定放行的资源
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();

    }
}
