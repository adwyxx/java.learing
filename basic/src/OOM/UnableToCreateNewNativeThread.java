package OOM;

import java.util.concurrent.TimeUnit;

/**
 * @Description: unable to create new native thread 不能再创建新的线程错误
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-18 15:10
 * 1. 提示信息：java.lang.OutOfMemoryError: unable to create new native thread
 * 2. 产生原因：程序运行时一个应用进程存活的线程已经达到了系统的上限，导致无法创建新的线程
 *    2.1 该错误与操作系统平台相关，例如Linux 允许单个进程最多可以开启1024个线程
 *    2.2 实际运行时单个进程的线程数一般不会达到1024就会报错（因为JVM运行还会占用一些线程，通常到800-900就会报错）
 * 3. 解决方法：
 *    4.1 降低应用创建线程的个数，优化程序
 *    4.2 如果确实需要创建如此多的线程，那么调整Linux系统单个进程最多可以开启1024个线程的设置，增大这个数
 */
public class UnableToCreateNewNativeThread {
    public static void main(String[] args) {
        //i没有截止判断，不停的循环执行
        //在Linux环境行运行该程序
       for(int i=1; ;i++){
           System.out.println("======>"+i); //windows下运行到59190个线程
           new Thread(()->{
                try {
                    //休眠足够长时间，保证线程不死
                    TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           },String.valueOf(i)).start();
       }
    }
}
