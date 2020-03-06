package com.duheng.security.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/*************************
 Author: 杜衡
 Date: 2020/3/5
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
