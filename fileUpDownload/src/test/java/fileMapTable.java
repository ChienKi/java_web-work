import fileUtils.Split;
import fileUtils.file_trans;
import org.junit.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;

public class fileMapTable implements Serializable{
    /**
     * 结构为
     * Server1:2-file.png, 5-file.png
     * Server2:0-file.png
     * Server3:1-file.png, 3-file.png, 4-file.png
     */
    public String fileName; // eg. file.png
    public HashMap<String, LinkedList<String>> mapTable;

    /**
     * 生成server和分割后文件的hashmap，并序列化输出到分割后的文件夹
     * @param fileSplit
     * @param server_metas
     */
    public fileMapTable(Split fileSplit, server_meta[] server_metas){
        this.fileName=fileSplit.getFileName();
        this.mapTable = new HashMap<>();
        for (server_meta server:server_metas) this.addServer(server.serverName);

        Random r = new Random();
        for (String splitFileName : fileSplit.getFileSplitNames()) {
            int serverIndex = r.nextInt(server_metas.length);
            this.addFile2Server(server_metas[serverIndex].serverName,splitFileName);
        }

        this.serialize(fileSplit.getGoalFileDirectory());
    }
    public boolean addServer(String serverName){
        if(mapTable.containsKey(serverName)){
            return false;
        }
        mapTable.put(serverName, new LinkedList<>());
        return true;
    }
    public boolean addFile2Server(String serverName, String fileName){
        if(addServer(serverName)){
            System.out.println("this Server not in mapTable, auto add:"+serverName);
        }
        mapTable.get(serverName).add(fileName);
        return true;
    }

    /**
     * 序列化
     * @param outputDir 包含hashmap.ser的目录
     */
    public void serialize(String outputDir){
        file_trans.mkdirsForParentFile(outputDir);
        try
        {
            FileOutputStream fos =
                    new FileOutputStream(outputDir+fileName+".fileMapTable.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
            System.out.printf("Serialized HashMap data is saved in " + outputDir + fileName+".fileMapTable.ser");
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * 通过反序列化生成
     * @param inputPath 包含文件名和目录的完整路径
     */
    public static fileMapTable deserialize(String inputPath){
        try
        {
            FileInputStream fis = new FileInputStream(inputPath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            fileMapTable fMT = (fileMapTable) ois.readObject();
            ois.close();
            fis.close();
            return fMT;
        }catch(IOException ioe)
        {
            ioe.printStackTrace();
            return null;
        } catch(ClassNotFoundException c)
        {
            System.out.println("Class not found");
            c.printStackTrace();
            return null;
        }
    }
    @Test
    public void testMapSerialized(){
        //testMapGenerate();
        //serialize();
        //deserialize();
        System.out.println("stop");
    }
    @Test
    public void testMapGenerate(){
        addServer("Server1");
        addFile2Server("Server2", "file-0.png");
        addFile2Server("Server1", "file-1.png");
        addFile2Server("Server2", "file-3.png");
        System.out.println("ok");
    }
}
