<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Change Password</title>
</head>
<body>
<a href="/">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateAccount">create_account</a>
<a href="/DeleteAccount">delete_account</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>
<h1>Change the Password</h1>
<form name="ChangePasswordForm" method="post" action="ChangePassword">
    <div style="color: RED;">${errorMessage}</div>
    New password:
    <input type="password" name="password"/> <br/> Confirm New
    Password:
    <input type="password" name="password2"/> <br/> <input
        type="submit" value="Submit"/>
</form>
</body>
</html>