<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>


<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul> <%-- 가입한 회원 정보 조회  --%>
    <li>id=${member.id}</li>
    <li>username=${member.username}</li>
    <li>age=${member.age}</li>
<%--    <li> id=<%=((Member)request.getAttribute("member")).getId()%> </li>--%>
<%--    <li> id=<%=((Member)request.getAttribute("member")).getUsername()%> </li>--%>
<%--    <li> id=<%=((Member)request.getAttribute("member")).getAge()%> </li>--%>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
