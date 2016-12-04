<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Admin Page</title>
</head>
<body>
<a href="/">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateAccount">create_account</a>
<a href="/DeleteAccount">delete_account</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>
<a href="/Admin">admin</a>
<h1>Admin Commands</h1>
<form name="AdminForm" method="post" action="/Admin">
    <div style="color: #FF0000;">${errorMessage}</div>
    Lock user: <input type="text" name="lockUsername"/> <br/>
    <input type="submit" name="submitButton" value="lock"/> <br/> <br/>
    Unlock user: <input type="text" name="unlockUsername"/> <br/>
    <input type="submit" name="submitButton" value="unlock"/>
</form>
</body>
</html>