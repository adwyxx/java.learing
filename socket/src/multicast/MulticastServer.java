package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    public static void main(String[] args) {

        MulticastSocket socket = null;
        try  {
            //1.声明一个组播Socket对象
            socket = new MulticastSocket();
            socket.setTimeToLive(32);
            //2.创建一个DatagramPacket
            String msg = "组播消息:Hello world!"; //组播消息
            //组播的组IP地址必须在：224.0.0.0至239.255.255.255之间
            //客户端和服务端的组播IP地址必须相同
            InetAddress address = InetAddress.getByName("224.1.1.1");
            //UDP数据包
            DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.getBytes().length,address,8080);

            //3.发送组播消息
            socket.send(packet);
            System.out.println("Server 发送组播消息："+msg);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.关闭sokect
            if(socket!=null && !socket.isClosed()){
                socket.close();
            }
        }
    }
}
