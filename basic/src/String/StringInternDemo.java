package String;

/**
 * @Description: String.intern():编译器会将字符串添加到常量池中（StringTable维护），并返回指向该常量的引用
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 16:12
 * 1. new String()都是在堆上创建字符串对象,每次都创建一个新的对象，所以 new String("A")!=new String("A")
 * 2. 通过字面量赋值创建字符串（如：String str="abc"）时,会先在常量池中查找是否存在相同的字符串:
 *    2.1 若存在，则将栈中的引用直接指向该字符串；
 *    2.2 若不存在，则在常量池中生成一个字符串，再将栈中的引用指向该字符串。
 * 3. 常量字符串的“+”操作，编译阶段直接会合成为一个字符串。
 *    3.1 String str = "ab"+"c"; ==编译阶段直接合并语句==> String str = "abc";
 *    3.2 然后再去常量池中查找是否有"abc",有则返回引用，无则创建（同2操作）。
 * 4. 对于final字段，编译期直接进行了常量替换（而对于非final字段则是在运行期进行赋值处理的）
 * 5. 常量字符串和变量拼接时(如：String str2 = STR+"C";),先进行常量替换，再编译时拼接，然后执行常量池查找创建操作
 * 6. JDK 1.7后，intern()方法还是会先去查询常量池中是否有已经存在:
 *    6.1 如果存在，则返回常量池中的引用，这一点与之前没有区别，
 *    6.2 区别在于: 如果在常量池找不到对应的字符串，则不会再将字符串拷贝到常量池，而只是在常量池中生成一个对原字符串的引用。
 *        简单的说，就是往常量池放的东西变了：原来在常量池中找不到时，复制一个副本放到常量池，1.7后则是将在堆上的地址引用复制到常量池。
 */
public class StringInternDemo {
    public static void main(String[] args) {
        newStringTest();
        System.out.println("------------------");
        literalAssignTest();
        System.out.println("------------------");
        finalTest();
        System.out.println("------------------");
        constantConectTest();
        System.out.println("------------------");
        internTest();
    }


    //1.new String()都是在堆上创建字符串对象,每次都创建一个新的对象
    private static void newStringTest() {
        String s1 = new String("abc"); //在堆中新建一个对象
        String s2 = new String("abc"); //在堆中新建一个对象
        System.out.println(s1==s2); //false
    }
    //2.通过字面量赋值创建字符串（如：String str="abc"）时,会先在常量池中查找,有则返回引用，无则创建再返回引用
    //3.常量字符串的“+”操作
    private static void literalAssignTest() {
        String s1 = "abc"; //在常量池中查找，发现没有，在常量池中创建"abc"
        String s2 = "abc"; //在常量池中查找，发现有，返回"abc"地址
        String s3 = "ab"+"c";  //编译时合并+号两边的字面常量，相当于 s3="abc"
        String s4 = "a"+"b"+"c"; //相当于 s4="abc"
        System.out.println(s1==s2); //true
        System.out.println(s2==s3); //true
        System.out.println(s3==s4); //true
    }

    //4. 对于final字段，编译期直接进行了常量替换（而对于非final字段则是在运行期进行赋值处理的）
    private static void finalTest() {
        String str = "abc";  //先查找常量池，有则返回引用，无则创建并返回引用
        final String str1 = "ab";
        final String str2 = "c";
        String str3 = str1+str2; //先做常量替换（str3="ab"+"c"）,再执行编译合并(str3="abc"),然后在从常量池中查找
        System.out.println(str==str3); //true
        System.out.println(str==str3.intern()); //true
    }

    //5.常量字符串和变量拼接时,先进行常量替换，再编译时拼接，然后执行常量池查找创建操作
    private static final String STR = "AB";
    private static void constantConectTest() {
        String str1 = "ABC";
        String str2 = STR+"C"; //-->str2 = "AB"+"C"; --> str2 = "ABC";
        System.out.println(str1==str2.intern()); //true
        System.out.println(str1.equals(str2)); //true
    }

    //6.JDK 1.7后，intern()方法还是会先去查询常量池中是否有已经存在:
    //如果存在，则返回常量池中的引用，这一点与之前没有区别，
    //区别在于: 如果在常量池找不到对应的字符串，则不会再将字符串拷贝到常量池，而只是在常量池中生成一个对原字符串的引用。
    //简单的说，就是往常量池放的东西变了：原来在常量池中找不到时，复制一个副本放到常量池，1.7后则是将在堆上的地址引用复制到常量池。
    private static void internTest() {
        String s2 = new String("12")+new String("34"); //s2="1234"
        s2.intern(); //注意，intern()方法调用在s2之前,在常量池中生成一个对堆中“1234”的引用（注意是引用，即内存地址）
        String s1 = "1234"; //常量池中存在对"1234"的引用地址，直接返回这个地址给s1
        System.out.println(s1==s2); //true

        String s3 = new String("56")+new String("78");
        String s4 = "5678"; //在常量池装创建一个“5678”
        s4.intern(); //注意，intern()方法调用在s4之后，此时没有相应的引用地址，索引创建一个对堆中“5678”的引用地址
        System.out.println(s3==s4); //false,常量池中一个是值，一个是对的引用地址，两者所指向的地址不同
    }

}
