package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static hello.Calibra.FILE_PATH;
import static hello.Calibra.jpgList;

@Controller
public class GreetingController {



    @GetMapping("/img")
    public void getImg(int page,int index, HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        //byte[] imgByte = new byte[1024];
        //File file = new File(jpgList.get(page * index));
        //if(!file.exists()) return;
        int idx = (page - 1) * 10 + index - 1;
        if(idx>=jpgList.size()){
            return;
        }
        byte[] imgByte = jpgList.get(idx);
        httpServletResponse.setContentType("image/jpeg");

        try {
           // int imgIdx = (page-1) * 10 + index;
          // FileInputStream fis = new FileInputStream(jpgList.get(imgIdx));
            OutputStream os = httpServletResponse.getOutputStream();
//            while( fis.read(imgByte)!= -1){
//                os.write(imgByte);
//            }
            os.write(imgByte);
            os.flush();
            os.close();
           // fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/")
    public String index(Integer page,Integer idx,Model model) {
        if(idx!=null){
            page = idx / 10 + 1;
        }
        if(page==null){
            page = 1;
        }
        model.addAttribute("page", page);
        return "index";
    }

    @PostMapping("/upload")
    public String index(@RequestParam("files") MultipartFile[] files,Integer page,int idx,
                        RedirectAttributes redirectAttributes,Model model) throws IOException {
        //MultipartFile file = files[0];
        if (files[0].isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "index";
        }

        List<byte[]> imgList = new ArrayList<>();
        for(MultipartFile file : files){
            byte[] bytes = file.getBytes();
            imgList.add(bytes);
        }
        jpgList.addAll(idx,imgList);
//        try {
//            // Get the file and save it somewhere
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get("/Users/geyuxu/test/" + file.getOriginalFilename());
//            Files.write(path, bytes);
//
//            redirectAttributes.addFlashAttribute("message",
//                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        this.save();
        if(page==null){
            page = 1;
        }
        model.addAttribute("page", page);
        return "index";
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String dateStr = sdf.format(new Date());

    @GetMapping("/save")
    public String save() throws IOException {


        File d = new File(FILE_PATH);
        String[] content = d.list();
        for(String name : content) {
            File temp = new File(FILE_PATH,name);

            if (!temp.delete()) {//直接删除文件
                System.err.println("Failed to delete " + name);
            }
        }

        Integer i = 0;
        for(byte[] bytes:jpgList){
            Path path = Paths.get(FILE_PATH+"/BOOKPAGE_"+dateStr+"_" + addZeroForNum(i.toString(),getNumLenght(jpgList.size())) + ".jpg");
            Files.write(path, bytes);
            i++;
        }

        return "index";
    }

    private static int getNumLenght(long num){
        num = num>0?num:-num;
        return String.valueOf(num).length();

    }
    public static String addZeroForNum(String str,int strLength) {
        int strLen =str.length();
        if (strLen <strLength) {
            while (strLen< strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
//    sb.append(str).append("0");//右补0
                str= sb.toString();
                strLen= str.length();
            }
        }

        return str;
    }

    @GetMapping("/removeImg")
    public String removeImg(Integer page,int idx,Model model) throws IOException {
        jpgList.remove(idx);
        this.save();

        if(page==null){
            page = 1;
        }
        model.addAttribute("page", page);
        return "index";
    }
}