package CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @Description: CyclicBarrier.CyclicBarrierTest（可重用屏障/栅栏），需要等待一组线程全部到达一个指定的点后才能一起执行，就如果开会，需要等人都到齐才开始一样。
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-02 15:46
 * @Remark: 应用场景：常用于多线程分组计算
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        //1.初始化CyclicBarrier实例对象，
        //  第一个参数：int parties 指定栅栏拦截的线程个数
        //  第二个参数：Runnable barrierAction 用于线程到达屏障时，优先执行 barrierAction，方便处理更复杂的业务场景
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3,()->{
            System.out.println(Thread.currentThread().getName()+"======等待线程数达到屏障数======");
        });
        for(int i=1;i<=3;i++){
            System.out.println("创建线程--T"+String.valueOf(i));
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"--等待其他线程...");
                try {
                    //2. CyclicBarrier.CyclicBarrierTest.await()使线程阻塞
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName()+"--开始执行");
                    //模拟线程处理业务耗时
                    TimeUnit.SECONDS.sleep(3);
                    System.out.println(Thread.currentThread().getName()+"--运行完毕");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"T"+String.valueOf(i)).start();
        }
        /*
        for(int i=1;i<=10;i++){
            System.out.println("创建线程：T"+String.valueOf(i));
            new Worker(cyclicBarrier,"T"+String.valueOf(i)).start();
        }*/
    }

    public  static  class Worker extends Thread {
        //定义所有线程公用的CyclicBarrier
        private CyclicBarrier cyclicBarrier;
        //构造函数：传入CyclicBarrier实例对象
        public Worker(CyclicBarrier barrier,String name){
            super(name);
            this.cyclicBarrier = barrier;
        }

        @Override
        public void run(){
            super.run();//基类run()
            System.out.println(Thread.currentThread().getName()+" waiting other Threads...");
            try {
                //挂起该线程
                this.cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName()+" running");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" stop!");
        }
    }
}

