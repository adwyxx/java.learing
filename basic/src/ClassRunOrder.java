/**
 * @Description: 类初始化启动运行顺序演示
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-29 11:28
 */
class OrderBase {
    //基类静态代码块
    static {
        System.out.println("父类静态代码块");
    }

    public OrderBase(){
        System.out.println("父类构造函数");
    }

}
public class ClassRunOrder extends OrderBase {
    //静态代码块
    static {
        System.out.println("子类静态代码块");
    }

    public ClassRunOrder(){
        System.out.println("子类构造函数");
    }

    public static void mehtod(){
        System.out.println("子类静态方法");
    }

    public static void main(String[] args) {
        ClassRunOrder order1 = new ClassRunOrder();
        ClassRunOrder order2 = new ClassRunOrder();
        //ClassRunOrder.mehtod();
    }
}
/**
 父类静态代码块
 子类静态代码块
 父类构造函数
 子类构造函数
 父类构造函数
 子类构造函数
*/

