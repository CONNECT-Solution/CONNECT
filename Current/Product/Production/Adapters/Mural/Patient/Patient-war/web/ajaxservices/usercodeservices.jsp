<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Iterator"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>


 
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ViewMergeTreeHandler"%>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
//This page is an Ajax Service, never to be used directly from the Faces-confg.
//This will render a datatable of minor object to be rendered on the calling JSP.
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
        <head>
            <title>Merge Tree</title> 
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

<%if (isSessionActive)  {%>







<%
double rand = java.lang.Math.random();
Enumeration parameterNames = request.getParameterNames();
/*if(parameterNames != null)   {
    while(parameterNames.hasMoreElements() )   {
          String attributeName = (String) parameterNames.nextElement();
          String parameterValue = (String) request.getParameter(attributeName);
   }
}
*/

HttpSession session1 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
ArrayList minorObjectsAddList = (ArrayList)session1.getAttribute("minorObjectsAddList");

HashMap thisMinorObject = new HashMap();
SourceAddHandler  sourceAddHandler   = (SourceAddHandler)session1.getAttribute("SourceAddHandler");
SourceHandler  sourceHandler   = (SourceHandler)session1.getAttribute("SourceHandler");

ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
String rootNodeName = objScreenObject.getRootObj().getName();
//get Field Config for the root node
FieldConfig[] fcRootArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
MasterControllerService masterControllerService = new MasterControllerService();

thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
thisMinorObject.put(MasterControllerService.MINOR_OBJECT_TYPE, request.getParameter("MOT"));
thisMinorObject.put(MasterControllerService.SYSTEM_CODE, sourceAddHandler.getSystemCode());
thisMinorObject.put(MasterControllerService.LID, sourceAddHandler.getLID());

//String URI = request.getRequestURI();
//URI = URI.substring(1, URI.lastIndexOf("/"));
//replace ajaxservices folder name 
//URI = URI.replaceAll("/ajaxservices","");

//Variables required for Delete
String deleteIndex = request.getParameter("deleteIndex");
boolean isDelete = (null == deleteIndex?false:true);

//Variables for Save
String minorObjSave = request.getParameter("minorObjSave");
boolean isminorObjSave = (null == minorObjSave?false:true);


//Variables required for Edit
String editIndex = request.getParameter("editIndex");
boolean isEdit = (null == editIndex?false:true);

//Variables for Validate LID
String validate = request.getParameter("validate");
boolean isValidate = (null == validate?false:true);
String validateLID = request.getParameter("LID");
String validateSystemCode  = request.getParameter("SystemCode");

//HashMap for the root node fields
HashMap rootNodesHashMap = new HashMap();

//Variables for adding new source fields
String saveString = request.getParameter("save");
boolean isSave= (null == saveString?false:true);

//Save Edited Values
//Variables for adding new source fields
String saveEditedValues= request.getParameter("editThisID");
boolean isSaveEditedValues = false;
if (saveEditedValues != null && !("-1".equalsIgnoreCase(saveEditedValues)))   {
	isSaveEditedValues = true;
}
//Variables for Validate LID
String userCodeMasking= request.getParameter("userCodeMasking");
boolean isuserCodeMasking = (null == userCodeMasking?false:true);
String usercodeValue = request.getParameter(request.getParameter("Field"));
//Field
//userCode
if(isSave) {
	isSaveEditedValues = false;
%>
          <script> 
            setEditIndex('-1')
          </script>

<%
}
%>


<f:view>
<body>	
 
	   <%
	

		 String userdefinedInputMask = ValidationService.getInstance().getUserCodeInputMask(request.getParameter("userCode"), usercodeValue);

	   %>
	   <table>
	    <tr>
		<td>
	   <script>
	       userDefinedInputMask = '<%=userdefinedInputMask%>';
           
 		</script>
	   </td>
	   </tr>
	   </table>
 </body>
</f:view>
<%} %>  <!-- Session check -->
</html>
