package hello.servlet.basic.response;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //응답 메시지에 HTTP 응답 코드 삽입
        response.setStatus(HttpServletResponse.SC_OK);  //SC_BAD_REQUEST 등 종류가 많음.

        //응답 메시지에 Header 정보 입력
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  //캐시 무효화
        response.setHeader("Pragma", "no-cache");   //캐시 무효화
        response.setHeader("my-header", "hello");   //header에 hello를 넣음.


        //[Header 편의 메서드]
        //content(response);  //content 정보 입력
        //cookie(response);   //쿠키 정보 입력
        redirect(response);  //리다이렉트 정보 입력


        //[Message Body에 들어감.]
        PrintWriter writer = response.getWriter();   //응답 메시지 웹 페이지에 출력
        writer.println("ok");
    }



    //1. content 편의 메서드
    private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2

        //(전) ContentType과 Encoding 설정
//        response.setHeader("Content-Type", "text/plain;charset=utf-8");

        //(후) ContentType과 Encoding 설정
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");


//        response.setContentLength(2); //(생략시 자동 생성)... 보통 생략함
    }



    //2. 쿠키 정보 입력 메서드
    private void cookie(HttpServletResponse response) {

        //(전)
        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");

        //(후)
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);      //600초
        response.addCookie(cookie);
    }


    //3. 리다이렉트 편의 메서드
    private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html

        //(전)
//        response.setStatus(HttpServletResponse.SC_FOUND); //302
//        response.setHeader("Location", "/basic/hello-form.html");

        //(후)
        response.sendRedirect("/basic/hello-form.html");
    }









}
