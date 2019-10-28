package JVM_GC;

import java.util.Random;

/**
 * @Description: ParNewGC : 新生代并行GC
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-20 11:22
 * 1.新生代并行GC
 *   使用多个线程并行的进行垃圾回收。在GC的过程中会暂停其他所有的工作线程（Stop-The-World），直到GC完毕。
 *   PraNew GC是新生代的Serial GC的多线程版。配合老年代的CMS GC（ConcMarkSweepGC）一起工作
 * 他是很多JVM运行在Server模式下的默认垃圾回收器
 * 2.JVM参数设置：
 *   -XX:+UseParNewGC
 *   启用ParNewGC只会影响新生代，不影响老年代（老年代继续使用SerialOldGC）
 *   即：ParNewGC（Yong区用）+ SerialOldGC（Tenured区用）
 *   新生代采用复制算法，老年代采用标记-整理算法
 *   Java8已经不再推荐使用ParNewGC
 *   -XX:ParallelGCThreads=N 限制并行GC线程数量，默认开启和CPU数目相同
 * 3.模拟JVM参数
 *   -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseParNewGC
 *   -XX:+PrintCommandLineFlags  输出JVM参数信息
 */
public class ParNewGCDemo {
    public static void main(String[] args) {
        String str=null;
        //模拟：在堆中不停的创建新的string对象
        while (true){
            str += str + new Random().nextInt(123456);
            str.intern();
        }
    }
}
/**
 * [GC (Allocation Failure) [ParNew: 1270K->14K(3072K), 0.0007916 secs] 5500K->6576K(9920K), 0.0008128 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [ParNew: 2358K->2358K(3072K), 0.0000102 secs][Tenured: 6562K->3690K(6848K), 0.0024674 secs] 8921K->3690K(9920K), [Metaspace: 3435K->3435K(1056768K)], 0.0025119 secs] [Times: user=0.02 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [ParNew: 2419K->38K(3072K), 0.0004745 secs] 6109K->6060K(9920K), 0.0005019 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [ParNew: 2370K->2370K(3072K), 0.0000122 secs][Tenured: 6022K->4298K(6848K), 0.0025917 secs] 8392K->4298K(9920K), [Metaspace: 3455K->3455K(1056768K)], 0.0026387 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [Tenured: 4298K->4132K(6848K), 0.0023364 secs] 4298K->4132K(9920K), [Metaspace: 3455K->3455K(1056768K)], 0.0023564 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 *  par new generation   total 3072K, used 133K [0x00000000ff600000, 0x00000000ff950000, 0x00000000ff950000)
 *   eden space 2752K,   4% used [0x00000000ff600000, 0x00000000ff6214c8, 0x00000000ff8b0000)
 *   from space 320K,   0% used [0x00000000ff900000, 0x00000000ff900000, 0x00000000ff950000)
 *   to   space 320K,   0% used [0x00000000ff8b0000, 0x00000000ff8b0000, 0x00000000ff900000)
 *  tenured generation   total 6848K, used 4132K [0x00000000ff950000, 0x0000000100000000, 0x0000000100000000)
 *    the space 6848K,  60% used [0x00000000ff950000, 0x00000000ffd590f0, 0x00000000ffd59200, 0x0000000100000000)
 *  Metaspace       used 3487K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 379K, capacity 388K, committed 512K, reserved 1048576K
 *
 */