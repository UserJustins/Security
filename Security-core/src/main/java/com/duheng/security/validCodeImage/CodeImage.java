package com.duheng.security.validCodeImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    封装图片验证码的相关信息
 *************************/
@Setter
@Getter
@AllArgsConstructor
public class CodeImage {

    //图片
    private BufferedImage image;

    //验证码
    private String code;

    //验证码的过期时间
    private LocalDateTime expireTime;

    /**
     *
     * @param image
     * @param code
     * @param expireIn 指定多长时间过期 比如60s过期
     */
    public CodeImage(BufferedImage image,String code,int expireIn){
        this.image = image;
        this.code =  code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    /**
     * 判断是否过期
     *
     * @return
     */
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expireTime);
    }


}
