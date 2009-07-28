<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>



<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.ResourceBundle"  %>
 


<%
//Author Rajani Kanth
//rkanth@ligaturesoftware.com
//http://www.ligaturesoftware.com
//This page is an Ajax Service, never to be used directly from the Faces-confg.
//This will render a datatable of minor objects to be rendered on the calling JSP.
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Enterprise object minor objects</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
     </head>
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
boolean isSessionActive = true;
%>
<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>

<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));

}
%>

<%if (isSessionActive)  {%>

<%
double rand = java.lang.Math.random();
Enumeration parameterNames = request.getParameterNames();
ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");

HttpSession session1 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
SourceHandler  sourceHandler   = new SourceHandler();
Operations operations = new Operations();
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());


//all field configs hashmap
HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();


//String URI = request.getRequestURI();
//URI = URI.substring(1, URI.lastIndexOf("/"));
//replace ajaxservices folder name 
//URI = URI.replaceAll("/ajaxservices","");

//Variables required for Delete
String euid = request.getParameter("euid");

//Variables for Save
String minorObjType = request.getParameter("MOT");
CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
MasterControllerService masterControllerService = new MasterControllerService();

//get the enterprise object here
EnterpriseObject  enterpriseObject = masterControllerService.getEnterpriseObject(euid);

HashMap eoHashMap = compareDuplicateManager.getEnterpriseObjectAsHashMap(enterpriseObject,screenObject);

//get the array list of minor objects here
ArrayList thisMinorObjectList = (ArrayList) eoHashMap.get("EO" + minorObjType + "ArrayList");

FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));

%>
<f:view>
<body>	
	
 

		  <% for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
		  %>
	    					
		    <% if ( i == 0)  { %>
                  <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">	
	<table align="right">			   
                    <tr align="right" >			   
                     <td align="right" colspan="2">
                       <a href="javascript:void(0);" onclick="javascript:closeDiv();"><%=bundle.getString("View_MergeTree_close_text")%></a>
                       <a href="javascript:void(0);" onclick="javascript:closeDiv();"><img src="images/close.gif" width="12" height="12" border="0" alt='<%=bundle.getString("View_MergeTree_close_text")%>'/></a>
                     </td>
		           </tr>
    </table>			   
			  <table border="0" width="100%" cellpadding="0">		  
                    <% } %>
                     <% for(int k=0;k<fcArray.length;k++) {
					  	  String styleClass = ((k%2==0)?"even":"odd");
					  %>
                          <tr class="<%=styleClass%>">			   
 			                    <td align="left">
				                  <%=fcArray[k].getDisplayName()%>: 
							    </td>
							    <td align="left">
								 <%if(eoHashMap.get("hasSensitiveData") != null &&  fcArray[k].isSensitive() && !operations.isField_VIP() ) { %>
                                        <%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":bundle.getString("SENSITIVE_FIELD_MASKING"))%>
                                <%} else {%>
								  <% if(fcArray[k].isKeyType()) { %>
								  <b><%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":minorObjectMap.get(fcArray[k].getFullFieldName()))%></b>							
							 	<%}else {%>
								  <%=(minorObjectMap.get(fcArray[k].getFullFieldName())==null?"&nbsp;":minorObjectMap.get(fcArray[k].getFullFieldName()))%>							
								 <%}%>
								<%}%>


								</td>
						   </tr>	
					<% } %>
				 <tr><td>&nbsp;</td></tr>	
                <% if ( i == thisMinorObjectList.size()-1)  { %>
                  </table>  
               </div>
           <% } %>
						   
      <% }  %>


 </body>
</f:view>
<%} %>  <!-- Session check -->
</html>
