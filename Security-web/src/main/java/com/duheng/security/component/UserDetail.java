package com.duheng.security.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/*************************
 Author: 杜衡
 Date: 2020/3/5
 Describe:
 *************************/
@Component
public class UserDetail implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PasswordEncoder passwordEncoder;


    /**
     *   根据登录时的账号，查询出用户的详情封装到UserDetails对象中
     *
     *   用户的信息没有进行数据库的查询
     *
     * @param userName 用户账号
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        logger.info("前台用户登录账号{}",userName);
        //假设数据库用户名：name
        String name = userName;
        logger.info("数据库账号：{}",name);
        //假设数据库密码：pwd
        String pwd = passwordEncoder.encode("123456");
        logger.info("数据库密码：{}",pwd);
        //连权限也假装是查的

        return new User(name,pwd,
                true,true,true,true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }
}
