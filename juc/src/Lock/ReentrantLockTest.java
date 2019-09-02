package Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 可重入锁（递归锁）含义演示
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-02 09:23
 * 可重入锁，也叫递归锁
 * 同一个线程外层函数获得锁之后，内层函数依然能工获得该锁的代码
 * 在同一个线程在外层方法F1获取锁的同时，在进入内层方法F2会自动获取锁
 * 即：F1，F2均加锁，F1方法里调用F2，在进入F1是获取锁，F1内层方法F2同样具有F1的锁
 * 线程可以进入任何一个他已经拥有的锁的同步着的代码块
 *
 * ReentrantLock 和 synchronized 都是可重入锁
 */
public class ReentrantLockTest {

    public static void main(String[] args) {
        ReentrantLockTest demo = new ReentrantLockTest();
        //验证ReentrantLock
        for (int i = 1; i <=3 ; i++) {
            new Thread(()->{
                demo.method1();
            },"RL"+String.valueOf(i)).start();
        }

        //验证synchronized
        for(int i=1;i<=3;i++){
            new Thread(()->{
                demo.method3();
            },"Sync"+String.valueOf(i)).start();
        }
    }

    //1.初始化可重入锁
    static final ReentrantLock lock = new ReentrantLock();
    //2.method1中使用可重入锁
    //  method1中调用了method2，运行时可发现method1和method2都执行了，说明两个方法的锁是相同的
    public void  method1()
    {
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t methrod1()");
            //调用method2，注意method2同样加了锁，此时method1与method2中的锁可以理解为是同一个锁
            //method1可以进入method2中锁的同步代码块
            method2();
        }finally {
            lock.unlock();
        }
    }
    //3.method2中使用可重入锁
    public void method2(){
        lock.lock();
        try{
            System.out.println(Thread.currentThread().getName()+"\t methrod2()");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    //注意：方法中的lock.lock()与lock.unlock()必须成对出现，即：加锁几次就得解锁几次
    //     如果不成对出现，将会出现死锁的情况
    public void method5(){
        lock.lock();
        lock.lock();//加锁两次
        try{
            System.out.println(Thread.currentThread().getName()+"\t methrod5()");
            method2();
        } finally {
            lock.unlock();
            lock.unlock();//解锁两次
            //如果这里少了解锁次数，则线程会一直阻塞在次数，不会执行method2()调用
        }
    }

    //synchronized可重入锁验证
    public synchronized void method3(){
        System.out.println(Thread.currentThread().getName()+"\t methrod3()");
        //method3可以进入method4中锁的同步代码块
        method4();
    }
    //synchronized method4:
    public synchronized void method4() {
        try{
            System.out.println(Thread.currentThread().getName()+"\t methrod4()");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

