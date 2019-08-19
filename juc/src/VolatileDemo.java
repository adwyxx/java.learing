public class VolatileDemo {
    public static void main(String[] args) {

        VolatileThreadDemo vt = new VolatileThreadDemo();
        Thread th = new Thread(vt,"子线程1");
        th.start();

        while(true)
        {
            //如果不适用volatile修饰flag，则当th线程执行完毕后vt的flag值并没有更新，导致此时vt.isFlag()始终返回false
            if(vt.isFlag())
            {
                System.out.println("主线程 : vt flag is "+ vt.isFlag());
                break;
            }
        }
        //输出：
        //  主线程 : vt flag is true
        //  子线程1 : flag is true
    }
}

class VolatileThreadDemo implements Runnable {

    //使用volatile关键字，强制将flag值刷入主内存，对其它线程可见
    private volatile boolean flag=false;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.flag=true;
        System.out.println(Thread.currentThread().getName()+" : flag is " + this.flag);
    }
}