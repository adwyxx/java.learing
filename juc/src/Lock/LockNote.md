## Lock
### 一、JUC 实现同步的方式
#### 1.synchronized ：隐式锁
- 同步代码块 ：使用synchronized {...} 标注代码块
- 同步方法 : 在方法声明是加上synchronized关键字
#### 2.同步锁 Lock ：显式锁
> 需要通过 lock() 方法上锁，必须通过 unlock() 方法进行释放锁
示例
#### Lock和synchronized的不同:
#### 1.原始构成：
 * synchronized 是关键字，属于JVM层面。主要是通过monitor来实现同步
    - 底层通过monitor对象来完成，wait()/notify()方法都依赖于monitor对象。所以wait()/notify()，必须和synchronized同时使用
    - javap查看字节码可知底层monitor操作：
       - monitorenter
       - monitorexit
 * Lock 是Java的Api（java.util.concurrent.locks.lock）
#### 2.使用：
 * synchronized 不需要用户手动释放锁，当synchronized代码块执行完毕后，系统会自动让线程释放锁
 * ReentrantLock 必须需要手工加锁和释放锁,不释放锁会出现死锁现象。且加锁和释放锁必须成对出现（即：加几把锁就要相应的解几把锁）
 ```java
lock.lock();
try{...}
catch(){...}
finally{
      lock.unlock();
}
```
#### 3.可以中断：
 * synchronized 作用的代码块内，程序运行时不能被中断，除非抛出异常或运行完毕
 * ReentrantLock 加锁的代码块内，程序可以中断：
   - 3.1 使用trylock(long timeout,TimeUnit unit)方法设置超时时中断
   - 3.2 使用lockInterruptibly()加锁，在代码块中使用interrupt()中断运行
```
//lockInterruptibly会抛出异常，所以使用时必须放在try中
//或者其调用的方法抛出InterruptedException异常
try{
    lock.lockInteruptibly();
    ...
    //可以中断当前线程
    Thread.currentThread().interrupt();
}catch(InterruptedException e){
  ...
}finally{
    lock.unlock();
}
```
#### 4.加锁是否是公平锁
 * synchronized 默认是非公平的
 * ReentrantLock 默认无参数构造函数是非公平锁，可以通过带参数的构造函数ReentrantLock(boolean isFair)来控制是否公平
#### 5.锁绑定多条件，精准唤醒线程
 * synchronized 不支持
 * ReentrantLock 支持
   - 可以使用Condition condition = ReentrantLock.newCondition() 生成并绑定多个条件
   - 可以使用不同的condition阻塞(condition.await())-唤醒(condition.signal())不同的线程

### 二、Lock使用
#### java.util.concurrent.locks 包
**ReentrantLock是唯一实现Lock接口的类**
```
//初始化一个锁
Lock locker = new ReentrantLock(); //可重入锁
lock.lock(); //上锁
try {
    ...
}
finally {
    locker.unlock(); //解锁
    // 注意：一定得使用try-catch-finally，并在finally中解锁
}

```

示例代码：
```
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    //需求：假设有100张票，5个售票窗口售票，售完为止
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        for (int i = 1; i <= 5; i++) {
            new Thread(ticket,"窗口"+i).start();
        }
    }
}

class Ticket implements Runnable
{
    private int totalTickets=100;
    Lock loker = new ReentrantLock();
    @Override
    public void run() {
        while (true)
        {
            loker.lock();//加锁
            try {
                if(this.totalTickets>0)
                {
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

```

### 三、Lock接口其它方法
```
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
```
#### 1.tryLock()方法
##### tryLock()方法 有返回值(true/false)
 **它表示用来尝试获取锁，如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false**，也就说这个方法无论如何都会立即返回。在拿不到锁时不会一直在那等待。

##### tryLock(long time, TimeUnit unit)方法
和tryLock()方法是类似的，只不过区别在于这个方法在**拿不到锁时会等待一定的时间**，在时间期限之内如果还拿不到锁，就返回false。如果如果一开始拿到锁或者在等待期间内拿到了锁，则返回true。
```
Lock lock = ...;
//尝试获取锁
if(lock.tryLock()) {
     try{
         //处理任务
     }catch(Exception ex){
         
     }finally{
         lock.unlock();   //释放锁
     } 
}else {
    //如果不能获取锁，则直接做其他事情
}
```

#### 2.lockInterruptibly()方法
**当通过lockInterruptibly()方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态**。也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，而线程B只有在等待，那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程。

　　由于lockInterruptibly()的声明中抛出了异常，所以**lock.lockInterruptibly()必须放在try块中或者在调用lockInterruptibly()的方法外声明抛出InterruptedException**。
```
public void method() throws InterruptedException {
    //lockInterruptibly会抛出异常，所以使用时必须放在try中
    //或者其调用的方法抛出InterruptedException异常
    lock.lockInterruptibly();
    try {  
     //.....
    }
    finally {
        lock.unlock();
    }  
}
```
#### 注意
当一个线程获取了锁之后，是不会被interrupt()方法中断的。因为本身在前面的文章中讲过单独调用interrupt()方法不能中断正在运行过程中的线程，只能中断阻塞过程中的线程。

　　因此当通过lockInterruptibly()方法获取某个锁时，如果不能获取到，只有进行等待的情况下，是可以响应中断的。

　　而用synchronized修饰的话，当一个线程处于等待某个锁的状态，是无法被中断的，只有一直等待下去。
