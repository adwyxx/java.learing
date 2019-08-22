import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

//信号量Semaphore
public class SemaphoreDemo {

    public static void main(String[] args) {
        //1.创建一个信号量对象，分配1个许可
        Semaphore  semaphore = new Semaphore(1);
        //2.运行线程，使用信号量acquire()方法获取许可，使用release()方法释放许可
        //3.获得许可的线程将继续执行
        //4.没有获取许可的线程将被park并加入等待队列。
        //5.当获得许可的线程执行完毕release()许可，信号量再从等待队列中获取一个线程
        // 如果能分配到该线程所需的许可数量，线程将被unpark
        new Thread(new SemaphoreTask(semaphore,1),"线程A").start();
        new Thread(new SemaphoreTask(semaphore,1),"线程B").start();
    }
}

class SemaphoreTask implements Runnable{
    //信号量
    private  Semaphore semaphore;
    private int permis;//线程申请许可个数

    public SemaphoreTask(Semaphore semaphore, int permis) {
        this.semaphore = semaphore;
        this.permis = permis;
    }

    @Override
    public void run() {
        try {
            //2.申请[permis]个许可，如果获得许可则继续执行，否则线程将被park
            //semaphore.acquire();
            semaphore.acquire(this.permis);
            String name = Thread.currentThread().getName();
            System.out.println(name+"-开始执行");
            TimeUnit.SECONDS.sleep(2);
            System.out.println(name+"-执行结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            //3.线程执行完毕释放[permis]个许可。
            //释放许可后，信号量将从阻塞队列中获取一个阻塞中的线程分配许可使之继续执行unpark
            semaphore.release(this.permis);
        }

    }
}