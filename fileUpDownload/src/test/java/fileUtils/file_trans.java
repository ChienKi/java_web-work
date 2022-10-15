package fileUtils;

import java.io.*;
import java.net.Socket;

public class file_trans {
    //InputStream可以被FileInputStream覆盖;
    InputStream is;

    //OutputStream可以被FileOutputStream覆盖;
    OutputStream os;

    /**
     * 从InputStream输出到OutputStream
     * @param is
     * @param os
     */
    public void InputStream_to_OutputStream(InputStream is, OutputStream os){
        try {
            int len = 0;

            byte[] bytes = new byte[1024];
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 文件传输（网络->本地）：获取文件的socket->本地目录
     * @param socket
     * @param outputDir 本地目录，不包含文件名
     */
    public file_trans(Socket socket, String outputDir){
        mkdirs(outputDir);
        try {
            is = socket.getInputStream();  //获取socket对象的输入流
            //以下两行用于接收文件的名字
            String fileName = new DataInputStream(is).readUTF();
            String outputPath = outputDir + fileName;
            os = new FileOutputStream(outputPath);  //本地输出文件 OutputStream可以被FileOutputStream覆盖;

            InputStream_to_OutputStream(is,os);

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件传输（本地->网络）：本地路径->传输文件的socket
     * @param socket
     * @param inputPath
     */
    public file_trans(String inputPath, Socket socket){
        try {
            is = new FileInputStream(inputPath);  //InputStream可以被FileInputStream覆盖;
            os = socket.getOutputStream();

            //以下三行用于发送文件的名字
            DataOutputStream dos = new DataOutputStream(os);	// 用于输出文件名
            File file = new File(inputPath);
            dos.writeUTF(file.getName());				//将文件名发送给服务器

            InputStream_to_OutputStream(is,os);

            dos.close();
            os.close();
            is.close();
        } catch (IOException e) {
        }
    }

    /**
     * 创建目录
     * @param outputDir : 目录
     */
    public static void mkdirs(String outputDir){
        File file = new File(outputDir);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 创建文件所在的目录
     * @param outputPath : 包含文件名的路径
     */
    public static void mkdirsForParentFile(String outputPath){
        File file = new File(outputPath);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
    }

}