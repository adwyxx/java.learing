package CAS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: AtomicReference<V> 示例
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019-08-29 14:21
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        User user1 = new User(1,"Tom");
        User user2 = new User(2,"Bill");
        User user3 = new User(3,"Jack");
        //初始化AtomicReference，初始值为user1
        AtomicReference<User> atomicReference = new AtomicReference<>(user1);

        System.out.println("atomicReference :"+atomicReference.get());
        //CAS=>期望值user1，更新为user2，CAS成功,返回true
        System.out.println("atomicReference CAS user1 -> user2:"+atomicReference.compareAndSet(user1,user2));
        System.out.println("atomicReference :"+atomicReference.get());
        //CAS=>期望值user1，更新为user3 ，CAS失败,返回false
        System.out.println("atomicReference CAS user1 -> user3:"+atomicReference.compareAndSet(user1,user3));
    }
}

@Data
@ToString
@AllArgsConstructor
class User{
    private int id;
    private String name;
}
