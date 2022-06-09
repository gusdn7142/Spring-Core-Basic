package hello.servlet.web.frontcontroller;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MyView {  //각 컨트롤러의 로직을 여기서 수행

    //viewPath 선언
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }


    //기존의 Controller의 forward과정을 프론트 컨트롤러에서 render() 함수 안에서 진행
    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //jsp로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);


    }



    //v3부터 적용됨
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }


    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }




}