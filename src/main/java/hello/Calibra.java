package hello;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Calibra {
    public static final List<String> jpgPathList = new ArrayList<>(500);

    public void doSth(String path) {
        File file = new File(path);

        File[] array = file.listFiles();


        for(int i=0;i<array.length;i++){
            if(array[i].isFile()){
                String filePath = array[i].getPath();
                String fileExtName = filePath.substring(filePath.length()-4);
                if (".jpg".equals(fileExtName)){
                    jpgPathList.add(filePath);
                }
            }
        }
    }

}
