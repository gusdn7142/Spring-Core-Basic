<%@ page contentType="text/html;charset=UTF-8" language="java" %>    <%--jsp 파일 설정 --%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/jsp/members/save.jsp" method="post">   <%--jsp URL 설정 --%>
    username: <input type="text" name="username" />
    age:      <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
