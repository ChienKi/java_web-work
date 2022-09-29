import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {
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
    public void testHomework() throws Exception{
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
            handleHTML_getHref(html_split);
        }

        //5.关闭资源
        httpClient.close();
        response.close();
    }

    @Test
    public void hw_main(){
        String URLs[] = new String[]{
                "www.tongji.edu.cn",
                "www.fudan.edu.cn",
                "www.sjtu.edu.cn",
                "www.mit.edu"
        };
    }

    private ArrayList<String> handleHTML_getHref(String[] html_split){
        String rs = null;
        ArrayList<String> hrefList = new ArrayList();

        for (String str: html_split) {
            Pattern pattern = Pattern.compile("<a href=(.*?)>");    //识别这一行是否符合网页的格式
            Matcher matcher = pattern.matcher(str);

            while (matcher.find()) {
                Pattern pattern1 = Pattern.compile("\"(.*?)\"");
                Matcher matcher1 = pattern1.matcher(matcher.group(1));
                if (matcher1.find()) {
                    rs = matcher1.group(1);      //将本行引号中的内容截取出来
                }
                if (rs.indexOf("http") != -1) {  //带http的为URL
                    if (rs != null)
                        hrefList.add(rs);
                }
            }
        }

        for (int i = 0; i < hrefList.size(); i++)
            System.out.println(hrefList.get(i));

        return hrefList;

    }

}