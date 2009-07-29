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
    <!-- Modified by Narayan Bhat on 22-aug-2008 for the incorparte with the functionality of back button in euiddetails.jsp  
    added String previousQueryStr = request.getQueryString();
    and appended the same with every href link
--> 
<%
//set locale value
String previousQueryStr = null;
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
previousQueryStr = request.getQueryString();
}
%>
    <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />  
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
NavigationHandler navigationHandler = new NavigationHandler();

//set the screen object of the assumed matches
session.setAttribute("ScreenObject", navigationHandler.getScreenObject("assumed-matches"));

ScreenObject screenObject = (ScreenObject) session.getAttribute("ScreenObject");
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

HashMap matchEuids = new HashMap();

/**
 *  ID printed
 */
boolean keyPrinted = false;

/**
 *  EUID printed
 */
boolean euidPrinted = false;

 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP,FacesContext.getCurrentInstance().getViewRoot().getLocale());
String print_text = bundle.getString("print_text");
String total_records_text = bundle.getString("total_records_text");

ArrayList fullFieldNamesList  = new ArrayList();

%>

<%
  boolean isValidationErrorOccured = false;
  //HashMap valiadtions = new HashMap();
   ArrayList requiredValuesArray = new ArrayList();

%>

<% //Build the request Map 
   while(parameterNames.hasMoreElements())   { 
    String attributeName = (String) parameterNames.nextElement();
    String attributeValue = (String) request.getParameter(attributeName);
       if ( !("editThisID".equalsIgnoreCase(attributeName)) && 
			!("random".equalsIgnoreCase(attributeName)) ) {
		     assumeMatchHandler.getParametersMap().put(attributeName,attributeValue);			
      }
   } 
%>

<%

if(request.getParameter("selectedSearchType") != null ) {
	   assumeMatchHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));
}
  
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

     <table border="0"> 
         <tr>
            <td style="width:85%;text-align:right">
                         <h:outputText value="#{msgs.total_records_text}"/>&nbsp;<%=results.size()%>&nbsp;
			</td>
			<td>
				<% if (results.size() > 0)   {%>
                    <h:panelGrid rendered="#{Operations.assumedMatch_Print}" >
                        <a title="<%=print_text%>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('advancedformData');openPrintWindow('/<%=URI%>/printservices/assumematch.jsf?random='+rand+'&'+queryStr)"><span><%=print_text%></span></a>
					</h:panelGrid>             
				<% } %>
            </td>
         </tr>
         <tr>
         <td colspan="2">
         <% if (results != null && results.size() > 0 )   {%>
             <div id="myMarkedUpContainer" class="reportresults">
                	<table id="myTable" border="0">
                  	   <thead>
                          <tr>
                     	   <% for (int i =0;i< keys.size();i++) { %>
                               <th><nobr><%=keys.toArray()[i]%></nobr></th>
                          <%}%>
                         </tr>
                	   </thead>	
                  	   <tbody>
<%
for (int i = 0; i < results.size(); i++) { //Outer Arraylist 
ArrayList valueList = (ArrayList) results.get(i);
for (int kc = 0; kc < fullFieldNamesList.size(); kc++) {
    for (int j = 0; j < valueList.size(); j++) { 
	   HashMap valueMap = (HashMap) valueList.get(j);
     if ((screenObject.getRootObj().getName()+"."+"EUID").equalsIgnoreCase((String)fullFieldNamesList.toArray()[kc])) {
		    matchEuids.put(valueMap.get(fullFieldNamesList.toArray()[kc]),valueMap.get(fullFieldNamesList.toArray()[kc]));
     } 
  }
 }
}
Object[] keysObj = matchEuids.keySet().toArray();
StringBuffer sbr  = new StringBuffer();
for(int i = 0 ; i < keysObj.length;i++) {
  String key  = (String) keysObj[i];
  sbr.append(key);
  if(i != keysObj.length-1 ) sbr.append(",");
}
%>
                       <% for (int i = 0; i < results.size(); i++) { //Outer Arraylist 
			              ArrayList valueList = (ArrayList) results.get(i);
						  int length=10;
						  euidPrinted = false;
			           %>
						<tr> 		
<!-- Collect all EUIDs -->
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
												       <td  style="border:none">	
													   <nobr>
												   <%  if ((screenObject.getRootObj().getName()+"."+"ID").equalsIgnoreCase((String)fullFieldNamesList.toArray()[kc])) { %>
														<%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 

												   <%  }  else if ((screenObject.getRootObj().getName()+"."+"EUID").equalsIgnoreCase((String)fullFieldNamesList.toArray()[kc])) { %>
												       <% if (!euidPrinted) { %>
														<a href="ameuiddetails.jsf?AMID=<%=valueMap.get(fullFieldNamesList.toArray()[kc])%>&euids=<%=sbr.toString()%>&<%=previousQueryStr%>&fromUrl=assumedmatches.jsf" >
														<%= (valueMap.get(fullFieldNamesList.toArray()[kc]) == null?"":valueMap.get(fullFieldNamesList.toArray()[kc]))  %> 
														</a>

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

		<script>
			var fieldsArray = new Array();
		</script>
			<% for(int index=0;index<keys.size();index++) {%>
					<script>
						fieldsArray[<%=index%>] = '<%=keys.toArray()[index]%>';
					</script>
			<%}%>
		<script type="text/javascript">
			var myDataSource = new YAHOO.util.DataSource(YAHOO.util.Dom.get("myTable"));
			myDataSource.responseType = YAHOO.util.DataSource.TYPE_HTMLTABLE;
			myDataSource.responseSchema = {
				fields: fieldsArray
			};
			var myConfigs = {
				paginator : new YAHOO.widget.Paginator({
					rowsPerPage    : 10, // REQUIRED
					totalRecords   : <%=results.size()%> //, // OPTIONAL
					//template       : "{PageLinks} Show {RowsPerPageDropdown} per page"
				})     
			};
			var myColumnDefs = <%=myColumnDefs.toString().length() == 0?"\""+ "\"":myColumnDefs.toString()%>;
			var myDataTable = new YAHOO.widget.DataTable("myMarkedUpContainer", myColumnDefs, myDataSource,myConfigs);
		</script>

	 <script>
		//nullify any previous error messages
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";
		 messages.style.visibility="hidden";
	 </script>

      <% } %>
	 <script>
		//nullify any previous error messages
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "";
		 messages.style.visibility="hidden";
	 </script>

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

	 <script>
		 var messages = document.getElementById("messages");
	     messages.innerHTML= "<%=msgs%>";
		 messages.style.visibility="visible";
	 </script>
	   </td>
	   </tr>
	 <table>
	 </div>

<% } %>
<%} %>  <!-- Session check -->
</f:view>
