package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class CallableDemo {
    public  static  void  main(String[] args)
    {
        CallableTask th1 = new CallableTask(10);
        CallableTask th2 = new CallableTask(100);
        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。
        FutureTask<Integer> result1 = new FutureTask<>(th1);
        FutureTask<Integer> result2 = new FutureTask<>(th2);
        //2.启动线程
        new Thread(result1,"线程A").start();
        new Thread(result2,"线程B").start();
        //3.线程执行结束后接受参数
        try
        {
            //接受参数
            Integer a = result1.get();
            Integer b = result2.get();
            //注意此处：等待所有调用了FutureTask 的get()方法的FutureTask示例线程都执行完毕后才继续执行下面语句
            System.out.println("结果合计：" + (a+b));
            //注意此处：根据执行结果我们可以发现，次数打印是在数据计算结果后打印的
            //由此可见：FutureTask开始执行后，等所有线程执行完毕后才调用get（）才会得到结果
            //这个效果相当于闭锁CountDownLatch
            System.out.println("线程A is done: "+result1.isDone());
            System.out.println("线程B is done: "+result1.isDone());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

//实现Callable<V>泛型接口
class CallableTask implements Callable<Integer>{

    private int sum;
    private int limit;
    public CallableTask(int limit)
    {
        this.limit=limit;
    }
    //重写Callable<V>接口方法，注意此处有异常抛出
    @Override
    public Integer call() throws Exception {
        //计算0~limit的求和
        for (int i = 0; i < limit; i++) {
            this.sum +=i;

            try {
                //休眠100毫秒
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName()+ ":"+this.sum);
        return this.sum;
    }
}