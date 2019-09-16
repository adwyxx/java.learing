/**
 * @Description: 类初始化启动运行顺序演示
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-29 11:28
 * 执行顺序：变量初始化 -> 静态代码块 -> 非静态代码块
 *  1.基类
 *  1.基类静态代码块（只执行一次）
 *  2.子类静态代码块（只执行一次）
 *  3.基类非静态代码块（每次初始化一个类实例都执行一次）
 *  4.基类构造函数（每次初始化一个类实例都执行一次）
 *  5.子类非静态代码块（每次初始化一个类实例都执行一次）
 *  6.子类构造函数（每次初始化一个类实例都执行一次）
 */
class OrderBase {
    private static String staticParam = "基类静态变量";
    private String nomalParam="基类非静态变量";

    //基类静态代码块
    static {
        System.out.println("基类静态代码块");
        System.out.println(staticParam);
    }
    //基类非静态代码块
    {
        System.out.println("基类非静态代码块");
        System.out.println(nomalParam);
    }
    public OrderBase(){
        System.out.println("父类构造函数");
    }

}
public class ClassRunOrder extends OrderBase {
    private static String staticParam = "子类静态变量";
    private String nomalParam="子类非静态变量";
    //子静态代码块
    static {
        System.out.println("子类静态代码块");
        System.out.println(staticParam);

    }
    //子类非静态代码块
    {
        System.out.println("子类非静态代码块");
        System.out.println(nomalParam);
    }
    public ClassRunOrder(){
        System.out.println("子类构造函数");
    }

    public static void mehtod(){
        System.out.println("子类静态方法");
    }

    public static void main(String[] args) {

        System.out.println("--------------");
        ClassRunOrder order1 = new ClassRunOrder();
        System.out.println("--------------");
        ClassRunOrder order2 = new ClassRunOrder();
        //ClassRunOrder.mehtod();
    }
}
/**
 基类静态代码块
 基类静态变量
 子类静态代码块
 子类静态变量
 --------------
 基类非静态代码块
 基类非静态变量
 父类构造函数
 子类非静态代码块
 子类非静态变量
 子类构造函数
 --------------
 基类非静态代码块
 基类非静态变量
 父类构造函数
 子类非静态代码块
 子类非静态变量
 子类构造函数
*/

