<%-- 
    Document   : Record details Ajax services Print preview
    Created on : May 5, 2008, 7:59:17 AM
    Author     : Admin
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.validations.EDMValidation"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>

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
<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
%>
<html>
<head>
 <title> <%=screenObject.getDisplayTitle()%> </title>
 <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen, print">
</head>
<body>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />   

<%
SourceHandler sourceHandler = new SourceHandler();
MasterControllerService masterControllerService = new MasterControllerService();

PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();

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

    ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());

String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

ArrayList fullFieldNamesList  = new ArrayList();


//Variables required compare euids
String collectEuids = request.getParameter("collectEuids");
boolean iscollectEuids = (null == collectEuids?false:true);

//Variables required for compare euids
String collectedEuids = request.getParameter("collecteuid");

ArrayList collectedEuidsList = new ArrayList();
%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>


<table cellspacing="0" cellpadding="0" class="printresultssearch">
<tr>
<td>
<span>
 <h:outputText value="#{msgs.SEARCH_CRITERIA}"/>:&nbsp;
 </span>
</td>
<% //Build the request Map 
   HashMap keyDescriptionsMap = patientDetailsHandler.getKeyDescriptionsMap();
   HashMap newHashMap = new HashMap();
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
	 attributeValue = attributeValue.replaceAll("~~","%");
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
            patientDetailsHandler.getParametersMap().put(attributeName,attributeValue);
			newHashMap.put(attributeName,attributeValue);
 %>
<%   }
   } 
  
%>
<%
  if(patientDetailsHandler.getParametersMap().get("selectedSearchType") != null) {
	//set the selected search type here....
	 patientDetailsHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));
%>
<td>
	  <span><%=bundle.getString("search_Type")%>:&nbsp;<b><%=patientDetailsHandler.getParametersMap().get("selectedSearchType")%></b></span>
</td>
<%}%>

<%
  ArrayList screenConfigArrayLocal = patientDetailsHandler.getScreenConfigArray();
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


<% 
String euidValue  = (String) patientDetailsHandler.getParametersMap().get("EUID");
%>

<%if(iscollectEuids) {%> <!-- if compare euids case -->
    
		<table><tr><td>
          <div class="ajaxalert">
		<script>
		if (checkedItems.length >= 2)  { 
		  var euids = "";
		  for(var j=0;j<checkedItems.length;j++) {
			euids +=checkedItems[j] + ",";
		  }
		  window.location = '/<%=URI%>/compareduplicates.jsf?euids='+checkedItems;
	    } else {
	        var messages = document.getElementById("messages");
	        messages.innerHTML= "<ul> <li>Please select at least two EUID to compare </li> </ul>";
	    } 

		</script>
		  </div>
		</td></tr>
		</table>

<%}else if( euidValue != null && euidValue.length() > 0) {
	 //results = patientDetailsHandler.performSubmit();
	 EnterpriseObject eo = masterControllerService.getEnterpriseObject(euidValue);
	%> <!-- if only EUID is entered by the user is entered by the user-->
	<%if(eo != null) {%>
<table>
  <tr><td>
  <script>
      window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=euidValue%>';
  </script>
  </td>
  </tr>
  </table>
  <%}else{%>
      <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		 String messages = "EUID " + euidValue + " Not Found. Please check the EUID value";
     %>     	 

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=messages%>";
		 messages.style.visibility="visible";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>
  <%}%>
<%
  } else if (patientDetailsHandler.getParametersMap().get("LID") != null && patientDetailsHandler.getParametersMap().get("SystemCode") != null) {%><!-- if only System Code/LID is entered by the user is entered by the user-->

<%
  String lid = (String) patientDetailsHandler.getParametersMap().get("LID");
  lid = lid.replaceAll("-", "");
  EnterpriseObject eo = null;
  String systemCode = (String) patientDetailsHandler.getParametersMap().get("SystemCode");
  SystemObject so = masterControllerService.getSystemObject(systemCode, lid);
  if(so != null ) {
     eo = masterControllerService.getEnterpriseObjectForSO(so);
  }
	
%>

<%if(eo != null) {%>
<table>
  <tr><td>
  <script>
           window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=eo.getEUID()%>';
  </script>
  </td>
  </tr>
  </table>
  <%}else{%>
      <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
	  
		 String messages =  sourceHandler.getSystemCodeDescription(systemCode) + "/"+ (String) patientDetailsHandler.getParametersMap().get("LID") +  " Not Found. Please check the entered values.";
     %>     	 

	   </td>
	   </tr>
	 <table>
	 </div>
  <%}%>


<%} else {%>


<%
 results = patientDetailsHandler.performSubmit();

ArrayList resultConfigArray = patientDetailsHandler.getResultsConfigArray();
if (results != null)   {
   keys.add("EUID");
   labelsList.add("EUID");
   fullFieldNamesList.add("EUID");

	for (int i=0;i<resultConfigArray.size();i++)    {
       	FieldConfig fieldConfig = (FieldConfig)resultConfigArray.get(i);
		//keys.add(fieldConfig.getDisplayName());
		keys.add(fieldConfig.getName());
		labelsList.add(fieldConfig.getDisplayName());
		fullFieldNamesList.add(fieldConfig.getFullFieldName());
    }
   //add weight fields here
   keys.add("Weight");
   labelsList.add("Weight");
   fullFieldNamesList.add("Weight");
	myColumnDefs.append("[");
    String value = new String();
	for(int ji=0;ji<keys.size();ji++) {
	    if ("EUID".equalsIgnoreCase((String)keys.toArray()[ji]))  {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",width:150,sortable:true,resizeable:true}";
	    }  else {
	      value = "{key:" + "\"" + keys.toArray()[ji]+  "\"" + ", label: " + "\"" + labelsList.toArray()[ji]+"\"" +  ",sortable:true,resizeable:true}";
	    }
       myColumnDefs.append(value);
       if(ji != keys.size()-1) myColumnDefs.append(",");
	}
    myColumnDefs.append("]");
%>
    

     <table border="0" cellpadding="0" cellspacing="0" class="printresultssummary"> 
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
		   <%request.setAttribute("eoResults",results);%>
             <div id="myMarkedUpContainer" class="printresults">
                	<table id="myTable" border="0" cellspacing="1" cellpadding="1">
                  	   <thead>
                          <tr>
                     	   <% for (int i =0;i< labelsList.size();i++) { %>
                               <th><%=labelsList.toArray()[i]%></th>
                          <%}%>
                         </tr>
                	   </thead>
                  	   <tbody>
                       <% for (int i3 = 0; i3 < results.size(); i3++) { %>
                        <tr> 
                            <%HashMap valueMap = (HashMap) results.get(i3);
                             for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {
								 //+ "Sridhar ==== " + (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc])));
								    String keyValue = (String) keys.toArray()[kc];
								    String fullfieldValue = (String) fullFieldNamesList.toArray()[kc];
									
				              %>
                                   <td>
								   <nobr>
								      <%if(keyValue.equalsIgnoreCase("EUID")) {%>									    
                                           <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"&nbsp;":valueMap.get(fullFieldNamesList.toArray()[kc]))%> 
									<%}else if(!keyValue.equalsIgnoreCase("Weight") && fullfieldValue.indexOf(screenObject.getRootObj().getName()) != 0) {
                                           String minorObjectType = fullfieldValue.substring(0,fullfieldValue.indexOf("."));                                  
								  %>
									    <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"&nbsp;":valueMap.get(fullFieldNamesList.toArray()[kc]))%> 
									 <%}else {%>
                                        <%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"&nbsp;":valueMap.get(fullFieldNamesList.toArray()[kc]))%> 
									 <%}%>
								   </nobr>
                                   </td>
                             <%}%>
                       </tr>
                     <%}%>
	                 </tbody>
                    </table>
                </div>
		   <% } %>
           </td>
           </tr>
       </table>
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
	   </table>

<% } else { %> <!-- End results!= null -->
    <div class="ajaxalert">
    <table>
	   <tr>
	     <td>
     <%
		  Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	      StringBuffer msgs = new StringBuffer("<ul>");	
          while (messagesIter.hasNext()) {
                     FacesMessage facesMessage = (FacesMessage) messagesIter.next();
                     msgs.append("<li>");
					 msgs.append(facesMessage.getSummary());
					 msgs.append("</li>");
          }
		  msgs.append("</ul>");		  
     %>     	 
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>

<%}%> <!-- if not euid or systemcode/lid values entered -->

<%} %>  <!-- Session check -->
</body>
</head>
</html>

</f:view>
