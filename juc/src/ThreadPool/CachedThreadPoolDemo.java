package ThreadPool;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolDemo {
    public static void main(String[] args) {
        //1.创建一个可缓冲线程池，不固定大小
        ExecutorService pool = Executors.newCachedThreadPool();

        //2.给线程池分配100个任务
        for (int i = 0; i < 100; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    int num = new Random().nextInt(100);//生成随机数
                    System.out.println(Thread.currentThread().getName()+":"+num);
                }
            });
        }

        //关闭线程池
        pool.shutdown();
    }
}
