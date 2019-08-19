import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ThreadPoolModel implements Runnable {
    @Override
    public void run() {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        System.out.println(Thread.currentThread().getName()+":"+sum);
    }
}

public class ThreadPoolDemo {
    public static void main(String[] args) {
        //1.定义一个线程池：使用Executors
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ThreadPoolModel th = new ThreadPoolModel();

        //2.使用线程池提交操作
        for (int i = 0; i <10 ; i++) {
            pool.submit(th);
            //注意：线程池中共有5个线程，但是提交了10次任务。
            //可见：线程池能够最大限度的利用线程，而不必为每个任务都创建线程，提高了效率
        }

        //3.关闭线程池。
        pool.shutdown();
        //注意：shutdown()方法不会立即关闭线程池，而是等待所有submit的任务执行完毕后再关闭。

    }
}
/**运行结果：可见每个线程基本被调用了两次
pool-1-thread-2:4950
pool-1-thread-5:4950
pool-1-thread-4:4950
pool-1-thread-3:4950
pool-1-thread-1:4950
pool-1-thread-3:4950
pool-1-thread-1:4950
pool-1-thread-5:4950
pool-1-thread-4:4950
pool-1-thread-2:4950
**/
