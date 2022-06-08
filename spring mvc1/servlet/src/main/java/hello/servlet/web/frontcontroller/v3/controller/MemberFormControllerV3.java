package hello.servlet.web.frontcontroller.v3.controller;


import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {


    @Override
    //뷰의 논리적인 이름이 담긴 Model 객체 리턴
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }



}
