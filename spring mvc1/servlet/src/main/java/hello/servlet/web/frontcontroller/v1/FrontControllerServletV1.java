package hello.servlet.web.frontcontroller.v1;


import hello.servlet.web.frontcontroller.v1.ControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")  //서블릿과 서블릿 url( *는 v1 이하 경로로 들어올시 호출된다는 뜻)  설정
public class FrontControllerServletV1 extends HttpServlet {

    //URL 매핑정보를 담을 ControllerV1을 담기 위해 map 선언
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    //서블릿 생성자 생성 : map에 컨트롤러들을 담음
    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");

        //요청 urI ("front-controller/v1/members/new-form")를 받아온다.
        String requestURI = request.getRequestURI();

        //map에서 해당 urI를 꺼내어 매핑된 컨트롤러를 찾는다.
        ControllerV1 controller = controllerMap.get(requestURI);  //다형성에 의해 ControllerV1 인터페이스로 받을 수 있는 것이다.
        //이거와 같다... ControllerV1 controller = MemberFormControllerV1()  //부모는 자식을 받을 수 있다...


        //해당 컨트롤러가 없으면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);  //404 에러 리턴
            return;
        }

        //해당 컨트롤러가 있으면 (ex,MemberFormControllerV1)의 process() 함수 호출~
        controller.process(request, response);   //단축키 : ctrl+alt+b..    다형성으로 인해 오버라이딩된 메서드 호출
    }




}