<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${username} Profile</title>
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

<a href="/main/webapp">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateUser">create_user</a>
<a href="/DeleteUser">delete_user</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>

${info}
<div style="color: #FF0000;">${errorMessage}</div>
<form name="MyProfileForm" method="post" action="/MyProfile">
    Change phone: <input type="text" name="phone"/> <input type="submit" name="submitButton" value="phone"/> <br/>
    Change email: <input type="text" name="email"/> <input type="submit" name="submitButton" value="email"/> <br/>
    Change bio: <input type="text" name="bio"/> <input type="submit" name="submitButton" value="bio"/> <br/>
    Change private info: <input type="text" name="secretInfo"/> <input type="submit" name="submitButton" value="private"/>
<br/>
</form>

</body>
</html>
