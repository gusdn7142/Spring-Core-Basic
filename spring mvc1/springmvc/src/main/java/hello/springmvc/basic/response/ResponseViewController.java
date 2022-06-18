package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello")  //ModelAndView에
                .addObject("data", "hello!");
        return mav;
    }


    @ResponseBody
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        return "response/hello";  //view의 논리 이름 반환
    }



    @RequestMapping("/response/hello")  //컨트롤러와 뷰의 논리적 경로가 같은 경우
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }




}
