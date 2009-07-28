<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="java.util.Locale"  %>
 

<%   String selectedLocale  = (String) session.getAttribute("selectedLocale");
 	
	Locale locale = Locale.US;

	if("English".equalsIgnoreCase(selectedLocale)) {
        locale = Locale.US;
 	} else if("German".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.GERMANY;
 	} else if("France".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.FRANCE;
 	} else if("Japanese".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.JAPANESE;
 	} else if("Chinese".equalsIgnoreCase(selectedLocale)) {
       locale = Locale.SIMPLIFIED_CHINESE;
 	} else {
       locale = Locale.US;
  	}
    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,locale);
    
    //ajax related variables
    double rand = java.lang.Math.random();
    String URI = request.getRequestURI();
    URI = URI.substring(1, URI.lastIndexOf("/"));

	
%>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
            <script type="text/javascript" src="scripts/edm.js"></script>
			<script>
				//not used in dashboard, but since its require in the script we fake it
				var editIndexid = ""; 
 				var rand = "";
				function setRand(thisrand)  {
					rand = thisrand;
				}
			</script>

        </head>
        <title><%=bundle.getString("error_500_title")%></title>  
        <body>
                        
            <div id="content">  <!-- Main content -->
            <table border="0" cellpadding="3" cellspacing="1">
                <tr>
                    <td> 
                        <div class="errorHeadMessage"><%=bundle.getString("error_500_title")%> </div> 
                        <div id="errorSummary" class="errorSummary"> 
                            
                            <p>
                               <%=bundle.getString("error_500_text")%>&nbsp;<%=bundle.getString("contact_system_admin_text")%>&nbsp;
                                 <a title="<%=bundle.getString("error_500_login_again_text")%>" href="login.jsf"><%=bundle.getString("error_500_login_again_text")%></a>	
                            </p>
                        </div>
                        <div id="errorDiv"></div>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
            
        </body>
    </html>
    
	<%  //invalidate the session and logut the user to start over once again
	    //request.setAttribute("Logout", "LoggedOut");
       // request.getSession().invalidate(); 
             
	%>
