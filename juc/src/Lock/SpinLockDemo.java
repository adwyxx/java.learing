package Lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 自定义自旋锁实现
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-02 11:14
 * 自旋锁：线程获取锁是如果没有获取到不会理解挂起，而是在自旋尝试再次获取锁，直到获取到锁为止
 * 思路：通过CAS的方法实现自旋锁
 *  1. 需要有一个lock()方法，通过自旋的方法尝试获取锁，如果获取不到就一直自旋
 *  2. 需要一个unlock()方法，用于解锁
 *  3. 尝试获取锁的对象时线程，这里使用AtomicReference<Thread> 对象来实现
 */
public class SpinLockDemo {
    public static void main(String[] args) {
        final SpinLock lock = new SpinLock();
        for(int i=1;i<=10;i++){
            new Thread(()->{
                try {
                    lock.lock();
                    System.out.println(Thread.currentThread()+"\t得到锁");
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            },String.valueOf(i)).start();
        }
    }
}

class SpinLock{
    //锁的持有者，注意是volatile修饰的
    private volatile AtomicReference<Thread> lockOwner;

    public SpinLock() {
        //初始化时，将AtomicReference初始值设置为null
        //null表示，当前没有任何线程得到锁
        this.lockOwner = new AtomicReference<>(null);
    }

    /**
     * @description: 获取锁方法
     * @author: Leo.Wang
     * @date: 2019/9/3 10:20
     * @comments 获取锁时将lockOwner设置为当前成功获取锁的线程
     */
    public void lock(){
        //循环CAS操作：当前线程尝试获取锁
        //期望值为null，表示没有线程占用锁
        // 如果没有线程占用锁，则当将null更新成当前线程，表示当前线程获得了锁
        // 如果主内存中lockOwner不为null，表示有线程正在占用锁，当前线程获取锁失败
        // 获取锁失败则自旋，当前线程一直在这里自旋等待。注意线程没有挂起
        while(!lockOwner.compareAndSet(null,Thread.currentThread()))
        {
            //System.out.println(Thread.currentThread().getName()+"尝试获取锁...");
        }
    }

    /**
     * @description: 解锁，将lockOwner更新成null，表示释放锁
     * @author: Leo.Wang
     * @date: 2019/9/3 10:34
     **/
    public void unlock(){
        while (!lockOwner.compareAndSet(Thread.currentThread(),null))
        {

        }
    }
}
