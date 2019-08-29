package CAS;

/**
 * @Description: 线程安全的单例模式，使用volatile及双重检查锁机制
 * @Author: Leo.Wang
 * @Date: 2019-08-28 9:15
 */
public class VolatileSingletonDemo {
    //1.私有的静态实例变量
    private static volatile VolatileSingletonDemo instance=null;

    //2.私有的构造函数
    private VolatileSingletonDemo(){
        System.out.println(Thread.currentThread().getName()+":调用构造函数");
    }

    //3.公开的获取单例实例的静态方法
    // DCL模式：双重检查锁机制,即：在加锁前后分别进行检查
    // DCL不是线程安全的，原因是指令重拍下，如果要保证DCL线程安全则需要和volatile一起使用
    // volatile禁止指令重排序
    public static VolatileSingletonDemo getInstance(){
        //加锁前检查
        if(instance==null){
            //加锁
            synchronized (VolatileSingletonDemo.class){
                //加锁后检查
                if(instance==null){
                    instance = new VolatileSingletonDemo();
                }
            }
        }
        return  instance;
    }

    /**DCL不能保证线程安全:
     * instance = new CAS.VolatileSingletonDemo() 该语句其实分三步操作:
     * 1. memory=allocate();    分配对象内存空间
     * 2. instance(memory);     初始化对象
     * 3. instance=memory;      设置instance的指向刚分配的内存地址,此时instance!=null
     * 步骤2和3不存在依赖关系，因此会发送指令重排序。
     * 假如线程1已经获得锁并进入instance = new CAS.VolatileSingletonDemo()，而系统指令重排将步骤3在步骤2前执行
     * 此时instance分配了内存（不为Null）,但是并没有做完初始化（instance的属性方法都没有，无法使用）
     * 此时线程2进入getInstance()第一步加锁前检查，发现instance!=null,就会直接返回instance
     * 但是此时的发现instance并没被初始化，还是不可用的，所以线程2内部调用instance的方法或属性时会报错
    **/

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            new Thread(()->{
                VolatileSingletonDemo.getInstance();
                //调用getInstance时会触发单例实例初始化，打印Thread-0:调用构造函数
                //线程安全的情况先，无论多少个线程调用多少次getInstance()方法，应该只有一次实例初始化的过程
            }).start();
        }

    }
}
