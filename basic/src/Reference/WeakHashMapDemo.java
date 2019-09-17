package Reference;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * @Description: 软弱引用队列demo
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-17 11:03
 * WeakHashMap:
 * 1. WeakHashMap中保存的Entry对象的Key是弱引用对象：class Entry<K,V> extends WeakReference<K> implements Map.Entry<K,V>
 * 2. 因此WeakHashMap里的entry可能会被GC自动回收，即使程序员没有调用remove()或者clear()方法。
 *    WeakHashMap利用了WeakReference的机制来实现不阻止GC回收Key：
 *    将一对key, value放入到 WeakHashMap 里并不能避免该key值被GC回收，除非在WeakHashMap之外还有对该key的强引用。
 * 3. 更直观的说，当使用 WeakHashMap 时，即使没有显示的添加或删除任何元素，也可能发生如下情况：
 *    - 调用两次size()方法返回不同的值；
 *    - 两次调用isEmpty()方法，第一次返回false，第二次返回true；
 *    - 两次调用containsKey()方法，第一次返回true，第二次返回false，尽管两次使用的是同一个key；
 *    - 两次调用get()方法，第一次返回一个value，第二次返回null，尽管两次使用的是同一个对象。
 */
public class WeakHashMapDemo {
    public static void main(String[] args) throws InterruptedException {
        Integer key1,key2,key3;
        key1= new Integer(1);
        key2= new Integer(2);
        key3= new Integer(3);

        WeakHashMap<Integer,String> map = new WeakHashMap<>();
        map.put(key1,"Value1");
        map.put(key2,"Value2");
        map.put(key3,"Value3");

        //1.初始化后打印，map中所有key此时都有一个外部引用，所以不会回收
        printMap(map);

        //2.由于WeakHashMap的所有键值都有一个强引用与之关联，所以weakHashMap的所有键值对都不会被回收
        System.gc();
        Thread.sleep(2000);
        printMap(map);

        //3.将其中一个键值对应的强引用置为null，让Java虚拟机来进行自动回收
        key1=null; //help GC
        printMap(map); //注意：虽然key1=null,但是map中对应的key是弱引用类型它和key1是两个对象，并不会被更新成null

        //4.经过一次垃圾回收之后，key1对应的key为弱引用将被回收，同时map自动移除对应的entity
        System.gc(); //此时，map中的key1为弱引用，将被回收
        Thread.sleep(2000);
        printMap(map);
    }

    private static void printMap(WeakHashMap<Integer, String> map) {
        System.out.println("map size : "+map.size());
        map.forEach((key,value)->{
            System.out.println("key = "+key+",value = "+value);
        });
        System.out.println("---------------------");
    }
}
