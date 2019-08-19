import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

public class LambdaTest {

    private List<Person> list = Arrays.asList(
            new Person("张三",18,"男"),
            new Person("李四",25,"女"),
            new Person("王五",16,"男"),
            new Person("马六",48,"男"),
            new Person("田七",8,"女")
    );

    @Test
    public void test1() {
        this.filterPerson(list,(l)->{
            List<Person> newList = new ArrayList<>();
            for (Person person : l) {
                if(person.getAge()>18)
                {
                    newList.add(person);
                }
            }
            return newList;
        }).forEach(p -> System.out.println(p.toString()));

    }

    public List<Person> filterPerson(List<Person> list, Function<List<Person>,List<Person>> function)
    {
        return function.apply(list);
    }
}