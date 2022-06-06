package hello.servlet.web.frontcontroller.v2;


import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV2", urlPatterns = "/front-controller/v2/*")  //서블릿 이름, 서블릿 url
public class FrontControllerServletV2 extends HttpServlet {

    //어떤 url이든 ControllerV2을 담기 위해 map 선언
    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    //서블릿 생성자 생성 : map에 컨트롤러들을 담음
    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //요청 urI ("front-controller/v2/members/new-form")를 받아온다.
        String requestURI = request.getRequestURI();

        //map에서 해당 urI를 꺼내어 매핑된 컨트롤러를 찾는다.
        ControllerV2 controller = controllerMap.get(requestURI);

        //해당 컨트롤러가 없으면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //해당 컨트롤러가 있으면 (ex,MemberFormControllerV2)의 process() 함수 호출하여 viewpath값을 얻어옴.
        MyView view = controller.process(request, response);

        //MyView객체의 render()함수를 호출하여 jsp로 forward
        view.render(request, response);
    }






}