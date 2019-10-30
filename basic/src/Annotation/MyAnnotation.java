package Annotation;

import java.lang.annotation.*;

/**
 * 自定义注解
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/29 15:25
 * – 其中的每一个方法实际上是声明了一个配置参数。
 * – 方法的名称就是参数的名称
 * – 返回值类型就是参数的类型（返回值类型只能是基本类型、Class、String、enum）
 * – 可以通过default来声明参数的默认值。
 * – 如果只有一个参数成员，一般参数名为value
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Inherited //允许继承，即：子类可以继承父类的注解
public @interface MyAnnotation {
    //注解参数 value，默认值: ""  。如果只有一个参数成员,最好把参数名称设为"value",后加小括号。
    String value() default "";
}
