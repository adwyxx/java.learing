package ClassLoader;
// 运行时 jvm把类信息加入方法区
public class AppMain {
	// main方法放入方法区
	public static void main(String[] args) {
		// test1是引用 放入栈中 new Sample("hello")对象放入堆中
		Sample test1 = new Sample("hello");
		test1.printName();
	}
}
// 运行时 jvm把 类信息放入方法区
class Sample {
	// new Sample()后， name 引用放入栈  name对象放入堆
	private String name;

	public Sample(String name) {
		this.name = name;
	}
	// 方法本身放入方法区
	public void printName() {
		System.out.println(name);
	}
}