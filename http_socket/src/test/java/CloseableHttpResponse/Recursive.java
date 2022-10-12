package CloseableHttpResponse;

import graph.Edge;
import graph.ListDGraph;
import org.apache.http.client.config.RequestConfig;
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
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Recursive {
    ListDGraph<String> websiteDG = new ListDGraph<String>();
    CloseableHttpClient httpClient;

    @Test
    public void hw_test2() {
        try {
            websiteDG.add(new URL("https://www.tongji.edu.cn").getHost());
            websiteDG.add(new URL("https://www.fudan.edu.cn").getHost());
            websiteDG.add(new URL("https://www.sjtu.edu.cn").getHost());
            websiteDG.add(new URL("https://www.mit.edu").getHost());
        } catch (MalformedURLException e) {
        }

        //1.创建HttpClient对象
        httpClient = HttpClients.createDefault();

        for (String url : websiteDG.getmVList()) {
            HttpGet_url("https://"+url, 1);
        }
        //5.关闭资源
        try {
            httpClient.close();
        } catch (IOException e) {
        }

        websiteDG.graphviz();
        //dot -Tpng DG.dot -o DG.png
    }

    /**
     *
     * @param url 注意，此url拥有https://，不是域名
     * @param level 递归深度
     */
    private void HttpGet_url(String url, int level) {
        if(level==5)return;
        try {
            //3.发起请求
            System.out.println("level:"+level + "----" + url + "-----------------------------");

            //2. 创建HttpGet请求，并进行相关设置
            HttpGet httpGet = new HttpGet(url);
            // 设置httpclient请求超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    /*// 默认连接超时100ms
                    .setConnectionRequestTimeout(100)
                    // 请求超时400ms
                    .setSocketTimeout(400)*/
                    // ?
                    .setConnectTimeout(500)
                    .build();
            // 具体执行请求
            httpGet.setConfig(requestConfig);
            //httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Mobile Safari/537.36 Edg/85.0.564.68");

            //3. 发起请求
            CloseableHttpResponse response = httpClient.execute(httpGet);


            //4.判断响应状态码并获取响应数据
            if (response.getStatusLine().getStatusCode() == 200) { //200表示响应成功
                String html = EntityUtils.toString(response.getEntity(), "GBK");
                String[] html_split = html.split("\\R");  // 本来是一个字符串，分成不同的行，注意\\R表示所有换行符

                for (String href : getHrefsNeedRecursive(url, html_split)) { // 得到这个url里的所需要递归的超链接
                    System.out.println(href);
                    HttpGet_url(href, level+1);
                }

            }
            response.close();
        } catch (IOException e) {
            System.out.println("HttpGetClient IOException");
        }
    }

    /**
     * 获取需要递归的hrefList
     * @param url
     * @param html_split
     * @return
     * @throws IOException
     */
    public ArrayList<String> getHrefsNeedRecursive(String url, String[] html_split) throws IOException {
        String href = null;
        ArrayList<String> hrefList = new ArrayList<String>();

        for (String str : html_split) {
            Pattern pattern = Pattern.compile("<a href=\"(.*?)\"");    //识别这一行是否符合网页的格式
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                href = matcher.group(1);
                // 判断条件
                //TODO: 域名相同怎么办
                if (href.contains("http") && judge_href(href, url)) {  //带http的为URL
                    String domain = new URL(href).getHost();
                    //需要先add再递归，因为edge要加到表里
                    int index = websiteDG.add(domain);
                    //TODO: url的域名
                    websiteDG.add(new Edge<String>(new URL(url).getHost(), domain));
                    if(index!=-1) {
                        hrefList.add(href);
                    }
                }
            }

            if (hrefList.size() >= 6) {
                return hrefList;
            }
        }
        return hrefList;
    }

    /*参数：超链接href， 父网站url
     * 匹配：相同子域名数量<=2的超链接
     * */
    private boolean judge_href(String href, String string_url) throws MalformedURLException {
        /*要求该外部url域名不同于当前机构，即要求域名url最右后缀至多有1个
        或2个相同的子域名，例如同济图书馆www.lib.tongji.edu.cn与www.tongji.edu.cn共享三
        个相同的最右后缀子域名cn、tongji和edu，则不符合要求、不能作为外部地址ur*/
        //首先解析出url中的域名
        String url_host = new URL(string_url).getHost();  //得到url中的域名
        String href_host = new URL(href).getHost();
        if(Objects.equals(url_host, href_host)){return false;}
        String[] match_items = url_host.split("\\.");  //得到子域名的数组
        int i = 0;
        for (int j = 1; j < match_items.length; j++) {  // 根据作业要求，除了第一个以外，要求相同子域名不能超过2个
            if (href.contains(match_items[j])) {
                i++;
            }
        }
        return i <= 2;
    }


}

