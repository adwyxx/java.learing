import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//2.接口实现类要继承UnicastRemoteObject类
public class HelloServiceImpl extends UnicastRemoteObject implements  HelloService
{
    //必须实现构造函数
    public HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String msg) throws RemoteException{
        return "Server Method sayHello:"+msg;
    }
}
