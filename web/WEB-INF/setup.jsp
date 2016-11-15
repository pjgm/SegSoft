<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Initial Setup</title>
</head>
<body>
<h1>Initial Setup</h1>
<h2>Please enter a root password to setup the web application:</h2>
<form name="SetupForm" method="post" action="/Setup">
    <div style="color: #FF0000;">${errorMessage}</div>
    Password: <input type="password" name="password"/> <br/> <input
        type="submit" value="Submit"/>
</form>
</body>
</html>