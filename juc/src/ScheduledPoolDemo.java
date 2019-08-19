import java.time.LocalDateTime;
import java.util.concurrent.*;

public class ScheduledPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.创建一个ScheduledThreadPool，线程池中最多5个线程
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);

        //2.给线程池中线程分配任务：总共10个任务，给每个任务都输出1~10的和，且每隔2秒钟执行
        for (int i = 0; i <10 ; i++) {
            //提交任务，并获取返回值，用Callable接口，返回值用Future接口实例接受
            ScheduledFuture<Integer> result = pool.schedule(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int sum = 0;
                    for (int j = 1; j <=100 ; j++) {
                        sum += j;
                    }
                    System.out.println(Thread.currentThread().getName()+"："+sum);
                    return sum;
                }
            },5, TimeUnit.SECONDS); //注意：这里设置线程的执行时间间隔5，单位是秒

            //注意：这里调用了result.get()，pool中的线程会每隔5秒执行一个，否则所有线程不会等待立即执行
            System.out.println(LocalDateTime.now() +":"+ result.get());
        }

        //3.关闭线程池
        pool.shutdown();
    }
}
