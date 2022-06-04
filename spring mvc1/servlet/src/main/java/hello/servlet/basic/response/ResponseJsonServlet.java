package hello.servlet.basic.response;


import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //Header의 Content-Type을 json으로 설정
        //Content-Type: application/json
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        //Response DTO인 HelloData 객체 생성 후 데이터 삽입
        HelloData helloData = new HelloData();
        helloData.setUsername("kim");
        helloData.setAge(20);

        //HelloData 객체를 JSON 형식으로 변경  ex, {"username":"kim", "age":20}
        String result = objectMapper.writeValueAsString(helloData);  //writeValueAsString() : 객체,배열,리스트,Map 등의 형식을 JSON 문자열 형식으로 변환

        response.getWriter().write(result);
//        PrintWriter writer = response.getWriter();
//        writer.println(result);

    }



}
