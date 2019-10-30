package Reflect;

import Reflect.annotation.DataFiled;
import Reflect.annotation.DataTable;

import java.io.Serializable;

/**
 * 用户类
 * @author: Leo.Wang, adwyxx@qq.com
 * @date: 2019/10/30 10:12
 */
@DataTable("users")
public class User  implements IEntity, Serializable {

    @DataFiled(filedName = "id",dataType = FiledDataType.INT)
    private int id;

    @DataFiled(filedName = "name",filterTypes = {GenerateFilterType.INSERT,GenerateFilterType.SELECT})
    private String name;

    public int age;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int setId(int id) {
        return this.id=id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String n) {
        this.name = n;
    }

    //一定要有无参构造方法，在反射创建对象实例时一般会用到，没有就会报错
    public User(){

    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return "{id:"+this.id+",name:"+this.name+"}";
    }
}
