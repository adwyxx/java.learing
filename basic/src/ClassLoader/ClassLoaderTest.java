package ClassLoader;

/**
 * Java类加载器
 *
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/11/5 16:04
 * 一、类加载器：
 * 1. BootStrap ClassLoader:
 *    最顶层的加载类，主要加载核心类库，也就是我们环境变量下面%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等。
 * 2. Extention ClassLoader:
 *    扩展的类加载器，加载目录%JRE_HOME%\lib\ext目录下的jar包和class文件
 * 3. Appliction ClassLoader
 *    也称为SystemAppClass。 加载当前应用的classpath的所有类。
 * 4. 自定义 ClassLoader：
 * 二、层级关系
 *  BootStrap ClassLoader <-- Extention ClassLoader <-- Appliction ClassLoader <-- 自定义 ClassLoader
 *  注意： 后面三者为继承关系，而BootStrap ClassLoader是由C++实现的所以不能被继承，但是确实最顶层的。
 */

public class ClassLoaderTest {
    public static void main(String[] args) {
        //Application ClassLoader
        System.out.println(ClassLoaderTest.class.getClassLoader());
        //Extention ClassLoader
        System.out.println(ClassLoaderTest.class.getClassLoader().getParent());
        //BootSrap ClassLoader
        System.out.println(ClassLoaderTest.class.getClassLoader().getParent().getParent());//null，因为BootStrap ClassLoader不能被继承
    }
}
