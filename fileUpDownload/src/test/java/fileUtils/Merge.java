package fileUtils;

import org.junit.Test;

import java.io.*;

public class Merge {
    /**
     * @param SingleFilePath
     * @param GoalFileDirectory
     */
    public static void Merge(String SingleFilePath[], String GoalFileDirectory) {
        if (GoalFileDirectory == null || "".equals(GoalFileDirectory)) {
            System.out.println("合并失败!");
            return;
        }

        int x1 = SingleFilePath[0].lastIndexOf("\\");
        int x2 = SingleFilePath[0].lastIndexOf(".");
        String GoalFileName = SingleFilePath[0].substring(x1, x2);

        file_trans.mkdirs(GoalFileDirectory);

        //合并后的完整路径名
        String CompleteGoalFilePath = GoalFileDirectory + File.separator + GoalFileName.substring(0, GoalFileName.lastIndexOf("-")) + SingleFilePath[0].substring(x2);

        byte bytes[] = new byte[1024 * 1024];//每次读取文件的大小
        int len = -1;

        FileOutputStream fos = null;//将数据合并到目标文件中
        BufferedOutputStream bos = null;//使用缓冲字节流写入数据
        try {
            fos = new FileOutputStream(CompleteGoalFilePath);
            bos = new BufferedOutputStream(fos);

            for (int i = 0; i < SingleFilePath.length; i++) {
                if (SingleFilePath[i] == null || "".equals(SingleFilePath)) {
                    System.exit(0);
                }

                FileInputStream fis = new FileInputStream(SingleFilePath[i]);//从分割后的文件读取数据
                BufferedInputStream bis = new BufferedInputStream(fis);//使用缓冲字节流读取数据
                while ((len = bis.read(bytes)) != -1)
                    bos.write(bytes, 0, len);

                bis.close();
                fis.close();
            }
            System.out.println("合并成功!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null)
                    bos.close();

                if (fos != null)
                    fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMerge() {
        String[] MergeFilePath = new String[63];//分割出来的文件个数
        for (int i = 0; i < MergeFilePath.length; i++)
            MergeFilePath[i] = new String(".\\out\\Server2\\file-" + i + ".mp4");//想要合并的文件路径
        Merge(MergeFilePath, ".\\out\\Server3");//合并之后保存的路径
    }
}