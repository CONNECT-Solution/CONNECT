<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Authenticate</title>
    </head>
    <body>
        <img src="connect.gif" width="650" height="250" alt="connect"/>

        <%
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String token = null;

        if (username == null || username.length() == 0 ||
                password == null || password.length() == 0) {
            out.println("<h2>Either user name or password is empty</h2><br>");
        } else {
            try {
                com.sun.identity.idsvcs.opensso.IdentityServicesImplService service = new com.sun.identity.idsvcs.opensso.IdentityServicesImplService();
                com.sun.identity.idsvcs.opensso.IdentityServicesImpl port = service.getIdentityServicesImplPort();

                String uri = "";
                com.sun.identity.idsvcs.opensso.Token result = port.authenticate(username, password, uri);
                out.println("<h2>Please select an activity below:</h2>");
                token = result.getId();
                //out.println("Token=" + token);
            } catch (Exception ex) {
                //ex.printStackTrace(new java.io.PrintWriter(out));
            }
        }

        if (token == null || token.length() == 0) {
            out.println("<h2>Invalid Username or Password:</h2>");
        %>
        <hr>
        <form name="login" action="login.jsp" method="POST">
            <input type="submit" value="Return To Login" />
        </form>

        <%} else {
        %>
        <hr>
        <form name="patientInfo" action="patientInfo.jsp" method="POST">
            <input type="hidden" name="token" value=<%= token%> />
            <input type="submit" value="Define Patient Authorization" />
        </form>
        <%        }
        %>
        <hr/>

    </body>
</html>
