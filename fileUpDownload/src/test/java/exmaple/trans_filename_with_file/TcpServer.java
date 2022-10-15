package exmaple.trans_filename_with_file;

import java.io.IOException;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 参考网址：https://blog.csdn.net/qq_46686697/article/details/117783121
 */
public class TcpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);		//服务器端口号

        while(true){
            Socket client_socket = serverSocket.accept();				//在此处阻塞，只有当有文件发送过来时才执行后面的操作

            DataInputStream in = new DataInputStream(client_socket.getInputStream());
            String filename = in.readUTF();						//读取客户端发送过来的文件名

            ExecutorService exec = Executors.newCachedThreadPool();	//创建一个执行器对象来为我们管理Thread对象
            exec.execute(new Thread(new ServerThread(client_socket,".\\fileUpDownload\\out\\"+filename))); //启动任务
        }
    }
}

