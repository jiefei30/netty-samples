package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author DELL
 * @date 2020/4/27 17:03
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = socketChannel.pipeline();

        //加入一个netty 提供的httpServerCodec      (codec => [coder - decoder])
        //HttpServerCodec 说明
        //1. HttpServerCodec 是netty提供的处理http
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());
        //2.增加一个自己的Handler
        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());
    }
}
