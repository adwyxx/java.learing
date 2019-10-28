package BasicDataType;

/**
 * @Description:
 * @Author: Leo.Wang
 * @Email: adwyxx@qq.com
 * @Date: 2019/10/28 22:12
 */
public class PackageClass {
    public static void main(String[] args) {
        int n = 10;
        Integer i = 10;
        Integer j = 10;
        Integer k = new Integer(10);
        Integer m = new Integer(10);
        System.out.println(n==i);   // true
        System.out.println(i==j);   // true
        System.out.println(i.equals(j));    //true
        System.out.println(i==k);   //false
        System.out.println(k==m);   //false
    }
}
