package exmaple.file_socket_transport;


import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer {
    @Test
    public void test() {

        ServerSocket SSo = null;
        try {
            SSo = new ServerSocket(8888);
            Socket Soc = SSo.accept();

            InputStream FIS = Soc.getInputStream();  //网络读取


            FileOutputStream FOS = new FileOutputStream(".\\out\\file.jpg");  //本地输出文件

            int len = 0;

            byte[] bytes = new byte[1024];
            while ((len = FIS.read(bytes)) != -1) {
                FOS.write(bytes, 0, len);
            }


            Soc.getOutputStream().write("上传成功".getBytes());

            SSo.close();
            FOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
