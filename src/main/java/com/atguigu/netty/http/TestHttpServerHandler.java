package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author DELL
 * @date 2020/4/27 17:02
 * 说明：
 * 1.SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter的子类
 * 2.HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    //channelRead0读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断 msg 是不是 httprequest请求
        if(msg instanceof HttpRequest){

            System.out.println("msg的类型是" + msg.getClass());
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            //获取到
            HttpRequest httpRequest = (HttpRequest)msg;
            //获取uri，过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico，不做响应");
                return;
            }

            //回复信息给浏览器 [http协议]

            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_16);

            //构建一个http的响应，即 httpreponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将构建好的 response 返回
            ctx.writeAndFlush(response);
        }
    }
}
