package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author DELL
 * @date 2020/4/8 9:38
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception{

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while(true){ //循环读取
            byteBuffer.clear();
            int read = channel01.read(byteBuffer);
            if(read == -1){ //表示读完
                break;
            }
            //将buffer中的数据写入到channel02 -- 2.txt
            byteBuffer.flip();
            channel02.write(byteBuffer);
        }

        //关闭相关的流
        fileInputStream.close();
        fileOutputStream.close();

    }
}
