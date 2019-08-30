package ListAndMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Description: ArrayList 非线程安全的演示
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-30 09:15
 */
public class ListUnSafe {
    public static void main(String[] args) {
        //线程不安全将抛出异常：java.util.ConcurrentModificationException
        //listUnSafe();
        //setUnSafe();
        mapUnSafe();
    }

    public static void listUnSafe(){
        //解决ArrayList线程安全问题：
        //List<String> list = new ArrayList(); //不是线程安全的
        //1.使用Vector，Vector.add方法是用synchronized修饰的，可以保证线程安全。
        // 但是Vector是Java1.1时的产物，较老不建议使用
        //List<String> list = new Vector<>();

        //2.使用Collections.synchronizedList()方法初始化一个List
        // Collections.synchronizedList()初始化的对象是SynchronizedList或SynchronizedRandomAccessList
        // 这两者的add()方法内采用同步代码块synchronized(mutex){...}的方式保证线程安全
        //List<String> list = Collections.synchronizedList(new ArrayList<>());

        //3.使用JUC包的CopyOnWriteArrayList
        // 其原理是在add()方法写入时使用可重入锁ReentrantLock加锁代码块：创建一个新列表newList,扩容后添加新元素，然后再将list的指针指向newList
        List<String> list = new CopyOnWriteArrayList<>(); //推荐使用
        for(int i=1;i<=10;i++){
            new Thread(()->{
                for (int j = 0; j <30 ; j++) {
                    list.add(UUID.randomUUID().toString().substring(0,8));
                }
                System.out.println(list);
            },String.valueOf(i)).start();
        }

    }

    public static void setUnSafe(){
        Set<String> set = new CopyOnWriteArraySet<>(); //推荐使用
        for(int i=1;i<=10;i++){
            new Thread(()->{
                for (int j = 0; j <30 ; j++) {
                    set.add(UUID.randomUUID().toString().substring(0,8));
                }
                System.out.println(set);
            },String.valueOf(i)).start();
        }
    }

    public static void mapUnSafe(){
        Map<String,String> map = new ConcurrentHashMap<>(); //推荐使用
        for(int i=1;i<=10;i++){
            new Thread(()->{
                for (int j = 0; j <30 ; j++) {
                    map.put(Thread.currentThread().getName() ,UUID.randomUUID().toString().substring(0,8));
                }
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}
