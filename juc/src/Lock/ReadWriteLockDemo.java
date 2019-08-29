package Lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    public static void main(String[] args) {
        ReaderAndWriter rw = new ReaderAndWriter();
        //一个线程负责写入
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    rw.write(i);
                }
            }
        },"Write:").start();
        //10个线程负责读取
        for (int i = 1; i <= 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rw.read();
                }
            },"Read"+i+":").start();
        }
    }
}

class ReaderAndWriter {

    //读写锁
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock ();
    //数据队列,使用volatile修饰，使队列在多线程间可见
    private volatile Queue<Integer> queue = new LinkedList<>();
    //写操作
    public void write(Integer value)
    {
        //写加锁
        lock.writeLock().lock();
        try {
            this.queue.add(value);
            System.out.println(Thread.currentThread().getName()+value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //写解锁
            lock.writeLock().unlock();
        }
    }
    //读操作
    public void read()
    {
        //读加锁
        lock.readLock().lock();
        try {
            if(this.queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName()+"队列已空");
            }
            else {
                System.out.println(Thread.currentThread().getName()+this.queue.poll());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //读解锁
            lock.readLock().unlock();
        }
    }
}
