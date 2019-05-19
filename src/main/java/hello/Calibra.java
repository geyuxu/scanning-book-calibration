package hello;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class Calibra {
    //public static final List<String> jpgPathList = new ArrayList<>(500);
//
//    public void doSth(String path) {
//        File file = new File(path);
//
//        File[] array = file.listFiles();
//
//
//        for(int i=0;i<array.length;i++){
//            if(array[i].isFile()){
//                String filePath = array[i].getPath();
//                String fileExtName = filePath.substring(filePath.length()-4);
//                if (".jpg".equals(fileExtName)){
//                    jpgPathList.add(filePath);
//                }
//            }
//        }
//    }

    //public static final Map<String,byte[]> jpgMap = new HashMap<>();

    public static String FILE_PATH = "";

    public static final List<byte[]> jpgList = new ArrayList<>(500);

    public void doSth2(String path) throws IOException {
        FILE_PATH = path;

        File file = new File(path);

        File[] array = file.listFiles();

        Arrays.sort(array);

        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){
                String filePath = array[i].getPath();
                String fileExtName = filePath.substring(filePath.length()-4);
                byte[] b = new byte[(int)array[i].length()];
                InputStream in=new FileInputStream(array[i]);
                in.read(b);
                if (".jpg".equals(fileExtName)){
                    jpgList.add(b);
                }
            }
        }

        System.out.println("end");

        int pathIdx = 0;
        File p = null;
        String pDir = null;
        do{
            pathIdx++;
            pDir = FILE_PATH+"/out"+pathIdx;
            p = new File(pDir);
        }while (p.isDirectory() && p.exists());

        p.mkdir();
        FILE_PATH = pDir;
    }

}
