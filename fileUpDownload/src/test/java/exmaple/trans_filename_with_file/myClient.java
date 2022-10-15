package exmaple.trans_filename_with_file;

import java.net.Socket;

public class myClient {
    public static void upload(String path){	//filename为文件名，上传到服务器建议文件名采用时间加随机字符的格式
        try{
            Socket socket = new Socket("127.0.0.1",8888);
            new file_trans(path, socket);
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
    }

    public static void main(String []args){
        upload(".\\fileUpDownload\\pom.xml");
    }
}


