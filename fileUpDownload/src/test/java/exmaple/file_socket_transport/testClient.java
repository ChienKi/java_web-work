package exmaple.file_socket_transport;



import org.junit.Test;

import java.io.*;
import java.net.Socket;


public class testClient {
    @Test
    public static void test() {

        FileInputStream InS = null;
        try {
            InS = new FileInputStream("D:\\Study\\JavaWeb\\day08-HTTP_Tomcat_Servlet\\java_web-work\\fileUpDownload\\file.png");
            Socket so = new Socket("127.0.0.1",8888);

            OutputStream OpS = so.getOutputStream();

            byte[] bytes = new byte[1024];

            int len = 0;

            while ((len = InS.read(bytes)) != -1){
                OpS.write(bytes,0,len);
            }

            so.shutdownOutput();

            InputStream ips = so.getInputStream();   //网络读取

            int lens = 0;

            while((lens = ips.read(bytes)) != -1){
                System.out.println(new String(bytes,0,lens));
            }

            InS.close();
            so.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
