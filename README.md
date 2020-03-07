# Security
SpringBoot中配置SpringSecurity，提供安全的RestfulAPI

.Restful一种编码规范    
.url锁定资源   
.http的method指定如何操作资源，并以json的方式返回结果    

# 一、使用SpringMVC开发RestfulAPI
 
## 1.简单RestfulAPI编写
### (1)编写针对RestfulAPI的测试用例
RestfulAPI
```java
@RestController
public class UserController {
    @GetMapping("/user")
    public List<User> getUser(@RequestParam String name){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(name, 12));
        list.add(new User("B", 13));
        list.add(new User("C", 14));

        return list;
    }
}
```
针对性的测试用例
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    WebApplicationContext application;

    MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(application).build();
    }
    
    @Test
        public void whenQueryUserSuccess() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/user")
                    .param("name","A") 
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
        }
}

```
### (2)@JsonView注解
不同请求返回同一个json类型的JavaBean(单一对象或集合)可见的属性不同    
.使用接口来声明多个视图    
.在对象的get方法上指定视图,使用lombok就标注在属性上    
.在Controller的方法上指定视图      

在JavaBean中声明不同的视图，并标注哪些属性属于哪种视图
```java
@Getter
@Setter
public class User {

    public interface simpleView{}

    public interface detailView  extends simpleView{}


    @JsonView(simpleView.class)
    private String name;

    @JsonView(simpleView.class)
    private Integer age;

    @JsonView(detailView.class)
    private String cardID;

    public User(String name, int age) {
        this.age = age;
        this.name = name;
    }


}
```
Controller中标注不同的请求返回不同的JavaBean视图
```java
@RestController
public class UserController {
    @GetMapping("/user")
    @JsonView(User.simpleView.class)
    public List<User> getUsers(@RequestParam String name){
        ArrayList<User> list = new ArrayList<>();
        list.add(new User(name, 12));
        list.add(new User("B", 13));
        list.add(new User("C", 14));
        return list;
    }


    @GetMapping("/user/{id}")
    @JsonView(User.detailView.class)
    public User getUserInfo(@PathVariable("id") Integer id){
        return new User("tom", id);
    }
}

```  
### (3)自定义数据校验注解
**1、创建自定义的校验注解**
```java
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConstraint.class)//指定哪个类赋予注解业务逻辑
public @interface MyValidAnnotation {
    String message() default "{自定义数据校验注解}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

```
**2、创建类实现ConstraintValidator<A,T>接口赋予注解意义**
```java
/*************************
 Describe:
    注解的业务逻辑处理
        1.该bean实现了ConstraintValidator就已经被Spring进行了管理了
        2.ConstraintValidator<A,T> A:注解的类名，T：注解标注的字段类型
 *************************/
public class MyConstraint implements ConstraintValidator<MyValidAnnotation,Object> {
    @Override
    public void initialize(MyValidAnnotation myValidAnnoation) {

    }

    /**
     *  字段值的业务校验就在此方法中进行
     *
     * @param s 被标注字段的值
     * @param constraintValidatorContext
     * @return true：校验通过 false：校验失败
     */
    @Override
    public boolean isValid(Object s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}

```
# 二、使用SpringSecurity开发基于表单的认证
**认证、授权、攻击防护**
## 1、认证
### (1)、自定义表单登录页面
```java
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

```
代码重构，loginPage()去往一个Handler，对引发跳转的URL进行.HTML区分，进而对不同的请求进行不同的响应；
其中登录页面可以进行自行配置，当然也存在默认的设置
### (2)、自定义认证成功和失败处理器
  SpringSecurity默认认证成功后跳回引发认证的页面、认证错误跳转到认证失败的页面,现在进行响应json和页面
的兼顾处理,枚举LoginResponseType定义redirect和json两实例由属性idu.security.browser.loginResponseType
决定认证之后到底是响应页面还是响应json，当然能做到这样，与自定义认证成功和失败处理器有很大的关系

```java

package com.duheng.security.component;

import com.duheng.security.enumeration.LoginResponseType;
import com.duheng.security.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Describe:
    登录认证成功后，Security默认跳转到引发认证的请求
    自定义IduAuthenticationSuccessHandler实现AuthenticationSuccessHandler：登录成功后返回json成功信息


 Authentication：
    封装了认证通过后相关的信息，最主要的就是UserDetails
 ObjectMapper:
    将对象转换成Json对象
 SavedRequestAwareAuthenticationSuccessHandler
    AuthenticationSuccessHandler的一个实现类，SpringBoot默认认证成功处理
 *************************/
@Component
public class IduAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {
        logger.info("登录成功！");
        if ( LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())){

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else{
            super.onAuthenticationSuccess(request,response,authentication);
        }
    }
}

```  
```java

package com.duheng.security.component;

import com.duheng.security.enumeration.LoginResponseType;
import com.duheng.security.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*************************
 Describe:
    自定义登录认证失败处理器 实现 AuthenticationFailureHandler
    SpringBoot默认的登录失败处理器类 SimpleUrlAuthenticationFailureHandler

 *************************/
@Component
public class IduAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        logger.info("登录失败！");
        if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())) {
            //自定义的登录失败处理
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(exception));
        }else{
            //SpringBoot默认的登录认证失败处理
            super.onAuthenticationFailure(request,response,exception);
        }
    }
}

```