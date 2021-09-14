package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author DELL
 * @date 2020/4/15 10:48
 *
 *  Scattering: 将数据写入到buffer时，可以采用buffer数组，依次写入【分散】
 *  Gathering: 从buffer读取数据时，可以采用buffer数组，依次读 【聚集】
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception{
        //使用 ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socket，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等客户端连接（telnet）
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8; //假定从客户端接收8个字节

        //循环读取
        while (true){
            int byteRead = 0;
            while(byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l; //累计读取的字节数
                System.out.println("byteRead = "+ byteRead);
                //使用流打印，看看当前的这个buffer的position 和 limit
                Arrays.asList(byteBuffers).stream().map(buffer -> "position" + buffer.position() + ",limit=" + buffer.limit())
                .forEach(System.out::println);
            }

            //将多有的buffer进行反转flip
            Arrays.asList(byteBuffers).forEach(buffer -> buffer.flip());

            //将数据读出显示到客户端
            long byteWrite = 0;
            while(byteWrite < messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            //将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(buffer -> {
                buffer.clear();
            });

            System.out.println("byteRead=" + byteRead + "byteWrite = " + byteWrite + ",messageLength" + messageLength);
        }
    }
}
