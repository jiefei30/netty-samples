package com.atguigu.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author DELL
 * @date 2020/4/18 10:18
 */
public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //构造器
    //初始化工作
    public GroupChatServer(){
        try{
            //得到选择器
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            //设置非阻塞模式
            listenChannel.configureBlocking(false);
            //将该listenChannel注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e){
          e.printStackTrace();
        }
    }

    //监听
    public void listen(){
        try {
            //循环处理
            while(true){
                int count = selector.select(2000);
                if (count > 0){//有事件处理
                    //遍历得到selectionKey集合
                    Iterator<SelectionKey> iterator =  selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        //取出selectionKey
                        SelectionKey key = iterator.next();

                        //监听到accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            //将该sc注册到seletor
                            sc.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(sc.getRemoteAddress() + " 上线 ");
                        }
                        if (key.isReadable()){ //通过到发送read事件，即通道是可读的状态
                            //处理读
                            readData(key);
                        }
                        //当前的key删除，防止重复读
                        iterator.remove();
                    }
                }else
                    System.out.println("waiting....");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void readData(SelectionKey key){
        //定义一个SocketChannel
        SocketChannel channel = null;
        try{
            //得到channel
            channel = (SocketChannel) key.channel();
            //创建缓冲buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            //根据count的值作处理
            if(count > 0){
                //把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                //输出消息
                System.out.println("from客户端: " + msg);

                //向其他的客户端转发消息，专门写一个方法来处理
                senddInforToOtherClients(msg, channel);
            }
        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + "离线");
                //取消注册
                key.channel();
                //关闭通道
                channel.close();
            }catch (IOException e2){
                e2.printStackTrace();
            }
        }
    }

    //转发消息给其他客户（通道）
    private void senddInforToOtherClients(String msg, SocketChannel self) throws IOException{
        //遍历 所有注册到selector上的SocketChannel，并排除self
        System.out.println("服务器转发消息中...");
        for (SelectionKey key:selector.keys()){
            //通过key取出对应的SocketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                //转型
                SocketChannel st = (SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer的数据写入通道
                st.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
