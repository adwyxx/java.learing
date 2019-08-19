import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomFixedThreadPool {
    public static void main(String[] args) {
        FixedThreadPool pool = new FixedThreadPool(5);
        //提交20个任务
        for (int i = 0; i < 20; i++) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    int sum = 0;
                    for (int j = 1; j <= 100 ; j++) {
                        sum += j;
                    }
                    System.out.println(Thread.currentThread().getName()+":"+sum);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //关闭线程池
        pool.shutdown();
    }
}

//自定义实现FixedThreadPool
class FixedThreadPool {
    //线程池最多线程数据
    private int maxPoolSize = 0;
    //任务队列，使用阻塞队列确保线程安全
    private BlockingQueue<Runnable> taskQueue;
    //工作线程队列
    private List<WorkerThread> workerList;
    //线程池状态：使用volatile关键字保证多线程间可见
    private volatile boolean isWorking = true;

    //构造函数，初始化线程池工作线程队列和任务队列，以及最大线程数
    public FixedThreadPool(int maxPoolSize)
    {
        if(maxPoolSize<=0)
        {
            throw new IllegalArgumentException("参数错误：线程池大小必须是大于0的整数");
        }
        this.taskQueue = new LinkedBlockingQueue<>();
        //使用Collections.synchronizedList，保证工作队列是线程安全的
        this.workerList = Collections.synchronizedList(new ArrayList<>());
        this.maxPoolSize = maxPoolSize;
        //创建工作线程，并运行线程
        for (int i = 0; i < maxPoolSize; i++) {
            WorkerThread wt = new WorkerThread(this);
            wt.start();
            this.workerList.add(wt);
        }
    }

    //添加任务方法
    public boolean submit(Runnable task)
    {
        if(!this.isWorking)
        {
            System.out.println("线程池已关闭，不允许提交任务");
            return  false;
        }
        return this.taskQueue.offer(task);
    }

    //关闭线程池
    public void shutdown()
    {
        this.isWorking = false;
        this.interruptBlockingWorkers();
    }

    //中断正在阻塞的工作线程，关闭线程池时使用
    private void interruptBlockingWorkers()
    {
        //循环线程池中线程，中断正在阻塞的线程
        for(WorkerThread worker : this.workerList)
        {
            if(worker.getState()== Thread.State.BLOCKED ||
                worker.getState()== Thread.State.TIMED_WAITING ||
                worker.getState()== Thread.State.WAITING)
            {
                worker.interrupt();
                System.out.println("中断线程：" + worker.getName());
            }
        }
    }

    //内部类：工作线程，从任务队列中获取任务并执行
    private final class WorkerThread extends Thread {
        //worker执行完毕的任务数量
        volatile long completedTasks;
        //工作现场所属的线程池，要从线程池的工作队列中获取任务
        private FixedThreadPool pool;

        public  WorkerThread (FixedThreadPool pool)
        {
            super();
            this.pool = pool;
        }

        @Override
        public void run() {
            //如果线程池正在工作，则获取任务并运行
            while(this.pool.isWorking || this.pool.taskQueue.size()>0)
            {
                //任务
                Runnable task = null;
                try {
                    if(this.pool.isWorking)
                    {
                        //注意：take()方法会阻塞线程
                        //如果程序运行到此处，线程会阻塞，同时外部调用了pool的shoutdown()方法，则线程会停留再此处无法继续执行
                        //此时需要正在shoutdown()方法中结束此类阻塞的线程
                        task = this.pool.taskQueue.take();
                    }
                    else
                    {
                        //注意：poll()方法不阻塞线程
                        task = this.pool.taskQueue.poll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(task != null)
                {
                    task.run();
                    this.completedTasks++;
                    System.out.println(this.getName()+"执行任务个数："+this.completedTasks);
                    task = null;
                }
            }
            System.out.println(this.getName()+"执行完毕");
        }
    }
}
