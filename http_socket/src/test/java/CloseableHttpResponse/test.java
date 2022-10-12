package CloseableHttpResponse;

import org.junit.Test;

public class test
{

    @Test
    public void testRecursive(){
        recursive_model(null, null, 1);
    }

    /**
     *
     * @param super_url
     * @param url
     * @param level 从1开始，level==5（大于需要的跳数）结束
     */
    public void recursive_model(String super_url, String url, int level){
        if (level==5)return;
        String href = null;
        for(int i=0; i<6;i++){
            System.out.println("level:"+level+"num:"+i);
            recursive_model(url, href,level+1);
        }
    }

}
