package JVM_GC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 并发标记清出GC (CMS) Concurrent Mark Sweep GC
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-20 14:13
 * 1.并发标记清除GC : Concurrent Mark Sweep GC:
 *   CMS GC是一种以获得最短回收挺多时间为目标的GC。最大的特点是允许GC线程和用户线程一起执行（用户线程不用再STW）
 *   适用于在互联网网站或者B/S系统的服务器上。此类应用尤其重视服务器响应速度，希望系统停顿时间最短。
 *   CMS非常适合堆内存大、CPU核数多的服务器端应用，也是G1出现之前大型应用的首选GC
 *
 * 2.JVM参数设置：
 *   -XX:+UseConcMarkSweepGC
 *    开启CMS后JVM会自动开启-XX+UseParNewGC。即：ParNewGC（新生代）+ CMS GC（老年代） 组合
 *    注意：SerialOldGC 将作为CMS GC的后备，一旦CMC GC出现问题老年代将会切换到SerialOldGC
 * 3.模拟JVM参数
 *  -Xms100m -Xmx100m -Xmn50m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails
 *  -Xmn50m(设置新生代大小)
 *  -XX:MaxTenuringThreshold=1  用于控制对象能经历多少次Minor GC(young gc)才晋升到老年代,默认15次
 *  -XX:+PrintTenuringDistribution 输出survivor区幸存对象的年龄分布
 *  -XX:+PrintCommandLineFlags  输出JVM参数信息
 */
public class ConcMarkSweepGCDemo {
    private static final int _10MB = 10 * 1024 * 1024;
    //-Xms100m -Xmx100m -Xmn50m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintCommandLineFlags -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
    public static void main(String[] args) throws InterruptedException {
        List<byte[]> list = new ArrayList<>();
        for (int n = 1; n < 8; n++) {
            byte[] alloc = new byte[_10MB];
            list.add(alloc);
        }
        TimeUnit.SECONDS.sleep(10);
    }
}
/**
 * //================CMS BEGIN================
 * //-----------第1步. Initial Mark-----------
 * [GC (CMS Initial Mark) [1 CMS-initial-mark: 40960K(51200K)] 73864K(97280K), 0.0012086 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * //第2步. Concurrent Mark / PreClean
 *  [CMS-concurrent-mark-start]
 * [CMS-concurrent-mark: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [CMS-concurrent-preclean-start]
 * [CMS-concurrent-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [CMS-concurrent-abortable-preclean-start]
 * [CMS-concurrent-abortable-preclean: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * //-----------第3步. Remark-----------
 * [GC (CMS Final Remark) [YG occupancy: 32904 K (46080 K)][Rescan (parallel) , 0.0014549 secs][weak refs processing, 0.0000121 secs][class unloading, 0.0003445 secs][scrub symbol table, 0.0007175 secs][scrub string table, 0.0002607 secs][1 CMS-remark: 40960K(51200K)] 73864K(97280K), 0.0029081 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * //-----------第4步. Concurrent Sweep-----------
 * [CMS-concurrent-sweep-start]
 * [CMS-concurrent-sweep: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * //-----------第5步. Reset-----------
 * [CMS-concurrent-reset-start]
 * [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * //================CMS END================
 * Heap
 *  par new generation   total 46080K, used 33723K [0x00000000f9c00000, 0x00000000fce00000, 0x00000000fce00000)
 *   eden space 40960K,  82% used [0x00000000f9c00000, 0x00000000fbceee40, 0x00000000fc400000)
 *   from space 5120K,   0% used [0x00000000fc900000, 0x00000000fc900000, 0x00000000fce00000)
 *   to   space 5120K,   0% used [0x00000000fc400000, 0x00000000fc400000, 0x00000000fc900000)
 *  concurrent mark-sweep generation total 51200K, used 40960K [0x00000000fce00000, 0x0000000100000000, 0x0000000100000000)
 *  Metaspace       used 3474K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 381K, capacity 388K, committed 512K, reserved 1048576K
 */