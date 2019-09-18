package Thread;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * @Description: Thread 线程
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019/9/18 23:11
 * 1. 线程：
 * 2. 主要方法：
 *    2.1 start() : 启动一个线程，此时JVM才会真正创建一个线程。由底层的native()方法具体实现
 *    2.2 run() ：需要重写该方法，具体实现线程要干的工作。在start()方法启动后JVM底层的native方法start0()会调用run()方法
 *    2.3 getName() : 获取线程的名称
 *    2.4 getId() : 获取线程运行时的PID
 */
public class ThreadDemo {
    public static void main(String[] args) {

        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        });

        t1.run();   //注意：单独调用run()方法和调用普通类实例的方法一下，该方法并不会创建一个线程
        t1.start(); //start()方法才会创建一个线程来执行run()

        Thread t2 = new Thread(()->{},"T1");
        t2.setPriority(Thread.MAX_PRIORITY);
    }


}
