package com.atguigu.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author DELL
 * @date 2020/4/21 9:48
 */
public class NewIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",7001));
        String filename = "";

        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();

        //准备发送
        long startTime = System.currentTimeMillis();

        //在linux下一个transferTo方法就可以完成传输
        //在windows下一次调用transferTo只能发送8m，就需要分段传输文件，而且要主要
        //传输时的位置
        //transferTo底层使用到零拷贝
        long transferCount = fileChannel.transferTo(0,fileChannel.size(),socketChannel);

        System.out.println("发送的总的字节数 = " + transferCount + "耗时："+(System.currentTimeMillis() - startTime));

        //关闭通道
        fileChannel.close();

    }
}
