package com.atguigu.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author DELL
 * @date 2020/4/4 9:55
 */
public class BIOServer {
    public static void main(String[] args) throws Exception{
        //线程池机制

        //思路
        //1.创建一个线程池
        //2.如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建一个ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了 ");

        while(true){
            //监听，等待客户端连接
            System.out.println("等待客户端连接");
            final Socket socket = serverSocket.accept();  //阻塞方法
            System.out.println("连接到一个客户端了");

            //就创建一个线程，与之通讯（单独写一个方法）
            newCachedThreadPool.execute(new Runnable() {
                public void run() { //我们重写
                    //可以和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket){
        System.out.println("线程信息 id = " + Thread.currentThread().getId() + "名字=" + Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        try{
            //通过socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            while (true){
                System.out.println("等待客户端输入");
                int read = inputStream.read(bytes);   //阻塞方法
                if(read != -1){
                    System.out.println(new String(bytes,0, read));  //输出客户端发送的数据
                }else {
                    break;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                System.out.println("关闭和client的连接");
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
