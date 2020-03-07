package com.duheng.security.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    封装通用的结果集
 *************************/
@Setter
@Getter
@AllArgsConstructor
public class SimpleResult implements Serializable {

    private Object content;
}
