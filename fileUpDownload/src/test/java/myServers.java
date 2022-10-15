import fileUtils.file_trans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/*
    自定义服务器
 */
public class myServers{
    int serverNum =3;
    /**
     * ServerName: Server0 ... Serveri
     * port: 8080 ... 8080+i
     */
    Server[] serverList;
    server_meta[] server_metas;
    public myServers(int ServerNum){
        this.serverNum =ServerNum;
        this.serverList = new Server[ServerNum];
        this.server_metas = new server_meta[ServerNum];
        for(int i=0; i<ServerNum; i++){
            server_metas[i]=new server_meta(i);
            //在这里会阻塞住嘛？？？
            Thread t = new Server(server_metas[i]);
            t.start();
            //serverList[i]= (Server) t;
        }
    }

    /**
     * Server端全局main函数
     * @param args
     */
    public static void main(String[] args) {
        int ServerNum = 3;
        myServers myServers = new myServers(ServerNum);

    }
}
class server_meta {
    /**
     * serverIndex: 0 ... i
     * serverName: Server0 ... Serveri
     * port: 8080 ... 8080+i
     */
    int serverIndex;
    String serverName;
    String host;
    int port;
    server_meta(int serverIndex){
        this.serverIndex=serverIndex;
        this.serverName="Server"+serverIndex;
        this.host="127.0.0.1";
        this.port=8080+serverIndex;
    }
}
class Server extends Thread{
    String serverName;
    int serverIndex;
    private int port = 8080;
    private ServerSocket server_socket;   //普通消息传输服务器套接字
    public Server(server_meta ms){
        this.serverIndex=ms.serverIndex;
        this.serverName =ms.serverName;
        this.port=ms.port;

    }
    public void run(){
        try {
            //1-初始化
            server_socket = new ServerSocket(port);   // 创建消息传输服务器
            System.out.println("server is running on port:"+port);

            //2-每次接收一个客户端请求连接时都启用一个线程处理
            while(true) {
                Socket client_socket = server_socket.accept(); //此处会阻塞，直到接收一个客户端连接请求
                System.out.println(serverName+" connected from " + client_socket.getRemoteSocketAddress());
                Thread t = new ServerThread(serverName, client_socket);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/**
 *   自定义服务线程：处理客户端的请求
 */
class ServerThread extends Thread{
    String ServerName;
    private final Socket client_socket;
    public ServerThread(String ServerName, Socket client_socket) {
        this.ServerName=ServerName;
        this.client_socket = client_socket;
    }

    public void run() {
        downloadSplitFiles();
        //TODO:接收文件名
        //String fileName = socket_reader(client_socket);
        //String fileName = "file.png";
        //System.out.println(fileName);
        //upload(fileName);
    }

    public static String socket_reader(Socket socket){
        try {
            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String str = reader.readLine();
            reader.close();
            is.close();
            return str;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     *
     */
    public void downloadSplitFiles(){
        String outputDir=".\\fileUpDownload\\out\\"+ServerName+"\\";
        //获取客户端上传的hashmap, 从中获取文件名
        file_trans file_download = new file_trans(client_socket, outputDir);
        //fileMapTable file_map = fileMapTable.deserialize(file_download.getOutputPath());
        //System.out.println("filename:"+file_map.fileName);

        //建立输出路径
        //outputDir = outputDir+file_map.fileName+"\\";  //分块的文件要存在这个文件名为名的目录下面

    }
    public void upload(String fileName){
        new file_trans(".\\fileUpDownload\\out\\" + ServerName + "\\" + fileName, client_socket);
    }
}


