package com.duheng.security.validCodeImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    开发生成图片验证码的接口
 操作Session可以使用SessionStrategy接口实现类
 *************************/
@RestController
public class ValidCodeImageController {

    Logger logger = LoggerFactory.getLogger(getClass());

    public final static String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    private SessionStrategy session = new HttpSessionSessionStrategy();

    @GetMapping("/image/code")
    public void codeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1、生成验证码图片
        CodeImage codeImage = creatCodeImage(request);
        logger.info("验证码的随机字符=====>{}",codeImage.getCode());
        //2、将验证码放到session中,供登录时认证
        session.setAttribute(new ServletWebRequest(request),SESSION_KEY,codeImage);
        //3、将图片验证码写到接口的响应中
        ImageIO.write(codeImage.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 生成验证码
     * @param request
     * @return
     */
    private CodeImage creatCodeImage(HttpServletRequest request) {
        int weight = 80;
        int height = 30;
        Random r = new Random();
        String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
        //创建图片缓冲区
        BufferedImage image = new BufferedImage(weight, height, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景色随机
        g.setColor(new Color(255, 255, r.nextInt(245) + 10));
        g.fillRect(0, 0, weight, height);
        //随机数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++)             //画四个字符即可
        {
            String s =String.valueOf(codes.charAt(r.nextInt(codes.length())));      //随机生成字符，因为只有画字符串的方法，没有画字符的方法，所以需要将字符变成字符串再画
            sb.append(s);                      //添加到StringBuilder里面
            float x = i * 1.0F * weight / 4;   //定义字符的x坐标
            g.setColor(new Color(r.nextInt(225),r.nextInt(225),r.nextInt(225)));         //设置颜色，随机
            g.drawString(s, x, height - 5);
        }
        //画干扰线
        int num = r.nextInt(10); //定义干扰线的数量
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(weight);
            int y1 = r.nextInt(height);
            int x2 = r.nextInt(weight);
            int y2 = r.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        return new CodeImage(image,sb.toString(),60);
    }


}
