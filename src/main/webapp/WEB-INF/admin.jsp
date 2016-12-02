<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
<form name="AdminForm" method="post" action="/Admin">
    <div style="color: #FF0000;">${errorMessage}</div>
    Lock user: <input type="text" name="lockUsername"/> <br/>
    <input type="submit" name="submitButton" value="lock"/> <br/> <br/>
    Unlock user: <input type="text" name="unlockUsername"/> <br/>
    <input type="submit" name="submitButton" value="unlock"/>
</form>
</body>
</html>