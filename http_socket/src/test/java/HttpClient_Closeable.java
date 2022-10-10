import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient_Closeable {
    @Test
    public void testGetWebsite() throws Exception {
        //1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2. 创建HttpGet请求，并进行相关设置
        //HttpGet httpGet = new HttpGet("https://www.kugou.com/?username==java");
        HttpGet httpGet = new HttpGet("https://www.tongji.edu.cn");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");

        //3.发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.判断响应状态码并获取响应数据
        if (response.getStatusLine().getStatusCode() == 200) { //200表示响应成功
            String html = EntityUtils.toString(response.getEntity(), "GBK");
            System.out.println(html);
        }

        //5.关闭资源
        httpClient.close();
        response.close();

    }

    @Test
    public void testGetHref() throws Exception {
        //1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2. 创建HttpGet请求，并进行相关设置
        //HttpGet httpGet = new HttpGet("https://www.kugou.com/?username==java");
        HttpGet httpGet = new HttpGet("https://www.tongji.edu.cn");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");

        //3.发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.判断响应状态码并获取响应数据
        if (response.getStatusLine().getStatusCode() == 200) { //200表示响应成功
            String html = EntityUtils.toString(response.getEntity(), "GBK");
            //System.out.println(html);
            String[] html_split = html.split("\\r\\n");
            getHrefFromHtml(html_split);
        }

        //5.关闭资源
        httpClient.close();
        response.close();
    }

    @Test
    public void hw_main() throws IOException {
        String URLs[] = new String[]{
                "https://www.tongji.edu.cn",
                "https://www.fudan.edu.cn",
                "https://www.sjtu.edu.cn",
                "https://www.mit.edu",
        };
        //1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
       /* for(int i = 0; i< URLs.length;i++){
            System.out.println("-----------------------"+URLs[i]+"-----------------------------");

            //2. 创建HttpGet请求，并进行相关设置
            //HttpGet httpGet = new HttpGet("https://www.kugou.com/?username==java");
            HttpGet httpGet = new HttpGet(URLs[i]);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");

            //3.发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);

            //4.判断响应状态码并获取响应数据
            if (response.getStatusLine().getStatusCode() == 200) { //200表示响应成功
                String html = EntityUtils.toString(response.getEntity(), "GBK");
                //System.out.println(html);
                String[] html_split = html.split("\\r\\n");
                ArrayList<String> hrefList =  getHrefFromHtml(html_split);  // 得到这个url里的所有超链接

                for (String s : hrefList) System.out.println(s);
            }
            response.close();
        }*/
        for (String url: URLs) {
            HttpClient(httpClient, url);
        }
        //5.关闭资源
        httpClient.close();

    }
    private void HttpClient(CloseableHttpClient httpClient, String url) throws IOException {
        System.out.println("-----------------------"+url+"-----------------------------");

        //2. 创建HttpGet请求，并进行相关设置
        //HttpGet httpGet = new HttpGet("https://www.kugou.com/?username==java");
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");

        //3.发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4.判断响应状态码并获取响应数据
        if (response.getStatusLine().getStatusCode() == 200) { //200表示响应成功
            String html = EntityUtils.toString(response.getEntity(), "GBK");
            String[] html_split = html.split("\\R");  // 本来是一个字符串，分成不同的行，注意\\R表示所有换行符
            ArrayList<String> hrefList =  getHrefFromHtml(html_split);  // 得到这个url里的所有超链接

            for (String s : hrefList) System.out.println(s);
        }
        response.close();
    }
    @Test
    public void getHostFromUrl() throws MalformedURLException {
        URL url = new URL("https://houqin.sjtu.edu.cn/service.php?cid=26");
        System.out.println(url.getHost());
        judge_href("https://vs.sjtu.edu.cn/", "https://houqin.sjtu.edu.cn/service.php?cid=26");
    }

    /*参数：超链接href， 父网站url
    * 匹配：相同子域名数量<=2的超链接
    * */
    private boolean judge_href(String href, String string_url) throws MalformedURLException {
        /*要求该外部url域名不同于当前机构，即要求域名url最右后缀至多有1个
        或2个相同的子域名，例如同济图书馆www.lib.tongji.edu.cn与www.tongji.edu.cn共享三
        个相同的最右后缀子域名cn、tongji和edu，则不符合要求、不能作为外部地址ur*/
        //首先解析出url中的域名
        URL url = new URL(string_url);
        String host = url.getHost();  //得到url中的域名
        String[] match_items = host.split("\\.");  //得到子域名的数组
        int i = 0;
        for (int j = 1; j<match_items.length;j++) {  // 根据作业要求，除了第一个以外，要求相同子域名不能超过2个
            if (href.contains(match_items[j])) {
                i++;
            }
        }
        return i <= 2;
    }

    private ArrayList<String> getHrefFromHtml(String[] html_split) {
        String rs = null;
        ArrayList<String> hrefList = new ArrayList();

        for (String str : html_split) {
            Pattern pattern = Pattern.compile("<a href=\"(.*?)\"");    //识别这一行是否符合网页的格式
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                rs=matcher.group(1);
                if (rs.indexOf("http") != -1) {  //带http的为URL
                    if (rs != null)
                        hrefList.add(rs);
                }
            }

            if(hrefList.size() >= 6){
                return hrefList;
            }
        }
        return hrefList;
    }


}