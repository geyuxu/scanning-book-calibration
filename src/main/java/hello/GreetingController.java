package hello;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static hello.Calibra.jpgPathList;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/img")
    public void getImg(int page,int index, HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        byte[] imgByte = new byte[1024];
        File file = new File(jpgPathList.get(page * index));
        if(!file.exists()) return;

        httpServletResponse.setContentType("image/jpeg");

        try {
            int imgIdx = (page-1) * 10 + index;
           FileInputStream fis = new FileInputStream(jpgPathList.get(imgIdx));
            OutputStream os = httpServletResponse.getOutputStream();
            while( fis.read(imgByte)!= -1){
                os.write(imgByte);
            }
            os.flush();
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/")
    public String index(@RequestParam(name="page", required=false, defaultValue="1") String page,Model model) {
        model.addAttribute("names", jpgPathList);
        model.addAttribute("page", page);
        return "index";
    }
}