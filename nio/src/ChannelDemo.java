import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

/*
 ** 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 * 
 ** 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 * 
 ** 三、获取通道
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 * 
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 * 		
 * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 * 
 ** 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 * 
 ** 五、分散(Scatter)与聚集(Gather)
 ** 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 ** 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 * 
 ** 六、字符集：Charset
 ** 编码：字符串 -> 字节数组
 ** 解码：字节数组  -> 字符串
 * 
 */
public class ChannelDemo {

	/**
	 ** 通过getChannel方法获取通道
	 * */
	@Test
	public void getChannelTest() {
		System.out.println("getChannel Test");
		
	}
	
	@Test
	public void newByteChannelTets()
	{
		try {
			ByteChannel channel = Files.newByteChannel(Paths.get("D:/test.txt"),StandardOpenOption.READ);
		}
		catch(IOException  e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void channelReadWriteTest()
	{
		
		long start = System.currentTimeMillis();

		FileInputStream fis = null;
		FileOutputStream fos = null;
		// 1-获取通道
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			fis = new FileInputStream("d:/1.mkv");
			fos = new FileOutputStream("d:/2.mkv");

			inChannel = fis.getChannel();
			outChannel = fos.getChannel();

			// 2-分配指定大小的缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);

			// 3-将通道中的数据存入缓冲区中
			while (inChannel.read(buf) != -1) {
				buf.flip(); // 切换读取数据的模式
				// 4-将缓冲区中的数据写入通道中
				outChannel.write(buf);
				buf.clear(); // 清空缓冲区
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outChannel != null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (inChannel != null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		long end = System.currentTimeMillis();
		System.out.println("耗费时间为：" + (end - start));
	}
	
	@Test
	public void testChannelTransferFrom()
	{
		try {
			FileChannel inChannel = FileChannel.open(Paths.get("D:\\1.jpg"), StandardOpenOption.READ);
		    FileChannel outChannel = FileChannel.open(Paths.get("D:\\2.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		    // 使用transferFrom传输数据
		    outChannel.transferFrom(inChannel,0, inChannel.size()); 
		    inChannel.close();
		    outChannel.close();
		}
		catch(IOException e) {		
			e.printStackTrace();
		}	
	}
	
}
