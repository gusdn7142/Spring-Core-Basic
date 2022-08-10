package hello.typeconverter.controller;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {


    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());

        model.addAttribute("form", form);
        return "formatter-form";
    }


    //"10,000" -> 10000
    //"2021-06-18 23:00:45" -> localDateTime
    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute Form form) {
        //생략 가능 : model.addAttribute("form", form);
        return "formatter-view";
    }




    @Data
    static class Form {
        @NumberFormat(pattern = "###,###")  //원하는 포맷 지정
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  //원하는 포맷 지정
        private LocalDateTime localDateTime;
    }








}
