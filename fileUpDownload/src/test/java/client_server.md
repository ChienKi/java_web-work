一、简单通信
①客户端发送数据
```java
// 创建客户端的socket服务，指定目的主机和端口
Socket sc = new Socket(" 172.20.1.34", 10000);
// 为了发送数据，应该获取socket中的输出流
OutputStream out = sc.getOutputStream();

// write接收字节数据
out.write("tcp test".getBytes());

sc.close();
```

②服务端接收数据
```java
public static void main(String[] args) throws Exception{
    // 建立服务端Socket服务，并指定监听一个端口
    ServerSocket ss = new ServerSocket(10000);

    // 通过accept方法来获取连接过来的客户端对象

    Socket s = ss.accept();

    String IP = s.getInetAddress().getHostAddress();
    System.out.println("IP:"+IP+"...connected");

    // 获取客户端发送过来的数据，那么要使用客户端对象的读取流来读取数据
    // 这里的源来自对方机器网络数据，
    InputStream is = s.getInputStream();

    byte[] b = new byte[1024];
    int len = is.read(b);
    System.out.println(new String(b,0,len));
//    System.out.println(is.read(b));
    s.close();
}
```

注意：@Test的相对路径在module下面，main的相对路径在project下面


# 报错与注意事项


## 1： java.net.SocketException: Socket is closed

新手上路，学习socket碰到关闭问题，解决好久找到出错问题。

```java
OutputStream os = socket.getOutputStream();
os.close();
InputStream is = socket.getInputStream();
is.close();
```

这样关闭会把socket一块关闭。报如下错误：

