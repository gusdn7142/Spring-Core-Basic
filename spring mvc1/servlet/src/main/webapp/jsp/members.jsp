<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%


    /* MemberRepository 객체 셍성 */
    MemberRepository memberRepository = MemberRepository.getInstance();

    //모든 회원 정보 조회
    List<Member> members = memberRepository.findAll();

%>






<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<a href="/index.html">메인</a>
<table>
    <thead>
    <th>id</th>
    <th>username</th>
    <th>age</th>
    </thead>
    <tbody>
    <%-- 동적 html 문자열 생성 --%>
    <%
        for (Member member : members) {
            out.write("    <tr>");
            out.write("        <td>" + member.getId() + "</td>");
            out.write("        <td>" + member.getUsername() + "</td>");
            out.write("        <td>" + member.getAge() + "</td>");
            out.write("    </tr>");
        }
    %>
    </tbody>
</table>

</body>
</html>