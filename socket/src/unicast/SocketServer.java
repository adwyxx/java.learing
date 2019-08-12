package unicast;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SocketServer {

    public static void main(String[] args) {
        //创建服务端Socket，监听端口 8888
        try (ServerSocket serverSocket = new ServerSocket(8888)){
            System.out.println("Socket Server 启动");
            //循环读取消息
            while(true){
                //等待一个接受请求，accept()阻塞线程
                Socket socket = serverSocket.accept();
                MessageReceiver receiver = new MessageReceiver(socket);
                new Thread(receiver).start();
            }
            //serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
//接受消息处理类
class MessageReceiver implements Runnable {
    private  Socket socket;

    public MessageReceiver(Socket socket)
    {
        this.socket=socket;

    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();

            //创建读取缓冲区，读取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            //读取数据
            while (true){
                String msg = reader.readLine();
                if(msg==null)
                {
                    break;
                }
                System.out.println(msg);
            }

            //获取写缓冲区
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
            //向缓冲区中写入消息
            writer.write("读完数据"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            //将写入缓冲区的消息立即发送，并清空缓冲区
            writer.flush();
            //关闭写缓冲区
            writer.close();
            //关闭客户端连接
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
