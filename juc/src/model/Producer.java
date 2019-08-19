package model;

//生产者
public class Producer implements Runnable {

    //生产者和消费者实体类中一定包含一个共用的信息载体
    private  Store store;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public  Producer(Store store)
    {
        this.store=store;
    }

    @Override
    public void run() {

    }
}
