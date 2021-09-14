package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author DELL
 * @date 2020/4/14 10:35
 * 说明：
 * 1.MappedByteBuffer 可让文件直接在内存（堆外内存）修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception{

        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");

        //获取对应的通道
        FileChannel channel = randomAccessFile.getChannel();

        /**
         *  参数1： FileChannel.MapMode.READ_WRITE 使用的读写模式
         *  参数2：    0： 可以直接修改的起始位置
         *  参数3：    5： 是映射到内存的大小(不是索引)，即将 1.txt 的多少个字节映射到内存
         *  可以直接修改的范围是 0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,0,5);

        mappedByteBuffer.put(0,(byte)'D');
        mappedByteBuffer.put(3,(byte)'A');

        randomAccessFile.close();
        System.out.println("success");
    }
}
