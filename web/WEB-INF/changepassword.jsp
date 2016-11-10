<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Change the Password</h1>
	<form name="ChangePasswordForm" method="post" action="ChangePassword">
		<div style="color: #FF0000;">${errorMessage}</div>
		Username: <input type="text" name="username" /> <br /> New password:
		<input type="password" name="password" /> <br /> Confirm New
		Password: <input type="password" name="password2" /> <br /> <input
			type="submit" value="Submit" />
	</form>
</body>
</html>