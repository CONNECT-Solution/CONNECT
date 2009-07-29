<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252" />
    <title>4 x 4 grid using div tags and css</title>
    <style type="text/css">
        .squarecontainerOriginal { 
            width: 450px;
            overflow:auto;
        }

       .squarecontainer { 
            overflow:auto;
        }
        .squares {
            float: left;
            width: 5em;
            height: 5em;
            margin: .5em;
            border: 1px solid black;
        }
        
    </style>
    <body>
        
        <h1>JSP Page</h1>
        
        <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
        --%>
        <!-- This is the first row-->
        <div class="squarecontainerOriginal">
        <div class="squarecontainer">
        <% 
            for (int i = 0; i < 10; i++) {%>
            <div class="squares"><%=i%></div>
        <%}%>
        </div>
        </div>
        
        <% int sel = 1;
            for (int i = 0; i < 10; i++) {%>
        <%if (i > 2) {%>
        <div style="width:10px;height:1px;border-top:1px solid #EFEFEF;border-left:1px solid #EFEFEF;background-color:green;"></div>
        <%} else {%>
        <div style="width:10px;height:1px;border-top:1px solid #EFEFEF;border-left:1px solid #EFEFEF;background-color:white;"></div>
        <%}%>
        <%}%>   
    </body>
</html>
