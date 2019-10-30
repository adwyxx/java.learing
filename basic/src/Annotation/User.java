package Annotation;

import java.lang.annotation.*;
/**
 * 自定义注解@User,显示注解属性成员定义的不同格式
 * @author: Leo.Wang, adwyxx@qq.com
 * 定义注解成员的注意点:
 * 第一：只能用public或默认(default)这两个访问权修饰.例如,String value();这里把方法设为defaul默认类型；
 * 第二：参数成员只能用基本类型byte,short,char,int,long,float,double,boolean八种基本数据类型和 String,Enum,Class,annotations等数据类型，以及这一些类型的数组。
 * 第三：如果只有一个参数成员,最好把参数名称设为"value",后加小括号。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface User {
    int id() default -1;
    String name() default "";
    //使用枚举做参数
    UserGender gender() default UserGender.MALE;
    //使用数组做参数，使用时格式为: mobiles={"1234","5678"}
    String[] mobiles();
}
