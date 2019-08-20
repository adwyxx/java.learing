package multicast.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.LinkedBlockingQueue;

//消费者：发送消息
public class Sender implements  Runnable{
    //使用阻塞队列保证消息发送前后一致性
    private LinkedBlockingQueue<String> massageQueue;
    private InetAddress group;
    private int port;
    private MulticastSocket socket;
    public Sender(InetAddress group,int port,LinkedBlockingQueue<String> queue) {
        this.group=group;
        this.port=port;
        this.massageQueue = queue;
        try {
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(group);
            this.socket.setTimeToLive(1);// 设置组播数据报的发送范围为本地网络
            this.socket.setSoTimeout(10000);// 设置套接字的接收数据报的最长时间

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        DatagramPacket packet;
        while (true){
            try {
                //从消息队列中获取消息，如果没有获取到，则线程阻塞在这里
                String msg = this.massageQueue.take();
                byte[] bytes = msg.getBytes();
                packet = new DatagramPacket(bytes,bytes.length,this.group,this.port);
                this.socket.send(packet);
                System.out.println("发送组播消息："+msg);

                if(this.massageQueue.isEmpty())
                {
                    System.out.println("请输入消息：");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
