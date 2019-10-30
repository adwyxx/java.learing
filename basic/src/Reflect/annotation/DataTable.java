package Reflect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义ORM注解类，用于被注解的类映射的数据表
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/30 10:21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataTable {
    //value参数表示对应的表名
    String value();
}
