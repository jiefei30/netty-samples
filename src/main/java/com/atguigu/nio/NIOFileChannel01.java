package com.atguigu.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author DELL
 * @date 2020/4/6 11:05
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception{

        String str = "hello,尚硅谷";

        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Study\\NIOChannelTest.txt");

        //通过 fileOutStream获取对应的 FileChannel
        //这个fileChannel真实类型是 FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

        //创建一个缓冲区 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //将 str 放入 byteBuffer
        byteBuffer.put(str.getBytes());

        //对byteBuffer 进行flip
        byteBuffer.flip();

        //将byteBuffer 数据写入到fileChannel
        fileChannel.write(byteBuffer);
        fileOutputStream.close(); //关闭流
    }
}
