<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Merge Tree</title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<!-- Required CSS> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/yui4jsfResources.jsf?name=tree.css&folder=treeview&fromAssets=true"--> 
	<link type="text/css" rel="stylesheet" href="css/yui/treeview/assets/skins/sam/treeview-skin.css"> 
       </head>
<%
//set locale value
if(session!=null ){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
<f:view>
 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       
<body>	
     <table  width="100%">
         <tr>
             <td align="left"><font style="font-size:14px;font-style:bold;">Merge Tree</font></td>
             <td align="right">
                 <a href="javascript:void(0);" title="<h:outputText value="#{msgs.View_MergeTree_close_text}"/>" onclick="javascript:closeTree();"><h:outputText value="#{msgs.View_MergeTree_close_text}"/></a>
                 <a href="javascript:void(0);" onclick="javascript:closeTree();"><img src="images/close.gif" width="12" height="12" border="0" alt="close"/></a>
             </td>
         </tr>

         <tr>
             <td colspan="2" align="left">
                 <h:form>
                     <yui:treeView id="treeView" value="#{ViewMergeTreeHandler.htmlNodeTreeDataModel}" expandAnim="FADE_IN" collapseAnim="fade_out"></yui:treeView>
                 </h:form>                 
             </td>
         </tr>
     </table>
 </body>
</f:view>
</html>
