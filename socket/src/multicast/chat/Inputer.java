package multicast.chat;

import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

//生产者，往消息队列中插入消息
public class Inputer implements Runnable{
    //消息队列
    private LinkedBlockingQueue<String> messageQueue;
    private Scanner scanner = new Scanner(System.in);
    public Inputer(LinkedBlockingQueue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true)
        {
            if(this.messageQueue.isEmpty())
            {
                String msg = scanner.nextLine();
                try {
                    this.messageQueue.put(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
