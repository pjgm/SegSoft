<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Page</title>
</head>
<body>
<a href="/">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateAccount">create_account</a>
<a href="/DeleteAccount">delete_account</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>
<br><br>
<div style="color: RED;">${errorMessage}</div>
${msg}
</body>
</html>