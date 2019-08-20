package multicast.chat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Receiver implements Runnable {

    private InetAddress group;
    private int port;
    private MulticastSocket socket;

    public Receiver(InetAddress group, int port) {
        this.group = group;
        this.port = port;
        try {
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(group);
            //this.socket.setSoTimeout(10000);// 设置套接字的接收数据报的最长时间
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("可以接收消息了！");
            byte[] bytes;
            DatagramPacket packet;
            while (true)
            {
                bytes = new byte[1024];
                packet = new DatagramPacket(bytes,1024);
                this.socket.receive(packet);
                System.out.println("收到消息："+new String(packet.getData()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
