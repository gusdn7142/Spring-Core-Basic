package hello.servlet.web.frontcontroller.v4;


import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();


    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //요청 urI ("front-controller/v3/members/new-form")를 받아온다.
        String requestURI = request.getRequestURI();

        //map에서 해당 urI("front-controller/v3/members/new-form")를 꺼내어 매핑된 컨트롤러를 찾는다.
        ControllerV4 controller = controllerMap.get(requestURI);

        //해당 컨트롤러가 없으면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);  //모든 "파라미터", "파라미터 값"을 꺼내옴

        //논리적인 뷰 이름과 회원 정보 모델을 가져와 논리적 view이름 추출
        Map<String, Object> model = new HashMap<>();
        String viewName = controller.process(paramMap, model);

        //논리적 view이름을 물리적 view 경로로 변환
        MyView view = viewResolver(viewName);

        //회원정보가 담긴 model과 requset,response 정보로 render()함수를 호출하여 jsp로 forward
        view.render(model, request, response);
    }






    //논리적인 뷰 이름을 물리적인 뷰 경로로 변환
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }


    //모든 파라미터 다꺼내서 paramMap에 넣음.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();  //"파라미터 명","파라미터 값"을 담을 수 있는 Map 생성

        //paramMap 객체("파라미터 명","파라미터 값") 에 모든 파라미터를 다 넣습니다.
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));

        //System.out.println(paramMap.get("username"));
        //System.out.println(paramMap.get("age"));
        return paramMap;
    }



}
