import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * 与Object类的wait/notify机制相比，park/unpark有两个优点：
 *  1. 以thread为操作对象更符合阻塞线程的直观定义；
 *  2. 操作更精准，可以准确地唤醒某一个线程（notify随机唤醒一个线程，notifyAll唤醒所有等待的线程），增加了灵活性。
**/
public class ParkAndUnpark {
    public static void main(String[] args) {
        //消息列表
        final Queue<String> messageQueue = new ArrayDeque<>(10) ;
        final Queue<Thread> parkedCosumers = new ArrayDeque<>() ;
        //创建两个生产者线程
        MassageProducer maker = new MassageProducer(messageQueue,parkedCosumers);
        Thread thread = new Thread(maker,"Maker");
        thread.start();

        //创建20消费者线程
        MassageConsumer consumer = new MassageConsumer(messageQueue,thread,parkedCosumers);
        for (int i = 1; i <= 20; i++) {
            new Thread(consumer,"Consumer"+i).start();
        }
    }
}

// 生产者
class MassageProducer implements Runnable{
    //消息队列
    private Queue<String> messageQueue;
    //消费者线程
    private Queue<Thread> parkedCosumers;
    public MassageProducer(Queue<String>  messageQueue,Queue<Thread> parkedCosumers)
    {
        this.messageQueue = messageQueue;
        this.parkedCosumers = parkedCosumers;
    }

    @Override
    public void run() {
        while(true)
        {
            if(this.messageQueue.size()==10)
            {
                Thread th = Thread.currentThread();

                for (int i = 0; i <parkedCosumers.size() ; i++) {
                    Thread thc = parkedCosumers.poll();
                    System.out.println(thc.getName()+":unpark");
                    LockSupport.unpark(thc);
                }
                System.out.println(th.getName()+":park");
                LockSupport.park(th);
            }

            Random rm = new Random();
            for (int i = 0; i < 10; i++) {
                String str =String.valueOf(rm.nextInt(100)+1);
                if(this.messageQueue.offer(str))
                {
                    System.out.println(Thread.currentThread().getName()+":"+str);
                }
            }

            //生产完信息后休眠2秒
            try {
                Thread.sleep(2000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
// 消费者
class MassageConsumer implements Runnable {
    //消息队列
    private Queue<String>  messageQueue ;
    //生产者线程
    private Thread producerThread;
    //阻塞的消费者线程
    private Queue<Thread> parkedCosumers ;
    public MassageConsumer(Queue<String>  messageQueue,Thread thread,Queue<Thread> parkedCosumers)
    {
        this.messageQueue=messageQueue;
        this.producerThread = thread;
        this.parkedCosumers = parkedCosumers;
    }

    @Override
    public void run() {
        while(true)
        {
            if(this.messageQueue.isEmpty())
            {
                LockSupport.unpark(producerThread);
                Thread th = Thread.currentThread();
                this.parkedCosumers.offer(th);
                System.out.println(th.getName()+":park");
                LockSupport.park(th);
            }
            System.out.println(Thread.currentThread().getName()+":"+this.messageQueue.poll());
        }
    }
}