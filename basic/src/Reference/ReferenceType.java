package Reference;

import java.lang.ref.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 引用分类
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019/9/16 21:45
 * 强引用：常见的普通对象引用，就算出现OOM(OutOfMomeryError)也不会回收的对象。死了都不回收
 * 软引用：内存充足时不会回收，内存不足时才去回收的对象
 *        java.lang.ref.SoftReference<T>
 * 弱引用：只要有垃圾回收就会回收的对象
 *        java.lang.ref.WeakReference<T>
 * 虚引用：任何时候都可能被回收，形同虚设
 *        java.lang.ref.PhantomReference<T>
 */
public class ReferenceType {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("-------------SoftReference----------------");
        //1.软引用demo
        softReference();
        System.out.println("-------------WeakReference----------------");
        //2.弱引用demo
        weakReference();
        System.out.println("------------PhantomReference---------------");
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
     * 2. 配合ReferenceQueue<T>使用的话GC后，弱引用对象会添加到引用队列中
     * 3. 一般来说，很少直接使用WeakReference，而是使用WeakHashMap:
     *    在WeakHashMap中，内部有一个引用队列，插入的元素会被包裹成WeakReference，并加入队列中，用来做缓存再合适不过。
     */
    private static void weakReference(){
        Object o1 = new Object();
        //引用队列：对于弱引用，GC前引用队列为空，GC后会把虚引用
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        //声明一个弱引用对象，其引用的对象是o1的引用地址
        WeakReference<Object> weakReference = new WeakReference<>(o1,referenceQueue);

        System.out.println(o1);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll()); //GC前为空
        o1=null;
        System.out.println("==========WeakReference:只要GC就会被回收==========");
        System.gc();
        System.out.println(o1);
        System.out.println(weakReference.get());
        System.out.println(referenceQueue.poll()); //GC后将弱引用对象添加到引用队列中
    }

    /**
     * @description: 虚引用demo
     * @author: Leo.Wang
     * @date: 2019/9/16 22:45
     * 使用虚引用对象PhantomReference.get()方法获取永远返回null值
     * 虚引用顾名思义：形同虚设。如果一个对象持有虚引用，那么他就和没有任何引用一样，随时都会被垃圾回收
     * 虚引用不会影响对象的生命周期,可以用来做为对象是否存活的监控
     * 虚引用必须和引用队列ReferenceQueue<T>联合使用，GC后弱引用对象会添加到引用队列中
     * 虚引用的主要作用：跟踪对象被垃圾回收的状态，提供了一种对象被finalize（终结）后做某些事情的机制
     *   - 这个对象被垃圾回收的时候收到一个系统通知或后续添加一些处理操作
     *   - Java允许使用finalize()方法：在垃圾收集器将对象清出内存之前作一些必要的清理操作
     */
    private static void phantomReference() throws InterruptedException {
        Object o = new Object();
        //1.引用队列：在对象被回收之前需要先保存到引用队列中
        ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
        //2.虚引用对象实例，构造函数需要传入一个引用队列
        //  虚引用对象在垃圾回收后会存放到ReferenceQueue中
        PhantomReference<Object> phantomReference = new PhantomReference<>(o,referenceQueue);

        System.out.println(o);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll());  //3.垃圾回收前引用队列为空

        System.out.println("==========PhantomReference:GC==========");

        o=null;
        System.gc();
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println(phantomReference.get());
        System.out.println(referenceQueue.poll()); //4.垃圾回收后引用队列中加入了虚引用对象
    }
}
