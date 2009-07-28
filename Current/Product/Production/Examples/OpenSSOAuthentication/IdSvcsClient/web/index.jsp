<%-- 
    Document   : index
    Created on : Apr 27, 2009, 2:49:22 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<!--        <h1>Hello World!</h1> -->
    <form name="authn" action="authenticate.jsp" method="POST" >
        Username: <input type="text" name="username" value="" size="25" /><br><br>
        Password: <input type="password" name="password" value="" size="25" /><br><br><br>

        Authenticate using Web Service (SOAP/WSDL) <input type="submit" value="WS" name="auth" /><br><br>
        Authenticate using REST <input type="submit" value="REST" name="auth" /><br>
        Enter REST URL: <input type="text" name="url" value="http://localhost:8080/opensso/identity" size="100" />
    </form>

    </body>
</html>
