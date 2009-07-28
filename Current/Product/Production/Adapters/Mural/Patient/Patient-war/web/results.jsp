<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LoginHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <%
          LoginHandler loginHandler = new LoginHandler();
          NavigationHandler navigationHandler = new NavigationHandler();
          
          String retString  = loginHandler.initializeApplication();
 
          if("initializationfailed".equalsIgnoreCase(retString)) {
            // Navigate the user to the login page when initialization fails
			 request.getSession().invalidate(); 
             FacesContext.getCurrentInstance().getExternalContext().redirect("loginerror.jsf?error=" + (String) request.getAttribute(LoginHandler.FAIL_INITIALIZATION));

		  } else {
             ConfigManager.init();
		     ScreenObject screenObject = ConfigManager.getInstance().getInitialScreen();
             session.setAttribute("ScreenObject", screenObject);

             // Navigate the user as per user configuration in  edm.xml   
             String tagName  = navigationHandler.getTagNameByScreenId(screenObject.getID());

          
            if(tagName.equalsIgnoreCase("dashboard")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.jsf");
            } else  if(tagName.equalsIgnoreCase("duplicate-records")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("duplicaterecords.jsf");
            } else  if(tagName.equalsIgnoreCase("record-details")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("recorddetails.jsf");
            } else  if(tagName.equalsIgnoreCase("assumed-matches")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("assumedmatches.jsf");
            } else  if(tagName.equalsIgnoreCase("source-record")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("sourcerecords.jsf");
            } else  if(tagName.equalsIgnoreCase("reports")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("reports.jsf");
            } else  if(tagName.equalsIgnoreCase("transactions")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("transactions.jsf");
            } else  if(tagName.equalsIgnoreCase("audit-log")) {
              FacesContext.getCurrentInstance().getExternalContext().redirect("auditlog.jsf");
            }   
          } 
 
        %>
       
        
    </head>
    <body>
    <h1>JSP Page</h1>
    
    <table>
        <tr>
            <td>
            This is the results page from the login details when submit button was clicked.                            
            
            </td>
        </tr>
    </table>    

    
    </body>
</html>
