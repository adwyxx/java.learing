package Reflect;

import Reflect.annotation.DataFiled;
import Reflect.annotation.DataTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 反射编程示例 {@link Class}
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/30 10:47
 */
public class ReflectDemo {
    public static void main(String[] args) {
        String classPath = "Reflect.User";

        try {
            //使用通配符Class<?>，不用纠结返回类型的问题
            Class<?> clazz1 = Class.forName("Reflect.User");

            //泛型的方式获取一个类的反射信息
            Class<User> clazz = (Class<User>)Class.forName(classPath);
            //获取public的字段
            System.out.println("----------clazz.getField()-----------");
            Field publicField = clazz.getField("age"); //如果没有对应的属性将抛出异常：java.lang.NoSuchFieldException: age
            Field[] publicFields = clazz.getFields();
            System.out.println(publicField);
            for(Field pf:publicFields){
                System.out.println(pf);
            }

            //获取定义的单个字段（private 或 public）
            Field privateFiled = clazz.getDeclaredField("id");//如果没有对应的属性将抛出异常：java.lang.NoSuchFieldException: id
            Field[] privateFields = clazz.getDeclaredFields();
            System.out.println("----------clazz.getDeclaredField()-----------");
            System.out.println(privateFiled);
            for(Field f:privateFields){
                System.out.println(f);
            }
            //使用反射实例化一个对象
            System.out.println("----------clazz.newInstance()-----------");
            User user = clazz.newInstance();
            System.out.println(user);

            //使用反射获取对象上的注解
            System.out.println("----------clazz.getAnnotations()-----------");
            //获取指定类型的注解,如果没有添加对应的注解则返回null
            DataTable dtAnnotation = clazz.getAnnotation(DataTable.class);
            System.out.println( dtAnnotation.value());
            System.out.println( clazz.getAnnotation(DataFiled.class)); //null
            Annotation[] annotations = clazz.getAnnotations();
            for(Annotation an:annotations){
                System.out.println(an);
            }

            //获取类继承的接口信息
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> interf : interfaces){
                System.out.println(interf);
            }

        } catch (ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
