package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * @author DELL
 * @date 2020/4/28 10:40
 */
public class NettyByteBuf02 {
    public static void main(String[] args) {
        //创建ByteBuf
        ByteBuf buf = Unpooled.copiedBuffer("Hello,world!", CharsetUtil.UTF_8);

        //使用相关的方法
        if (buf.hasArray()){
            byte[] content = buf.array();

            //将 content 转成字符串
            System.out.println(new String(content,CharsetUtil.UTF_8));

            System.out.println("byteBuf = " + buf);

            System.out.println(buf.arrayOffset());  //0
            System.out.println(buf.readerIndex());  //0
            System.out.println(buf.writerIndex());  //12
            System.out.println(buf.capacity());  //36

            System.out.println(buf.readableBytes());  //可读的字节数  12
        }
    }
}
