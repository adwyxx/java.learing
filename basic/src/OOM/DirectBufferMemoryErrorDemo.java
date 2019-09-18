package OOM;

import java.nio.ByteBuffer;

/**
 * @Description: Direct Buffer Memory 直接缓冲区内存溢出错误。
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 14:20
 * 1. 提示信息：java.lang.OutOfMemoryError: Direct buffer memory
 * 2. 产生原因：该错误基本上是NIO程序占满直接内存导致，即占满了分配给JVM堆外的物理内存（本地内存）
 *    2.1 在NIO程序中经常使用ByteBuffer来缓冲数据，他可以使用Native函数分配直接内存，然后通过存储在DirectByteBuffer对象中作为这块内存的引用
 *      这样来操作直接内存，避免了 [直接内存<-->JVM内存] 之间的数据复制，提高了性能
 *    2.2 JVM堆缓冲区：ByteBuffer.allocate(size) ，由JVM管理，可以被GC回收
 *    2.3 直接内存缓冲区：ByteBuffer.allocateDirect(size)，不被JVM管理，不能自动被GC回收。避免了缓冲区复制
 *    2.4 由于直接缓冲区在 JVM里被包装进Java对象DirectByteBuffer中，当它的包装类被垃圾回收时，会调用相应的JNI方法释放本地内存，
 *      所以本地内存的释放 也依赖于JVM中DirectByteBuffer对象的回收。
 *    2.5 当JVM堆内存充足时，JVM长时间不进行GC操作DirectByteBuffer不会被回收，从而本地内存不断分配，无法释放，最终导致OutOfMemoryError。
 *      +————————————————————+
 *      |    Direct Memory   |
 *      |  +——————————————+  |
 *      |  |  JVM Memory  |  |
 *      |  +——————————————+  |
 *      +————————————————————+
 * 3. JVM参数设置：-XX:MaxDirectMemorySize=5m 最大直接内存
 * 4. 解决方法：
 *    4.1 通过-XX:MaxDirectMemorySize= 调整直接内存的大小
 *    4.2 关闭禁止显示的GC设置：-XX:-DisableExplicitGC
 *    4.3 优化程序
 */
public class DirectBufferMemoryErrorDemo {
    public static void main(String[] args) {
        System.out.println("maxMemory:"+Runtime.getRuntime().maxMemory());
        System.out.println("MaxDirectMemorySize:"+sun.misc.VM.maxDirectMemory());
        //1.设置JVM参数：-XX:MaxDirectMemorySize=5m 最大直接内存为5MB
        //2.分配直接内存为6MB
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024*6);
        //java.lang.OutOfMemoryError: Direct buffer memory
    }
}
