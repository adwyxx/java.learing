package Annotation;

import java.lang.annotation.*;
import java.lang.reflect.Field;

/**
 * 使用注解示例
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/29 15:56
 */
@User(id=1,name="张三",mobiles = {"18888888888","13696969696"})
public class UseAnnotationDemo extends BaseAnnotationDemo {

    @MyPrivateAnnotation
    private String test;

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {

        Class<?> clazz1 = Class.forName(MyAnnotation.class.getName());
        //根据反射判断类型是不是注解
        System.out.println("MyAnnotation is Annotation? " + clazz1.isAnnotation());

        //1. 通过Class.forName()方法获取类的反射信息
        Class<?> clazz = Class.forName("Annotation.UseAnnotationDemo");
        System.out.println("UseAnnotationDemo is Annotation? " + clazz.isAnnotation());

        //2.使用 Class.getAnnotations()的所有注解信息,包含父类的注解
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation temp : annotations){
            System.out.println(temp);
        }
        //使用getDeclaredAnnotation(Class<T>) 方法获取注解信息（不包含父类的注解）
        MyAnnotation myAnnotation = clazz.getDeclaredAnnotation(MyAnnotation.class);
        System.out.println(myAnnotation);

        //3.获取特定注解信息，如果该类没有被相应的注解修饰则返回null
        User user = clazz.getAnnotation(User.class);
        System.out.println(user);
        System.out.println("user.name = "+user.name());
        System.out.println("user.id = "+user.id());

        Field field = clazz.getDeclaredField("test");
        MyPrivateAnnotation myPrivateAnnotation = field.getDeclaredAnnotation(MyPrivateAnnotation.class);
        System.out.println(myPrivateAnnotation);
    }

    /**
     * 自定义私有的标记注解（无参数的注解）
     * @author: Leo.Wang, adwyxx@qq.com
     * @date: 2019/10/30 11:30
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    private  @interface MyPrivateAnnotation {
    }
}
