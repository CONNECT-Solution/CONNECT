<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="java.util.Locale"  %>
<%
    String selectedLocale  = (String) session.getAttribute("selectedLocale");
 
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
    %>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet">            
        </head>
        <title><%=bundle.getString("error_404_title")%></title>  
        <body>
                        
            <div id="content">  <!-- Main content -->
            <table border="0" cellpadding="3" cellspacing="1">
                <tr>
                    <td> 
                        <div class="errorHeadMessage"> <%=bundle.getString("error_404_page_not_found")%> </div> 
                        <div id="errorSummary" class="errorSummary">  
                            <p><%=bundle.getString("error_404_text")%></p>
                        </div>
                    </td>
                </tr>
            </table>
            <div> <!-- End Main Content -->
        
        </body>
    </html>
    
