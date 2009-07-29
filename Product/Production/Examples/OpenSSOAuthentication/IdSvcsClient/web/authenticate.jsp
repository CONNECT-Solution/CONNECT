<%-- 
    Document   : authenticate
    Created on : Apr 27, 2009, 3:05:27 PM
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
<%

    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String type = request.getParameter("auth");
    String url = request.getParameter("url");
    String token = null;

%>

<%-- start web service invocation --%><hr/>
<%
    try
    {
        if (type.equals("WS"))
        {
            com.sun.identity.idsvcs.opensso.IdentityServicesImplService service = new com.sun.identity.idsvcs.opensso.IdentityServicesImplService();
            com.sun.identity.idsvcs.opensso.IdentityServicesImpl port = service.getIdentityServicesImplPort();

            java.lang.String uri = "";
            com.sun.identity.idsvcs.opensso.Token result = port.authenticate(username, password, uri);
            out.println("<h2>Successful Authentication using " +
                        "Web Services (SOAP/WSDL) Result = '" + result + "'</h2>");
        }
        else        // Using REST
        {
            url += "/authenticate";
            java.net.URL iurl = new java.net.URL(url);
            java.net.URLConnection connection = iurl.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // Send POST output.
            //connection.setRequestMethod("POST"); - Not an option...
            java.io.DataOutputStream printout = new java.io.DataOutputStream(connection.getOutputStream());
            String content = "username=" + java.net.URLEncoder.encode(username) +
                             "&password=" + java.net.URLEncoder.encode(password);
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader((java.io.InputStream) connection.getContent()));
            out.println(" ,h2>Successful Authentication using REST</h2>");
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.println(line + "<br>");
                int index = line.indexOf("token");
                if (index != -1)
                {
                    token = line.substring(9);
                }
            }
        }
    }
    catch (Exception e)
    {
        try
        {
            e.printStackTrace(new java.io.PrintWriter(out));
        }
        catch (Exception e1)
        {
            // Ignore
        }
    }
%>
<%-- end web service invocation --%><hr/>
    </body>
</html>
