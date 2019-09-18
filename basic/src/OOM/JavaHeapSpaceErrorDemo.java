package OOM;

import java.util.Random;

/**
 * @Description:  Java Heap Space JVM堆内存溢出错误
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 09:43
 * 1. 提示信息：java.lang.OutOfMemoryError: Java heap space
 * 2. 产生原因：JVM对空间内存不足导致，即使GC进行回收也无法满足程序运行需要
 * 3. 配合JVM参数使用：
 *    -Xms  JVM最小分配的堆内存大小，默认为物理内存1/64
 *    -Xmx  JVM最大分配的堆内存大小，默认为物理内存1/4
 */
public class JavaHeapSpaceErrorDemo {
    //模拟堆空间不足：
    //不停地 new 对象分配堆空间
    //JVM运行参数: -Xms5m -Xmx5m -XX:+PrintGCDetails
    public static void main(String[] args) {
        String str="JVM Heap Space Error:";

        //注意for循环证件没有结束条件，所以一直运行下去
        for (int i = 0; ; i++) {
            //不停的创建string对象，分配新的堆空间
            str += str + new Random().nextInt(111111)+new Random().nextInt(222222);
            str.intern();//将字符串添加到常量池中（stringTable维护），并返回指向该常量的引用。
        }
    }
}
