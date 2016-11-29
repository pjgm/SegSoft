<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>FriendList</title>
</head>
<body>
<a href="/main/webapp">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateUser">create_user</a>
<a href="/DeleteUser">delete_user</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>
<h1>Add new Friend</h1>

<form name="FriendsForm" method="post" action="Friends">
    <div style="color: #FF0000;">${errorMessage}</div>
    Username: <input type="text" name="username"/> <br/>
    <input type="submit" value="Submit"/>

<h1>Friend List</h1>

    <table  class="table table-striped">
        <thead>
        <tr>
            <td>Name</td>
            <td>Email</td>
            <td>Phone</td>
        </tr>
        </thead>
                <forEach var="account" items="${friendlist}">
                    <tr>
                        <td><a href="/User/${account.name}">${account.name}</a></td>
                        <td>${account.emai}</td>
                        <td>${account.phone}</td>
                    </tr>
                </forEach>
            </table>

</form>
</body>
</html>
