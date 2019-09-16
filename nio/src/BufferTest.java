import java.nio.ByteBuffer;
import org.junit.Test;

public class BufferTest {

	/**
	 * 1、缓冲区（Buffer）：在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同数据类型的数据
	 * * 根据数据类型不同（boolean 除外），提供了相应类型的缓冲区：
	 * ByteBuffer  //最常用
	 * CharBuffer
	 * DoubleBuffer
	 * FloatBuffer
	 * IntBuffer
	 * LongBuffer
	 * ShortBuffer
	 * 
	 * 2、Buffer的4个核心属性：
	 * capacity : 容量，表示缓冲区中最大存储数据的容量。一旦声明不能改变。
	 * limit 	： 界限，表示缓冲区中可以操作数据的大小。（limit 后数据不能进行读写）
	 * position : 位置，表示缓冲区中正在操作数据的位置索引。
	 * mark 	： 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置
	 * 
	 * 0 <= mark <= position <= limit <= capacity
	 * 
	 * 3、Buffer存取数据的两个核心方法：
	 * get() : 获取Buffer的内容，有众多重载方法
	 * put() : 向Buffer中添加内容，重载方法
	 * 
	 * 4、直接缓冲区与非直接缓冲区：
	 * * 非直接缓冲区：通过 allocate() 方法分配缓冲区，将缓冲区建立在 JVM_GC 的内存中
 	 * * 直接缓冲区：通过 allocateDirect() 方法分配直接缓冲区，将缓冲区建立在物理内存中。可以提高效率
	 * */
	@Test
	public void bufferMethodsTest() {
		
		//allocate()方法：初始化Buffer存储空间，一旦初始化后不能改变
		ByteBuffer buf = ByteBuffer.allocate(1024);
		System.out.println("---------allocate()----------");
		System.out.println("capacity:"+buf.capacity()); //1024
		System.out.println("position:"+buf.position()); //0
		System.out.println("limit:"+buf.limit()); 		//1024
		
		String str="ABCDE";
		byte[] btyes = str.getBytes();
		//put()方法：向缓冲区中存入数据
		buf.put(btyes);
		System.out.println("---------put()---------");
		System.out.println("capacity:"+buf.capacity()); //1024
		System.out.println("position:"+buf.position()); //5
		System.out.println("limit:"+buf.limit()); 		//1024
		
		//flip()方法：切换到读取模式
		buf.flip(); 
		System.out.println("---------flip()，get()----------");
		byte[] result = new byte[buf.limit()];
		//get()方法：读取缓冲区中数据
		buf.get(result);
		String s=new String(result);
		System.out.println("输出内容:"+ s); //ABCDE
		System.out.println("capacity:"+buf.capacity()); //1024
		System.out.println("position:"+buf.position()); //5  /** 从A读到E，positon从0增加到5，即数组重最后一个读取值得索引+1 **/
		System.out.println("limit:"+buf.limit()); 		//5  /** 注意：读取操作时limit值为Buffer中的内容长度  **/
		
		//rewind()方法：重复读取
		buf.rewind();
		System.out.println("-----------------rewind()----------------");
		System.out.println(buf.capacity());	//1024
		System.out.println(buf.position()); //0   /** rewind()后从头开始读取，positon为0 **/
		System.out.println(buf.limit());	//5

		//clear()方法：清空Buffer中数据。注意：实际数据还在，只是处于被遗忘状态（只是把引用指针清空掉）。
		buf.clear();
		System.out.println("-----------------clear()----------------");
		System.out.println(buf.capacity());	//1024
		System.out.println(buf.position()); //0   /** clare()后positon为0 **/
		System.out.println(buf.limit());	//1024
		System.out.println((char)buf.get(1));//B  数据还存在可以读取到
	}
}
