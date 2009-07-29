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
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>

<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Iterator"  %>

<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.EditMainEuidHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>

<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"%>


 
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
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
             
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/balloontip.js"></script>
            <!-- Required for View Merge Tree -->
            <script type="text/javascript" src="scripts/yui4jsf/yahoo/yahoo-min.js"></script>           
            <!-- Additional source files go here -->
            <script type="text/javascript" src="scripts/yui4jsf/core/yui4jsf-core.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/treeview/treeview-min.js"></script>
            
            <script type="text/javascript" src="scripts/yui4jsf/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/animation/animation-min.js"></script>                        

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
EditMainEuidHandler  editMainEuidHandler   = (EditMainEuidHandler) session1.getAttribute("EditMainEuidHandler");
SourceHandler  sourceHandler   = (SourceHandler)session1.getAttribute("SourceHandler");

ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
String editTitle = bundle.getString("source_rec_edit_but");
String deleteTitle = bundle.getString("source_rec_delete_but");

ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
HashMap allNodeFieldConfigsMap = sourceHandler.getAllNodeFieldConfigs();
String rootNodeName = objScreenObject.getRootObj().getName();
//get Field Config for the root node
FieldConfig[] fcRootArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
MasterControllerService masterControllerService = new MasterControllerService();
SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");

CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
Iterator messagesIter = null ;
String sucessMessage = new String();
//String URI = request.getRequestURI();
//URI = URI.substring(1, URI.lastIndexOf("/"));
//remove the app name 
//URI = URI.replaceAll("/ajaxservices","");

//Variables for load
String isLoadStr = request.getParameter("load");
boolean isLoad = (null == isLoadStr?false:true);

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

//CALL THE SAVE METHOD HERE 
String editEuid = (String) session.getAttribute("editEuid");

//Variables for linking 
String isLinkingString = request.getParameter("linking");
boolean isLinking = (null == isLinkingString?false:true);

//Variables for Unlinking 
String isUnLinkingString = request.getParameter("unlinking");
boolean isUnLinking = (null == isUnLinkingString?false:true);

//Variables for isUnLocking 
String isUnLockingString = request.getParameter("unlocking");
boolean isUnLocking = (null == isUnLockingString?false:true);

//Variables for isLocking 
String isLockingString = request.getParameter("locking");
boolean isLocking = (null == isLockingString?false:true);

//Variables for deactive 
String isdeactiveEOString = request.getParameter("deactiveEO");
boolean isDeactiveEO = (null == isdeactiveEOString?false:true);

//Variables for cancel operation
String isCancelEOEditString = request.getParameter("canceleoedit");
boolean isCacncelEOEdit = (null == isCancelEOEditString?false:true);

//HashMap for the root node fields
HashMap rootNodesHashMap = (HashMap) editMainEuidHandler.getEditSingleEOHashMap().get("ENTERPRISE_OBJECT_CODES");

HashMap rootNodesHashMapOld = (HashMap) editMainEuidHandler.getEditSingleEOHashMapOld().get("ENTERPRISE_OBJECT_CODES");
HashMap rootNodesHashTemp = new HashMap();

//Variables for adding new source fields
String saveString = request.getParameter("save");
boolean isSave= (null == saveString?false:true);

String saveSbrString = request.getParameter("saveSbr");
boolean isSaveSbr= (null == saveSbrString?false:true);


boolean checkOverWrites  = false ;

Operations operations = new Operations();
 //Save Edited Values
//Variables for adding new source fields
String saveEditedValues= request.getParameter("editThisID");

boolean isSaveEditedValues = false;
if (saveEditedValues != null && !("-1".equalsIgnoreCase(saveEditedValues)))   {
	isSaveEditedValues = true;
}

if(isSave) {
	isSaveEditedValues = false;
%>
   <script> 
        setEOEditIndex('-1')
   </script>

<%
}
%>

<f:view>
<body>	

<%   boolean isValidationErrorOccured = false;
     ArrayList requiredValuesArray = new ArrayList();
	 HashMap valiadtions = new HashMap();


while(parameterNames.hasMoreElements() && !isLoad && !isEdit && !isValidate && !isSaveEditedValues)   { %>
               <% String attributeName = (String) parameterNames.nextElement(); %>
               <% String attributeValue = (String) request.getParameter(attributeName); %>
   		   <%

				if (isminorObjSave) { 
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
                      for(int k=0;k<fcArray.length;k++) {

						  //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
    						 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 

                         //--------------------------Is Numeric Validations -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) &&
							   (fcArray[k].getValueType() == 0 || 
							    fcArray[k].getValueType() == 4 || 
							    fcArray[k].getValueType() == 7))   {
                         	 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": " + bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------
						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());								  
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------


						 //--------------------------Validations End -------------------------------------
                     }
				      if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                      if (!isValidationErrorOccured && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
                           thisMinorObject.put(attributeName, attributeValue);  //Add minorObject to the HashMap
					  }
                 }  else if (isSaveSbr) {   //Final Save hence add Root fields to the Hashmap
					  //validate all the mandatory fields before adding to the hashmap
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(rootNodeName);
                      for(int k=0;k<fcArray.length;k++) {

						  //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
    						 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 

                         //--------------------------Is Numeric Validations -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) &&
							   (fcArray[k].getValueType() == 0 || 
							    fcArray[k].getValueType() == 4 || 
							    fcArray[k].getValueType() == 7))   {
                         	 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": " + bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------

						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());								  
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------


						 //--------------------------Validations End -------------------------------------
                     }
					  

				      if (!isValidationErrorOccured && !("rand".equalsIgnoreCase(attributeName)) && 
						  !("save".equalsIgnoreCase(attributeName)) && 
						  !("saveSbr".equalsIgnoreCase(attributeName)) && 
						  !("MOT".equalsIgnoreCase(attributeName)) && 
						  !("listIndex".equalsIgnoreCase(attributeName)) && 
						  !("minorObjSave".equalsIgnoreCase(attributeName)) && 
						  !("editThisID".equalsIgnoreCase(attributeName)) 
						  )  {
						  //if (attributeValue.equalsIgnoreCase("")) { continue; }	   
                          // rootNodesHashMap.put(attributeName, attributeValue); 

						  if (rootNodesHashMap.get(attributeName) == null) {
  						      if (attributeValue.equalsIgnoreCase("")) { 
								  continue; 
							  }	   	  	   
                              rootNodesHashMap.put(attributeName, attributeValue); 
						  } else {
                               if (attributeValue.equalsIgnoreCase("")) { 
                                 rootNodesHashMap.put(attributeName, null); 
							  } else {
                                rootNodesHashMap.put(attributeName, attributeValue); 
							  }
						  }					  
					  }
				 
					  
				 }
			   %>
 <%  } %>
 
  <% if (valiadtions.isEmpty() && isSaveSbr) {%>
   <div class="ajaxsuccess">
   	   	  <table>
			<tr>
				<td>  
				   '<%=rootNodeName%>' <%=bundle.getString("update_root_node_message")%>.
				</td>  
			</tr>
		</table>
    </div>

<%}%>

<!--Validate all the mandatory fields in root node fields-->
<% if (!valiadtions.isEmpty()) {
	Object[] keysValidations = valiadtions.keySet().toArray();
%>
	<div class="ajaxalert">
   	   	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
					       <%for(int i =0 ;i<keysValidations.length;i++) {%>
				             <li>
							   <%=keysValidations[i]%> <%=valiadtions.get((String) keysValidations[i])%>.
				             </li>
							 <%}%>
				      </ul>
				<td>
			<tr>
		</table>
    </div>
<%}%>

<% if (isminorObjSave) {
     thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_BRAND_NEW);
     thisMinorObject.put(MasterControllerService.MINOR_OBJECT_TYPE, request.getParameter("MOT"));
}%>

<%if (!isValidationErrorOccured && isSave) { %>  <!-- Save System Object -->
  <%
     String megredEuid  = compareDuplicateManager.getMergedEuid(editEuid);
	  
  %>
<%if(megredEuid == null) {%> <!-- EUID merge condition-->
	 <% 
       rootNodesHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE);
   
	     // removefield masking here
		if(rootNodesHashMap.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(rootNodesHashMap, rootNodeName);
		ArrayList newFinalMinorArrayList  = new ArrayList();

		// check and modify the updated SBR values only
        editMainEuidHandler.checkAndBuildModifiedSBRValues(editEuid);

		//build modified SBR values hashmap here
		editMainEuidHandler.buildModifiedSBRValues();

        //build modified SBR values hashmap here for the System objects
		 editMainEuidHandler.buildModifiedSystemObjects();
		
   		//this.changedSBRArrayList
		sucessMessage = editMainEuidHandler.performSubmit(); //"EO_EDIT_SUCCESS";
		
        messagesIter = FacesContext.getCurrentInstance().getMessages(); 
		//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%} else if ("EO_EDIT_SUCCESS".equalsIgnoreCase(sucessMessage))  { 
		 //SET ALL THE VALUES HERE
          editMainEuidHandler.getChangedSBRArrayList().clear();//Changed SBR hashmap array 
          editMainEuidHandler.getEditSOHashMapArrayList().clear();//Changed/New System objects hashmap array
          editMainEuidHandler.getEditSOMinorObjectsHashMapArrayList().clear();// ChangedNew Minor Objects hashmap Array
		 %>
			   <!-- // close the Minor objects 
			   // Close the Root node fields
			   // Hide the Save button -->
		  <script> 
            //setEditIndex('-1')
         </script>
		  <% Object[] keysSetMinorObjects  = allNodeFieldConfigsMap.keySet().toArray();
            for(int j = 0 ;j <keysSetMinorObjects.length;j++) {
			   String key = (String) keysSetMinorObjects[j]; 
		   %>
		  <script>
			   //document.getElementById('<%=key%>InnerForm').reset();
		  </script>
          <%if(!key.equalsIgnoreCase(rootNodeName)) {%>
		    <script>
   	           //document.getElementById('<%=key%>buttonspan').innerHTML = 'Save '+ '<%=key%>';
               //document.getElementById('<%=key%>cancelEdit').style.visibility = 'hidden';
               //document.getElementById('<%=key%>cancelEdit').style.display = 'none';

			   //document.getElementById('extra<%=key%>AddDiv').style.visibility = 'hidden';
               //document.getElementById('extra<%=key%>AddDiv').style.display = 'none';

			   //document.getElementById('<%=key%>NewDiv').innerHTML = '';               
		    </script>
		  <%}%>


		  <%
			}
          %>
 		 <script>
			 window.location = "#top";
		     alert('<%=bundle.getString("eo_edit_update_success_text")%>');
             ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
	     </script>


          <%
			  //reset all the fields here for root node and minor objects 
		      } else { //servicelayererror			   
		      
			   %>
		 <script>
			 window.location = "#top";
		 </script>
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
 <%} else {%> <!-- merged euid condition-->
        <table>
         <tr><td>
         <script>
      	     alert("'<%=megredEuid%>'  <%=bundle.getString("active_euid_text")%>  '<%=editEuid%>'.");			   
             window.location = '/<%=URI%>/euiddetails.jsf?euid=<%=megredEuid%>';
         </script>
         </td>
         </tr>
         </table>

 <%}%>
<%}else if (isLoad) {%>
   <script> 
        setEOEditIndex('-1')
        document.getElementById('EO<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+ '<%=request.getParameter("MOT")%>';
   	    document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
        document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none'; 
    </script>

 <!-- Get the minor Objects to display -->
  <%  
	  ArrayList thisMinorObjectListCodes = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EOCODES"+request.getParameter("MOT")+"ArrayList");
	  ArrayList thisMinorObjectList = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EO"+request.getParameter("MOT")+"ArrayList");
  %>	
 <!-- Regenerate the table -->
		  <% 
		  int minorObjCount = 0;
		  for (int i =0 ; i <thisMinorObjectListCodes.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectListCodes.get(i); 
				if (MasterControllerService.MINOR_OBJECT_UPDATE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  {
				     checkOverWrites = compareDuplicateManager.checkOverWrites(editEuid, minorObjectMap);
				} else {
					 checkOverWrites = true;
				}
  			  	FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
				%>
	    					
		    <% if ( i == 0)  { %>
                       <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">

                          <table border="0" " cellpadding="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;">		  		  
                         <input type="hidden" name="minorindex" value="<%=i%>" />
                          <tr>			   
                                 <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {
				                   if(fcArray[k].isRequired()) {
				              %>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
		                      <%}%>
			              </tr>
                    <% } %>
				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" title="<%=editTitle%>" 
											 onclick='javascript:setEOEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
								<% if (checkOverWrites){ %>
								 								   
									  <a href="javascript:void(0)" title="<%=deleteTitle%>"
											 onclick='javascript:ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EOMinorDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
                                <% } %>
                              </td>
							  <% for(int k=0;k<fcArray.length;k++) { %>
   
							 <% if(fcArray[k].isRequired()) { %>
								 <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( editMainEuidHandler.getEditSingleEOHashMap().get("hasSensitiveData") !=null && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
							   <% } %>
						   </tr>	
                    <% } %>
	<% if ( i == thisMinorObjectList.size()-1)  { %>
        </table>  
     </div>
   <% } %>
						   
  <% }  %>

   <% if (thisMinorObjectList.size() == 0 ) { %>
                      <div style="BORDER-RIGHT: #91bedb 1px solid; width:100%;BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;overflow:auto">
                       <table border="0" width="100%" cellpadding="0" style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; 
							   font-size: 10px; text-align: left;">		  		  						 
						  <tr>
						   <td align="center">  <b> <%= request.getParameter("MOT") %> <%=bundle.getString("no_minor_objects_text")%></b></td>
						 </tr>
                       </table>  
                      </div>
    <% } %>
 <!-- End Regenerate -->
<% } else if (isDelete) { %>   <!-- Delete Minor Object  -->
    <script>
    setEOEditIndex('-1')
    document.getElementById('EO<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
    document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none';
    </script>
		  <% 
		  ArrayList thisMinorObjectList = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EOCODES"+request.getParameter("MOT")+"ArrayList");
              thisMinorObject = (HashMap)thisMinorObjectList.get(new Integer(deleteIndex).intValue());
	          String thisminorObjectType = (String)thisMinorObject.get(MasterControllerService.HASH_MAP_TYPE);
	       %>
		   <%  if (thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) { 
		          thisMinorObjectList.remove(new Integer(deleteIndex).intValue());
			   }  else {
                  thisMinorObject.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.MINOR_OBJECT_REMOVE);
			   }
           %>
            
 <!-- Regenerate the table -->
		  <% 
		  int minorObjCount = 0;
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
		  %>
		  <% for (int i =0 ; i <  thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
			     
				if (MasterControllerService.MINOR_OBJECT_UPDATE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  {
				     checkOverWrites = compareDuplicateManager.checkOverWrites(editEuid, minorObjectMap);
				} else {
					 checkOverWrites = true;
				}
 
		  %>
           <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                       <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">

                          <table border="0" " cellpadding="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;">		  		  
                         <input type="hidden" name="minorindex" value="<%=i%>" />
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                 <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {
				                   if(fcArray[k].isRequired()) {
				              %>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
		                      <%}%>
			              </tr>
                    <% } %>
					
				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>
			              <tr class="<%=styleClass%>">
			                    <td valign="center" width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" title="<%=editTitle%>" 
											 onclick='javascript:setEOEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>

  							   <td valign="center" width="14px">							   
								<% if (checkOverWrites){ %>
																   
									  <a href="javascript:void(0)" title="<%=deleteTitle%>"
											 onclick='javascript:ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EOMinorDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
                                <% } %>
                              </td>
							  <% for(int k=0;k<fcArray.length;k++) { %>
  							 <% if(fcArray[k].isRequired()) { %>

                             <td>

								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( editMainEuidHandler.getEditSingleEOHashMap().get("hasSensitiveData") != null  && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>
							   <% } %>
							   <% } %>
						   </tr>	
                    <% } %>
	     <% if ( i == thisMinorObjectList.size()-1)  { %>
           </table>  
         </div>
       <% } %>
   <% } %>
 <!-- End Regenerate -->
<% } else if (!isValidationErrorOccured && isSaveEditedValues) { %>   <!-- this condition has to be before isminorObjectSave  -->
	  <% 
		  ArrayList thisMinorObjectList = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EOCODES"+request.getParameter("MOT")+"ArrayList");
		   thisMinorObject = (HashMap)thisMinorObjectList.get(new Integer(saveEditedValues).intValue());
		  HashMap tempMinorObjectMap = new HashMap();

	  %>
<%    while(parameterNames.hasMoreElements())   { %>
               <% String attributeName = (String) parameterNames.nextElement(); %>
               <% String attributeValue = (String) request.getParameter(attributeName); %>
   		   <%
				      FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
                      for(int k=0;k<fcArray.length;k++) {
                        //--------------------------Validations -------------------------------------					 
						  if(fcArray[k].isRequired() && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) && attributeValue.equalsIgnoreCase("")) {  
                             isValidationErrorOccured = true;
							 //build array of required values here
                             requiredValuesArray.add(fcArray[k].getDisplayName());
							 valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("ERROR_ONE_OF_GROUP_TEXT2"));
						  }							 
                         //--------------------------Is Numeric Validations -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName) &&
							   (fcArray[k].getValueType() == 0 || 
							    fcArray[k].getValueType() == 4 || 
							    fcArray[k].getValueType() == 7))   {
                         	 //Check numeric values
							 if (!sourceHandler.isNumber(attributeValue,fcArray[k].getValueType()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),": "+bundle.getString("number_validation_text"));
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End Is Numeric Validation -------------------------------------
						 //--------------------------Start Check field maskings  -------------------------------------
						 if (fcArray[k].getName().equalsIgnoreCase("EUID")) {continue; } // Ignore validation of EUID
				         if (attributeValue.equalsIgnoreCase("")) { continue; }	   
						 if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0 && fcArray[k].getFullFieldName().equalsIgnoreCase(attributeName))   {
							 //Check numeric values
							 
							 if (!sourceHandler.checkMasking(attributeValue,fcArray[k].getInputMask()))   {
                                  valiadtions.put(fcArray[k].getDisplayName(),bundle.getString("lid_format_error_text") + " " +fcArray[k].getInputMask());								  
								  isValidationErrorOccured = true;
							 }
						 }
                         //--------------------------End field maskings -------------------------------------

                         //--------------------------Validations End -------------------------------------
			         }
			     if (attributeValue.equalsIgnoreCase("rand")) continue;
			     if (attributeValue.equalsIgnoreCase("MOT")) continue;
			     if (attributeValue.equalsIgnoreCase("listIndex")) continue;
			     if (attributeValue.equalsIgnoreCase("editThisID")) continue;
			     if (attributeValue.equalsIgnoreCase("minorObjSave")) continue;

                  //listIndex=1, minorObjSave=save, editThisID=-1,

			     if (! ("rand".equalsIgnoreCase(attributeName) 					                
					   && !"MOT".equalsIgnoreCase(attributeName)  
 					   && !"listIndex".equalsIgnoreCase(attributeName)  
 					   && !"editThisID".equalsIgnoreCase(attributeName)  
 					   && !"minorObjSave".equalsIgnoreCase(attributeName)  
					  ))  {
					 	if (attributeValue.equalsIgnoreCase(""))   {
							attributeValue = null;
						}
					 //Update the values
                     //thisMinorObject.put(attributeName, attributeValue);  //Add minorObject to the HashMap
					 tempMinorObjectMap.put(attributeName, attributeValue);
                 }  

			%>
 <%  } %>

<!--Validate all the mandatory fields in root node fields-->
<%if (!valiadtions.isEmpty()) {
	Object[] keysValidations = valiadtions.keySet().toArray();
	%>
	<div class="ajaxalert">
   	   	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
					       <%for(int i =0 ;i<keysValidations.length;i++) {%>
				             <li>
							   <%=keysValidations[i]%> <%=valiadtions.get((String) keysValidations[i])%>.
				             </li>
							 <%}%>
				      </ul>
				<td>
			<tr>
		</table>
   </div>
<%}%>

	  <% 
		  boolean checkKeyTypes = false;
		  String keyTypeValues = new String();
		  String keyType = new String();
	     //Validate to check the uniqueness of the Address 
		 FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
         for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			 HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);

			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) tempMinorObjectMap.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
                     //CHECK FOR THE KEY TYPE VALUES WITH USER CODES AND VALUE LIST
				     if (fcArrayLocal[k].getValueList() != null){  
				       if (fcArrayLocal[k].getUserCode() != null) {  
						 keyTypeValues = ValidationService.getInstance().getUserCodeDescription(fcArrayLocal[k].getUserCode(),originalValue);
					   } else{
                          keyTypeValues  = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
					  }
					}
					keyType = fcArrayLocal[k].getDisplayName();
				  }
			    }
	           } 
			 }
		 }

         if(checkKeyTypes) {
	%>
	 <div class="ajaxalert">
	  <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  <%=keyType%> <b>'<%=keyTypeValues%>'</b> <%=bundle.getString("duplicate_minor_object_message_text")%>
				      </ul>
				<td>
			<tr>
		</table>
	   </div>
	<%} else {
		if(!isValidationErrorOccured) {
		   //copy the content into the minor objects
	       for(int k=0;k<fcArrayLocal.length;k++) {
		     thisMinorObject.put(fcArrayLocal[k].getFullFieldName(),tempMinorObjectMap.get(fcArrayLocal[k].getFullFieldName()) );
		   }
		 }
	}%>
      

 <!-- Regenerate the table -->
		  <% 
		  int minorObjCount = 0;
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
			
			 for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
				
				if (MasterControllerService.MINOR_OBJECT_UPDATE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  {
				     checkOverWrites = compareDuplicateManager.checkOverWrites(editEuid, minorObjectMap);
				} else {
					 checkOverWrites = true;
				}
 
		  %>
                         <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                       <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">

                          <table border="0" " cellpadding="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;">		  		  
                         <input type="hidden" name="minorindex" value="<%=i%>" />
                          <tr>			   
                                 <td class="tablehead"> &nbsp;</td>
                                <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {
				                   if(fcArray[k].isRequired()) {
				              %>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
		                      <%}%>
			              </tr>
                    <% } %>

				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>

			              <tr class="<%=styleClass%>">
			                    <td valign="center"n width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" title="<%=editTitle%>"
											 onclick='javascript:setEOEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   
							   <td valign="center" width="14px">							   
								<% if (checkOverWrites){ %>
																   
									  <a href="javascript:void(0)" title="<%=deleteTitle%>"
											 onclick='javascript:ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EOMinorDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
                                <% } %>
                              </td>
							  <% for(int k=0;k<fcArray.length;k++) { %>
  							   <% if(fcArray[k].isRequired()) { %>

								  <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if( editMainEuidHandler.getEditSingleEOHashMap().get("hasSensitiveData") != null && !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>							     <% } %>
							   <% } %>
						   </tr>			   
            <% }  %>   <!-- end if mark for delete condition -->
<% if ( i == thisMinorObjectList.size()-1)  { %>
     </table>  
    </div>
<% } %>
<% }  %>

 <!-- End Regenerate -->
<!-- reset the Edit index -->
<%if (!isValidationErrorOccured) {%>
    <script>
      document.getElementById('EO<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+ '<%=request.getParameter("MOT")%>';
	  document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
      document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none'; 
   </script>
   <script> 
    setEOEditIndex('-1')
   </script>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>EOInnerForm').reset();	
       enableallfields('<%=request.getParameter("MOT")%>EOInnerForm');
   </script>
<%}%>

<% } else if (!isSaveEditedValues && isminorObjSave)  { %> <!--Add new Minor objects to EO-->
    <script>
    document.getElementById('EO<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("source_rec_save_but")%> '+ '<%=request.getParameter("MOT")%>';
	document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'hidden';
    document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.display = 'none';

    </script>
		  <% 
		  int minorObjCount = 0;
		
		  ArrayList thisMinorObjectList = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EOCODES"+request.getParameter("MOT")+"ArrayList");
         
         // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
%>

 <% 
     boolean checkKeyTypes = false;
	 String keyTypeValues = new String();
	 String keyType = new String();
	  //Validate to check the uniqueness of the Address 
	 FieldConfig[] fcArrayLocal = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
     if (!isValidationErrorOccured) {
         for(int mo = 0; mo < thisMinorObjectList.size();mo++) {
			 HashMap moHashMap = (HashMap)thisMinorObjectList.get(mo);
			 if(new Integer(saveEditedValues).intValue() != mo) {
	          for(int k=0;k<fcArrayLocal.length;k++) {
			    if("MenuList".equalsIgnoreCase(fcArrayLocal[k].getGuiType()) && fcArrayLocal[k].isKeyType()) {
				  String tempValue = (String) thisMinorObject.get(fcArrayLocal[k].getFullFieldName());
				  String originalValue = (String) moHashMap.get(fcArrayLocal[k].getFullFieldName());
                  if(tempValue.equalsIgnoreCase(originalValue)) {
                   checkKeyTypes = true;
                     //CHECK FOR THE KEY TYPE VALUES WITH USER CODES AND VALUE LIST
				     if (fcArrayLocal[k].getValueList() != null){  
				       if (fcArrayLocal[k].getUserCode() != null) {  
						 keyTypeValues = ValidationService.getInstance().getUserCodeDescription(fcArrayLocal[k].getUserCode(),originalValue);
					   } else{
                          keyTypeValues  = ValidationService.getInstance().getDescription(fcArrayLocal[k].getValueList(), originalValue);
					  }
					}
					keyType = fcArrayLocal[k].getDisplayName();
				  }
			    }
	           } 
			 }
		 }
       
         if(checkKeyTypes) {
	%>
	<div class="ajaxalert">
   	   <table>
			<tr>
				<td>  
				   <%=bundle.getString("validation_error_text")%>
				</td>  
			</tr>
			<tr>
				<td>  
				      <ul>
							  <li><%=keyType%> <b>'<%=keyTypeValues%>'</b> <%=bundle.getString("duplicate_minor_object_message_text")%></li>
				      </ul>
				<td>
			<tr>
		</table>
	</div>   
	<%} else {

	  
	  String thisminorObjectType = (String)thisMinorObject.get(MasterControllerService.HASH_MAP_TYPE);
      //build the hashmap with null values for the newly added minor object to the SBR, this is used create locks for all the fields 
	 if (thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW)) { 
        for(int k=0;k<fcArrayLocal.length;k++) {
	       if(thisMinorObject.get(fcArrayLocal[k].getFullFieldName()) == null ) {
               thisMinorObject.put(fcArrayLocal[k].getFullFieldName(),null);
		   }
	   }
	}
 	//copy the content into the minor objects
    //add to the array list of ONLY when mandatory fields are addded
    thisMinorObjectList.add(thisMinorObject); 
		  
	}
	
   }%>
      

 <!-- Regenerate the table -->
		  <% 
		  // removefield masking here
	      if(thisMinorObject.keySet().size() > 0 ) sourceHandler.removeFieldInputMasking(thisMinorObject, request.getParameter("MOT"));
		
		 for (int i =0 ; i <thisMinorObjectList.size();i++)  { 
			    String styleClass = ((i%2==0)?"even":"odd");
			    HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(i); 
				FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
				
				if (MasterControllerService.MINOR_OBJECT_UPDATE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  {
				     checkOverWrites = compareDuplicateManager.checkOverWrites(editEuid, minorObjectMap);
				} else {
					 checkOverWrites = true;
				}
 
		  %>
            <input type="hidden" name="minorindex" value="<%=i%>" />
	    					
		    <% if ( i == 0)  { %>
                       <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left; overflow:auto">

                          <table border="0" " cellpadding="0" style="width:100%;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;">		  		  
                         <input type="hidden" name="minorindex" value="<%=i%>" />
                          <tr>			   
                                <td class="tablehead"> &nbsp;</td>
                                 <td class="tablehead"> &nbsp;</td>
                             <% for(int k=0;k<fcArray.length;k++) {
				                   if(fcArray[k].isRequired()) {
				              %>
 			                    <td class="tablehead">
				                  <%=fcArray[k].getDisplayName()%>
                                </td>
		                      <%}%>
		                      <%}%>
			              </tr>
                    <% } %>

				<% if (!MasterControllerService.MINOR_OBJECT_REMOVE.equalsIgnoreCase((String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE)))  { %>

			              <tr class="<%=styleClass%>">
                    <td valign="center" width="14px">
								  <% 
									  String minorObjType = request.getParameter("MOT");
								  %>						  

									  <a href="javascript:void(0)" title="<%=editTitle%>"
											 onclick='javascript:setEOEditIndex(<%=i%>);ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&editIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EditMessages","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/edit.gif'></nobr> 
									  </a>
								</td>
							   <td valign="center" width="14px">							   
								<% if (checkOverWrites){ %>
																   
									  <a href="javascript:void(0)" title="<%=deleteTitle%>"
											 onclick='javascript:ajaxMinorObjects("/<%=URI%>/ajaxservices/editeuidminorobjects.jsf?&deleteIndex=<%=i%>&MOT=<%=minorObjType%>","<%=minorObjType%>EOMinorDiv","")'> 
												 <nobr><img border="0" src='/<%=URI%>/images/delete.gif'></nobr> 
									  </a>
                                <% } %>
                              </td>
							  <% for(int k=0;k<fcArray.length;k++) { %>
 
 							 <% if(fcArray[k].isRequired()) { %>
								   <td>
								      <%if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>  <!--if has value-->
                                       	   <%if( fcArray[k].isSensitive()){%>
												<%if(editMainEuidHandler.getEditSingleEOHashMap().get("hasSensitiveData") != null &&  !operations.isField_VIP() ) {%> 
													<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>
												<%} else {%> 
												   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
														  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
															 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														  <%}else{%>
															<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
														 <%}%>
													   <%} else {%> <!-- In other cases-->
													   <%
														String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
														if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
														  if (value != null) {
															 //Mask the value as per the masking 
															 value = fcArray[k].mask(value.toString());
														   }
														} 
														%> 
														 <%=value%>
													   <%}%>
												<%}%>
										   <%} else {%>
											   <%if(fcArray[k].getValueList() != null) {%> <!-- if the field config has value list-->
												  <%if (fcArray[k].getUserCode() != null){%> <!-- if it has user defined value list-->
													 <%=ValidationService.getInstance().getUserCodeDescription(fcArray[k].getUserCode(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												  <%}else{%>
													<%=ValidationService.getInstance().getDescription(fcArray[k].getValueList(), (String) minorObjectMap.get(fcArray[k].getFullFieldName()))%>
												 <%}%>
											   <%} else {%> <!-- In other cases-->
											   <%
												String value = minorObjectMap.get(fcArray[k].getFullFieldName()).toString();   
												if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
												  if (value != null) {
													 //Mask the value as per the masking 
													 value = fcArray[k].mask(value.toString());
												   }
												} 
												%> 
												 <%=value%>
											   <%}%>
										   <%}%>

									  <%} else {%> <!-- else print &nbsp-->
									    &nbsp;
									  <%}%>
										 
										 <input type="hidden" name="<%=fcArray[k].getFullFieldName()%>" value=<%=minorObjectMap.get(fcArray[k].getFullFieldName())%> />
										 
								   </td>						     <% } %>
							   <% } %>
						   </tr>			   
            <% }  %>   <!-- end if mark for delete condition -->
<% if ( i == thisMinorObjectList.size()-1)  { %>
     </table>  
    </div>
<% } %>
<% }  %>

 <!-- End Regenerate -->

<%if (!isValidationErrorOccured) {%>
   <script>
	   document.getElementById('<%=request.getParameter("MOT")%>EOInnerForm').reset();		  
   </script>
<%}%>

<% }  else if (isEdit)  { %>
    
    <script>
      document.getElementById('EO<%=request.getParameter("MOT")%>buttonspan').innerHTML = '<%=bundle.getString("edit_euid")%> '+ ' '+'<%=request.getParameter("MOT")%>';
	  document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.visibility = 'visible';
      document.getElementById('EO<%=request.getParameter("MOT")%>cancelEdit').style.display = 'block'; 
    </script>
  <%  
	          ArrayList thisMinorObjectList = (ArrayList) editMainEuidHandler.getEditSingleEOHashMap().get("EOCODES"+request.getParameter("MOT")+"ArrayList");
		       FieldConfig[] fcArray = (FieldConfig[]) allNodeFieldConfigsMap.get(request.getParameter("MOT"));
			   String formName = request.getParameter("MOT")+"EOInnerForm";
			   int intEditIndex = new Integer(editIndex).intValue();
			 %>
			 <%HashMap minorObjectMap  = (HashMap) thisMinorObjectList.get(intEditIndex);%>
               <!-- Generate the script to populate the user code maskings -->
			       <% for(int k=0;k<fcArray.length;k++) {	
				        String constarintBy = fcArray[k].getConstraintBy();
						if(constarintBy != null && constarintBy.length() > 0) {
				        int refIndex = sourceHandler.getReferenceFields(fcArray,constarintBy);
                        
                        String userInputMask = ValidationService.getInstance().getUserCodeInputMask(fcArray[refIndex].getUserCode(), (String)   minorObjectMap.get(fcArray[refIndex].getFullFieldName()));
                       
			        
				     %>
						<script>
                         userDefinedInputMask = '<%=userInputMask%>';
				         
						</script>
				      <%}%> 
				   <%}%> 
               <!-- Generate the script to populate the form -->
			   <script>
			       <% 
				   	  String thisminorObjectType = (String)minorObjectMap.get(MasterControllerService.HASH_MAP_TYPE);
     		          for(int k=0;k<fcArray.length;k++) {					     
				   %>
					<%
						String value = (minorObjectMap.get(fcArray[k].getFullFieldName())) != null ?minorObjectMap.get(fcArray[k].getFullFieldName()).toString():null;   
                        if (fcArray[k].getInputMask() != null && fcArray[k].getInputMask().length() > 0) {
                          if (value != null) {
                              //Mask the value as per the masking 
                              value = fcArray[k].mask(value.toString());
                          }
                        } 
					%> 

  					    var thisFrm = document.getElementById('<%=formName%>');
                        elemType = thisFrm.elements[<%=k%>].type.toUpperCase();
					<%if( fcArray[k].isSensitive() && editMainEuidHandler.getEditSingleEOHashMap().get("hasSensitiveData") != null && !operations.isField_VIP()){%>
					
					   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>
						<%	if(fcArray[k].isRequired()) {
				       %>

						<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
				       %>
 						  
                            thisFrm.elements[<%=k%>].readOnly = true;
                            thisFrm.elements[<%=k%>].disabled = true;
							thisFrm.elements[<%=k%>].options.selectedIndex = 0;
   
						<%} else {%>
							if(elemType != 'HIDDEN') {
                               thisFrm.elements[<%=k%>].readOnly = true;
                               thisFrm.elements[<%=k%>].disabled = true;
 							   thisFrm.elements[<%=k%>].value = '<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>';
						    }
						<%}%>

					    <%} else {%>
						<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
				       %>
 						  
                            thisFrm.elements[<%=k%>].readOnly = true;
                            thisFrm.elements[<%=k%>].disabled = true;
							thisFrm.elements[<%=k%>].options.selectedIndex = 0;
                            thisFrm.elements[<%=k%>].title = '';
  
						<%} else {%>
							if(elemType != 'HIDDEN') {
                               thisFrm.elements[<%=k%>].readOnly = true;
                               thisFrm.elements[<%=k%>].disabled = true;
 							   thisFrm.elements[<%=k%>].value = '<%=bundle.getString("SENSITIVE_FIELD_MASKING")%>';
                               thisFrm.elements[<%=k%>].title = '';
						    }
						<%}%>
						<%}%>

					   <%}%>
					<%} else {%>

						<%	if(!thisminorObjectType.equalsIgnoreCase(MasterControllerService.MINOR_OBJECT_BRAND_NEW) && fcArray[k].isKeyType()) {
				       %>
						   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%> 
							<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
						   %>
							  
								thisFrm.elements[<%=k%>].readOnly = true;
								thisFrm.elements[<%=k%>].disabled = true;

								for (var i=0; i< thisFrm.elements[<%=k%>].options.length; i++)  {
									if ( (thisFrm.elements[<%=k%>].options[i].value) ==  '<%=value%>')   {
										thisFrm.elements[<%=k%>].options.selectedIndex = i
									}
								 }
 		
							<%} else {%>
								if(elemType != 'HIDDEN') {
								   thisFrm.elements[<%=k%>].readOnly = true;
								   thisFrm.elements[<%=k%>].disabled = true;
								   thisFrm.elements[<%=k%>].value = '<%=value%>'
								}
							<%}%>
						<%}%>

					   <%} else {%>
						   <%  if(minorObjectMap.get(fcArray[k].getFullFieldName()) != null ) {%>
							<%	if("MenuList".equalsIgnoreCase(fcArray[k].getGuiType()) ) {
						   %>
							   if(elemType != 'HIDDEN') {
							  
								for (var i=0; i< thisFrm.elements[<%=k%>].options.length; i++)  {
									if ( (thisFrm.elements[<%=k%>].options[i].value) ==  '<%=value%>')   {
										thisFrm.elements[<%=k%>].options.selectedIndex = i
									}
								 }
							   }

							<%} else {%>
								if(elemType != 'HIDDEN') {
								  thisFrm.elements[<%=k%>].value = '<%=value%>'
								}
							<%}%>
						<%}%>
				   <%}%>

					<%}%>
					
						
		           <%}%>
			   </script>
<% } else if (isLinking){ %> 	<!-- Linking the SBR fields-->
     
     <% String sbrFullFieldName = request.getParameter("sbrfullfieldname");%>
     <% String systemCodeWithLid = request.getParameter("systemCodeWithLid"); 
		String fieldDisplayName = request.getParameter("fieldDisplayName");

		//set linkedFields HashMap By User
		editMainEuidHandler.getLinkedFieldsHashMapByUser().put(sbrFullFieldName,systemCodeWithLid);

		//Save the links selected
		sucessMessage  = editMainEuidHandler.saveLinksSelected();
		messagesIter = FacesContext.getCurrentInstance().getMessages(); 

	  
		//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%}else if("success".equalsIgnoreCase(sucessMessage)) {%> 			  
  <script>

	  document.getElementById('linkSoDiv').style.visibility='hidden';
	  document.getElementById('linkSoDiv').style.display='none';

	  document.getElementById('linkSourceDiv:'+'<%=sbrFullFieldName%>').style.visibility = 'hidden';
      document.getElementById('linkSourceDiv:'+'<%=sbrFullFieldName%>').style.display = 'none';

      // //Person.PersonCatCode:HOSPITAL:1238990001
	 document.getElementById('<%=sbrFullFieldName%>:<%=systemCodeWithLid%>').style.visibility = 'visible';
     document.getElementById('<%=sbrFullFieldName%>:<%=systemCodeWithLid%>').style.display = 'block';
	 
	 window.location = "#top";
 
	 alert(' <%=fieldDisplayName%> <%=bundle.getString("link_field_success_text")%>');
     
	 ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
  </script>

          <% //reset all the fields here for root node and minor objects  
		  } else { //servicelayererror			    %>
		 <script>
			 window.location = "#top";
		 </script>
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



<% } else if (isUnLinking){ %> 	<!-- UnLinking the SO fields-->
     
     <% String sbrFullFieldName = request.getParameter("sbrfullfieldname");
	    String[] keysValues = sbrFullFieldName.split(">>");
		String fieldDisplayName = request.getParameter("fieldDisplayName");
        
	    if(keysValues.length == 2) {
		   //set UnlinkedFields HashMap By User
	       editMainEuidHandler.getUnLinkedFieldsHashMapByUser().put(keysValues[0],keysValues[1]);

		   //Save the links selected
	     	sucessMessage = editMainEuidHandler.saveUnLinksSelected();
            messagesIter = FacesContext.getCurrentInstance().getMessages(); 

		}
			//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%}else if("success".equalsIgnoreCase(sucessMessage)) {%> 			  
 			  
  <script>

    document.getElementById('unLinkSoDiv').style.visibility = "hidden";
    document.getElementById('unLinkSoDiv').style.display = "none";
  
     
	document.getElementById('linkSourceDivData:'+'<%=keysValues[0]%>' ).style.visibility = 'visible';
    document.getElementById('linkSourceDivData:'+'<%=keysValues[0]%>').style.display = 'block';

	  // //Person.PersonCatCode:HOSPITAL:1238990001
	 document.getElementById('<%=keysValues[0]%>:<%=keysValues[1]%>').style.visibility = 'hidden';
     document.getElementById('<%=keysValues[0]%>:<%=keysValues[1]%>').style.display = 'none';


  </script>
  <script>
     window.location = "#top";
	 alert('<%=fieldDisplayName%> <%=bundle.getString("unlink_field_success_text")%>');     ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent',''); 	 
  </script>

          <% //reset all the fields here for root node and minor objects  
		  } else { //servicelayererror			    %>
		 <script>
			 window.location = "#top";
		 </script>
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
 
<% } else if (isLocking){ %> 	<!-- Explicitly  Locking the SBR fields as chosen by the user-->
  <% 
     HashMap sbrHashMap  = new HashMap();
	 String key = request.getParameter("hiddenLockFields");
     String lockValue  = (request.getParameter("hiddenLockFieldValue") != null && request.getParameter("hiddenLockFieldValue").trim().length() > 0)?request.getParameter("hiddenLockFieldValue"):null;

     sbrHashMap.put(key,lockValue);
     sbrHashMap.put(MasterControllerService.HASH_MAP_TYPE, MasterControllerService.SBR_UPDATE);
     
   
	// removefield masking here
	if(sbrHashMap.keySet().size() > 0 && lockValue != null) sourceHandler.removeFieldInputMasking(sbrHashMap, rootNodeName);
     
    //this.changedSBRArrayList     
    ArrayList changedSBRArrayList = new ArrayList();
	//set UnlockedFields HashMap By User
	changedSBRArrayList.add(sbrHashMap);
  
    //Lock the field here.
	sucessMessage = editMainEuidHandler.saveLocksSelected(changedSBRArrayList,null); //"EO_EDIT_SUCCESS";
    messagesIter = FacesContext.getCurrentInstance().getMessages(); 
 		//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%}else if ("EO_EDIT_SUCCESS".equalsIgnoreCase(sucessMessage))  { 
 		 %>
       <script>
		   document.getElementById('lockSBRDiv').style.visibility = 'hidden';
       	   window.location = "#top";
		   //lock_field_success_text
	        alert('<%=request.getParameter("hiddenLockDisplayValue")%> <%=bundle.getString("lock_field_success_text")%>');
            ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
      </script>
 <% //reset all the fields here for root node and minor objects  
   } else { //servicelayererror			    %>
		 <script>
			 window.location = "#top";
		 </script>
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
 
<% } else if (isUnLocking){ %> 	<!-- UnLocking the SBR fields-->
     <% 
	    String sbrFullFieldName = request.getParameter("hiddenUnLockFields");
	 
       
	   //set UnlockedFields HashMap By User
	    editMainEuidHandler.getUnLockedFieldsHashMapByUser().put(sbrFullFieldName,null);

		//Save the Un locks selected
	    sucessMessage = editMainEuidHandler.saveUnLocksSelected();
	
		messagesIter = FacesContext.getCurrentInstance().getMessages(); 

		//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%}else if("success".equalsIgnoreCase(sucessMessage)) {%> 			  
  <table><tr><td>
  <script>
    	    window.location = "#top";
	        alert('<%=request.getParameter("hiddenUnLockFieldDisplayName")%> <%=bundle.getString("unlock_field_success_text")%>');
            ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
  </script>
  </td></tr></table>
 <% //reset all the fields here for root node and minor objects  
   } else { //servicelayererror			    %>
		 <script>
			 window.location = "#top";
		 </script>
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


<% } else if (isDeactiveEO){ %> 	<!-- isDeactiveEO the SBR -->
 <% sucessMessage = editMainEuidHandler.deactivateEO(editEuid); 
    messagesIter = FacesContext.getCurrentInstance().getMessages(); 

 		//CONCURRENT_MOD_ERROR
    %> 
	 <%	if ("CONCURRENT_MOD_ERROR".equalsIgnoreCase(sucessMessage))  { %>
		 <div class="ajaxalert">
	  <table>
			<tr>
				<td>
 				          <script>
 								alert("EUID ' <%=editEuid%> ' <%=bundle.getString("concurrent_mod_text")%> ");
                                window.location = "#top";
                                ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=editEuid%>','ajaxContent','');
				          </script>
 			   <td>
			<tr>
		</table>
		</div>

	 <%}else if("success".equalsIgnoreCase(sucessMessage)) {%> 			  
		 <script>
			 window.location = "#top";
		 </script>

 <% //reset all the fields here for root node and minor objects  
   } else { //servicelayererror			    %>
		 <script>
			 window.location = "#top";
		 </script>
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

<% } else if (isCacncelEOEdit){ %> 	<!-- isCancel Edit EO operation-->
 <% 
   //SET ALL THE VALUES HERE
   editMainEuidHandler.getChangedSBRArrayList().clear();//Changed SBR hashmap array 
   editMainEuidHandler.getEditSOHashMapArrayList().clear();//Changed/New System objects hashmap array
   editMainEuidHandler.getEditSOMinorObjectsHashMapArrayList().clear();// ChangedNew Minor Objects hashmap Array
 
 %>

<% } %>

 </body>
</f:view>
<%} %>  <!-- Session check -->
</html>
