<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"%>

<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService" %>
<%@ page import="javax.servlet.http.HttpSession" %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceMergeHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%
 double rand = java.lang.Math.random();
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
%>

<f:view>
 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>
           <!--there is no custom header content for this example-->
     </head>
    
 <%
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
ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
Operations operations = new Operations();
 
ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());

HashMap thisMinorObject = new HashMap();
//SourceMergeHandler  sourceMergeHandler   = new SourceMergeHandler();
SourceMergeHandler sourceMergeHandler = (SourceMergeHandler) session.getAttribute("SourceMergeHandler");

SourceHandler  sourceHandler   = new SourceHandler();
CompareDuplicateManager compareDuplicateManager  = new CompareDuplicateManager();
HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();

ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
String rootNodeName = objScreenObject.getRootObj().getName();
Object[] roorNodeFieldConfigs = sourceHandler.getRootNodeFieldConfigs().toArray();

HashMap resultArrayMapCompare = new HashMap();
HashMap resultArrayMapMain = new HashMap();


//get Field Config for the root node
FieldConfig[] fcRootArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
MasterControllerService masterControllerService = new MasterControllerService();
Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 

  
//Variables for Validate LID
String validate = request.getParameter("validate");
boolean isValidate = (null == validate?false:true);

String localIdDesignation =	 ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");
//HashMap for the root node fields
HashMap rootNodesHashMap = new HashMap();

//Variables for adding new source fields
String saveString = request.getParameter("save");
boolean isSave= (null == saveString?false:true);

//Variables for lid merge preview 
String mergePreviewStr = request.getParameter("mergePreview");
boolean isMergePreview = (null == mergePreviewStr?false:true);

//populate Merge Fields variables

String  populateMergeFieldsStr = request.getParameter("populateMergeFields");
boolean isPopulateMergeFields = (null == populateMergeFieldsStr?false:true);

String epathValue = new String();  
HashMap soMergePreviewMap = null;
HashMap mergePersonfieldValuesMapEO = new HashMap();
String previewEuidValue = new String();
HashMap previewEuidsHashMap = new HashMap();

//showEuid variables
//Variables for lid merge preview 
String showEuidStr = request.getParameter("showEuid");
boolean isShowEuid = (null == showEuidStr?false:true);
String soLID = request.getParameter("LID");
String soSystemCode  = request.getParameter("SYSTEM_CODE");
 
if(isShowEuid) {
  SystemObject systemObject  = masterControllerService.getSystemObject(soSystemCode,soLID);
  EnterpriseObject soEnterpriseObject = masterControllerService.getEnterpriseObjectForSO(systemObject);
  messagesIter = FacesContext.getCurrentInstance().getMessages(); 
  //if EO is not null then navigate to the euid details page.
  if(soEnterpriseObject != null ) {
	  NavigationHandler navigationHandler = new NavigationHandler();
      //set the screen object of the record details screen once EUID is found 
      session.setAttribute("ScreenObject", navigationHandler.getScreenObject("record-details"));
	  
	  %>

<table>
<tr>
  <td>
   <script> 
 	 window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=soEnterpriseObject.getEUID()%>';
   </script>
  </td>
 </tr>
</table>

<%  } else {%>
    <div class="ajaxalert">
	  <table>
			<tr>
				<td>
				      <ul>
			            <% while (messagesIter.hasNext())   { %>
				             <li>
								<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
								<%= facesMessage.getSummary() %>
				             </li>
						 <% } %>
				      </ul>
				<td>
			<tr>
		</table>
	</div>

<%}%>
<%}%>

<%
 boolean validationFailed = false;
 String validtionMessage  = bundle.getString("enter_lid_and_systemcode_text") + " " + localIdDesignation ;

if(  (request.getParameter(bundle.getString("transaction_source")) != null &&  request.getParameter(bundle.getString("transaction_source")).trim().length() == 0 ) && 
    (request.getParameter(localIdDesignation+" 1") != null &&  request.getParameter(localIdDesignation+" 1").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 2") != null &&  request.getParameter(localIdDesignation+" 2").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 3") != null &&  request.getParameter(localIdDesignation+" 3").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 4") != null &&  request.getParameter(localIdDesignation+" 4").trim().length()  == 0 ) ) {
    validationFailed = true;
 
} else if(
	(request.getParameter(bundle.getString("transaction_source")) != null &&  request.getParameter(bundle.getString("transaction_source")).trim().length() >  0 ) && 
    (request.getParameter(localIdDesignation+" 1") != null &&  request.getParameter(localIdDesignation+" 1").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 2") != null &&  request.getParameter(localIdDesignation+" 2").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 3") != null &&  request.getParameter(localIdDesignation+" 3").trim().length()  == 0 ) &&    
    (request.getParameter(localIdDesignation+" 4") != null &&  request.getParameter(localIdDesignation+" 4").trim().length()  == 0 ) ) {
     validationFailed = true; 
 }
  
 int countLid1 = 0;
 
 String  duplicateFound = request.getParameter("duplicateLid");
 
 
if (duplicateFound != null && duplicateFound.length() > 0 ) {
 	%>
		<div class="ajaxalert">
   	   	  <table>
 			<tr>
				<td>  
				  <script>
				   document.getElementById("duplicateIdsDiv").innerHTML = "<%=duplicateFound%>";
 			       document.getElementById("duplicateLid").value = "";
                   </script>
 				</td>  
			</tr>
		</table>
   </div>


<%} else {%>

   	   	  <table>
 			<tr>
				<td>  
				  <script>
				   document.getElementById("duplicateIdsDiv").innerHTML = "";
			       document.getElementById("duplicateLid").value = "";
                  </script>
 				</td>  
			</tr>

<%
sourceMergeHandler.setLid1(request.getParameter(localIdDesignation+" 1"));
 sourceMergeHandler.setLid2(request.getParameter(localIdDesignation+" 2"));
 sourceMergeHandler.setLid3(request.getParameter(localIdDesignation+" 3"));
 sourceMergeHandler.setLid4(request.getParameter(localIdDesignation+" 4"));
 sourceMergeHandler.setSource(request.getParameter(bundle.getString("transaction_source")));

//Variables for lid merge preview 
String mergeFinalStr = request.getParameter("mergeFinal");
boolean isMergeFinal = (null == mergeFinalStr?false:true);
 	
 
%>
 <% //If it is final merge 
  if(isMergeFinal) {
    sourceMergeHandler.setFormlids(request.getParameter("mergeFinalForm_LIDS"));
    sourceMergeHandler.setLidsource(request.getParameter("mergeFinalForm_SOURCE"));
  }
%>

<%  
	//if validation fails null value is returned to newSoArrayList
    // if validation sucessfull or final merge array list of system objects are returned to newSoArrayList
	ArrayList newSoArrayList= (isPopulateMergeFields || isShowEuid || validationFailed) ? null : (isMergeFinal)?sourceMergeHandler.mergeSystemObject():sourceMergeHandler.sourcerecordMergeSearch();

    messagesIter = FacesContext.getCurrentInstance().getMessages();  %>

 <% //If it is final merge 
  if(isMergeFinal && newSoArrayList != null && newSoArrayList.size() > 0 ) {
 	
	String[] srcs  = request.getParameter("mergeFinalForm_LIDS").split(":");
    String  lidsSource  = request.getParameter("mergeFinalForm_SOURCE");		
	//Clear the values here
    sourceMergeHandler.getDestnRootNodeHashMap().clear();
    sourceMergeHandler.getDestnMinorobjectsList().clear();

	%>
<table>
<tr>
  <td>
   <script>  
	   document.getElementById("mergeDiv").style.visibility = "hidden";
	   document.getElementById("mergeDiv").style.display = "none";

	   //Final success full message
       document.getElementById("duplicateIdsDiv").innerHTML  = "<b><%=sourceHandler.getSystemCodeDescription(lidsSource)%>/<%=srcs[0]%></b> <%=bundle.getString("so_merge_confirm_text")%> <b><%=sourceHandler.getSystemCodeDescription(lidsSource)%>/<%=srcs[1]%></b>" ;
	   
	   ClearContents("basicMergeformData");
     </script>
  </td>
 </tr>
</table>

  
<%  }%>


<%if(isPopulateMergeFields) {
     
	String POPULATE_KEY  = request.getParameter("POPULATE_KEY");
	String LID  = request.getParameter("LID");
    String POPULATE_VALUE  = ("null".equalsIgnoreCase(request.getParameter("POPULATE_VALUE"))) ? null: request.getParameter("POPULATE_VALUE");
    String POPULATE_VALUE_DESC  = ("null".equalsIgnoreCase(request.getParameter("POPULATE_VALUE_DESC"))) ? null: request.getParameter("POPULATE_VALUE_DESC");

    sourceMergeHandler.getDestnRootNodeHashMap().put(POPULATE_KEY,POPULATE_VALUE);

     /*
	HashMap rootNodeValuesHashMap  =  (HashMap) sourceMergeHandler.getSoMergePreviewMap().get("SYSTEM_OBJECT");
	HashMap rootNodeValuesHashMapCodes  =  (HashMap) sourceMergeHandler.getSoMergePreviewMap().get("SYSTEM_OBJECT_EDIT");
	
 
    rootNodeValuesHashMap.put(POPULATE_KEY,POPULATE_VALUE);
    rootNodeValuesHashMapCodes.put(POPULATE_KEY,POPULATE_VALUE_DESC);
    


	rootNodeValuesHashMap  =  (HashMap) sourceMergeHandler.getSoMergePreviewMap().get("SYSTEM_OBJECT");
	rootNodeValuesHashMapCodes  =  (HashMap) sourceMergeHandler.getSoMergePreviewMap().get("SYSTEM_OBJECT_EDIT");
    
 	soMergePreviewMap = sourceMergeHandler.getSoMergePreviewMap();
	*/
	String[] src_destns  = request.getParameter("mergeFinalForm_LIDS").split(":");
    String lid_not_selected = new String();
 	for(int i = 0; i< src_destns.length;i++) {
      if(!LID.equalsIgnoreCase(src_destns[i])) {
          lid_not_selected = src_destns[i];
      }
    } 
%>

		 <table>
		   <tr><td>
		 <script>
			 //Hide/visible the selected div layers accordingly as per the user selection in the lid merge screen
             if(document.getElementById('highlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility == 'hidden') {
 			  document.getElementById('highlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility = 'visible';
			  document.getElementById('highlight<%=LID%>:<%=POPULATE_KEY%>').style.display = 'block';

			  document.getElementById('unhighlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.visibility = 'hidden';
			  document.getElementById('unhighlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.display = 'none';
			
			} else {
  			  document.getElementById('highlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility = 'hidden';
			  document.getElementById('highlight<%=LID%>:<%=POPULATE_KEY%>').style.display = 'none';

			  document.getElementById('unhighlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.visibility = 'visible';
			  document.getElementById('unhighlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.display = 'block';

			}

            if(document.getElementById('unhighlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility == 'hidden' ) {
 
			   document.getElementById('unhighlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility = 'visible';
			   document.getElementById('unhighlight<%=LID%>:<%=POPULATE_KEY%>').style.display = 'block';

			   document.getElementById('highlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.visibility = 'hidden';
			   document.getElementById('highlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.display = 'none';

			} else {
 			   document.getElementById('unhighlight<%=LID%>:<%=POPULATE_KEY%>').style.visibility = 'hidden';
			   document.getElementById('unhighlight<%=LID%>:<%=POPULATE_KEY%>').style.display = 'none';

			   document.getElementById('highlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.visibility = 'visible';
			   document.getElementById('highlight<%=lid_not_selected%>:<%=POPULATE_KEY%>').style.display = 'block';

			}

		 </script>
		  </td></tr>
		  </table>



 <%}%>


 
<% if(isMergePreview) {
   //Clear the values here
   sourceMergeHandler.getDestnRootNodeHashMap().clear();
   sourceMergeHandler.getDestnMinorobjectsList().clear();

   sourceMergeHandler.setFormlids(request.getParameter("PREVIEW_SRC_DEST_LIDS"));
   sourceMergeHandler.setLidsource(request.getParameter("PREVIEW_SYSTEM_CODE"));
   sourceMergeHandler.previewLIDMerge();
   soMergePreviewMap = sourceMergeHandler.getSoMergePreviewMap();
   messagesIter = FacesContext.getCurrentInstance().getMessages(); 
   //confirmationButton
   if(soMergePreviewMap != null ) { //if preview is found 
        String[] srcs  = request.getParameter("PREVIEW_SRC_DEST_LIDS").split(":");
        String  lidsSource  = request.getParameter("PREVIEW_SYSTEM_CODE");		
		for(int i=0;i<srcs.length;i++) {
         //set the selected values involved in getting the preview for the selection
         previewEuidsHashMap.put(srcs[i],srcs[i]);
        }

%>
<table>
<tr>
  <td>
   <script>  
	   document.getElementById("confirmationButton").style.visibility = "visible";
	   document.getElementById("confirmationButton").style.display = "block";
	   document.getElementById("soMergeConfirmContent").innerHTML = "<%=srcs[1]%>";
	   document.getElementById("mergeFinalForm:previewhiddenLid1").value = "<%=sourceMergeHandler.getFormlids()%>";
  	   document.getElementById("mergeFinalForm:previewhiddenLid1source").value = "<%=lidsSource%>";
     </script>
  </td>
 </tr>
</table>
  
<%  } else {%>
    <div class="ajaxalert">
	  <table>
			<tr>
				<td>
				      <ul>
			            <% while (messagesIter.hasNext())   { %>
				             <li>
								<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
								<%= facesMessage.getSummary() %>
				             </li>
						 <% } %>
				      </ul>
				<td>
			<tr>
		</table>
	</div>

<%  } %>

 <%} %>



<!-- Initialize the all lids check array-->
<table>
<tr>
  <td>
   <script> 
		   lids="";
           lidArray = [];
           alllidsArray = [];
           alllidsactionText = [];
   </script>
  </td>
 </tr>
</table>
  <%if(!validationFailed && newSoArrayList != null && newSoArrayList.size() > 0) {	   %>

    <table cellpadding="0" cellspacing="0">  
                                            <tr>
                                                <td>
                                                    <div style="height:600px;overflow:auto;">
                                                        <table cellspacing="0" cellpadding="0" >
                                                          <tr>
                                                              
                                                               <%
																  
                                                    Object[] soHashMapArrayListObjects = newSoArrayList.toArray();
                                                    String cssClass = "dynaw169";
                                                    String cssMain = "maineuidpreview";
                                                    String menuClass = "menutop";
                                                    String dupfirstBlue = "dupfirst";
                                                    for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) {
                                                        if (countEnt > 0) {
                                                            cssClass = "dynaw169";
                                                            menuClass = "menutop";
                                                            dupfirstBlue = "dupfirst";
                                                        }
                                                        HashMap soHashMap = (HashMap) soHashMapArrayListObjects[countEnt];
 
                                                             %>


															   
															   <!-- Display the field Values-->
                                                               <%if(countEnt ==0 ) {%>
                                                                  <td  valign="top">
                                                                          <div id="labelmainEuidContent" class="yellow">
                                                                               <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                                    <tr>
                                                                                       <td id="menu<%=soHashMap.get("LID")%>">&nbsp</td>
                                                                                    </tr> 
                                                                                    <tr>
                                                                                        <td valign="top"  id="Label<%=soHashMap.get("LID")%>"><b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b></td>

                                                                                     </tr>
                                                                               </table>
                                                                           </div>
                                                                          <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                                             <div id="labelpersonEuidDataContent" class="yellow">
                                                                                <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>">
                                                                         <%

                                                        for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
                                                                        %>  
                                                                                    <tr>
                                                                                      <td>
                                                                                         <%=fieldConfigMap.getDisplayName()%>                 
                                                                                      </td>
                                                                                    </tr>
                                                                        <%}%>
                                                             
                                                                                     <tr><td>&nbsp;</td></tr>
                                                                    <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                                     int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                                    %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
                                                                    <%
                                                                      for (int max = 0; max< maxMinorObjectsMAX; max++) {
 																    %>

																  <%
                               		 		                       for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                        FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%=fieldConfigMap.getDisplayName()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // field config loop
 																	  %>

																	<%
                                                                      } // max minor objects loop
																 	 %>
																	<%
                                                                      } // max minor objects loop
																 	 %>

                                                                                 </table>
                                                                            </div>
                                                                          </div>
                                                                   </td>
                                                               <%}%>



                                                                  <td  valign="top">
                                                                     <div id="outerMainContentDivid<%=countEnt%>">
															<%if(newSoArrayList.size() == 1 || previewEuidsHashMap.get((String)soHashMap.get("LID")) != null) {%>
                                                             <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="blue">
															<%} else {%>
															<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="yellow">
															<%}%>
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td class="menutop"><b> <%=localIdDesignation%>&nbsp;<%=countEnt + 1%></b> </td>
                                                                    </tr> 
                                                                     <tr>
                                                                    <script> alllidsArray.push('<%=soHashMap.get("LID")%>')</script>
                                                                    <td valign="top" name="sri" id="curve<%=soHashMap.get("LID")%>">
                                                                   <% if(newSoArrayList.size() == 1 || soMergePreviewMap != null ) { %>
                                                                     <span title ="<%=soHashMap.get("LID")%>" class="dupbtn" >
                                                                     <%=soHashMap.get("LID")%>
                                                                     </span>
                                                                     <%} else {%>
                                                                     <a title ="<%=soHashMap.get("LID")%>" class="dupbtn" id="button<%=soHashMap.get("LID")%>" href="javascript:void(0)" onclick="javascript:collectLid('<%=soHashMap.get("LID")%>','<%=bundle.getString("source_keep_btn") + " " + localIdDesignation%>')">
                                                                     <%=soHashMap.get("LID")%>
                                                                     <script> var thisText = document.getElementById('curve<%=soHashMap.get("LID")%>').innerHTML; alllidsactionText.push(thisText);</script>
                                                                      </a>
                                                                  <%}%>
                                                                 </td>
                                                                 </tr>
                                                                </table>
                                                            </div>
															<%if(newSoArrayList.size() == 1 || previewEuidsHashMap.get((String)soHashMap.get("LID")) != null) {%>
                                                                <div id="personEuidDataContent<%=soHashMap.get("LID")%>" class="blue">
															<%} else {%>
                                                                <div id="personEuidDataContent<%=soHashMap.get("LID")%>" class="yellow">
															<%}%>
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="buttoncontent<%=soHashMap.get("LID")%>"  >
                                                                        <%
                                                        HashMap personfieldValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT");
                                                        HashMap codesValuesMapEO = (HashMap) soHashMap.get("SYSTEM_OBJECT_EDIT");
				                	String checkEuidValue = new String();
							//If it is preview
                                                        if( soMergePreviewMap != null  ) {
                                                          String[] srcs  = (String[]) request.getAttribute("lids");
                                                          String  lidsSource  = (String) request.getAttribute("lidsource");		
                                                           
                                                          mergePersonfieldValuesMapEO = (HashMap) soMergePreviewMap.get("SYSTEM_OBJECT");
                                                          previewEuidValue = (String) mergePersonfieldValuesMapEO.get("LID");
                					  checkEuidValue =  (String) personfieldValuesMapEO.get("LID");
                                                       }    
                    %>
					 
 							
							<%for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                epathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
															/*
                                                            if (countEnt > 0) {
                                                                resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                                            } else {
                                                                resultArrayMapMain.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                                            }*/

 										//Compare the surviving EUID if it is previewed
                                        if( soMergePreviewMap != null) {         
                                           resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue)); //Compare with other values 
						                   resultArrayMapMain.put(epathValue, mergePersonfieldValuesMapEO.get(epathValue));//keep the surviving EO values to compare 
					                    } else { //Compare with the main EUID only
                                           if (countEnt > 0) { 
                                             resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                           } else {
                                             resultArrayMapMain.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                           }
					                   }
                                         %>  
                                                                        <tr>
                                                                            <td>
                                                     <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
                                                         <%if (soMergePreviewMap != null) {%> <!-- if preview is then display the links-->
                                                                <%if (previewEuidsHashMap.get((String) personfieldValuesMapEO.get("LID")) != null) {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->

                                                                <%if ((resultArrayMapCompare.get(epathValue) != null  && resultArrayMapMain.get(epathValue) != null)  && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
                                                                                <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" >
                                                                                    <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
                                                                                        <font class="highlight">
                                                                                            <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>                     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                            <%} else {%> 
                                                                                            <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                            <%}%>
                                                                                        </font>
                                                                                    </a>  
                                                                                </div>
                                                                                <div id="unhighlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" style="visibility:hidden;display:none;">
                                                                                             <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>                     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                            <%} else {%> 
                                                                                            <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                            <%}%>
                                                                                  </div>
                                                                <%} else {%>
 																	<%if(resultArrayMapMain.get(epathValue) == null) { %>
                                                                      <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" >
                                                                          <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
																					  <font class="highlight">
																				         <%if(soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                         <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     </font>
																					 </a>
																		 </div>

 																	  <%} else {%> 


														<% if( ( (String) soHashMap.get("LID") ).equalsIgnoreCase( (String)soMergePreviewMap.get("LID") ) )  {%>
 
                                                                      <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" > 
                                                                         		<%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%> 
                                                                                    <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                <%}%>
 																	 </div>

                                                                      <div id="unhighlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" style="visibility:hidden;display:none">
																					<a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
																					  <font class="highlight">
																				         <%if(soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                         <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     </font>
																					 </a>
																		 </div>


                                                        <%}else {%>
                                                                       <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" >
 
                                                                                <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>
																					   <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%} else {%> 
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                <%}%>
																	</div>
                                                                    
																	<div id="unhighlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" style="visibility:hidden;display:none">
  
                                                                                    <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
																					  <font class="highlight">
																				         <%if(soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                         <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     </font>
																					 </a>
																		 </div>

														<%}%>

                                                                      <%}%>
																<%}%>
                                                         <%} else {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->

                                                              <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>
                                                                   <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                              <%} else {%> 
                                                                    <%=personfieldValuesMapEO.get(epathValue)%>
                                                              <%}%>
                                                         <%}%>
                                                     <%} else {%> <!--if not [preview -->
                                                                                    <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
                                                                                     <font class="highlight">
                                                                                        <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>                          <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%} else {%> 
                                                                                          <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                        <%}%>
                                                                                    </font>
                                                                                    <%} else {%>

 																					<%if(resultArrayMapMain.get(epathValue) == null) { %>
 																					    <font class="highlight">
																				          <%if(soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()){%> 
																					        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                          <%}else{%>
                                                                                           <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                        <%}%>
                                                                                       </font>
                                                                                     <%} else {%> 
                                                                                       <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                      <%}%>
                                                                                    <%}%> 


                                                                                    <%}%>
                                                                                        
                                                                                    <%}%>
                                                                                        
                                                                                    
                                                                                <%} else {%> <!-- Not null condition  -->
                                                                                    <%if (soMergePreviewMap != null) {%> <!-- if preview is then display the links-->
                                                                                        <%if (previewEuidsHashMap.get((String) personfieldValuesMapEO.get("LID")) != null) {%>
 		                                                                                <%if(mergePersonfieldValuesMapEO.get(epathValue) != null) { %> 
                                                                                        <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" >		
                                                                                          <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
                                                                                                <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                                 </font>
                                                                                            </a>  
                                                                                        </div>

                                                                                        <div id="unhighlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" style="visibility:hidden;display:none;">		
                                                                                                 <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                                 </font>
                                                                                         </div>
																						<%} else {%>
                                
                                                                                        <div id="un	highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" style="visibility:hidden;display:none;">		
                                                                                          <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>');getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   >
                                                                                                <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                                 </font>
                                                                                            </a>  
                                                                                        </div>

                                                                                        <div id="highlight<%=personfieldValuesMapEO.get("LID")%>:<%=epathValue%>" >&nbsp;		
                                                                                          </div>
								

 																						<%}%>

                                                                                        <%} else {%>
                                                                                        &nbsp; 
                                                                                        <%}%>
                                                                                    <%} else {%>		
                                                                                      <%if(resultArrayMapMain.get(epathValue) != null ) { %>
                                                                                       <font class="highlight">
 																						   <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                       </font>
																					 <%}else {%>
                                                                                       &nbsp;
                                                                                     <%}%>

																					<%}%>
                                                                                <%}%>
                                                                              </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                        <tr><td>&nbsp;</td></tr>
                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
																	   %>

																	   <%
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                                    ArrayList  minorObjectMapList =  (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                    int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
                                                int  maxMinorObjectsPreview =  (soMergePreviewMap  != null) ?((Integer) soMergePreviewMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue():0;
                                                ArrayList minorObjectsListPreview =  (soMergePreviewMap  != null) ?((ArrayList) soMergePreviewMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList")):new ArrayList();
												String minorObjectStatus = new String();
                                                 int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
												 int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;

                                                 FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
 			 %>
                                                                    <tr>
																	    <td>
																		    <% if(maxMinorObjectsMinorDB ==0) {%>
																	         <b><%=bundle.getString("source_inpatient2_text")%> <%=childObjectNodeConfig.getName()%></b>
																			 <%}else{%>
																	         &nbsp;
																			 <%}%>
																	     </td>
																     </tr>

		     <%
								for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
                                                                       minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                   %>
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                     epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td> 
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
																				<%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b><%=minorObjectHashMap.get(epathValue)%></b>
																			<%if(soMergePreviewMap != null && previewEuidsHashMap.get((String) personfieldValuesMapEO.get("LID")) != null) {%>
																			 <!-- Condition to check if the minor object type is already found in the preview and render the action buttons accordingly -->
   																			  <%if(!((String)minorObjectHashMap.get("LID")).equalsIgnoreCase((String)soMergePreviewMap.get("LID")) ){%> 
 																			    <%if(sourceMergeHandler.isNotAvailableInPreview(minorObjectsListPreview, minorObjectHashMap, epathValue)) {%>
                                                                                                 <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="MOVE <%=minorObjectHashMap.get(epathValue)%> <%=minorObjectHashMap.get("MINOR_OBJECT_TYPE")%> TO PREVIEW"/>
                                                                                                 </font>
  																			   <%} else {%>
                                                                                                 <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="UPDATE  <%=minorObjectHashMap.get(epathValue)%> <%=minorObjectHashMap.get("MINOR_OBJECT_TYPE")%> VALUE"/>
                                                                                                 </font>
     																		   <%}%>
																			<%}%>


																				<%}%>
																				<%} else {%>
                                                                                 <%if (soHashMap.get("hasSensitiveData") != null && !operations.isField_VIP() && fieldConfigMap.isSensitive()) {%>     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
																				 <%} else {%> 
                                                                                  <%=minorObjectHashMap.get(epathValue)%>
                                                                                 <%}%> 
																				<%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      }//field config loop
								       } //minor objects values list
                                                                      %>

                                                                    <%
								    for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								   %>

								 <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
                                                                        }//Extra minor objects loop
								 %>


								 <%  
                                                                    } //total minor objects loop
                                                                    %>

                                                                    </table>
                                                                </div>
                                                                     </div>
                                                                  </td>
                                                               <% if (soMergePreviewMap != null && countEnt + 1 == soHashMapArrayListObjects.length)   {%>
                                                                  <td  valign="top">
                                                                       <div id="preview<%=countEnt%>">
                                                                <%
                                                                          HashMap mergedSOMap = new HashMap();
                                                                          String styleclass = "yellow";
													                      ArrayList mergePreviewMinorObjectList = new ArrayList();
                                                                          HashMap previewpersonfieldValuesMapEO = new HashMap();
                                                                         if(soMergePreviewMap != null) {
                                                                          mergedSOMap = soMergePreviewMap;
                                                                          previewpersonfieldValuesMapEO = (HashMap) mergedSOMap.get("SYSTEM_OBJECT");
                                                                          styleclass ="blue";
                                                                         } 
                                                                
                                                                %>
                                                                       
                                                            <div id="previewmainEuidContent" class="blue">
                                                                <table border="0" cellspacing="0" cellpadding="0" id="<%=soHashMap.get("LID")%>">
                                                                    <tr>
                                                                        <td id="previewmenu" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                                    </tr> 
                                                                    <tr>
                                                                            <td valign="top"  id="previewcurve">
																			<%if(soMergePreviewMap != null) {%>
                                                                             <span title ="<%=previewpersonfieldValuesMapEO.get("LID")%>" class="dupbtn" >
																			   <b><%=previewpersonfieldValuesMapEO.get("LID")%></b>
																			  </span>
																			<%} else {%>
																			   &nbsp;
																			<%}%>
																			</td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                        <div id="previewmainEuidContentButtonDiv">
                                                            <div id="assEuidDataContent">
                                                                <div id="personassEuidDataContent" class="blue">
                                                                    <table border="0" cellspacing="0" cellpadding="0" id="previewbuttoncontent<%=soHashMap.get("LID")%>">
                                                                        <%

                                                        String previewepathValue = new String();
                                                        for (int ifcp = 0; ifcp < roorNodeFieldConfigs.length; ifcp++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifcp];
                                                            if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                previewepathValue = fieldConfigMap.getFullFieldName();
                                                            } else {
                                                                previewepathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                            }
                                                        
                                                                        %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if(soMergePreviewMap != null) {%>
                                                                                     <%if (previewpersonfieldValuesMapEO.get(previewepathValue) != null) {%> 
                                                                                          <span id="<%=previewepathValue%>"><%=previewpersonfieldValuesMapEO.get(previewepathValue)%></span>
                                                                                    <%} else {%>
                                                                                        <span id="<%=previewepathValue%>">&nbsp;</span>
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                            </td>
                                                                        </tr>
                                                                        <%}%>
                                                             
                                                                    <tr><td>&nbsp;</td></tr>
                                                                   <%
                                                                   HashMap minorObjectHashMapPreview = new HashMap();
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
																	 ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
																   %>
                                                                    <%
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    int maxMinorObjectsMAX  = compareDuplicateManager.getSOMinorObjectsMaxSize(newSoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                                    int  maxMinorObjectsMinorDB =  (soMergePreviewMap  != null) ?((Integer) mergedSOMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue():0;
								    int  maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;
								    mergePreviewMinorObjectList = (soMergePreviewMap  != null)?(ArrayList) mergedSOMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList"):new ArrayList();
                                                                   %>
                                                                    <tr><td>
																		    <% if(maxMinorObjectsMinorDB ==0) {%>
																	         <b><%=bundle.getString("source_inpatient2_text")%> <%=childObjectNodeConfig.getName()%></b>
																			 <%}else{%>
																	         &nbsp;
																			 <%}%>
																	  </td></tr>

                                                                    <%
                                                                     for(int ar = 0; ar < mergePreviewMinorObjectList.size() ;ar ++) {
                                                                       minorObjectHashMapPreview = (HashMap) mergePreviewMinorObjectList.get(ar);                                                                    %>
                                                                  
								    <%
                               		 		            for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
								       previewepathValue = fieldConfigMap.getFullFieldName();
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if(soMergePreviewMap != null) {%>
                                                                                    <%if (minorObjectHashMapPreview.get(previewepathValue) != null) {%> 
                                                                                     <span id="<%=previewepathValue%>">
																						<%if(fieldConfigMap.isKeyType()) {%>
                                                                                          <b><%=minorObjectHashMapPreview.get(previewepathValue)%></b>
																					   <%}else {%>
																					      <%=minorObjectHashMapPreview.get(previewepathValue)%>
																					   <%}%>
  																					  </span>
                                                                                    <%} else {%>
                                                                                        <span id="<%=previewepathValue%>">&nbsp;</span>
                                                                                    <%}%>
                                                                                
                                                                                <%}else{  %>
                                                                                    &nbsp;
                                                                                <%} %>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // field config loop
								    %>

								    <%
                                                                      } // max minor objects loop
								     %>

                                    <%
								    for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								   %>

								   <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
                                                                        }//Extra minor objects loop
								  %>

                                                                      
							       <%
                                                                     } // all child nodes loop
								   %>

                                                                    </table>
                                                                </div>
                                                                
                                                                <!--Displaying view sources and view history-->
                                                                
                                                            </div>
                                                        </div>
                                                    </div>
                                                                  </td>
                                                               <%}%>
                                                                <td>&nbsp;</td>                                                
                                                              <%}%>
                                                             
                                                          </tr>
                                                      </table>
                                                    </div>
                                                 </td>
                                             </tr>   

                                             <tr>
                                                 <td>
                                                     <table width="100%" cellpadding="0" cellspacing="0">
                                                       <tr>
                                                          <td colspan="<%=soHashMapArrayListObjects.length*2 + 3%>">
                                                             <div class="blueline">&nbsp;</div>
                                                          </td>   
                                                       </tr>
                                                      </table>
                                                 </td>
                                             </tr>
                                           <tr>
                                               <td>
                                                   <div id="actionmainEuidContent" class="actionbuton">
                                                   <table cellpadding="0" cellspacing="0" border="0">
												   <tr>
                                           <% ValueExpression soValueExpressionMerge = null;
                                           for (int countEnt = 0; countEnt < soHashMapArrayListObjects.length; countEnt++) { 
                                               HashMap soHashMapMerge = (HashMap) soHashMapArrayListObjects[countEnt];
											   HashMap soHashMap = (HashMap) soHashMapMerge.get("SYSTEM_OBJECT");
                                               soValueExpressionMerge = ExpressionFactory.newInstance().createValueExpression(soHashMap, soHashMap.getClass());

                                               %>


 

                                               <% if (countEnt == 0)    { %>
                                                    <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                               <% }%>
                                                     <!--Displaying view sources and view history-->
                                                     <td valign="top">
                                                         <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                                    <table border="0" cellspacing="0" cellpadding="0" border="0">
                                                                             <tr> 
                                                                                <td valign="top">
                                                                                 <%if(operations.isEO_SearchViewSBR()) {%>
                                                                                 <a href="javascript:void(0)"  
                                                                                       onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&showEuid=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&rand=<%=rand%>','sourceRecordEuidDiv','');"   
																					   title="<h:outputText value="#{msgs.source_rec_vieweuid_but}"/>"  class="button" ><span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                                    </a>                                                    
																					<%}%>
                                                                                </td>                                              
                                                                            </tr>
                                                                     </table>
                                                            </div> 
                                                     </td>
                                               <% if (countEnt + 1 == soHashMapArrayListObjects.length) { 
																			String keepLid1 = bundle.getString("source_keep_btn") + " " + localIdDesignation + " 1" ;
																			 ValueExpression keepLid1ValueExpression = ExpressionFactory.newInstance().createValueExpression(keepLid1, keepLid1.getClass());
																			String keepLid2 = bundle.getString("source_keep_btn") + " " + localIdDesignation + " 2" ;
ValueExpression keepLid2ValueExpression = ExpressionFactory.newInstance().createValueExpression(keepLid2, keepLid2.getClass());
																			%>
                                                     <td>                                                                <!--Displaying view sources and view history-->
                                                         <div id="previewActionButton">
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <form  id="previewlid1Form">
 
                                                                                 <%if(operations.isSO_Merge()) {%>
                                                                                 <a href="javascript:void(0)"  id="previewlid1Form:lid1Link"
                                                                                       onclick="javascript:getDuplicateFormValues('basicMergeformData','previewlid1Form');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&mergePreview=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&rand=<%=rand%>','sourceRecordMergeDiv','');"   
																					   title="<%=keepLid1%>"  class="button" ><span id="LID1"> <%=keepLid1%>  </span>
                                                                                    </a>                                                    
																					<%}%>
                                                                                    <input type="hidden" id="previewlid1Form:previewhiddenLid1" title="PREVIEW_SRC_DEST_LIDS"  />
                                                                                    <input type="hidden" id="previewlid1Form:previewhiddenLid1source" title="PREVIEW_SYSTEM_CODE"  />

                                                                                </form>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                <form id="previewlid2Form">
                                                                                  <%if(operations.isSO_Merge()) {%>
                                                                                 <a href="javascript:void(0)"  id="previewlid2Form:lid2Link"
                                                                                       onclick="javascript:getDuplicateFormValues('basicMergeformData','previewlid2Form');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&mergePreview=true&SYSTEM_CODE=<%=soHashMap.get("SYSTEM_CODE")%>&LID=<%=soHashMap.get("LID")%>&rand=<%=rand%>','sourceRecordMergeDiv','');"   
																					   title="<%=keepLid2%>"  class="button" ><span id="LID2"> <%=keepLid2%>  </span>
                                                                                    </a>                                                    
																					<%}%>
                                                                                    <input type="hidden" id="previewlid2Form:previewhiddenLid2" title="PREVIEW_SRC_DEST_LIDS"  />
                                                                                    <input type="hidden" id="previewlid2Form:previewhiddenLid2source" title="PREVIEW_SYSTEM_CODE"  />
                                                                                </form>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>  
                                                         <div id="confirmationButton" style="visibility:hidden">
                                                                <%if(operations.isSO_Merge()) {%>
                                                                    <table>
                                                                        <tr>
                                                                            <td>
                                                                                <a title='<h:outputText value="#{msgs.ok_text_button}" />' class="button" href="javascript:void(0)" onclick="javascript:showLIDDiv('mergeDiv',event)" > 
                                                                                   <span id="confirmok"><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                                </a>
                                                                            </td>
                                                                            <td>
                                                                            <a title="<h:outputText value="#{msgs.cancel_but_text}" />"
                                                                              href="javascript:void(0)"
                                                                             onclick="javascript:getFormValues('basicMergeformData');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&save=true&rand=<%=rand%>','sourceRecordMergeDiv','');"  
                                                                             class="button" ><span><h:outputText value="#{msgs.cancel_but_text}" /></span></a>                                      
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                              <%}%>
                                                         </div>  
 
                                                     </td>
                                               <%}%>
                                               
                                            <%}%>
											    </tr>
                                                   </table>
                                               </div>
                                                </td>
                                           </tr>
                                            </table>
 <%} else {%>
   <% 
     if(validationFailed) { //If validation fails display the error message here
    %>
     <div class="ajaxalert">
	  <table>
			<tr>
				<td>
				      <ul>
 				             <li><%=validtionMessage%></li>
  				      </ul>
				<td>
			<tr>
		</table>
	</div>
  <%} else {%>
    <div class="ajaxalert">
	  <table>
			<tr>
				<td>
				      <ul>
			            <% while (messagesIter.hasNext())   { %>
				             <li>
								<% FacesMessage facesMessage  = (FacesMessage)messagesIter.next(); %>
								<%= facesMessage.getSummary() %>
				             </li>
						 <% } %>
				      </ul>
				<td>
			<tr>
		</table>
	</div>
  <%}%>

<%}%>

<%}%> <!-- if duplicate LIDS not found-->


           
           <%}%> <!-- if session is active -->

</html>
</f:view>
