<%-- 
    Document   : Assume Match services
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Sridhar Narsingh
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionIterator" %>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSearchObject"%>
<%@ page import="com.sun.mdm.index.master.search.transaction.TransactionSummary"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>


<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.ArrayList"  %>
<f:view>
<%
String URI = request.getRequestURI();URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
URI = URI.replaceAll("/printservices","");
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

<%ScreenObject  screenObject = (ScreenObject) session.getAttribute("ScreenObject");%>
<html>
<head>
 <title> <%=screenObject.getDisplayTitle()%> </title>
 <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen, print">
</head>
<body>

<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
 
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   



<%
AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
Enumeration parameterNames = request.getParameterNames();


//Map to hold the validation Errors
HashMap valiadtions = new HashMap();

EDMValidation edmValidation = new EDMValidation();         

//Column Definitions for YUI Datatable
StringBuffer myColumnDefs = new StringBuffer();

//Labels for YUI Datatable
ArrayList labelsList  = new ArrayList();

//Keys for YUI Datatable
ArrayList keys = new ArrayList();

//List to hold the results
ArrayList results = new ArrayList();

/**
 *  ID printed
 */
boolean keyPrinted = false;

/**
 *  EUID printed
 */
boolean euidPrinted = false;

 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");
ArrayList fullFieldNamesList  = new ArrayList();
%>

<%
  boolean isValidationErrorOccured = false;
  ArrayList requiredValuesArray = new ArrayList();
%>
<table>
<tr>
<td>
<table cellspacing="0" cellpadding="0" class="printresultssearch">
<tr>
<td>
<span>
 <h:outputText value="#{msgs.SEARCH_CRITERIA}"/>:&nbsp;
 </span>
</td>
<% //Build the request Map 
   HashMap keyDescriptionsMap = assumeMatchHandler.getKeyDescriptionsMap();
   HashMap newHashMap = new HashMap();
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
            assumeMatchHandler.getParametersMap().put(attributeName,attributeValue);
			newHashMap.put(attributeName,attributeValue);
 %>
 <% if (attributeValue.length() != 0) { %>
 <% } %>
<%
      }
   } 
%>
 <%if(assumeMatchHandler.getParametersMap().get("selectedSearchType") != null) {%>
<td>
	  <span><%=bundle.getString("search_Type")%>:&nbsp;<b><%=assumeMatchHandler.getParametersMap().get("selectedSearchType")%></b></span>
</td>
<%}%>


<%
  ArrayList screenConfigArrayLocal = assumeMatchHandler.getScreenConfigArray();
%>
 <%
String strVal = new String();
  for (Iterator it = screenConfigArrayLocal.iterator(); it.hasNext();) {
	  %>
	  
	      <td>

	  <%
     FieldConfig fieldConfig = (FieldConfig) it.next();
 	   
 	 String   value = (fieldConfig.isRange()) ? (String) newHashMap.get(fieldConfig.getDisplayName()):(String) newHashMap.get(fieldConfig.getName());
	 %>

	 <%
     if(value != null && value.length() > 0) {
       if ((fieldConfig.getValueList() != null && fieldConfig.getValueList().length() > 0)) {
                        
        //SET THE VALUES WITH USER CODES AND VALUE LIST 
        if (fieldConfig.getUserCode() != null) {
               strVal = ValidationService.getInstance().getUserCodeDescription(fieldConfig.getUserCode(), value.toString());
        } else {
              strVal = ValidationService.getInstance().getDescription(fieldConfig.getValueList(), value.toString());
        }
    %>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=strVal%></b>&nbsp;</span>
       
   <%  } else {%>
	  <span><%=fieldConfig.getDisplayName()%>:&nbsp;<b><%=value%></b>&nbsp;</span>
   <% }%>
   <%}%>
   </td>
  <% }%>
<tr>
</table>
</td>
</tr>

<tr>
<td>
<%
results = assumeMatchHandler.performSubmit();
ArrayList resultConfigArray = assumeMatchHandler.getResultsConfigArray();
if (results != null)   {
	for (int i=0;i<resultConfigArray.size();i++)    {
       	FieldConfig fieldConfig = (FieldConfig)resultConfigArray.get(i);
		//keys.add(fieldConfig.getDisplayName());
		keys.add(fieldConfig.getName());
		labelsList.add(fieldConfig.getDisplayName());
		fullFieldNamesList.add(fieldConfig.getFullFieldName());

    }

	myColumnDefs.append("[");
    String value = new String();
	for(int ji=0;ji<keys.size();ji++) {
	    if ("EUID".equalsIgnoreCase((String)keys.toArray()[ji]))  {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:true,resizeable:true,width:150}";
	    } else {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",resizeable:true}";
	    }

       myColumnDefs.append(value);
       if(ji != keys.size()-1) myColumnDefs.append(",");
	}
    myColumnDefs.append("]");

%>

     <table class="printresultssummary"> 
         <tr>
			<td>
			  <em>
               <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
			   &nbsp;
			   <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
               &nbsp;			   
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=results.size()%>
			   </em>
            </td>
         </tr>
         <tr>
         <td>
         <% if (results != null && results.size() > 0 )   {%>
             <div id="myMarkedUpContainer" class="printresults">
                	<table id="myTable" border="0" cellspacing="0" cellpadding="0">
                  	   <thead>
                          <tr>
                     	   <% for (int i =0;i< labelsList.size();i++) { %>
                               <th><%=labelsList.toArray()[i]%></th>
                          <%}%>
                         </tr>
                	   </thead>
                  	   <tbody>
                       <% for (int i = 0; i < results.size(); i++) { //Outer Arraylist 
			              ArrayList valueList = (ArrayList) results.get(i);
						  int length=10;
						  euidPrinted = false;
			           %>
						<tr> 		
							                 <%for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {%>
											   <td>
											      <table border="0" style="border:none">
                                                  <% for (int j = 0; j < valueList.size(); j++) { //The values itself can be an array %>
										                <%HashMap valueMap = (HashMap) valueList.get(j);
													if (valueMap.get(fullFieldNamesList.toArray()[kc])!=null) {
													  if (fullFieldNamesList.toArray()[kc].toString().endsWith("SystemCode"))  {
														length=120;
													  } else {
														length = valueMap.get(fullFieldNamesList.toArray()[kc]).toString().length();
													  }
													 }
					                               %>
												    <tr>
												       <td style="border:none">	
													   <nobr>
												   <%  if ((screenObject.getRootObj().getName()+"."+"ID").equalsIgnoreCase((String)fullFieldNamesList.toArray()[kc])) { %>
														<%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 

												   <%  }  else if ((screenObject.getRootObj().getName()+"."+"EUID").equalsIgnoreCase((String)fullFieldNamesList.toArray()[kc])) { %>
												       <% if (!euidPrinted) { %>
														    <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
														   <% euidPrinted = true;%>
														<% } %>

												   <%  } else { %>
														<%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
												   <%  } %>
												   </nobr>
												     </td>
													 </tr>												 
  									             <%}%><!-- end value list loop -->
												 </table>
												</td>
							                 <%}%> <!-- end Full field loop -->
							</tr>
		                  <% } %>
	                 </tbody>
                 </table>
              </div>
      <% } %>
         <tr>
			<td>
			  <em>
               <a href="javascript:window.print()"><img src='/<%=URI%>/images/print.gif' border="0" alt="print"/></a>
			   &nbsp;
			   <img src='/<%=URI%>/images/YUIhead.jpg' border="0" height="13px" width="1px"/>
               &nbsp;			   
			   <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=results.size()%>
			   </em>
            </td>
		 </tr>

                 </table>
</td>
</tr>
</table>

<% } %> <!-- End results!= null -->

<%} %>  <!-- Session check -->
</body>
</head>
</html>

</f:view>
