import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
// www.tongji.edu.cn  www.fudan.edu.cn  www.sjtu.edu.cn  www.mit.edu

/*
* String regex = "<span class=\".*\">.*</span>"; //这个正则表达式是取所有span标签
* <a>标签是超链接
* */
public class HTTPClient {
    private String webServer="localhost";
    private int port=8081;
    private String page="index.html";
    private Socket serviceSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * @param args
     */
    public static void main(String[] args) {
        //HTTPClient client = new HTTPClient("hx.tongji.edu.cn","index.php");
        HTTPClient client = new HTTPClient("localhost","/servlet_demo/");
        client.doGet();
    }
    public HTTPClient(String server,String p){
        this.webServer=server;
        this.page=p;
    }

    public void doGet() {
        try {
            String httpRequest = "GET /"+page+" HTTP/1.1\r\n";
            httpRequest = httpRequest
                    + "Accept: application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
            httpRequest = httpRequest + "Accept-Language: zh-CN\r\n";
            httpRequest = httpRequest
                    + "User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)\r\n";
            httpRequest = httpRequest + "Accept-Encoding: gzip, deflate\r\n";
            httpRequest = httpRequest + "Host: "+this.webServer+":"+port+"\r\n";
            httpRequest = httpRequest + "Connection: Keep-Alive\r\n\r\n";

            // Open a Socket
            serviceSocket = new Socket(webServer, port);
            if (serviceSocket != null) {
                // transfer message with socket
                out = new PrintWriter(serviceSocket.getOutputStream());
                out.println(httpRequest);
                out.flush();

                in = new BufferedReader(new InputStreamReader(serviceSocket.getInputStream(), StandardCharsets.UTF_8));

                System.out.println("Connection is  Success");

                System.out.println("--  HTTP Response  ------------------");
                String str = null;
                while (null != (str = in.readLine())) {
                    System.out.println(str);
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

}

