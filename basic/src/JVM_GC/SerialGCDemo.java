package JVM_GC;

import java.util.HashMap;
import java.util.Random;

/**
 * @Description: 串行GC: Serial Garbage Collector
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-20 10:26
 * 1.SerialGC:
 * 一个单线程的收集器，在垃圾收集是必须暂停其他所有工作线程，直到GC完毕。
 * 串行收集器是最古老的GC收集器，也是最稳定及效率高的收集器。
 * 他只是用一个线程去收集垃圾，其他线程在垃圾回收过程中可能会产生较长时间的停顿（"Stop-The-World"状态）
 * 虽然在收集垃圾的过程中需要暂停其他所有工作线程，但是他简单高效，对于单个CPU环境来说没有线程交互的开销可以获得最好的单线程垃圾回收效率，因此SerialGC依然是JVM运行在Client模式下默认的新生代垃圾回收器
 *
 * 2.对应的JVM参数:
 *   2.1 -XX:+UseSerialGC
 *      开启后会使用：SerialGC (新生代用) + Serial Old GC (老年代用) 的组合模式
 *      表示：新生代、老年代都会使用串行垃圾回收器，新生代使用复制算法，老年代使用标记-整理算法
 *
 * 3.模拟JVM参数
 *  -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseSerialGC
 *  -XX:+PrintCommandLineFlags  输出JVM参数信息
 */
public class SerialGCDemo {
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
 * 截取GC日志如下：
 * ...
 * [GC (Allocation Failure) [DefNew: 2302K->0K(3072K), 0.0005157 secs][Tenured: 5415K->4264K(6848K), 0.0019669 secs] 5415K->4264K(9920K), [Metaspace: 3455K->3455K(1056768K)], 0.0025160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * [Full GC (Allocation Failure) [Tenured: 4264K->4087K(6848K), 0.0021789 secs] 4264K->4087K(9920K), [Metaspace: 3455K->3455K(1056768K)], 0.0021980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
 * Heap
 *  def new generation   total 3072K, used 133K [0x00000000ff600000, 0x00000000ff950000, 0x00000000ff950000)
 *   eden space 2752K,   4% used [0x00000000ff600000, 0x00000000ff621468, 0x00000000ff8b0000)
 *   from space 320K,   0% used [0x00000000ff8b0000, 0x00000000ff8b0000, 0x00000000ff900000)
 *   to   space 320K,   0% used [0x00000000ff900000, 0x00000000ff900000, 0x00000000ff950000)
 *  tenured generation   total 6848K, used 4087K [0x00000000ff950000, 0x0000000100000000, 0x0000000100000000)
 *    the space 6848K,  59% used [0x00000000ff950000, 0x00000000ffd4dfd0, 0x00000000ffd4e000, 0x0000000100000000)
 *  Metaspace       used 3487K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 379K, capacity 388K, committed 512K, reserved 1048576K
 * ...
 *
 * 分析：
 * 1.GC操作的堆内存控件为：DefNew(新生代),Tenured(老年代),Metaspace(方法区，也称元数据区)
 * 2.日志中 [GC ...] 和 [Full GC ...]:
 *   [GC]: 新生代的GC，也叫做Minor GC。触发条件：当Eden区满时，触发Minor GC。
 *   [Full GC]: 老年代的GC,在发生Minor GC时，虚拟机会检查每次晋升进入老年代的大小是否大于老年代的剩余空间大小，如果大于，则直接触发一次Full GC
 * 3.Heap 以下的打印出当前的JVM堆信息。
 *   def new generation  total xxx ,used xxx 新生代 总共分配内存大小 已使用内存大小
 *      eden    伊甸园区
 *      from    survivor区
 *      to      survivor区
 *   tenured generation 老年代信息
 *   metaspace  方法区
 *
 */
