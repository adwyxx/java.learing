package ThreadPool;

import java.util.concurrent.*;

public class ForkJoinPoolDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.初始化一个线程吃
        ForkJoinPool pool = new ForkJoinPool();

        //2.分配任务
        //2.1 分配一个无返回值的任务
        RecursiveActionDemo action = new RecursiveActionDemo(1,1000);
        pool.submit(action);
        //2.2 分一个有返回值的任务，并使用ForkJoinTask<T>接收返回值
        RecusiveTaskDemo task = new RecusiveTaskDemo(1,1000);
        ForkJoinTask<Integer> res = pool.submit(task);
        System.out.println("RecusiveTask result:"+res.get());

        //3.关闭线程
        pool.shutdown();
    }
}

//计算1-1000的和并返回
class RecusiveTaskDemo extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 100; //每个小任务 最多只累加100个数
    private int start;
    private int end;

    public RecusiveTaskDemo(int start,int end)
    {
        this.start=start;
        this.end=end;
    }

    @Override
    protected Integer compute() {
        int sum =0;
        //当end与start之间的差小于threshold时，开始进行实际的累加
        if(end - start <THRESHOLD){
            for(int i= start;i<end;i++){
                sum += i;
            }
            System.out.println(Thread.currentThread().getName()+" RecusiveTask : "+sum);
        }else {
            //当end与start之间的差大于threshold，即要累加的数超过100个时候，将大任务分解成小任务
            int middle = (start+ end)/2;
            RecusiveTaskDemo left = new RecusiveTaskDemo(start, middle);
            RecusiveTaskDemo right = new RecusiveTaskDemo(middle, end);
            //并行执行两个小任务
            left.fork();
            right.fork();

            return left.join()+left.join();
        }
        return sum;
    }
}

//计算1-1000的和直接打印
class RecursiveActionDemo extends RecursiveAction {
    private static final int THRESHOLD = 100; //每个小任务 最多只累加100个数
    private int start;
    private int end;

    public RecursiveActionDemo(int start,int end)
    {
        super();
        this.start=start;
        this.end=end;
    }

    @Override
    protected void compute() {
        int sum =0;
        //当end与start之间的差小于threshold时，开始进行实际的累加
        if(end - start <THRESHOLD){
            for(int i= start;i<end;i++){
                sum += i;
            }
            System.out.println(Thread.currentThread().getName()+" RecursiveAction : "+sum);
        }else {
            //当end与start之间的差大于threshold，即要累加的数超过100个时候，将大任务分解成小任务
            int middle = (start+ end)/2;
            RecursiveActionDemo left = new RecursiveActionDemo(start, middle);
            RecursiveActionDemo right = new RecursiveActionDemo(middle, end);
            //并行执行两个小任务
            left.fork();
            right.fork();
        }
    }
}