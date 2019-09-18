package OOM;

/**
 * @Description: StackOverflowError,栈溢出错误
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 09:10
 * 1. 错误类型：java.lang.StackOverflowError	at OOM.StackOverflowErrorDemo.stackOverflow
 * 2. 当栈深度超过虚拟机分配给线程的栈大小时就会出现此error。
 * 3. 引起该错误的程序表现为：方法的递归调用，导致JVM不停的为方法分配新的栈空间
 * 4. 可以使用命令：java -XX:+PrintFlagsInitial 命令查看JVM最大栈深度默认值：（默认1024）
 *    intx MaxJavaStackTraceDepth  = 1024
 */
public class StackOverflowErrorDemo {

    public static void main(String[] args) {
        stackOverflow();
    }

    //stackOverflow()递归调用时，都会产生一个新的栈帧区块，这时就会连续的产生新的栈帧区块
    //当栈内存超过系统配置的栈内存-Xss:2048[-Xss 为JVM启动的每个线程分配的内存大小]，就会出现java.lang.StackOverflowError异常。
    //这也是为什么对于需要谨慎使用递归调用的原因！
    private static void stackOverflow() {
        //递归调用，每调用一次就会分配栈空间
        //栈的深度不停加深
        stackOverflow();//at OOM.StackOverflowErrorDemo.stackOverflow(StackOverflowErrorDemo.java:21)
    }
}
