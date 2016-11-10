<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Delete User</title>
</head>
<body>
	<h1>Deletes a User</h1>
	<form name="deleteUserForm" method="post" action="DeleteUser">
		<div style="color: #FF0000;">${errorMessage}</div>
		Username: <input type="text" name="username" /> <br /> <input
			type="submit" value="Submit" />
	</form>
</body>
</html>