package CloseableHttpResponse;

import graph.Edge;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpUtils {
    /**
     * 请求客户端
     */
    //TODO: static未new（httpClient = HttpClients.createDefault()）时，被其他static方法（processRequest）调用怎么办
    private static CloseableHttpClient httpClient;
    /**
     * http请求的返回码
     */
    private static final int RESPONSE_SUCCESS_CODE = 200;

    /**
     * 1. 创建HttpClient对象
     */
    public HttpUtils(){
        //1.创建HttpClient对象
        httpClient = HttpClients.createDefault();
    }

    /**
     * 2. 执行GET请求
     * @param url 请求地址
     * @return true：请求成功，false：请求失败
     */
    public static CloseableHttpResponse processGet(String url){
        //2. 创建HttpGet请求，并进行相关设置
        HttpGet httpGet = new HttpGet(url);
        // 设置httpclient请求超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                // 从connect Manager获取connect超时100ms
                .setConnectionRequestTimeout(100)
                // 请求超时400ms
                .setSocketTimeout(400)
                // 默认连接超时200ms
                .setConnectTimeout(200)
                .build();
        // 具体执行请求
        httpGet.setConfig(requestConfig);
        /** 这个setHeader好像可有可无？
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");
        */
        return processRequest(httpGet);
    }

    /**
     * 3.发起请求+4.判断响应状态码并获取响应数据
     * @param requestBase 请求方法 get、post、head
     * @return true：请求成功，false：请求失败
     */
    private static CloseableHttpResponse processRequest(HttpRequestBase requestBase){
        // 执行供应商接口请求
        //3.发起请求
        try (CloseableHttpResponse response = httpClient.execute(requestBase)) {
            //4.判断响应状态码并获取响应数据
            int code = response.getStatusLine().getStatusCode();
            if (code > RESPONSE_SUCCESS_CODE) {
                return null;
            }
            return response;
        } catch (IOException e) {
            return null;
        }
    }
    public static void close (){
        //5.关闭资源
        try {
            httpClient.close();
        } catch (IOException e) {
        }
    }


}
