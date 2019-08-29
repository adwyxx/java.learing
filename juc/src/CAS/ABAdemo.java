package CAS;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Description: Unsafe类CAS产生的ABA问题及解决方案
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-29 08:43
 */
public class ABAdemo {
    //什么是ABA问题：
    //假设两个线程T1和T2同时操作数据V，V的初始值为A
    //1.在初始阶段T1和T2都从主内存中Copy了一份V=A的副本到本地工作内存
    //2.假设线程T1的运行时间较长（10秒），T1需要在最后（第10秒）才将V的值C写回主内存
    //3.线程T2运行时间短（2秒），期间T2将V的值A改成B写回主内存，之后T2又将V的值改回A写回主内存，即:主内存中V的值改变过程： A->B->A
    //4.而此时T1还没有运行完，等T1运行完将V=C写回主内存时通过CAS （期望值A,更新值C）比较发现此时主内存中V的值为A，符合期望值，于是更新成功
    //5.T1对于T2在这段时间内更改主内存中V的值（A->B->A）的过程无从知晓
    public static void main(String[] args) {
        //一、验证ABA问题
        AtomicInteger atomicInteger = new AtomicInteger(5);
        new Thread(()->{
            System.out.println("-----Th1 running-----");
            int expect = atomicInteger.get(); //获取操作前值
            try {
                //线程Th1休眠5秒，使线程Th2有足够时间完成ABA操作，
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Th1 update 2019 : "+atomicInteger.compareAndSet(expect, 2019));
            System.out.println("Th1 atomicInteger="+atomicInteger.get());
            System.out.println("-----Th1 stop-----");
        },"Th1").start();

        new Thread(()->{
            System.out.println("-----Th2 ABA begin-----");
            int oldValue = atomicInteger.get(); //获取操作前值
            //A->B->A
            atomicInteger.compareAndSet(oldValue,1001);
            atomicInteger.compareAndSet(atomicInteger.get(),oldValue);
            System.out.println("-----Th2 ABA end-----");
        },"Th2").start();
        
        //如何解决ABA问题：
        //1.在每次CAS操作时加上版本号，用于区分本次操作的数据版本和主内存中的版本是否一致
        //2.期望值和版本号一致，则任务没有其他线程对其做修改，允许更新
        //3.期望值和版本号任何一个不一致则操作失败
        //使用AtomicStampedReference带版本号的原子操作类来解决CAS的ABA问题
        //AtomicStampedReference<T>(T 初始值，int 初始版本号)
        System.out.println("=========AtomicStampedReference ABA=========");
        AtomicStampedReference<Integer> atomicStamped = new AtomicStampedReference<Integer>(5,1);
        new Thread(()->{
            System.out.println("Th3 Beginning");
            int oldValue = atomicStamped.getReference();  //原始值
            int stamp = atomicStamped.getStamp(); //版本号
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //A->C,Version+1,对比期望值和期望版本号
            boolean success = atomicStamped.compareAndSet(oldValue,2019,stamp,stamp+1);
            System.out.println("Th3 oldValue="+oldValue+",oldVersion="+stamp);
            System.out.println("Th3 更新"+success);
        },"Th3").start();
        //Th4 :模拟ABA操作
        new Thread(()->{
            System.out.println("Th4 Beginning");
            int oldValue = atomicStamped.getReference();  //原始值
            //参数：期望值，更新值，期望版本号，新版本号
            //A->B,Version+1
            atomicStamped.compareAndSet(oldValue,
                    1001,
                     atomicStamped.getStamp(),
                    atomicStamped.getStamp()+1);
            //B->A ,Version+1
            atomicStamped.compareAndSet(atomicStamped.getReference(),
                    oldValue,
                    atomicStamped.getStamp(),
                    atomicStamped.getStamp()+1);
            System.out.println("Th4 value="+atomicStamped.getReference()+",version="+atomicStamped.getStamp());
            System.out.println("Th4 end");
        },"Th1").start();
    }
}
