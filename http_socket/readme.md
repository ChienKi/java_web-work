第一次写java_socket
最基础的版本，并未使用tomcat，而是直接调用ServerSocket
别问，问就是黑马程序员，成就it hei~ma~

# java_socket知识笔记

## 1- ServerSocket 多线程处理连接
accept 方法，在未接受到连接请求时阻塞，连接成功后
getRemoteSocketAddress()返回此套接字连接的端点的地址，如果未连接则返回 null
每次连接都创建一个新的线程去处理，就可以处理多个连接了
```java
ServerSocket ss = new ServerSocket(8080); // 监听指定端口
System.out.println("server is running...");
while (true){
    Socket sock = ss.accept();
    System.out.println("connected from " + sock.getRemoteSocketAddress());
    Thread t = new Handler(sock);
    t.start();
}
```

## 2- 数据流处理 Input和OutputStream

> 对InputSteam和OutputStream的详解
> https://www.liaoxuefeng.com/wiki/1252599548343744/1298069163343905

**流程**

输入输出流
```java
InputStream input = this.sock.getInputStream()
OutputStream output = this.sock.getOutputStream()
```
缓冲区
```java
BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
```
reader和writer

- 循环，一行一行读入
```java
for (;;) {
    String header = reader.readLine();
    if (header.isEmpty()) { // 读取到空行时, HTTP Header读取完毕
        break;
    }
    System.out.println(header);
}
```
- 将html文件通过http协议的形式发出
```java
//读取html文件，转换为字符串
BufferedReader br = new BufferedReader(new FileReader("http/html/a.html"));
StringBuilder data = new StringBuilder();
String line = null;
while ((line = br.readLine()) != null){
    data.append(line);
}
br.close();
int length = data.toString().getBytes(StandardCharsets.UTF_8).length;

writer.write("HTTP/1.1 200 OK\r\n");
writer.write("Connection: keep-alive\r\n");
writer.write("Content-Type: text/html\r\n");
writer.write("Content-Length: " + length + "\r\n");
writer.write("\r\n"); // 空行标识Header和Body的分隔
writer.write(data.toString());
writer.flush();
```

## 3- 内网穿透：将web服务器公开到Internet
使用cpolar（之前远程桌面使用过，不多提了），此处使用的是http服务器功能
> 见如下链接   1.3-将web服务器公开到Internet
> https://www.cpolar.com/docs

输入
```shell
cpolar http 8080
```
显示如下
```shell
cpolar by @bestexpresser                                                                                (Ctrl+C to quit)                                                                                                                        Tunnel Status       online
Account             QiQi (Plan: Free)
Version             2.92/2.96
Web Interface       127.0.0.1:4042
Forwarding          http://44ad009e.cpolar.top -> http://localhost:8080
Forwarding          https://44ad009e.cpolar.top -> http://localhost:8080
# Conn              0
Avg Conn Time       0.00ms         
```
点击其中的网址 http://44ad009e.cpolar.top 即可

