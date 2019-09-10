package Lock;

/**
 * @Description: 死锁Demo
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-10 11:14
 * 产生死锁的原因:
 *
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        LockHolder holder = new LockHolder("lockA","lockB");

        new Thread(()->{
            holder.getLockA();
        },"AAA").start();

        new Thread(()->{
            holder.getLockB();
        },"BBB").start();
    }
}

class LockHolder {
    private String lockA;
    private String lockB;

    public LockHolder(String lockA, String lockB) {
        this.lockA = lockA;
        this.lockB = lockB;
    }

    /**
    * @description: 获取了lockA后再尝试获取lockB
    * @author: Leo.Wang
    * @date: 2019/9/10 22:04
    */
    public void getLockA(){
        synchronized (lockA){
            System.out.println(Thread.currentThread().getName()+" 获得了"+lockA);
            synchronized (lockB){
                System.out.println(Thread.currentThread().getName()+" 获得了"+lockB);
            }
        }
    }

    /**
    * @description: 获取了lockB后再尝试获取lockA
    * @author: Leo.Wang
    * @date: 2019/9/10 22:05
    */
    public void getLockB(){
        synchronized (lockB){
            System.out.println(Thread.currentThread().getName()+" 获得了"+lockB);
            synchronized (lockA){
                System.out.println(Thread.currentThread().getName()+" 获得了"+lockA);
            }
        }
    }


}
