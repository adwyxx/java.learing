package Lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: ReentrantLock和Condition实现多条件精确唤醒线程
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-05 09:23
 * ReentrantLock 与 synchronized 的区别：
 * 1.原始构成：
 *   synchronized 是关键字，属于JVM层面。主要是通过monitor来实现同步
 *   底层通过monitor对象来完成，wait()/notify()方法都依赖于monitor对象。所以wait()/notify()，必须和synchronized同时使用
 *   javap查看字节码可知底层monitor操作：
 *      - monitorenter
 *      - monitorexit
 *   Lock 是Java的Api（java.util.concurrent.locks.lock）
 * 2.使用：
 *   synchronized 不需要用户手动释放锁，当synchronized代码块执行完毕后，系统会自动让线程释放锁
 *   ReentrantLock 必须需要手工加锁和释放锁,不释放锁会出现死锁现象。且加锁和释放锁必须成对出现（即：加几把锁就要相应的解几把锁）
 *                 lock.lock();
 *                 try{...}
 *                 catch(){...}
 *                 finally{
 *                      lock.unlock();
 *                 }
 * 3.可以中断：
 *   synchronized 作用的代码块内，程序运行时不能被中断，除非抛出异常或运行完毕
 *   ReentrantLock 加锁的代码块内，程序可以中断：
 *                3.1 使用trylock(long timeout,TimeUnit unit)方法设置超时时中断
 *                3.2 使用lockInterruptibly()加锁，在代码块中使用interrupt()中断运行
 * 4.加锁是否是公平锁
 *   synchronized 默认是非公平的
 *   ReentrantLock 默认无参数构造函数是非公平锁，可以通过带参数的构造函数ReentrantLock(boolean isFair)来控制是否公平
 *
 * 5.锁绑定多条件，精准唤醒线程
 *   synchronized 不支持
 *   ReentrantLock 可以使用Condition condition = ReentrantLock.newCondition() 生成并绑定多个条件
 *                可以使用不同的condition阻塞(ondition.await())-唤醒(condition.signal())不同的线程
 */
public class ReentrantLock_Condition {
    //模拟个打印程序，线程A打印完成->唤醒线程B->唤醒线程C->唤醒A，循环打印
    public static void main(String[] args) {
        final Lock lock = new ReentrantLock();
        Printer printer = new Printer(lock);
        List<Thread> threads = new ArrayList<>();
        for(int i=1;i<=10;i++){
            //添加当前线程的condition
            printer.addCondition(lock.newCondition());
            final int index = i-1;
            threads.add(
                    new Thread(()->{
                        printer.print(index);
                    },"T"+String.valueOf(i))
            );
        }

        for(Thread t:threads){
            t.start();
        }
    }
}

/**
 * @description:打印程序
 * @author: Leo.Wang
 * @date: 2019/9/5 15:53
 *
 */
class Printer {
    //锁
    private Lock lock;
    //当前线程的索引
    private int currentThreadIndex=0;
    //所有线程的Condition列表
    private List<Condition> conditions;
    public Printer(Lock lock) {
        this.lock = lock;
        conditions = new ArrayList<>();
    }

    public void addCondition(Condition condition){
        this.conditions.add(condition);
    }
    //获取要唤起的下一个线程的condition
    private Condition getNextCondition()
    {
        if(this.currentThreadIndex==(this.conditions.size()-1)){
            return conditions.get(0);
        }
        else
        {
            return conditions.get(this.currentThreadIndex+1);
        }
    }

    /**
     * @description: 打印方法
     * @author: Leo.Wang
     * @date: 2019/9/5 16:43
     * @param index : 请求打印的线程索引
     */
    public void print(int index)
    {
        lock.lock(); //1.加锁
        try {
            //2.循环判断：不是如果当前执行打印的线程，则阻塞线程
            while(this.currentThreadIndex!=index) {
                //当前线程等待
                conditions.get(index).await();
            }
            //3.干活儿
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName()+": Printing...");
            }
            //打印完毕，唤醒下一个线程
            if(this.currentThreadIndex==(this.conditions.size()-1))
            {
                this.currentThreadIndex=0;
            }else {
                this.currentThreadIndex++;
            }
            //4.唤醒先一个线程
            this.getNextCondition().signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //5.解锁
            lock.unlock();
        }
    }
}