<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="javax.faces.context.FacesContext"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<f:view>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

     <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   
<%
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/ajaxservices","");
double rand = java.lang.Math.random();
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

<%if (isSessionActive)  {%>
<%
        ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP , FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String undoAssumedMatchId = (String)request.getParameter("undoAssumedMatchId");
        AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
		String newEuid = assumeMatchHandler.undoMatch(undoAssumedMatchId);
%>
											       <table border="0" cellpadding="0" cellspacing="0" >
											         <tr><th colspan="2">Information</th></tr>
											         <tr>
													       <td colspan="2"><nobr><img src="/<%=URI%>/images/info_medium_.gif">&nbsp;<%=bundle.getString("merge_result")%></nobr>
														   </td>
													 </tr>
													 <tr><td colspan="2"><nobr><%=bundle.getString("view_new_EO")%></nobr></td></tr>
											         <tr id="actions">
											            <td align="center">
														  <table>
														    <tr>
															 <td>&nbsp;</td>
															 <td>
                                                                <a class="button" href="/<%=URI%>/euiddetails.jsf?euid=<%=newEuid%>"> <span><%=bundle.getString("ok_text_button")%></span></a>
														     </td>
														     <td>
                                                                <a class="button"  href='javascript:void(0)' onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/AMdetails.jsf?operation=prev&random=rand'+'&'+'AMID='+pages[thisIdx],'outputdiv','');"><span><%=bundle.getString("login_cancel_button_prompt")%></span></a>
															</td>
														   </tr>
														 </table>
											           </td>
											         </tr>
											        </table>

<%}%> <!-- End session check if condition -->



</f:view>
