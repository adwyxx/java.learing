package JVM_GC;

import java.util.Random;

/**
 * @Description: G1 GC : Garbage First GC
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-20 16:08
 *
 * -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+UseG1GC -XX:+PrintCommandLineFlags
 */
public class G1Demo {
    public static void main(String[] args) {
        String str=null;
        //模拟：在堆中不停的创建新的string对象
        while (true){
            str += str + new Random().nextInt(123456);
            str.intern();
        }
    }
}
