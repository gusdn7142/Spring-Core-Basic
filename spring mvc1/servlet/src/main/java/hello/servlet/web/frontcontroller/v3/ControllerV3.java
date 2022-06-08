package hello.servlet.web.frontcontroller.v3;


import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    //요청 파라미터 정보는 Java의 Map으로 활용
    ModelView process(Map<String, String> paramMap);

}
