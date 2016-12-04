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
<a href="/">home</a>
<a href="/MyProfile">my_profile</a>
<a href="/Friends">friends</a>
<a href="/CreateAccount">create_account</a>
<a href="/DeleteAccount">delete_account</a>
<a href="/ChangePassword">change_pwd</a>
<a href="/Logout">logout</a>
<a href="/Admin">admin</a>
${info}
<div style="color: #FF0000;">${errorMessage}</div>
<form name="MyProfileForm" method="post" action="/MyProfile">
    Change email: <input type="text" name="email"/> <input type="submit" name="submitButton" value="email"/>
    Change email privacy level: <input type="text" name="emailPL"/> <input type="submit" name="submitButton" value="emailpl"/> <br/>
    Change phone: <input type="text" name="phone"/> <input type="submit" name="submitButton" value="phone"/>
    Change phone privacy level: <input type="text" name="phonePL"/> <input type="submit" name="submitButton" value="phonepl"/> <br/>
    Change public info: <input type="text" name="publicInfo"/> <input type="submit" name="submitButton" value="public"/>
    Change public info privacy level: <input type="text" name="publicInfoPL"/> <input type="submit" name="submitButton" value="pipl"/> <br/>
    Change internal info: <input type="text" name="internalInfo"/> <input type="submit" name="submitButton" value="internal"/>
    Change internal info privacy level: <input type="text" name="internalInfoPL"/> <input type="submit" name="submitButton" value="iipl"/> <br/>
    Change secret info: <input type="text" name="secretInfo"/> <input type="submit" name="submitButton" value="secret"/>
    Change secret info privacy level: <input type="text" name="secretInfoPL"/> <input type="submit" name="submitButton" value="sipl"/>
<br/>
</form>
</body>
</html>