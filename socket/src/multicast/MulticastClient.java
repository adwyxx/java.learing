package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

//接受消息
public class MulticastClient {

    public static void main(String[] args) {

        MulticastSocket socket = null;
        try {
            //1.创建一个组包socket,监听端口8080,注意：和消息发送端端口一致
            socket = new MulticastSocket(8080);

            //2.指定组播的IP地址，加入组播
            //注意：IP地址要与组播发送端的一致
            InetAddress address = InetAddress.getByName("224.1.1.1");
            socket.joinGroup(address); //加入组播
            System.out.println("Client 监听组播IP:"+address.getHostName()+",Port:"+socket.getPort());
            //3.创建UDP数据包：DatagramPacket对象，用于接收数据
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);

            //4.接收组播数据，阻塞线程
            socket.receive(packet);

            //5.读取数据
            String msg = new String(packet.getData(),0,packet.getData().length);
            System.out.println("接收到组播数据："+msg);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //6.关闭Multicast对象
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        }
    }
}
