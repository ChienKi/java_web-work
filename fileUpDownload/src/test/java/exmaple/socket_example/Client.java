package exmaple.socket_example;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    // 向服务器发送数据
    public void uploadFile(Socket socket) {
        BufferedReader reader = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            System.out.println("请输入要上传文件的完成路径:");
            // 接收键盘输入
            InputStream in = System.in;
            // 获取Socket管道字节输出流
            OutputStream out = socket.getOutputStream();
            // 使用BufferedReader包装，便于按行读取客户端输入的数据
            reader = new BufferedReader(new InputStreamReader(in));
            // 客户端上传到服务端文件的名字
            String fileName = reader.readLine();
            // 创建字符文件输入流，用于读取上传文件中的数据
            FileInputStream fis = new FileInputStream(fileName);
            // 使用字节输入缓冲提高读取的速度
            bis = new BufferedInputStream(fis);
            // 获取Socket管道输出流，将从文件中的数据写入到管道中
            bos = new BufferedOutputStream(out);
            byte[] nbt = fileName.getBytes();
            // 将文件名字符串转换成字节数组的长度写入到Socket管道中
            bos.write(nbt.length);
            // 将字节数组写入到Socket管道中
            bos.write(nbt);
            // 定义临时变量，将从文件中读取到的数据写入到Socket管道中
            int data = 0;
            // 将文件中的数据写入到Socket管道中
            while ((data = bis.read()) != -1) {
                bos.write(data);
            }
            System.out.println("");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭io流和socket
            try {
                if (reader != null) {
                    reader.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Socket client = null;
        try {
            // 创建客户端套接字对象
            client = new Socket("127.0.0.1", 8888);
            new Client().uploadFile(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
