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
import java.util.List;

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

    @PostMapping("/upload")
    public String index(@RequestParam("files") MultipartFile[] files,Integer idx,
                        RedirectAttributes redirectAttributes) {
        MultipartFile file = files[0];
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "index";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get("/Users/geyuxu/test/" + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "index";
    }
}