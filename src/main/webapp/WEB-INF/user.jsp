
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User page</title>
</head>
<body>

<a href="/main/webapp">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateUser">create_user</a>
<a href="/DeleteUser">delete_user</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>

<br><br>

<div style="color: RED;">${errorMessage}</div>
${msg}

</body>
</html>
