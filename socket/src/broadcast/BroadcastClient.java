package broadcast;

import java.io.IOException;
import java.net.*;

//广播的实现：有客户端发出广播，服务端监听端口接受
//广播是通过UDP协议传输
public class BroadcastClient {
    public static void main(String[] args) {

        //0.广播数据
        String msg = "Hello world!";
        try {
            //1.获取广播地址
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
            //2.初始化广播Socket,Java通过DatagramSocket实现UDP
            DatagramSocket socket = new DatagramSocket();
            //3.初始化广播数据包
            DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.length(),inetAddress,9999);
            //4.发送广播数据
            socket.send(packet);

            System.out.println("客户端发送广播："+msg);

            //5.关闭广播
            socket.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
