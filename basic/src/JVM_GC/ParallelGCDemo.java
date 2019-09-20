package JVM_GC;

import java.util.Random;

/**
 * @Description: 并行GC: ParallelGC
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-20 14:03
 * 1.并行收集器：
 *   ParallelGC: 多个GC线程的同时并行进行GC操作，在垃圾收集是必须暂停其他所有工作线程，直到GC完毕
 *   Parallel Scavenge收集器类似ParNew，也是一个新生代垃圾收集器，使用复制算法。
 * 需要关注的是：
 * 可控制的吞吐量（Thoughput=运行用户代码时间/(运行用户代码时间+GC时间)  ，比如程序运行100秒，其中GC运行2秒，那么吞吐量=98%）
 * 高吞吐量意味着高效的利用CPU，它多用于后台运算而不需要太多交互的任务
 * 自适应调节策略也是ParallelScavengeGC与ParNewGC的一个区别。（自适应调节策略：JVM根据当前程序运行的情况收集性能监控信息，动态的调整这些参数，已提供最合适的停顿时间(-XX:MaxGCPauseMillis)或最大吞吐量）
 *
 * 2.JVM参数设置：
 *  -XX:+UseParallelGC 或 –XX:+UseParallelOldGC  两者可相互激活
 *   开启后会使用：ParallelGC (新生代用) + ParallelOldGC (老年代用) 的组合模式
 *   表示：新生代、老年代都会使用串行垃圾回收器，新生代使用复制算法，老年代使用标记-整理算法
 * -XX:ParallelGCThreads=N 表示启动N个GC线程
 * CPU>8 N=5/8
 * CPU<8 N=实际个数
 *3.模拟JVM参数
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseParallelGC -XX:+PrintCommandLineFlags
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseParallelOldGC -XX:+PrintCommandLineFlags
 * -XX:+PrintCommandLineFlags  输出JVM参数信息
 */
public class ParallelGCDemo
{
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
 * ParallelGC(新生代)对应的是PSYongGen
 * ParallelOldGC(老年代)对应的是ParOldGen
 *
 * [GC (Allocation Failure) [PSYoungGen: 1825K->256K(2560K)] 8419K->6857K(9728K), 0.0006738 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) --[PSYoungGen: 1449K->1449K(2560K)] 8051K->8051K(9728K), 0.0163801 secs] [Times: user=0.09 sys=0.00, real=0.02 secs]
 * [Full GC (Ergonomics) [PSYoungGen: 1449K->0K(2560K)] [ParOldGen: 6601K->2961K(7168K)] 8051K->2961K(9728K), [Metaspace: 3455K->3455K(1056768K)], 0.0058367 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]
 * [GC (Allocation Failure) [PSYoungGen: 41K->0K(2560K)] 5309K->5268K(9728K), 0.0003882 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [PSYoungGen: 0K->0K(1536K)] 5268K->5268K(8704K), 0.0002041 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(1536K)] [ParOldGen: 5268K->4114K(7168K)] 5268K->4114K(8704K), [Metaspace: 3455K->3455K(1056768K)], 0.0031813 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [GC (Allocation Failure) [PSYoungGen: 0K->0K(2048K)] 4114K->4114K(9216K), 0.0002563 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [PSYoungGen: 0K->0K(2048K)] [ParOldGen: 4114K->4094K(7168K)] 4114K->4094K(9216K), [Metaspace: 3455K->3455K(1056768K)], 0.0060692 secs] [Times: user=0.09 sys=0.00, real=0.01 secs]
 * Heap
 *  PSYoungGen      total 2048K, used 50K [0x00000000ffd00000, 0x0000000100000000, 0x0000000100000000)
 *   eden space 1024K, 4% used [0x00000000ffd00000,0x00000000ffd0ca60,0x00000000ffe00000)
 *   from space 1024K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x00000000fff00000)
 *   to   space 1024K, 0% used [0x00000000fff00000,0x00000000fff00000,0x0000000100000000)
 *  ParOldGen       total 7168K, used 4094K [0x00000000ff600000, 0x00000000ffd00000, 0x00000000ffd00000)
 *   object space 7168K, 57% used [0x00000000ff600000,0x00000000ff9ffa28,0x00000000ffd00000)
 *  Metaspace       used 3487K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 379K, capacity 388K, committed 512K, reserved 1048576K
 */
