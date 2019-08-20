package multicast.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;

//聊天工具Server端
public class ChatServer {
    public static void main(String[] args) {
        //消息队列
        LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
        try {
            //服务端发送消息组播地址：即客户端接收消息的组播地址
            InetAddress group = InetAddress.getByName("224.0.0.1");
            //端口
            int port = 8888;
            //生产者：控制台输入信息
            Inputer inputer = new Inputer(messageQueue);
            //消费者：利用组播发送消息
            Sender sender = new Sender(group,port,messageQueue);
            //接收消息组播地址：即客户端发送消息组播地址
            InetAddress receiveGroup = InetAddress.getByName("224.0.0.2");
            //接收消息
            Receiver receiver = new Receiver(receiveGroup,port);

            new Thread(inputer,"Inputer").start();
            //启动发送消息线程
            new Thread(sender,"Sender").start();
            //启动接受消息线程
            new Thread(receiver,"Receiver").start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
