package com.duheng.security.validCodeImage;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Author: 杜衡
 Date: 2020/3/7
 Describe:
    图片验证码校验的过滤器
 过滤谁？ 登陆请求并且请求的方式是Post
 别忘记过滤器需要进行配置
 *************************/
@Getter
@Setter
public class ValidCodeImageFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(getClass());

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = "/authentication/form";
        if (StringUtils.equals(uri,httpServletRequest.getRequestURI())
            && StringUtils.equalsIgnoreCase("post",httpServletRequest.getMethod())){
            try {
                validate(new ServletWebRequest(httpServletRequest));
            }catch (ValidCodeImageException exception){
                //异常交给登陆认证失败处理器
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,
                        httpServletResponse,exception);
                //然后返回，不再执行下一个过滤器
                return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    /**
     * 验证码校验
     * 1、获取Session中的code值
     * 2、获取页面input中的验证码
     * @param request
     */
    private void validate(ServletWebRequest request) {
        CodeImage codeInSession = (CodeImage) sessionStrategy.getAttribute(request,
                ValidCodeImageController.SESSION_KEY);
        String inputCode = request.getParameter("imageCode");
        logger.info("用户输入的验证码===>{}",inputCode);

        if (codeInSession == null) {
            throw new ValidCodeImageException("验证码不存在");
        }

        if (StringUtils.isBlank(inputCode)) {
            throw new ValidCodeImageException("验证码不能为空");
        }

        if (!StringUtils.equals(codeInSession.getCode(),inputCode)) {
            throw new ValidCodeImageException("验证码输入不正确");
        }

        if (codeInSession.isExpired()) {
            sessionStrategy.removeAttribute(request,ValidCodeImageController.SESSION_KEY);
            throw new ValidCodeImageException("验证码已经过期");
        }

        sessionStrategy.removeAttribute(request,ValidCodeImageController.SESSION_KEY);
    }
}
