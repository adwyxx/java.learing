package ReferenceType;

import java.lang.ref.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 引用分类
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019/9/16 21:45
 * 强引用：常见的普通对象引用，就算出现OOM(OutOfMemeryError)也不会回收的对象。死了都不回收
 * 软引用：内存充足时不会回收，内存不足时才去回收的对象
 *        java.lang.ref.SoftReference<T>
 * 弱引用：只要有垃圾回收就会回收的对象
 *        java.lang.ref.WeakReference<T>
 * 虚引用：任何时候都可能被回收，形同虚设
 *        java.lang.ref.PhantomReference<T>
 */
public class ReferenceType {

    public static void main(String[] args) throws InterruptedException {
        //1.软引用demo
        softReference();
        //2.弱引用demo
        weakReference();
        //3.虚引用demo
        phantomReference();
    }

    /**
     * @description: 软引用demo
     * @author: Leo.Wang
     * @date: 2019/9/16 22:06
     * 1. 内存充足时不会回收
     * 2. 模拟内存不做，需要设置运行时JVM参数：-Xmx5m，即：JVM分配的最大堆内存为5M
     *  -XX:+PrintGCDetails 打印GC信息
     */
    private static void softReference(){
        Object o1 = new Object();
        //声明一个软引用对象，其引用的对象是o1的引用地址
        SoftReference<Object> softReference = new SoftReference<>(o1);

        System.out.println(o1);
        System.out.println(softReference.get());
        o1=null;
        System.out.println("==========SoftReference:内存充足，不回收==========");
        System.gc();
        System.out.println(o1);
        System.out.println(softReference.get());

        System.out.println("==========SoftReference:内存不足，自动回收==========");

        try{
            //声明个10M的字节数组，模拟内存不足。
            //需要设置JVM -Xmx参数：-Xmx5m
            byte[] bytes = new byte[10*1024*1024];
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }finally {
            System.out.println(softReference.get());
        }
    }
    /**
     * @description: 弱引用demo
     * @author: Leo.Wang
     * @date: 2019/9/16 22:06
     * 1. 只要发生GC就会被回收的对象
     */
    private static void weakReference(){
        Object o1 = new Object();
        //声明一个弱引用对象，其引用的对象是o1的引用地址
        WeakReference<Object> weakReference = new WeakReference<>(o1);

        System.out.println(o1);
        System.out.println(weakReference.get());
        o1=null;
        System.out.println("==========WeakReference:只要GC就会被回收==========");
        System.gc();
        System.out.println(o1);
        System.out.println(weakReference.get());
    }
    /**
     * @description: 虚引用demo
     * @author: Leo.Wang
     * @date: 2019/9/16 22:45
     *
     */
    private static void phantomReference() throws InterruptedException {
        Object o = new Object();
        //1.引用队列：在对象被回收之前需要先保存到引用队列中
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        //2.虚引用对象实例，构造函数需要传入一个引用队列
        PhantomReference<Object> phantomReference = new PhantomReference<>(o,referenceQueue);

        System.out.println(o);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll());

        System.out.println("==========PhantomReference:GC==========");

        o=null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll());

    }
}
