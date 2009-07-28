<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>
        <table border="0" width="100%" cellpadding="10">
            <tbody>
                <tr>
                    <td width="100%" valign="top">
                        <img src="connect.gif" width="650" height="250" alt="connect"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <h2>Please sign in</h2>
                        <br>
                        <form name="authn" action="authenticate.jsp" method="POST">

                            Username: <input type="text" name="username" value="" size="25" /><br><br>
                            Password: <input type="password" name="password" value="" size="25" /><br><br><br>

                            <input type="submit" value="Log In"/>
                            <input type="reset" value="Reset"/>
                            <br><br>
                        </form>
                        <br><br>
                    </td>
                </tr>
            </tbody>
        </table>
    </body>

</html>
