package ProducerAndConsumer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 使用Lock实现生产者-消费者模式
 * @Author: Leo Wang
 * @Email: adwyxx@qq.com
 * @Date: $2019-09-04 20:59
 **/
public class DemoForLock {
    public static void main(String[] args) {
        final Worker worker = new Worker(3);
        //1.生产者线程
        for (int i = 1; i <= 2; i++) {
            new Thread(()->{
                for (int j = 1; j <= 10; j++) {
                    worker.put(String.valueOf(j));
                    System.out.println(Thread.currentThread().getName()+"\t生产消息："+j);
                }
            },"Producer"+String.valueOf(i)).start();
        }

        //2.消费者线程
        for (int i = 1; i <= 2; i++) {
            new Thread(()->{
                for (int j = 1; j <= 10; j++) {
                    System.out.println(Thread.currentThread().getName()+"\t消费消息："+worker.take());
                }
            },"Consumer"+String.valueOf(i)).start();
        }
    }

}

/**
 * @Description: 使用Lock实现生产者-消费者模式
 * Lock lock = new ReentrantLock();
 * Condition condition = lock.newCondition();
 * 步骤：
 *  1. 加锁：lock.lock();
 *  2. 循环判断,防止假唤醒：判断是否需要阻塞线程
 *     满足条件，则阻塞线程：condition.await();
 *  3. 业务操作
 *  4. 唤醒：condition.signalAll();
 *  5. 解锁：lock.unlock();
 */
class Worker {
    private int queueSize=0;
    private Queue<String> queue = null;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Worker(int queueSize){
        this.queueSize = queueSize;
        this.queue = new ConcurrentLinkedQueue<>();
    }

    /**
     * @description: 生产者调用，生产信息
     * @param msg: 信息
     */
    public void put(String msg){
        //1.加锁
        lock.lock();
        try {
            //2.循环判断，防止假唤醒
            while(this.queue.size()==this.queueSize)
            {
                //3.阻塞线程
                condition.await();
            }
            //4.生产消息
            this.queue.offer(msg);
            //5.唤醒消费者线程
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * @description: 消费者调用，获取信息
     * @return: 信息
     */
    public String take(){
        //1.加锁
        lock.lock();
        try {
            //2.循环判断
            while(this.queue.size()==0)
            {
                //3.阻塞线程
                condition.await();
            }
            //4.消费数据
            String msg = this.queue.poll();
            //5.唤醒生产者线程
            condition.signalAll();
            return msg;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }
}
