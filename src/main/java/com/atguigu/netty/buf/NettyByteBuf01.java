package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author DELL
 * @date 2020/4/28 10:01
 */
public class NettyByteBuf01 {
    public static void main(String[] args) {
        //创建一个ByteBuf
        //说明
        //1.创建对象，该对象包含一个数组arr，是一个byte[10]
        //2.在netty的buffer中，不需要使用flip进行反转
        //底层维护了 readerindex 和 writerIndex
        ByteBuf buffer = Unpooled.buffer(10);

        for(int i = 0; i < 10; i++){
            buffer.writeByte(i);
        }

        System.out.println("capacity = " + buffer.capacity());

        //输出方式1，指定index
        for(int i = 0; i < buffer.capacity(); i++){
            System.out.println(buffer.getByte(i));
        }

        //输出方式2
        for(int i = 0; i < buffer.capacity(); i++){
            System.out.println(buffer.readByte());
        }
    }
}
