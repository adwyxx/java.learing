import java.rmi.Remote;
import java.rmi.RemoteException;

//1.接口要继承Remote接口
public interface HelloService extends Remote {
    //2.定义接口方法，注意：必须抛出RemoteException异常
    String sayHello(String msg) throws RemoteException;
}
