package com.duheng.security.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*************************
 Author: 杜衡
 Date: 2020/3/5
 Describe:
 *************************/
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MyConstraint.class)//指定哪个类赋予注解业务逻辑
public @interface MyValidAnnotation {
    String message() default "{自定义数据校验注解}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
