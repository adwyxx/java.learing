import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Synchronize demo
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-28 16:56
 */
public class SynchronizeDemo {
    public static void main(String[] args) {
        SynchronizeTest demo = new SynchronizeTest();

        //1.静态方法加synchronized，所有线程同一个锁，不同线程交替执行synMethod
        for(int i=1;i<=10;i++){
            new Thread(()->{SynchronizeTest.staticSynMethod();},"StaticSync"+String.valueOf(i)).start();
        }

        //2.普通方法加synchronized,同一个对象同一个锁，不同线程交替执行synMethod
        for(int i=1;i<=10;i++){
            new Thread(()->{demo.synMethod();},"Sync"+String.valueOf(i)).start();
        }

        //3.不同的对象,不同的锁，不同的线程几乎同时执行了synMethod
        for(int i=1;i<=10;i++){
            new Thread(()->{new SynchronizeTest().synMethod();},"Sync"+String.valueOf(i)).start();
        }

        //4.synchronized(this)，同一个对象同一个锁
        for(int i=1;i<=10;i++){
            new Thread(()->{demo.syncThisMethod();},"Sync"+String.valueOf(i)).start();
        }

        //5.synchronize(this) 不同的对象不同的锁
        for(int i=1;i<=10;i++){
            new Thread(()->{new SynchronizeTest().syncThisMethod();},"Sync"+String.valueOf(i)).start();
        }

        //6.synchronize(SynchronizeTest.class)相当于静态方法加synchronized修饰，所有对象的实例竞争同一个锁
        for(int i=1;i<=10;i++){
            new Thread(()->{new SynchronizeTest().syncClassMethod();},"Sync"+String.valueOf(i)).start();
        }
        //7.synchronize(object) 相当于synchronized修饰的非静态方法或synchronized(this) 相同的对象同一个锁，不同的对象不同的锁
        for(int i=1;i<=10;i++){
            new Thread(()->{new SynchronizeTest().syncObjectMethod();},"Sync"+String.valueOf(i)).start();
        }
        //8.synchronize(static_object) 相当于static synchronized方法或synchronized(*.class)
        for(int i=1;i<=10;i++){
           new Thread(()->{new SynchronizeTest().syncStaticObjectMethod();},"Sync"+String.valueOf(i)).start();
        }
    }
}

class SynchronizeTest{
    final Object lock = new Object();
    static final Object staticLock = new Object();
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    //修饰静态方法
    public static synchronized void staticSynMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " +
                Thread.currentThread().getName()+
                ",Static Synchronized Method!");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //修饰普通方法
    public synchronized void synMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",Synchronized Method!");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //修饰代码块 synchronized(this)
    public void syncThisMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",进入方法syncThisMethod，等待获取锁...");
        synchronized (this) {
            System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",synchronized(this)");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //修饰代码块synchronized(*.class)
    public void syncClassMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",syncClassMethod，等待获取锁...");
        synchronized (SynchronizeTest.class) {
            System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",synchronized(SynchronizeTest.class)");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncObjectMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",syncObjectMethod，等待获取锁...");
        synchronized (lock) {
            System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",synchronized(lock)");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //synchronized (staticLock) 相当于static synchronized方法或synchronized(SynchronizeTest.class)
    public void syncStaticObjectMethod(){
        System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",syncStaticObjectMethod，等待获取锁...");
        synchronized (staticLock) {
            System.out.println(LocalDateTime.now().format(formatter) + " : " + Thread.currentThread().getName()+",synchronized(staticLock)");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
