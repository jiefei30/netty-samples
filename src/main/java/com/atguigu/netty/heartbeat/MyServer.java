package com.atguigu.netty.heartbeat;

import com.atguigu.netty.groupchat2.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author DELL
 * @date 2020/5/4 16:25
 */
public class MyServer {
    public static void main(String[] args) throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();  //8个

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            //向pipeline加入一个netty提供的 IdleStateHandler
                            //long readerIdleTime: 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            //long writerIdleTime: 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            //long addIdleTime: 表示多长时间没有读写， 就会发送一个心跳检测包检测是否连接
                            //文档说明：
                            //当 IdleStateEvent 出发后 ，就会传递给管道的下一个handler去处理，通过调用（触发），
                            //下一个handler的userEventTiggered，在该方法中去处理IdleStateEvent
//                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));

                            //加入一个对空闲检测进一步处理的handler
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = bootstrap.bind(7000).sync();

            //监听关闭
            channelFuture.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
