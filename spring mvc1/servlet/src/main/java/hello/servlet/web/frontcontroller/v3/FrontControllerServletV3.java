package hello.servlet.web.frontcontroller.v3;


import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //요청 urI ("front-controller/v3/members/new-form")를 받아온다.
        String requestURI = request.getRequestURI();

        //map에서 해당 urI("front-controller/v3/members/new-form")를 꺼내어 매핑된 컨트롤러를 찾는다.
        ControllerV3 controller = controllerMap.get(requestURI);

        //해당 컨트롤러가 없으면
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        //해당 컨트롤러가 있으면 (ex,MemberFormControllerV3)의 process() 함수 호출하여 논리적인 viewpath값을 얻어옴.
        Map<String, String> paramMap = createParamMap(request);  //모든 "파라미터", "파라미터 값"을 꺼내옴
        ModelView mv = controller.process(paramMap);          //논리적인 뷰 이름과 (+회원 정보 모델)이 담긴 ModelView객체를 가져옴
        //ex, 이거와 같다... ModelView mv = MemberFormControllerV2().process(paramMap)

        String viewName = mv.getViewName();   //ModelView 객체에서 논리적인 뷰 이름을 가져옴
        MyView view = viewResolver(viewName);   //논리적 뷰이름을  물리적인 뷰 이름으로 변경한 MyView 객체 생성

        //물리적인 view경로가 담긴 MyView객체의 render()함수를 호출하여 jsp로 forward
        view.render(mv.getModel(), request, response);


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
