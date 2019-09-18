package OOM;

import java.util.Map;
import java.util.Random;

/**
 * @Description: GC回收时间过长错误
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 10:30
 * 1. 提示信息：java.lang.OutOfMemoryError: GC overhead limit exceeded
 * 2. 产生原因：程序在垃圾回收上花费了98%的时间，却收集不回2%的空间，通常这样的异常伴随着CPU的冲高
 *            GC回收内存后很快又被填满，导致频繁的GC操作，CPU很快飙升。而这样恶性循环操作回收的内存并不多（<2%）进而导致JVM罢工
 * 3. JVM参数设置：
 *       -Xms5m 最小堆内存大小5MB
 *       -Xmx5m 最大堆内存大小5MB
 *       -XX:+UseParallelGC 启用并行垃圾回收器
 *       -XX:MaxDirectMemorySize=5m JVM占用最大的直接物理内存大小，一般为NIO使用。
 *                                  可以使用sun.misc.VM.maxDirectMemory()查看运行时的最大直接内存大小kb
 * 4. 解决方法：
 *    - 4.1 配置JVM参数（不推荐）：-XX:-UseGCOverheadLimit  禁用GCOverheadLimit 。不推荐使用的原因是治标不治本
 *    - 4.2 分析优化程序，看那些代码频繁的分配内存空间，进而进行优化
 */
public class GCOverheadLimitError {
    public static void main(String[] args) {
        //JVM运行时试图使用的最大内存量(以byte为单位),约等于机器内存的1/4
        System.out.println("maxMemory = "+Runtime.getRuntime().maxMemory()/(1024*1024)+"MB");
        //JVM外使用的直接内存(以byte为单位)
        System.out.println("maxDirectMemory = "+sun.misc.VM.maxDirectMemory()/(1024*1024)+"MB");

        //模拟：创建一个Map，不停的调用map.put加元素，使JVM进行GC
        //配合JVM参数：-Xms20m -Xmx20m -XX:MaxDirectMemorySize=20m -XX:+UseParallelGC
        Map map = System.getProperties();
        Random r = new Random();
        while (true) {
            try{
                map.put(r.nextInt(), "value");
            }catch(Exception e){
                e.printStackTrace();//java.lang.OutOfMemoryError: GC overhead limit exceeded
            }
        }
    }
}
