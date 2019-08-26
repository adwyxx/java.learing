import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Leo Wang
 * @Email: adwyxx@qq.com
 * @Date: $2019-08-26 21:04
 **/
public class VolatileTest {
    //volatile:阉割版的sychornized
    //1. 支持可见性
    //2. 不支持原子性   注意此点
    //3. 禁止指令冲排序
    public static void main(String[] args) {
        TestData data = new TestData();

        //一、可见性测试
        //线程A执行，改变data.num
        new Thread(()->{
            System.out.println("ThreadA running");
            //改变data.num，测试线程外部是否可见
            data.add10();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ThreadA stop:"+data.num);
        },"ThreadA").start();

        while (data.num==0)
        {
            //如果data.num==0则线程在此处自旋，不往下执行
        }
        //测试volatile关键字的可见性：如果不可见则在ThreaA执行完毕后，main线程的data.num==0是成立的，则不会执行到下面语句
        System.out.println("data.num="+data.num);


        //二、不支持原子性测试
        data.num=0;
        for (int i = 1; i <=10 ; i++) {
            new Thread(()->{
                for (int j = 1; j <=1000 ; j++) {
                    data.addPlus();
                }
            }).start();
        }

        while (Thread.activeCount()>2)
        {
            //如果当前活动线程数量>2，则自旋
            //当前运行的线程至少包括一个main线程和一个GUI线程，没有其他线程则表示上面的10个线程已经运行完
        }
        //如果每个线程都是原子性操作，那么结果应该是10*1000=10000
        System.out.println("data.num = "+data.num);
    }

}

class TestData {

    int num;

    public void add10(){
        this.num = 10;
    }
    public void addPlus(){
        this.num++;
    }
}
