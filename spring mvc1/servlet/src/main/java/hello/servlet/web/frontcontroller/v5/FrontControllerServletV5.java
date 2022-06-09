package hello.servlet.web.frontcontroller.v5;


import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")   //서블릿 이름과 서블릿 url 설정
public class FrontControllerServletV5 extends HttpServlet {


    private final Map<String, Object> handlerMappingMap = new HashMap<>();          // 핸들러 매핑정보를 담는 공간
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();       //핸들러 어댑터를 담는 공간


    //서블릿 생성자 : 핸들러 매핑정보와 핸들러 어댑터 자동 생성
    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }


    //핸들러 매핑정보 추가
    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        //V4 추가
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }


    //핸들러 어댑터 목록 추가
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }




    @Override
    //서블릿 url에 접속될시 자동 실행되는 함수
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //핸들러(컨트롤러) 조회 : uri에 맞는 핸들러를 반환 받음.
        Object handler = getHandler(request);    //ex, MemberFormControllerV3() 반환

        //핸들러(컨트롤러)가 없다면 에러코드 반환
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //핸들러(컨트롤러) 어댑터 조회 : 핸들러(컨트롤러)로 어댑터 조회
        MyHandlerAdapter adapter = getHandlerAdapter(handler);  //ex, MemberFormControllerV3()가 ControllerV3 타입이 맞는지 확인 후 맞으면 ControllerV3HandlerAdapter 반환

        //핸들러 어댑터에서 핸들러(컨트롤러) 호출 후 ModelView 반환
        ModelView mv = adapter.handle(request, response, handler);   //ex, ControllerV3HandlerAdapter.handle()  ==>  MemberFormControllerV3.process() 수행 후 ModelView 반환

        //논리적인 뷰 이름을 물리적인 뷰 경로로 변환
        String viewName = mv.getViewName();   //ModelView에서 논리적인 view 이름을 꺼냄
        MyView view = viewResolver(viewName);  //전체 뷰 경로를 자긴 Myview 객체 생성

        //회원정보가 담긴 model과 requset,response 정보로 render()함수를 호출하여 jsp로 forward
        view.render(mv.getModel(), request, response);

    }





    //핸들러 조회 : 핸들러 매핑정보를 통해 핸들러를 찾는다.
    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();    //uri를 불러와
        return handlerMappingMap.get(requestURI);       //uri에 맞는 핸드러를 가진 map을 반환
    }




    //핸들러 어댑터 조회 : 핸들러를 통해 해당 어대버 조회
    private MyHandlerAdapter getHandlerAdapter(Object handler) {

        //MemberFormControllerV4
        for (MyHandlerAdapter adapter : handlerAdapters) {   //handlerAdapters에서 adapter를 하나씩 빼와서, ex, adapter가 ControllerV3HandlerAdapter  이면
            if (adapter.supports(handler)) {         //핸들러(컨트롤러)가 컨트롤러 인터페이스와 맞는지 확인,     ex, MemberFormControllerV3가 ControllerV3 타입이 맞는지 확인 후 맞으면
                return adapter;                     //해당 어댑터를 반환,  ex, ControllerV3HandlerAdapter 반환
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }


    //논리적인 뷰 이름을 물리적인 뷰 경로로 변환
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }





}
