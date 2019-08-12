package unicast;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SocketClient {
    public static void main(String[] args) {
        try {
            //定义客户端Socket，连接服务器IP+port
            Socket socket = new Socket("localhost", 8888);

            //获取读操作缓冲区
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            //获取写操作缓冲区
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            //PrintWriter writer = new PrintWriter(socket.getOutputStream(),true);

            //写入数据到缓冲区
            writer.write("客户端消息，" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //将写入缓冲区的数据立即发送
            writer.flush();

            //循环等待读取服务返回消息
            while(true){
                //接受服务器端返回的数据
                String res = reader.readLine();
                if(res==null)
                {
                    break;
                }
                System.out.println("服务器端回复："+res);
            }
            //关闭写操作
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        while(true) {
//            new Thread(()->{
//
//
//            }).start();
//        }
    }
}
