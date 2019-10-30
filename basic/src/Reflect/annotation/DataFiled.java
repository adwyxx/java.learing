package Reflect.annotation;

import Reflect.FiledDataType;
import Reflect.GenerateFilterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义ORM注解类，用于标识被注解的属性字段映射的数据库字段
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/30 10:25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataFiled {
    String filedName();
    FiledDataType dataType() default FiledDataType.VARCHAR;
    GenerateFilterType[] filterTypes() default {GenerateFilterType.SELECT,
            GenerateFilterType.UPDATE,
            GenerateFilterType.INSERT,
            GenerateFilterType.DELETE
    };
}
