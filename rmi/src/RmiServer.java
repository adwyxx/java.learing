import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {

    public static void main(String[] args) {

        try {
            //1.创建远程对象
            HelloService helloService = new HelloServiceImpl();
            //2.注册RMI服务端端口
            LocateRegistry.createRegistry(8888);
            //3.如果RMI要远程端调用，需要注册服务端IP
            System.setProperty("java.rmi.server.hostname","127.0.0.1");

            //方式一:使用Naming注册远程对象
            //4.注册远程对象。使用Naming.bind绑定服务
            Naming.bind("rmi://localhost:8888/helloService",helloService);
            System.out.println("Naming.bind : rmi://localhost:8888/helloService");

            //方式二:使用Registry注册远程对象
            //获取注册中心引用,注意：host的IP地址必须是真实的IP地址，否则无法访问到
            //如果本地测试需要设置hosts文件，指定服务地址
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",8000);
            registry.bind("HelloService",helloService);
            System.out.println("Registry.bind : HelloService");

            //客户端程序向服务端请求一个对象的时候，返回的stub对象里面包含了服务器的hostname，客户端的后续操作根据这个hostname来连接服务器端。
            //要想知道这个hostname具体是什么值可以在服务器端bash中打入指令：hostname -i 如果返回的是127.0.0.1，那么你的客户端肯定会抛如标题的异常了。

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
