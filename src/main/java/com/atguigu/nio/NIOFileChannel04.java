package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author DELL
 * @date 2020/4/8 10:04
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception{

        //创建相关的流
        FileInputStream fileInputStream = new FileInputStream("D:\\Study\\gtx1050ti.png");
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\Study\\gtx1050ti2.png");

        //获取各个流对应的filechannel
        FileChannel sourceCh = fileInputStream.getChannel();
        FileChannel destCh = fileOutputStream.getChannel();

        //使用transferForm完成拷贝
        destCh.transferFrom(sourceCh,0,sourceCh.size());

        //关闭相关通道和流
        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
