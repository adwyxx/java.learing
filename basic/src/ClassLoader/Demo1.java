package ClassLoader;

/**
 * 类的初始化：为静态变量赋值
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/11/5 14:35
 *
 * 在初始化阶段，主要为类的静态变量赋予正确的初始值，JVM负责对类进行初始化，主要对类变量进行初始化。在Java中对类变量进行初始值设定有两种方式：
 * 1. 声明类变量是指定初始值
 * 2. 使用静态代码块为类变量指定初始值
 *
 * 主动调用： 即new一个类对象，通过类对象调用类的成员
 *      初始化父类 --> 初始化子类
 * 被动调用： 调用类的静态常量.
 * 1.调用静态常亮，不会初始化类（静态代码块不会执行）
 * 2.调用静态变量：
 *      调用继承自父类的静态变量，父类会初始化，子类不会初始化
 *      调用自身定义的静态变量，父类会初始化，子类也会被初始化
 * 3.调用静态方法，父类和子类都会被初始化
 */
public class Demo1 {
    public static void main(String[] args) {
        //主动调用时：会导致类的初始化，如果类的基类没有初始化则先初始化基类
        initiative();
        //被动调用时：不会导致类的初始化，如果被动调用继承自父类的成员变量则会导致父类初始化
        //passive();
    }

    //主动调用：导致类被初始化的六种情况
    public static void initiative(){
        //1.创建类的实例，也就是new的方式
        Child child = new Child();
        //2.访问某个类或接口的静态变量，或者对该静态变量赋值
        System.out.println(Child.childCount);
        //3.调用类的静态方法
        Child.method();
        //4.反射（如 Class.forName(“com.shengsiyuan.Test”)）
        try {
            Class.forName("ClassLoader.Child");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //5.初始化某个类的子类，则其父类也会被初始化
        //6.Java虚拟机启动时被标明为启动类的类（ JavaTest），直接使用 java.exe命令来运行某个主类
    }

    //被动调用
    public static void passive(){
        //调用自身的静态常量，不会进行类初始化
        System.out.println(Parent.PARENT_NAME);
        //子类调用父类的静态常量，不会导致父类的静态代码块执行
        System.out.println(Child.PARENT_NAME);
        //子类调用自身的静态常量，不会导致父类的静态代码块执行
        System.out.println(Child.NAME);
        //子类调用父类的静态变量，会导致父类的静态代码块执行
        System.out.println(Child.max);
        //子类调用自己的静态变量，会导致父类及子类的静态代码块执行
        System.out.println(Child.childCount);
        //调用类的静态方法会导致其父类和自身的静态代码块执行
        //Child.method();
    }
}

class Parent {
    public static final String PARENT_NAME = "Parent Class";
    public static int max;

    private static int number ;

    static {
        System.out.println("Parent类静态初始化");
        number = 10;
    }

    public Parent(){
        System.out.println("创建Parent对象");
    }
}

class Child extends Parent{
    public static final String NAME = "Child Class";
    public static int childCount ;
    static {
        System.out.println("Child类静态初始化");
        childCount = 20;
    }

    public Child(){
        System.out.println("创建Child对象");
    }

    public static void method(){
        System.out.println("Child类的静态方法：method");
    }
}