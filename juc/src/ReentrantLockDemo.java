import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    //需求：假设有100张票，5个售票窗口售票，售完为止
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(ticket,"窗口1").start();
        new Thread(ticket,"窗口2").start();
        new Thread(ticket,"窗口3").start();
        new Thread(ticket,"窗口4").start();
        new Thread(ticket,"窗口5").start();
    }
}

class Ticket implements Runnable
{
    private int totalTickets=100;
    private Lock loker = new ReentrantLock();
    @Override
    public void run() {
        while (true)
        {
            loker.lock();//加锁
            try {
                if(this.totalTickets>0)
                {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    this.totalTickets--;
                    System.out.println(Thread.currentThread().getName()+"售票,余票："+this.totalTickets);
                }
                else
                {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            finally {
                loker.unlock();//解锁
                //注意：一定要讲unlock放在finally中执行，防止出现异常不能解锁
            }
        }
    }
}
