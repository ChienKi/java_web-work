import fileUtils.file_trans;

import java.io.*;
import java.net.Socket;

public class myClient {
    /**
     * 用于记录server数量、名字、host、port
     */
    server_meta[] server_metas;

    public myClient(int serverNum) {
        this.server_metas = new server_meta[serverNum];
        for(int i=0; i<serverNum; i++){
            server_metas[i]=new server_meta(i);
        }
    }

    /**
     * 本地已分块文件->服务器
     * 建立socket，传输文件
     * @param inputPath 包含文件所在目录和文件名
     */
    public static void upload(String inputPath, server_meta server_meta){
        try{
            Socket socket = new Socket(server_meta.host,server_meta.port);
            new file_trans(inputPath, socket);
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    /**
     * 注意：文件被拆分成了很多份，这个文件名是本地的、拆分前的名字
     * @param fileName 所需下载的文件名
     * @param outputDir 文件所在目录,不含文件名
     */
    public static void download(String fileName, String outputDir){
        try{
            Socket socket = new Socket("127.0.0.1",8080);

            //TODO: 把文件名传输过去
            //socket_writer(fileName, socket);

            new file_trans(socket, outputDir);
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public static void socket_writer(String str, Socket socket){
        try {
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            byte[] nbt = str.getBytes();
            // 将字节数组写入到Socket管道中
            bos.write(nbt);
            bos.close();
            os.close();
        }catch (IOException e){

        }
    }

    /**
     * 将文件分割, 记录分发到服务器的哈希map
     */
    public void fileDistribute(File file){
    }

    /**
     * client端全局main函数
     * @param args
     */
    public static void main(String []args){
        int serverNum = 3;
        myClient myClient = new myClient(serverNum);
        String clientDir=".\\fileUpDownload\\out\\Client\\";

//        //分割文件
//        Split splitFile = new Split(".\\fileUpDownload\\out\\Client\\file.mp4", 1, ".\\fileUpDownload\\out\\Client\\tmpSlices\\");
//        //通过分割的文件和server生成hashmap并序列化
//        fileMapTable file_map = new fileMapTable(splitFile,myClient.server_metas);

        String fileName="file.png";
        String fileSplitDir=clientDir +  "tmpSlices\\"+fileName+"\\";
        String fileMapName=fileName+".fileMapTable.ser";
        //反序列化获取fileMapTable
        fileMapTable file_map = fileMapTable.deserialize(fileSplitDir+fileMapName);
        //逐个上传
        for (String serverName:file_map.mapTable.keySet()) { //每个服务器
            int serverIndex=Integer.parseInt(serverName.substring(6));  //通过名字获取服务器编号
            server_meta server_meta=myClient.server_metas[serverIndex];  //该服务器的元数据（包含host、port等

            upload(fileSplitDir+fileMapName, server_meta);  //上传映射表
            //System.out.println(serverName);
            //System.out.println(serverIndex);
            for(String splitFileName:file_map.mapTable.get(serverName)){ //逐个上传每个分块的文件
                //System.out.println(splitFileName);
                upload(fileSplitDir+splitFileName, server_meta);
            }
        }

        //上传串通测试
        //upload(".\\fileUpDownload\\file.mp4");
        //多线程上传测试
        //upload(".\\fileUpDownload\\file.png");
        //TODO:下载串通测试
        //download("file.png", ".\\fileUpDownload\\");


        //命令行测试


    }
}


