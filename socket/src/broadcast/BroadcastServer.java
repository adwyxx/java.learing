package broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class BroadcastServer {

    public static void main(String[] args) {

        DatagramSocket socket = null;

        try{
            //1.初始化DatagramSocket,监听端口9999
            socket = new DatagramSocket(9999);
            System.out.println("服务器端监听广播端口打开");
            //2.初始化DatagramPacket,接受广播数据
            //接收的数据存荣缓冲区Bug，每次接受buf.length的长度的数据
            byte[] buf = new byte[2014];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            //3.接收数据，receive()方法阻塞线程
            socket.receive(packet);
            //4.读取数据
            String msg = new String(packet.getData(),0,packet.getLength());
            System.out.println("收到广播消息："+msg+",广播IP:"+packet.getAddress().getHostAddress());

            //方法二：使用StringBuffer读取数据
            //StringBuffer buffer = new StringBuffer();
            //for (int i = 0; i <buf.length ; i++) {
            //    if(buf[i]==0)
            //    {
            //        break;
            //    }
            //    buffer.append((char)buf[i]);
            //}

        }catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(socket != null)
            {
                //5.关闭socket
                socket.close();
            }
        }
    }
}
