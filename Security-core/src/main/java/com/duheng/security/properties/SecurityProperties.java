package com.duheng.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
 *************************/
@Setter
@Getter
@ConfigurationProperties(prefix = "idu.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

}
