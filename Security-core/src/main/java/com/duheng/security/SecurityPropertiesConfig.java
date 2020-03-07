package com.duheng.security;

import com.duheng.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    SecurityProperties 属性文件配置属性生效
 *************************/
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityPropertiesConfig {

}
