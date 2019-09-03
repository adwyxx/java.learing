package CountDownLatch;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    //需求：分多个线程计算1-1000的和，等所有线程执行完毕后汇总并输出结果
    public static void main(String[] args) {
        //初始话CountDownLatch，注意：参数3一定要和下面启动的线程数一致
        final CountDownLatch latch =  new CountDownLatch(3);
        long start = System.currentTimeMillis();
        //初始化Runnable实例，注意：将上面初始化的CountDownLatch传递给Runnable实例
        //注意：三个线程的CountDownLatch实例必须时同一个
        CountDownThread cdh1 = new CountDownThread(1,300,latch);
        CountDownThread cdh2 =new CountDownThread(301,500,latch);
        CountDownThread cdh3 =new CountDownThread(501,1000,latch);
        new Thread(cdh1,"线程1").start();
        new Thread(cdh2,"线程2").start();
        new Thread(cdh3,"线程3").start();

        try {
            //使主线程等待，等待上面三个线程都执行完毕
            //每个线程执行完毕都调用了一次latch.countDown()
            latch.await(); //阻塞当前线程
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //待上述三个线程执行完毕后执行下面语句
        long end = System.currentTimeMillis();
        System.out.println("合计："+ (cdh1.getResult()+cdh2.getResult()+cdh3.getResult()));
        System.out.println("执行完毕,耗时："+ (end-start));
    }
}

class CountDownThread implements Runnable {
    //CountDownLatch实例
    private CountDownLatch latch;

    private int min;
    private int max;
    private int result;
    public int getResult() {
        return result;
    }

    public CountDownThread(int min,int max,CountDownLatch latch)
    {
        this.min=min;
        this.max=max;
        this.result=0;
        this.latch=latch;
    }
    @Override
    public void run() {
        try{
            for (int i = this.min; i <= this.max; i++) {
                this.result+=i;
                try {
                    Thread.sleep(10);
                }catch (Exception ex)
                {}
            }
            System.out.println(Thread.currentThread().getName()+" : "+this.result);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            //注意：执行完毕一定要掉用latch.countDown()
            //为避免该方法因为异常未能调用，一定要放在finally中调用
            latch.countDown();
            System.out.println("未执行完成的线程数："+latch.getCount());
        }
    }
}