package CyclicBarrier;

import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Description: CyclicBarrier Demo
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-09-03 08:56
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        fireRocket();
        callTheDragon();
    }

    /**
    * @description: 集齐7颗龙珠召唤神龙
    * @author: Leo.Wang
    * @date: 2019-09-03 08:56
    */
    public static void callTheDragon(){
        final HashMap<Integer,String> actions = new HashMap<Integer, String>();
        actions.put(1,"贝吉塔收集到1星龙珠，交给悟空...");
        actions.put(2,"龟仙人收集到2星龙珠，交给悟空...");
        actions.put(3,"布尔玛收集到3星龙珠，交给悟空...");
        actions.put(4,"特兰克斯收集到4星龙珠，交给悟空...");
        actions.put(5,"撒旦先生收集到5星龙珠，交给悟空...");
        actions.put(6,"孙悟饭收集到6星龙珠，交给悟空...");
        actions.put(7,"沙鲁收集到7星龙珠，交给悟空...");

        CyclicBarrier barrier = new CyclicBarrier(7,()->{
            System.out.println("**收集齐七颗龙珠，悟空召唤神龙!!!!**");
        });

        for(int i=1;i<=7;i++){
            final String action = actions.get(i);
            new Thread(()->{
                System.out.println(action);
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }

    /**
    * @description: 发射火箭
    * @author: Leo.Wang
    * @date: 2019-09-03
    */
    public static void fireRocket(){
        int steps = Preparations.getSize();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(steps,()->{
            System.out.println("所有准备工作就绪，发射火箭！");
        });
        for(int i=1;i<=steps;i++){
            //lambda表达式中必须使用final变量
            final Preparations prepar = Preparations.getPreparation(i);
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t"+prepar.getStepName());
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
//Preparations for launching the rocket
enum Preparations{

    ONE(1,"发射系统准备"),
    TOW(2,"风速风向测定"),
    THREE(3,"发射空域申请"),
    FOUR(4,"发动机检查"),
    FIVE(5,"点火系统检查"),
    SIX(6,"发射系统线路检测");

    @Getter
    private Integer step;
    @Getter
    private String stepName;

    private Preparations(Integer step,String name){
        this.step=step;
        this.stepName=name;
    }
    //获取枚举的个数
    public static int getSize(){
        return Preparations.values().length;
    }
    //根据值获取一个枚举
    public static Preparations getPreparation(int step){
        Preparations[] items = Preparations.values();
        for(Preparations item : items){
            if(item.getStep()==step)
            {
                return item;
            }
        }
        return null;
    }
}