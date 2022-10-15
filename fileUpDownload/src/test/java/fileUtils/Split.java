package fileUtils;

import org.junit.Test;
import java.io.*;

public class Split {
    String fileName;
    int splitNum;
    String[] fileSplitNames;  //0-filename.filetype ... i-filename.filetype
    String GoalFileDirectory;
    /**
     * 文件分割的方法
     * Split(".\\out\\Server1\\file.mp4", 1, ".\\out\\Server2");
     * 另外，会在目标位置建立与文件名同名的新目录file.mp4
     * @param SrcFilePath        指定分割的文件路径
     * @param SingleGoalFileSize 分割文件的大小（单位MB, 另外1MB = 1024kb
     * @param GoalFileDirectory  分割之后的路径
     */
    public Split(String SrcFilePath, int SingleGoalFileSize, String GoalFileDirectory) {
        int MB_byte = 1024*1024; //1MB = 1024kb = 1024*1024byte
        //SingleGoalFileSize 单位：MB ，校验路径和目录
        if ("".equals(SrcFilePath) || SrcFilePath == null || "".equals(GoalFileDirectory) || GoalFileDirectory == null) {
            System.out.println("wrong path!");
            return;
        }

        File SrcFile = new File(SrcFilePath); //新建文件
        this.fileName=SrcFile.getName();//源文件的文件名
        long SrcFileSize = SrcFile.length();//源文件的大小
        long SingleFileSize = (long) MB_byte * SingleGoalFileSize;//分割后的单个文件大小(单位字节)

        GoalFileDirectory=GoalFileDirectory+File.separator+fileName;
        this.GoalFileDirectory=GoalFileDirectory + File.separator;
        file_trans.mkdirs(GoalFileDirectory);//为目标路径建立目录

        this.splitNum = (int) ((SrcFileSize+SingleFileSize-1) / SingleFileSize); //分割后的数目：源文件大小/目标文件大小  【注意，是向上取整
        this.fileSplitNames = new String[this.splitNum];

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        byte[] bytes = new byte[MB_byte];//每次读取文件的大小
        int len = -1;

        try {
            fis = new FileInputStream(SrcFilePath); //新建输入流对象
            bis = new BufferedInputStream(fis);

            for (int i = 0; i < this.splitNum; i++) {
                //分割后单个文件名
                this.fileSplitNames[i] = i+"-"+this.fileName;
                //分割后的单个文件完整路径名
                String CompleteSingleGoalFilePath = GoalFileDirectory + File.separator + this.fileSplitNames[i];
                FileOutputStream fos = new FileOutputStream(CompleteSingleGoalFilePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos); //包装
                int count = 0;
                while ((len = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, len);//从源文件读取规定大小的字节数写入到单个目标文件中
                    count += len;
                    if (count >= SingleFileSize)
                        break;
                }
                bos.flush();
                bos.close();
                fos.close();
            }
            System.out.println("分割成功!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }

                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileName(){return this.fileName;}
    public int getSplitNum(){return this.splitNum;}
    public String[] getFileSplitNames(){return this.fileSplitNames;}
    public String getGoalFileDirectory(){return this.GoalFileDirectory;}

    public static void main(String[] args) {
        //Split("D:\\Study\\JavaWeb\\day08-HTTP_Tomcat_Servlet\\java_web-work\\fileUpDownload\\file.png", 1, "D:\\Study\\JavaWeb\\day08-HTTP_Tomcat_Servlet\\java_web-work\\fileUpDownload\\out");//
        Split splitFile = new Split(".\\fileUpDownload\\file.png", 1, ".\\fileUpDownload\\out\\Server0");
        for (String str:splitFile.fileSplitNames) System.out.println(str);

        //SrcFilePath 指定分割的文件路径 SingleGoalFileSize 分割文件的个数 GoalFileDirectory 分割完成之后的路径
    }

}