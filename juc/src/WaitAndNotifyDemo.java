import java.time.LocalDateTime;

public class WaitAndNotifyDemo {

    public static void main(String[] args) {

        Store store = new Store();
        Producer producer = new Producer(store);
        Consumer consumer = new Consumer(store);

        new Thread(producer,"生产者A").start();
        new Thread(consumer,"消费者A").start();

        new Thread(producer,"生产者B").start();
        new Thread(consumer,"消费者B").start();
    }
}

//商店，负责信息接受发送，链接生产者和消费者
class Store
{
    //商品库存数量
    private  int stockNumber;

    public Store()
    {
        this.stockNumber=10;
    }

    //同步方法：进货：库存++
    public synchronized void stock()
    {
        //if(this.stockNumber>=10) //注意：如果使用if此处会产生虚假唤醒问题
        while (this.stockNumber>=10)
        {
            System.out.println(LocalDateTime.now()+" "+Thread.currentThread().getName()+ " 满仓！");
            try
            {
                this.wait();//释放锁，等待出货后加仓
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        ++this.stockNumber;
        System.out.println(LocalDateTime.now()+" "+Thread.currentThread().getName()+ " 进货 : "+this.stockNumber);
        this.notifyAll(); //加锁，通知可以出货，唤醒使用wait()的线程
    }

    //同步方法：出货：库存--
    public synchronized void sale()
    {
        //if(this.stockNumber<=0) //注意：如果使用if此处会产生虚假唤醒问题
        while (this.stockNumber<=0) //为了避免虚假唤醒问题，应该总是使用在循环中
        {
            System.out.println(LocalDateTime.now()+" "+Thread.currentThread().getName()+ " 缺货！");
            try
            {
                this.wait();//释放锁，等待加仓后出货
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        System.out.println(LocalDateTime.now()+" "+Thread.currentThread().getName() + " 出货 : "+ --this.stockNumber);
        this.notifyAll();//加锁，通知已出货，可以加仓，唤醒使用wait()的线程
    }
}
//生产者，生产信息
class Producer implements Runnable {

    //生产者和消费者必须同时拥有一个共用的信息载体
    private Store store;

    public Producer(Store store)
    {
        this.store=store;
    }

    //生产信息
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            this.store.stock();
        }

    }
}
//消费者，消费信息
class Consumer implements Runnable {

    //生产者和消费者必须同时拥有一个共用的信息载体
    private Store store;

    public Consumer(Store store)
    {
        this.store=store;

    }

    //消费信息
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            this.store.sale();
            //System.out.println(Thread.currentThread().getName()+":sale");
        }
    }
}