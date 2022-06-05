<%@ page contentType="text/html;charset=UTF-8" language="java" %>   <%--jsp 파일 설정 --%>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%


    /* MemberRepository 객체 셍성 */
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");

    //요청으로 받은 파라미터 값 ("이름", "나이") 조회    + request, response는 자동 사용 가능 (jsp도 결국 서블릿으로 나중에 바뀐다))
    String username = request.getParameter("username");      //username 파라미터 값 조회
    int age = Integer.parseInt(request.getParameter("age")); //age 파라미터 값 조회


    //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
    Member member = new Member(username, age);
    memberRepository.save(member);

%>


<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<% out.println(member.getId());  %></li>            <%-- 회원 id 조회  --%>
    <li>username=<%=member.getUsername()%></li>  <%-- 회원 이름 조회  --%>
    <li>age=<%=member.getAge()%></li>             <%-- 회원 나이 조회  --%>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
