<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Create Account</title>
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
<h1>Create a new Account</h1>
<form name="createAccountForm" method="post" action="CreateAccount">
    <div style="color: #FF0000;">${errorMessage}</div>
    Username: <input type="text" name="username"/> <br/>
    Password: <input type="password" name="password"/> <br/>
    Confirm Password: <input type="password" name="password2"/> <br/>
    Email: <input type="text" name="email"/> <br/>
    Phone Number: <input type="text" name="phone"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>