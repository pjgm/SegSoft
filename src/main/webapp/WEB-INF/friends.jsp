<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Friend List</title>
    <style>
        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }
        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }
        tr:nth-child(even) {
            background-color: #dddddd;
        }
    </style>
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
<h1>Add a new Friend</h1>
<form name="FriendsForm" method="post" action="Friends">
    <div style="color: #FF0000;">${errorMessage}</div>
    Username: <input type="text" name="usernameAdd"/>
    <input type="submit" name="submitButton2" value="add"/> <br/>
<h1>Remove a Friend</h1>
    <div style="color: #FF0000;">${errorMessage2}</div>
    Username: <input type="text" name="usernameRemove"/>
    <input type="submit" name="submitButton2" value="remove"/> <br/>
<h1>Friend Requests</h1>
${pendingFriendList}
<h1>Friend List</h1>
${friendlist}
</form>
</body>
</html>