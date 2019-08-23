import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient {

    public static void main(String[] args) {
        try {
            //方式一:使用Naming
            //1.从Registry中检索远程对象的存根/代理，用Naming.lookup()方法远程获取客户端服务
            HelloService service = (HelloService) Naming.lookup("rmi://localhost:8888/helloService");
            //2.调用
            System.out.println("Using Naming.lookup:"+service.sayHello("HO HO HO!"));

            //方式二:使用Registry
            // 如果RMI Registry就在本地机器上，URL就是:rmi://localhost:1099/hello
            // 否则，URL就是：rmi://RMIService_IP:1099/hello
            Registry registry = LocateRegistry.getRegistry("10.0.0.1",8000);
            HelloService helloService = (HelloService)registry.lookup("HelloService");
            System.out.println("Using Registry.lookup:"+helloService.sayHello("HA HA HA"));

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
