package Lock;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 死锁Demo
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-10 11:14
 * 1.产生死锁的原因:
 *      两个或两个以上的线程互相尝试获取对方已经持有的锁（抢夺共享资源），形成交叉竞争，导致线程死锁
 * 2.排查死锁方法：
 *      2.1 使用命令：jps -l 查询当前运行的java线程对应的PID（相当于Linux命令：ps -ef|grep java）
 *      2.2 使用命令：jstack -l pid 查询对应线程的错误堆栈
 * 3.死锁的提示信息如下：
===================================================
"BBB":
at Lock.LockHolder.getLockB(DeadLockDemo.java:72)
- waiting to lock <0x0000000780d93838> (a java.lang.String)     //这里等待的锁与AAA持有的锁一样
- locked <0x0000000780d93870> (a java.lang.String)              //这里持有的锁与AAA等待的锁一致，形成死锁
at Lock.DeadLockDemo.lambda$main$1(DeadLockDemo.java:25)
at Lock.DeadLockDemo$$Lambda$2/1747585824.run(Unknown Source)
at java.lang.Thread.run(Thread.java:748)
"AAA":
at Lock.LockHolder.getLockA(DeadLockDemo.java:53)
- waiting to lock <0x0000000780d93870> (a java.lang.String)     //AAA等待的锁
- locked <0x0000000780d93838> (a java.lang.String)              //AAA持有的锁
...
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        final String lockA="LockA";
        final String lockB="LockB";

        LockHolder holder = new LockHolder(lockA,lockB);

        //1.线程AAA调用获取了共享资源lockA的锁，然后尝试获取lockB的锁
        //  但是此时线程BBB持有了lockB的锁，两个线程形成竞争
        new Thread(()->{
            holder.getLockA();
        },"AAA").start();
        //2.线程BBB调用获取了共享资源lockB的锁，然后尝试获取lockA的锁
        //  但是此时线程AAA持有了lockA的锁，两个线程形成竞争
        new Thread(()->{
            holder.getLockB();
        },"BBB").start();
    }
}

class LockHolder {
    private String lockA;
    private String lockB;

    public LockHolder(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    /**
    * @description: 获取了lockA后再尝试获取lockB
    * @author: Leo.Wang
    * @date: 2019/9/10 22:04
    */
    public void getLockA(){
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+" 获得了"+lockA);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+" 获得了"+lockB);
            }
        }
    }

    /**
    * @description: 获取了lockB后再尝试获取lockA
    * @author: Leo.Wang
    * @date: 2019/9/10 22:05
    */
    public void getLockB(){
        synchronized (lockB){
            System.out.println(Thread.currentThread().getName()+" 获得了"+lockB);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lockA){
                System.out.println(Thread.currentThread().getName()+" 获得了"+lockA);
            }
        }
    }
}
