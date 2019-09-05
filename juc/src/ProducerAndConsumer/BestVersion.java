package ProducerAndConsumer;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 最优方案：使用BlockingQueue,volatile,Atomic来实现
 * @Author: Leo Wang
 * @Email: adwyxx@qq.com
 * @Date: $2019-09-05 21:11
 **/
public class BestVersion {

    public static void main(String[] args) {
        //1.初始化一个资源
        Resource resource = new Resource(new ArrayBlockingQueue<String>(5));

        //2.初始化一个线程池,5个线程，2个生产者，3个消费者
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //生产者
        for (int i = 1; i <=2 ; i++) {
            pool.submit(new Thread(()-> {
                try {
                    resource.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Producer"+String.valueOf(i)));
        }

        //消费者
        for (int i = 1; i <=3 ; i++) {
            pool.submit(new Thread(()-> {
                try {
                    resource.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },"Consumer"+String.valueOf(i)));
        }

        try {
            //10秒后关闭生产
            TimeUnit.SECONDS.sleep(10);
            resource.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //关闭线程池
        pool.shutdown();
    }
}

/**
 * @Description: 线程访问的资源类
 */
class Resource {
    //volatile修饰的标示位变量：表示是否开启生产模式
    private volatile boolean FLAG = true;
    //原子操作整型数据封装类实例，默认值：0
    private AtomicInteger atomicInteger = new AtomicInteger();

    //数据存储阻塞队列，使用BlockingQueue接口定义，方便外部线程调用是传入具体实例
    private BlockingQueue<String> blockingQueue = null;

    //构造函数，传入具体的阻塞队列实例
    public Resource(BlockingQueue<String> queue){
        this.blockingQueue = queue;
        //打印日志：通过反射获取传入的队列具体是BlockingQueue接口的那个实现类
        System.out.println(Thread.currentThread().getName()+",queue type:"+queue.getClass().getTypeName());
    }

    /**
     * @description: 生产者：生产数据
     * @author: Leo Wang
     * @date: 2019/9/5 21:33
     */
    public void produce() throws InterruptedException {

        //1.注意：由于业务处理在while循环中，切记变量一定要放在循环外
        String msg = null;
        boolean success = false;
        //2.多线程并发的情况下，一定得使用while循环，不能使用if判断，防止假唤醒现象
        while(this.FLAG){
            msg = Thread.currentThread().getName()+" 生产消息【"+this.atomicInteger.getAndIncrement()+"】";
            //3.offer()方法：阻塞线程，向队列中添加元素，如果不成功2秒后解除阻塞
            success = this.blockingQueue.offer(msg,2L, TimeUnit.SECONDS);
            if(success){
                System.out.println(msg);
            }
        }
    }

    /**
     * @description: 消费者：消费数据
     * @author: Leo Wang
     * @date: 2019/9/5 21:34
     */
    public void consume() throws InterruptedException {

        String msg = null;
        //1.使用while循环
        while(this.FLAG){
            //2.使用阻塞线程的poll()方法，如果没有获取成功，2秒后解除阻塞
            msg = this.blockingQueue.poll(2L,TimeUnit.SECONDS);
            //3.判断msg，如果为null说明消息队列为空，需要生产数据。跳出循环
            if(null==msg || msg.equalsIgnoreCase("")){
                this.FLAG=false;
                return;
            }
            System.out.println(Thread.currentThread().getName()+" 消费消息【"+msg+"】");
        }
    }

    /**
     * @description: 停止生产，将FLAG设为false
     * @author: Leo Wang
     * @date: 2019/9/5 21:35
     */
    public void stop(){
        this.FLAG=false;
    }
}
