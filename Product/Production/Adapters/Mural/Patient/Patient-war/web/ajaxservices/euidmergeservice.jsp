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
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%
 double rand = java.lang.Math.random();
String URI = request.getRequestURI();
URI = URI.substring(1, URI.lastIndexOf("/"));
%>

<f:view>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));

}
%>

 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
         <title><h:outputText value="#{msgs.application_heading}"/></title>
           <!--there is no custom header content for this example-->
     </head>
<%
 //Author Rajani Kanth
 //rkanth@ligaturesoftware.com
 //http://www.ligaturesoftware.com
 //Date Created : 03-July-2008
 //This page is an Ajax Service, never to be used directly from the Faces-confg.
%>

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
<%          Enumeration parameterNames = request.getParameterNames();
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();
            SourceHandler sourceHandler = new SourceHandler();
            PatientDetailsHandler patientDetailsHandler= new PatientDetailsHandler();
			//Eo Multi merge preview hashmap 
            HashMap eoMultiMergePreview = null;
            HashMap previewEuidsHashMap = new HashMap();
            Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 

            Object[] personConfigFeilds = sourceHandler.getPersonFieldConfigs().toArray();
            HashMap personfieldValuesMapEO = new HashMap();
            int countEnt = 0;

            Operations operations = new Operations();
            ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
            
			//Variables for EUID  merge preview 
			String mergePreviewStr = request.getParameter("mergePreview");
			boolean isMergePreview = (null == mergePreviewStr?false:true);
           
		    //Variables for euid merge  
            String mergeFinalStr = request.getParameter("mergeFinal");
            boolean isMergeFinal = (null == mergeFinalStr?false:true);

			//Variables for EUID  merge preview 
			String cancelMultiMergeStr = request.getParameter("cancelMultiMerge");
			boolean isCancelMultiMerge = (null == cancelMultiMergeStr?false:true);
            

			//Variables for resolve
            String resolveDuplicateStr = request.getParameter("resolveDuplicate");
            boolean isResolveDuplicate = (null == resolveDuplicateStr?false:true);

			//unresolveDuplicate
            String unresolveDuplicateStr = request.getParameter("unresolveDuplicate");
            boolean isUnResolveDuplicate = (null == unresolveDuplicateStr?false:true);

			HashMap eoMultiMergePreviewMap = new HashMap();
            HashMap mergePersonfieldValuesMapEO = new HashMap();
			String previewEuidValue = new String();
			String checkEuidValue = new String();

			//populate Merge Fields variables

			String  populateMergeFieldsStr = request.getParameter("populateMergeFields");
			boolean isPopulateMergeFields = (null == populateMergeFieldsStr?false:true);

			%>
            <%if(isCancelMultiMerge) {%>
             <%if(request.getParameter("euids") != null) {%>
             	<script>
               	 ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&euids=<%=request.getParameter("euids")%>','mainDupSource','');
              	</script>
             <%} else {%>
             	<script>
               	  ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>','mainDupSource','');
              	</script>
             <%}%>
            <table>
            <tr>
              <td>
               <script> 
  	               euids="";
                   euidArray = [];
                    alleuidsArray = [];
 		       </script>
              </td>
             </tr>
            </table>

            <%}%>
            <%
				
			ArrayList eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");

		    //ArrayList newSoArrayList= (isPopulateMergeFields ) ? null : (isMergeFinal)?sourceMergeHandler.mergeSystemObject():sourceMergeHandler.sourcerecordMergeSearch();

 			if (request.getParameter("euids") != null) {
				String[]  compareEuids = request.getParameter("euids").split(",");
                eoArrayList = patientDetailsHandler.buildCompareEuids(compareEuids);
 				session.setAttribute("comapreEuidsArrayList",eoArrayList);
            }

			if (request.getParameter("fromPage") != null && request.getParameter("duplicateEuids") != null ) {
 				SearchDuplicatesHandler searchDuplicatesHandler = new SearchDuplicatesHandler();
				//build the arraylist of compare euids and navigate o the compare duplicates page.
                 searchDuplicatesHandler.buildCompareDuplicateEuids(request.getParameter("duplicateEuids"));   
				 eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList"); 
            }
			
			%>

<%if(isPopulateMergeFields) {
     
	String POPULATE_KEY  = request.getParameter("POPULATE_KEY");
    String POPULATE_VALUE  = ("null".equalsIgnoreCase(request.getParameter("POPULATE_VALUE"))) ? null: request.getParameter("POPULATE_VALUE");
    String POPULATE_VALUE_DESC  = ("null".equalsIgnoreCase(request.getParameter("POPULATE_VALUE_DESC"))) ? null: request.getParameter("POPULATE_VALUE_DESC");
	HashMap destnRootNodeHashMap = (session.getAttribute("destnRootNodeHashMap") != null )? (HashMap)session.getAttribute("destnRootNodeHashMap") :new HashMap() ;
    
	 
    destnRootNodeHashMap.put(POPULATE_KEY,POPULATE_VALUE);
	//set the key and values in the session for updating the EO root node values
	session.setAttribute("destnRootNodeHashMap",destnRootNodeHashMap);
  
 %>
 <%}%>
 


	
<%						    
          
	     if(isMergePreview) {
              
			  String[] srcDestnEuids = request.getParameter("PREVIEW_SRC_DESTN_EUIDS").split(",");
 
              eoMultiMergePreview = patientDetailsHandler.previewMultiMergeEnterpriseObject(srcDestnEuids);
			  messagesIter = FacesContext.getCurrentInstance().getMessages(); 
 		   %>
           <% if(eoMultiMergePreview != null ) {%>
               <%if (eoMultiMergePreview.get(PatientDetailsHandler.CONCURRENT_MOD_ERROR) != null ) {
			   eoArrayList.clear();

			   
                %> <!-- Clear all the values in the arraylist here -->
              <table>
              <tr>
                <td>
               		<script> 
 			            alert("EUID '<%=srcDestnEuids[0]%>' <%=bundle.getString("concurrent_mod_text")%> ");
						if((fromPage != null && fromPage.length > 0 ) && (duplicateEuids != null && duplicateEuids.length > 0 ) ) {
                           window.location = '/<%=URI%>/compareduplicates.jsf?fromPage='+fromPage+'&duplicateEuids='+duplicateEuids;
						 } else {
 		                    window.location = '/<%=URI%>/compareduplicates.jsf?euids='+alleuidsArray;
						}
              	    </script>
                   </td>
               </tr>
             </table>
             
             <%} else {%>

			<%    for(int i = 0;i< srcDestnEuids.length;i++) {
				  previewEuidsHashMap.put(srcDestnEuids[i],srcDestnEuids[i]);
			    }
              
			%>
               <!-- Initialize the all global euids js varaibles here -->
              <table>
              <tr>
                <td>
                 <script> 
  	               euids="";
                   euidArray = [];
                    alleuidsArray = [];
					//merge_destnEuid
					 
		          </script>
                 </td>
               </tr>
             </table>
			<%}%>

			
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

        <%}
			}%>
   


<%			
  boolean isConcurrentModification = true;
	     if(isMergeFinal) {
              
			  String[] srcDestnEuids = request.getParameter("MERGE_SRC_DESTN_EUIDS").split(",");
 
              eoArrayList = patientDetailsHandler.multiMergeEnterpriseObject(srcDestnEuids) ;
			  messagesIter = FacesContext.getCurrentInstance().getMessages(); 

              
			
  %>

            <% if(eoArrayList != null ) {
				Object[] eoArrayListObjects = eoArrayList.toArray();
				HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
    			
				%>
              <%if (eoHashMapValues.get(PatientDetailsHandler.CONCURRENT_MOD_ERROR) != null ) {%>
			  <table>
              <tr>
                <td>
               		<script> 
					    //merge_destnEuid
					    document.getElementById('mergeDiv').style.visibility ='hidden';
					    document.getElementById('mergeDiv').style.display ='none';
  			            alert("EUID '<%=srcDestnEuids[0]%>' <%=bundle.getString("concurrent_mod_text")%> ");
 		                window.location = '/<%=URI%>/compareduplicates.jsf?euids='+alleuidsArray;
  				      </script>
                  </td>
               </tr>
             </table>
             <%eoArrayList.clear();
					  %> <!-- Clear all the values in the arraylist here -->
             <%} else {%>

            <%  session.removeAttribute("destnRootNodeHashMap");
              
			%>
            <!-- Initialize the all global euids js varaibles here -->
            <table>
            <tr>
              <td>
					<%StringBuffer message = new StringBuffer("<font style=\'padding-top:100px;color:green;\'>");
						for (int i = 1; i<srcDestnEuids.length; i++) {
						    message.append(srcDestnEuids[i]).append(",");
					    }
						message.replace(message.lastIndexOf(","),message.lastIndexOf(",")+1," ");
						message.append(bundle.getString("so_merge_confirm_text")).append(" ");
						message.append(srcDestnEuids[0]);
					    message.append("</font>");
 					%>

               <script> 
  	               euids="";
                   euidArray = [];
                    alleuidsArray = [];
					document.getElementById('populatePreviewDiv').innerHTML = "<%=message%>";
					document.getElementById('mergeDiv').style.visibility ='hidden';
					document.getElementById('mergeDiv').style.display ='none';				      
		       </script>
              </td>
             </tr>
            </table>
           <%}%> <!-- Concurrent modification error -->
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

    <%if(isResolveDuplicate) {%>
     <%    
           eoArrayList = patientDetailsHandler.resolvePotentialDuplicate(request.getParameter("resolvePotentialDuplicateId"),request.getParameter("resolveType")) ;
		   messagesIter = FacesContext.getCurrentInstance().getMessages(); 
		%>
            <% if(eoArrayList != null ) {%>
            <!-- Initialize the all global euids js varaibles here -->
            <table>
            <tr>
              <td>
               <script> 
  	               euids="";
                   euidArray = [];
                    alleuidsArray = [];
					//merge_destnEuid
					document.getElementById('resolvePopupDiv').style.visibility ='hidden';
					document.getElementById('resolvePopupDiv').style.display ='none';
					 
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
   <%}%>  <!-- if resolve condition ends here  -->

    <%if(isUnResolveDuplicate) {
           eoArrayList = patientDetailsHandler.unresolvePotentialDuplicate(request.getParameter("unresolvePotentialDuplicateId")) ;
		   messagesIter = FacesContext.getCurrentInstance().getMessages(); 
		%>
            <% if(eoArrayList != null ) {%>
            <!-- Initialize the all global euids js varaibles here -->
            <table>
            <tr>
              <td>
               <script> 
  	               euids="";
                   euidArray = [];
                   alleuidsArray = [];
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

   <%}%>  <!-- if un resolve condition ends here  -->
 

            <%//eoMultiMergePreview
			HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;

            if (!isPopulateMergeFields && eoArrayList != null) {
            %>  
                        <table cellspacing="0" cellpadding="0" border="0">
                        <tr>
                            <td>
                                <div style="height:500px;overflow:auto;">
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            <!-- Display the field Names first column-->
                                            <!--end displaying first column-->       
                                           <%
                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                            String dupHeading = "Main Euid";
                                            String cssMain = "maineuidpreview";
                                            String cssClass = "dynaw169";
                                            String cssHistory = "differentHistoryColour";
                                            String cssSources = "differentSourceColour";
                                            String cssDiffPerson = "differentPersonColour";
                                            String menuClass = "menutop";
                                            String dupfirstBlue = "dupfirst";
                                            String styleClass="yellow";
                                            String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                            String mainEUID = new String();
                                            if (eoArrayListObjects.length == 1) {
                                              styleClass = "blue";
                                            }
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                HashMap codesValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT_CODES");

                                              //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);

                                                //int weight = (eoHashMapValues.get("Weight") != null)?((Float) eoHashMapValues.get("Weight")).intValue():0;
                                                String  weight =  (eoHashMapValues.get("Weight") !=null)?eoHashMapValues.get("Weight").toString():"0";

                                                String potDupStatus = (String) eoHashMapValues.get("Status");
												String potDupStatusText = (potDupStatus != null) ? ValidationService.getInstance().getDescription("RESOLVETYPE", potDupStatus):"";
                              
											    String eoStatus  = (String) eoHashMapValues.get("EO_STATUS");
                                                String potDupId = (String) eoHashMapValues.get("PotDupId");
                                                if (countEnt > 0) {
													if (session.getAttribute("eocomparision") != null ) {
                                                        dupHeading = "<b> EO " + (countEnt+1) + "</b>";
													} else {
                                                       dupHeading = "<b> " + countEnt + "<sup>" + subscripts[countEnt] + "</sup> Duplicate</b>";

													}
                                                } else if (countEnt == 0) {

													if (session.getAttribute("eocomparision") != null ) {
                                                       dupHeading = "<b> EO " + (countEnt+1) + "</b>";
													} else {
                                                       dupHeading = "<b> Main EUID</b>";
													}
                                                    mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                }

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
                   %>
                   <%
										//If it is preview
                                        if( eoMultiMergePreview != null  ) {
                                          eoMultiMergePreviewMap = eoMultiMergePreview;
                                          mergePersonfieldValuesMapEO = (HashMap) eoMultiMergePreviewMap.get("ENTERPRISE_OBJECT");
                                          previewEuidValue = (String) mergePersonfieldValuesMapEO.get("EUID");
									      checkEuidValue =  (String) personfieldValuesMapEO.get("EUID");
                                       }    
                    %>
 
                                          <%if (countEnt == 0) {%>
                                            <td  valign="top" align="left">
                                                <div id="outerMainContentDivid" >
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="yellow">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="yellow">
                                                        <div id="assEuidDataContent<%=countEnt%>">
                                                            <div id="personassEuidDataContent" >
                                                                
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td>EUID</td></tr>
																<tr><td><h:outputText value="#{msgs.source_rec_status_but}"/></td></tr>
                                                                    <%

                                                                String mainDOB;
                                                                ValueExpression fnameExpression;
                                                                ValueExpression fvalueVaueExpression;
                                                                String epathValue;

                                                                for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                                                  FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
																    if(!"EUID".equalsIgnoreCase(fieldConfigMap.getDisplayName())) {
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%=fieldConfigMap.getDisplayName()%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                     }
																	}

                                                                    %>
                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

                                                                   %>
                                                                    <tr><td><b style="font-size:12px; color:blue;"><%=childObjectNodeConfig.getName()%> Info</b></td></tr>
                                                                    <%
												                    for (int max = 0; max< maxMinorObjectsMAX; max++) {
                                                                       for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                    %>  
                                                                       <tr>
                                                                          <td>
                                                                              <%=fieldConfigMap.getDisplayName()%>
                                                                          </td>
                                                                       </tr>
                                                                    <%
                                                                      }//FIELD CONFIG LOOP
																	%>
                                                                     <tr><td> &nbsp;</td></tr>

																	 <%
                                                                     } // MAX MINOR OBJECTS LOOP
																	 %>

																	<%
                                                                     } // CHILD OBJECTS LOOP
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>     
                                            <!-- START Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>">
                                                <div style="width:170px;overflow:hidden">
                                                <% if (countEnt > 0 &&  "R".equalsIgnoreCase(potDupStatus) ) {  %>
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="deactivate" >
                                                <%} else if (countEnt > 0 && "A".equalsIgnoreCase(potDupStatus) ){%>        
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="source" >
                                                <%} else {%>    
                                                   <% if (eoArrayListObjects.length == 1 || previewEuidsHashMap.get((String)personfieldValuesMapEO.get("EUID")) != null) {  %>
                                                     <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="blue" >
												    <%} else {%>
													<div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow" >
												   <%}%>
													<script>alleuidsArray.push('<%=personfieldValuesMapEO.get("EUID")%>')</script>
                                                <%}%>        
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                            </tr> 
                                                            <tr>
                                                                <td valign="top" align="left" class="dupfirst">
                                                                       <%
                                                                       if (countEnt > 0 && ("A".equalsIgnoreCase(potDupStatus) || "R".equalsIgnoreCase(potDupStatus)) ) {       
                                                                      %>
                                                                          <%=personfieldValuesMapEO.get("EUID")%>
                                                                     <%}else{%>  
                                                                       <%if( eoMultiMergePreview != null || eoArrayListObjects.length == 1) { %> 
                                                                          <span class="dupbtn"  title="<%=personfieldValuesMapEO.get("EUID")%>" >
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                          </span>
																		<%} else {%> <!-- if NOT preview is then display the links-->
                                                                        <a class="dupbtn"  title="<%=personfieldValuesMapEO.get("EUID")%>"
                                                                           id="clickButton<%=personfieldValuesMapEO.get("EUID")%>" 
                                                                           href="javascript:void(0)" 
                                                                           onclick="javascript:accumilateMultiMergeEuids('<%=personfieldValuesMapEO.get("EUID")%>')">
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </a>
																		<%}%>
                                                                     <%}%>                       
                                                               </td>
                                                            </tr>
                                                         </table>
                                                    </div>
                                                </div>
                                      <% if (session.getAttribute("eocomparision") == null && countEnt > 0) {%>
                                         <%
                                            String userAgent = request.getHeader("User-Agent");
                                            boolean isFirefox = (userAgent != null && userAgent.indexOf("Firefox/") != -1);
                                            response.setHeader("Vary", "User-Agent");
                                         %>
                                         <% if (isFirefox) {%>
                                         <div id = "bar" title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />" style = "float:right;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;
                                              border-right: 1px solid #000000;border-top:1px solid #000000;position:relative;right:20px;" >
                                         <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                            <div id = "bar" style = "width:5px;padding-top:35px;position:relative;font-size:10px;" >
                                                 <%=weight%>
                                             </div>                                             
                                         </div>
                                         
                                           <% }else{%>
                                            <div id = "bar" title="<h:outputText value="#{msgs.potential_dup_table_weight_column}" />" style = "margin-left:140px;height:100px;width:5px;background-color:green;border-left: 1px solid #000000;border-right: 1px solid #000000;border-top:1px solid #000000;position:absolute;" >
                                             <div style= "height:<%=100 - new Float(weight).floatValue() %>px;width:5px;align:bottom;background-color:#ededed;" ></div> 
                                         </div>                                             
                                         <div id = "bar" style = "margin-left:135px;padding-top:100px;width:5px;position:absolute;font-size:10px;" >
                                             <%=weight%>
                                         </div> 
                                      
                                         <%}%>
                                                <%}%>    

                                                 <div id="mainEuidContentButtonDiv<%=countEnt%>" class="yellow">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                <% if (countEnt > 0 && "R".equalsIgnoreCase(potDupStatus) ) {%>
                                                     <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="deactivate">
                                                  <%} else if (countEnt > 0 && "A".equalsIgnoreCase(potDupStatus)  ){%>            
                                                    <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="source">
                                                  <%} else {%>            
                                                   <% if (eoArrayListObjects.length == 1 || previewEuidsHashMap.get((String)personfieldValuesMapEO.get("EUID")) != null) {  %>
                                                     <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="blue" >
												    <%} else {%>
													<div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="yellow" >
												   <%}%>
                                                  <%}%>          
                                                                <table border="0" cellspacing="0" cellpadding="0">
									  <% if (session.getAttribute("eocomparision") != null) {
													 %>
                                              <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoStatus)%> </font></td></tr>
										 <%}else {%>
										    <%if(countEnt > 0) {%>
                                                <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(potDupStatusText)%> </font></td></tr>
											  <%} else {%>
                                                <tr><td> &nbsp;</td></tr>
											  <%}%>
										 <%}%>
                                     <%

                                    String mainDOB;
                                    ValueExpression fnameExpression;
                                    ValueExpression fvalueVaueExpression;
                                    String epathValue;
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                        }
                                        
										//Compare the surviving EUID if it is previewed
                                        if( eoMultiMergePreview != null) {         
                                           resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue)); //Compare with other values 
										   resultArrayMapMain.put(epathValue, mergePersonfieldValuesMapEO.get(epathValue));//keep the surviving EO values to compare mergePersonfieldValuesMapEO
										   
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
                                                                                    
                                                                                <div id="highlight<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>" style="background-color:none;">
                                                                                  <%if (eoMultiMergePreview != null) {%> <!-- if preview is then display the links-->
                                                                                 <%if (previewEuidsHashMap.get((String) personfieldValuesMapEO.get("EUID")) != null) {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->

                                                                                    <%if ((resultArrayMapCompare.get(epathValue) != null  && resultArrayMapMain.get(epathValue) != null)  && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
                                                                                    <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&EUID=<%=personfieldValuesMapEO.get("EUID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','populatePreviewDiv','');"   >
                                                                                        <font class="highlight">
                                                                                            <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                     <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                            <%} else {%> 
                                                                                            <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                            <%}%>
                                                                                        </font>
                                                                                    </a>  
                                                                                    <%} else {%>
 																					<%if(resultArrayMapMain.get(epathValue) == null) { %>
                                                                                    <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=personfieldValuesMapEO.get("SYSTEM_CODE")%>&LID=<%=personfieldValuesMapEO.get("EUID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','populatePreviewDiv','');"   >
																					  <font class="highlight">
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                         <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     </font>
																					 </a>
                                                                                   <%} else {%> 
                                                                                       <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                      <%}%>
                                                                                    <%}%>
																				 <%}%>
                                                                       <%} else {%> <!-- When preview is found only highlight the differences from the resulted preview and compare with the ones which are involved in preview  -->
                                                                                       <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                      <%}%>

                                                                       <%}%>
                                                                              <%} else {%> <!--if not [preview -->
                                                                                    <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) && !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {%>
                                                                                     <font class="highlight">
                                                                                        <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                          <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%} else {%> 
                                                                                          <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                        <%}%>
                                                                                    </font>
                                                                                    <%} else {%>

 																					<%if(resultArrayMapMain.get(epathValue) == null) { %>
 																					    <font class="highlight">
																				          <%if(eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%> 
																					        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                          <%}else{%>
                                                                                           <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                        <%}%>
                                                                                       </font>
                                                                                     <%} else {%> 
                                                                                       <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()) {%>                              <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%} else {%> 
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                      <%}%>
                                                                                    <%}%> 


                                                                                    <%}%>
                                                                                        
                                                                                    <%}%>
                                                                                        
                                                                                </div>
                                                                                    
                                                                                <%} else {%> <!-- Not null condition  -->
                                                                                    <%if (eoMultiMergePreview != null) {%> <!-- if preview is then display the links-->
                                                                                        <%if (previewEuidsHashMap.get((String) personfieldValuesMapEO.get("EUID")) != null) {%>
 		                                                                                <%if(mergePersonfieldValuesMapEO.get(epathValue) != null) { %> 
                                                                                        <div id="highlight<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>" style="background-color:none;">		
                                                                                          <a href="javascript:void(0)" onclick="javascript:populateMergeFields('<%=epathValue%>','<%=codesValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get(epathValue)%>','<%=personfieldValuesMapEO.get("EUID")%>:<%=epathValue%>');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&populateMergeFields=true&SYSTEM_CODE=<%=personfieldValuesMapEO.get("SYSTEM_CODE")%>&LID=<%=personfieldValuesMapEO.get("EUID")%>&POPULATE_KEY=<%=epathValue%>&POPULATE_VALUE=<%=codesValuesMapEO.get(epathValue)%>&POPULATE_VALUE_DESC=<%=personfieldValuesMapEO.get(epathValue)%>&rand=<%=rand%>','populatePreviewDiv','');"   >
                                                                                                <font class="highlight">
                                                                                                     <!-- blank image -->
                                                                                                     <img src="./images/calup.gif" border="0" alt="<%=bundle.getString("blank_value_text")%>"/>
                                                                                                 </font>
                                                                                            </a>  
                                                                                        </div>
																						<%} else {%>
																						   &nbsp;
																						<% }%>
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
                      <%
                        }
                       }
                       %>

                           <%
                              for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                               ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
int  maxMinorObjectsMinorDB =  ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


                                                                    ArrayList  minorObjectMapList =  (ArrayList) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    %>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp; 
																			<%}%>
																	   </td>
																	</tr>
 
                                                                    <%
                                                                    for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
																	  minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);
                                                                      for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                       FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                       epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                    <tr>
                                                                        <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
										<%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
     																					<%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
																				   </b>
										<%}else{if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>																				  
                                                                                <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                <%}else {%>
                                                                                <%=minorObjectHashMap.get(epathValue)%>
										<%}}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                  <%
                                                                      } //FIELD CONFIG LOOP
																 %>
                                                                  <tr><td> &nbsp;</td></tr>

                                                                  <%  } // TOTAL MINOR OBJECTS LOOP
																  %>


                                                                  <%
								                                  for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								                                  %>

								                                  <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td> &nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	 %>
                                                                      <tr><td>&nbsp;</td></tr>
																	 <%
                                                                        }//Extra minor objects loop
								                                    %>


                                                                    <%} //MINOR OBJECT TYPES LOOPS
                                                                    %>

                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <!-- END Display the field Values-->
                                            <!--Start displaying the sources-->
                                               <% 
                                               eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");

                                              if(eoSources.size() > 0 ) {
                                                //ArrayList soArrayList = (ArrayList) request.getAttribute("eoSources"+(String)personfieldValuesMapEO.get("EUID"));
                                                personfieldValuesMapEO = new HashMap();
                                                for(int i=0;i<eoSources.size();i++) {
                                                    personfieldValuesMapEO = (HashMap) eoSources.get(i);
                                                    HashMap personfieldValuesMapEOValues = (HashMap) personfieldValuesMapEO.get("SYSTEM_OBJECT");
													String soStatus = (String) personfieldValuesMapEO.get("Status");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupSources<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                    <div style="width:170px;overflow:hidden;">
											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="deactivate" >
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												<div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="transaction" >
												<%} else {%>
												<div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="source" >
												<%}%>
												<table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=personfieldValuesMapEO.get("SYSTEM_CODE")%></td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <b><%=personfieldValuesMapEO.get("LID")%></b>
                                                                    </td>
                                                                </tr>
                                                         </table>
                                                    </div>
                                                </div>
											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                   <div id="mainEuidContentButtonDiv<%=countEnt%>" class="deactivate">
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												   <div id="mainEuidContentButtonDiv<%=countEnt%>" class="transaction">
												<%} else {%>
												<div id="mainEuidContentButtonDiv<%=countEnt%>" class="source">
												<%}%>

                                                   <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent" class="source">

                                                                <table border="0" cellspacing="0" cellpadding="0">
                                              <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(soStatus)%> </font></td></tr>

                                                                    <%
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                        }
                                                                    %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (personfieldValuesMapEOValues.get(epathValue) != null) {%>
                                                                                      <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=personfieldValuesMapEOValues.get(epathValue)%>
                                                                                     <%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    <%
                                        }
                                        }
                                                                    %>

                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                                    ArrayList  minorObjectMapList =  (ArrayList) personfieldValuesMapEO.get("SO" + childObjectNodeConfig.getName() + "ArrayList");

int  maxMinorObjectsMinorDB =  ((Integer) personfieldValuesMapEO.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;

                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    HashMap minorObjectHashMap = new HashMap();
																	%>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp;
																			<%}%>
																	   </td>
																	</tr>

																	<%
                                                                    for (int ii = 0; ii < minorObjectMapList.size(); ii++) {
                       						                           minorObjectHashMap = (HashMap) minorObjectMapList.get(ii);
                                                                    

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
                                                                                   <b>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
 																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
 																				  <%}%>

                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                      } // FIELD CONFIGS LOOP
																    %>
                                                                    <tr><td> &nbsp;</td></tr>
                                                                    <%} // MINOR OBJECTS LOOP FOR THE SO
																	%>

                                                                  <%
								                                  for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								                                  %>

								                                  <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td> &nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	%>
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%   }//Extra minor objects loop
								                                    %>



																   <%}
                                                                    %>

                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%                                                
                                                }
                                              }%>
                                                
                                            <!--END displaying the sources-->
                                            <!--START displaying the History-->
                                               <% 
                                               eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");

                                              if(eoHistory.size() > 0) {
                                               // ArrayList soArrayList = (ArrayList) request.getAttribute("eoHistory"+(String)personfieldValuesMapEO.get("EUID"));
                                                 
                                                for(int i=0;i<eoHistory.size();i++) {
                                                    HashMap objectHistMap = (HashMap) eoHistory.get(i);
                                                    String key = (String) objectHistMap.keySet().toArray()[0];
                                                    String keyTitle = key.substring(0, key.indexOf(":"));
                                                    HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                    HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
													String eoHistStatus = (String) objectHistMapValues.get("EO_STATUS");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                  <div style="width:170px;overflow:hidden;">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="history" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=keyTitle%></td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=objectHistMapValues.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                         </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>" class="history">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent" class="history">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                 <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoHistStatus)%> </font></td></tr>

																	<%
                                    for (int ifc = 0; ifc < rootFieldConfigArray.length; ifc++) {
                                        FieldConfig fieldConfigMap =  rootFieldConfigArray[ifc];
                                        if(!(objScreenObject.getRootObj().getName()+".EUID").equalsIgnoreCase(fieldConfigMap.getFullFieldName())) {
                                            
                                        if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                            epathValue = fieldConfigMap.getFullFieldName();
                                        } else {
                                            epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                        }
                                                                    %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (eoValuesMap.get(epathValue) != null) {%>
                                                                                  <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=eoValuesMap.get(epathValue)%>
                                                                                     <%}%>
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
                                                                                
                                                                            </td>
                                                                        </tr>
                                                                    <%
                                        }
                                        }
                                                                    %>

                                                                   <%
                                                                   
                                                                   for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];

int  maxMinorObjectsMinorDB =  ((Integer) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


																	ArrayList  minorObjectMapList =  (ArrayList) objectHistMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
                                                                    HashMap minorObjectHashMap = new HashMap();
                                                                     //if(minorObjectMapList.size() >0) {
                                                                       //minorObjectHashMap = (HashMap) minorObjectMapList.get(0);
                                                                     //}  
                                                                     FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                    

                                                                   %>
                                                                    <tr>
																	   <td>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	          &nbsp;
																			<%}%>
																	   </td>
																	</tr>
																	<%for(int ar = 0 ; ar <minorObjectMapList.size() ; ar++ ) {
																	  minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                     for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                         FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                         epathValue = fieldConfigMap.getFullFieldName();
                                                                    %>  
                                                                       <tr>
                                                                          <td>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                 <%if(fieldConfigMap.isKeyType()) {%>
                                                                                   <b>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
 																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
 																				  <%}%>
                                                                                <%} else {%>
                                                                                 &nbsp;
                                                                                <%}%>
                                                                          </td>
                                                                      </tr>
                                                                    <%
                                                                      } //FIELD CONFIG LOOP
																	%>
                                                                     <tr><td> &nbsp;</td></tr>

																	<%
																     }  //MINOR OBJECTS LOOP 
                                                                     %>

                                                                  <%
								                                  for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								                                  %>

								                                  <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
													                 %>  
                                                                    <tr><td> &nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	 %>
                                                                     <tr><td> &nbsp;</td></tr>

																	 <%
                                                                        }//Extra minor objects loop
								                                    %>

																	  
                                                                    <%} // TOTAL CHILD OBJECTS LOOP
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%                                                
                                                }
                                              }%>                                            
                                            <!--END displaying the History-->
                                            
                                            <%
                                               if (countEnt + 1 == eoArrayListObjects.length) {
                                                    if( eoMultiMergePreview != null) {
                                                        styleClass = "blue";
                                                    }
                                                %>
                                                <%if( eoMultiMergePreview != null) {%>
                                            <td  valign="top">
                                                   <div id="previewPane" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=styleClass%>">
                                                            <table border="0" width="100%" cellspacing="0" cellpadding="0">
                                                                <tr>
                                                                    <td width="100%" class="menutop"><h:outputText value="#{msgs.preview_column_text}" /></td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" >
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                                    <tr>
                                                                        <td>
                                                                          <%
                                                                            if( eoMultiMergePreview != null  ) {
                                                                            %>
                                                                                <b><%=mergePersonfieldValuesMapEO.get("EUID")%></b>
                                                                             <%} else {%>       
                                                                                 &nbsp;
                                                                             <%}%>       
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                          <%
                                                                            if( eoMultiMergePreview != null  ) {
                                                                            %>
                                                                                <b>&nbsp;</b>
                                                                             <%} else {%>       
                                                                                 &nbsp;
                                                                             <%}%>       
                                                                        </td>
                                                                    </tr>
                                                           
                                                                    <%
                                                                     for (int ifc = 0; ifc < personConfigFeilds.length; ifc++) {
                                                                      FieldConfig fieldConfigMap = (FieldConfig) personConfigFeilds[ifc];
                                                                       if (fieldConfigMap.getFullFieldName().startsWith(objScreenObject.getRootObj().getName())) {
                                                                         epathValue = fieldConfigMap.getFullFieldName();
                                                                       } else {
                                                                         epathValue = objScreenObject.getRootObj().getName() + "." + fieldConfigMap.getFullFieldName();
                                                                       }
                                                                      %>  
                                                                    <tr>
                                                                        <td>
                                                                            <%
                                                                            if( eoMultiMergePreview != null  ) {
                                                                            %>
                                                                              
                                                                              <%if(mergePersonfieldValuesMapEO.get(epathValue) != null ) {%>
                                                                               <span id="<%=epathValue%>">
                                                                                      <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=mergePersonfieldValuesMapEO.get(epathValue)%>
                                                                                     <%}%>
  																			   </span>
                                                                              <%}else{%>
                                                                               <span id="<%=epathValue%>">&nbsp;</span>
                                                                             <%}%>
                                                                             
                                                                             <%}else{%>
                                                                              &nbsp;
                                                                            <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%
                                                                     }
                                                                    %>
                                                                   <%
                                                                  
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
int  maxMinorObjectsMinorDB =  (eoMultiMergePreview != null) ?((Integer) eoMultiMergePreviewMap.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue():0;

int maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;

FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                                 
								                                 ArrayList  minorObjectMapList =  new ArrayList();
								                                if( eoMultiMergePreview != null  ) {
                                                                      minorObjectMapList =  (ArrayList) eoMultiMergePreviewMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
								                                   }
                                                                    HashMap minorObjectHashMap = new HashMap();
																	%>
                                                                     <tr>
																	   <td>
																	   <%if( eoMultiMergePreview != null  ) {%>
																	        <%if(minorObjectMapList.size() == 0) {%>
																			  No <%=childObjectNodeConfig.getName()%>.
																			<%} else {%>
																	         &nbsp;
																			<%}%>
																		 <%} else  {%>
																		   &nbsp;
																		 <%} %>
																	   </td>
																	</tr>

                                                                   <%								
                                                                    for(int ar =0;ar<minorObjectMapList.size();ar++) {
                                                                        minorObjectHashMap = (HashMap) minorObjectMapList.get(ar);
                                                                    

                                                                   %>

                                                                    <tr>
                                                                    <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
                                                                     epathValue = fieldConfigMap.getFullFieldName();

                                                                    %>  
                                                                    <tr>
                                                                        <td>
										                                 <%if( eoMultiMergePreview != null  ) {%>
                                                                                <%if (minorObjectMapList.size() >0 && minorObjectHashMap.get(epathValue) != null) {%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && !operations.isField_VIP() &&  fieldConfigMap.isSensitive()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
  
                                                                                <%} else {%>
                                                                                &nbsp;
                                                                                <%}%>
											                                 <%} else {%>
											                                  &nbsp;
											                                 <%}%>
                                                                        </td>
                                                                    </tr>
                                                                    <%}%> <!-- FILED CONFIG LOOP -->
                                                                     <tr><td>&nbsp;</td></tr>

                                                                      <%}%> <!-- MINOR OBJECTES LIST LOOP -->

                                                                  <%
								                                  for (int iex = 0; iex < maxMinorObjectsDiff; iex++) {							
								                                  %>

								                                  <%
                                                                    for (int ifc = 0; ifc < fieldConfigArrayMinor.length; ifc++) {
                                                                     FieldConfig fieldConfigMap =  fieldConfigArrayMinor[ifc];
																	                               
													                 %>  
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	 %>
                                                                     <tr><td>&nbsp;</td></tr>

																	 <%
                                                                        }//Extra minor objects loop
								                                    %>

   
																	  <%} %> <!-- TOTAL NO OF CHILD OBJECTS  LOOP -->
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>
                                            <%}%>     
                                            
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
                                        <td colspan="<%=eoArrayListObjects.length * 2 + 3%>">
                                            <div class="blueline">&nbsp;</div>
                                        </td>   
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!--BEFORE-->
                        <tr>
                            <td>
                                <div id="actionmainEuidContent" class="actionbuton">
                                    <table cellpadding="0" cellspacing="0" border="0">
									  <tr>
									       <% for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");
                                                eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");

                                                String euid = (String) personfieldValuesMapEO.get("EUID");
                                                String potDupStatus = (String) eoHashMapValues.get("Status");
                                                String potDupId = (String) eoHashMapValues.get("PotDupId");
                                                                                     
                                                ValueExpression euidValueExpression = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());        
                                                
                                                ValueExpression potDupIdValueExpression = null;
                                                ValueExpression eoArrayListValueExpression = null;
                                                if(potDupId != null) {
                                                 potDupIdValueExpression = ExpressionFactory.newInstance().createValueExpression(potDupId, potDupId.getClass());        
                                                }
                                                eoArrayListValueExpression = ExpressionFactory.newInstance().createValueExpression(eoArrayList, eoArrayList.getClass());        

                                                                                                                                                 
                                            %>
											<script>
												 euidValueArraySrc.push('<%=euid%>');
												 euidValueArrayHis.push('<%=euid%>');
 											</script>

                                            <% if (countEnt == 0) {%>
                                              <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                            <%}%>										 
                                            <td align="left">
											<div id="dynamicMainEuidButtonContent<%=countEnt%>" style="padding-left:0px;visibility:visible;display:block;">
                                              <table border="0" cellspacing="0" cellpadding="0" border="0">
                                               <% if (session.getAttribute("eocomparision") == null  && countEnt > 0 ) { %>
                                                <tr> 
												   <td align="left">

                                                      <% if (countEnt > 0 && "A".equalsIgnoreCase(potDupStatus) ||
                                                              "R".equalsIgnoreCase(potDupStatus)) { %>
					                                   <form id="unresolveForm">
													    <input type="hidden" title="unresolvePotentialDuplicateId" id="potentialDuplicateId" value="<%=potDupId%>" >
  							                           <%if(operations.isPotDup_Unresolve()) {%>
                   										<a class="diffviewbtn" title="<h:outputText value="#{msgs.potential_dup_button}"/>"
                                                            href="javascript:void(0)"
                                                            onclick="javascript:getFormValues('unresolveForm');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&unresolveDuplicate=true&rand=<%=rand%>','mainDupSource','');"   >
                                                              <h:outputText value="#{msgs.potential_dup_button}"/>
                                                        </a>  
							                               <%}%>


					                                   </form>
                                                      <%} else  {%> 
												        <%
                                                         String diff_person_heading_text = bundle.getString("diff_person_heading_text");
												         %>
							                               <%if(operations.isPotDup_ResolveUntilRecalc()) {%>
                                                           <a href="javascript:void(0)" class="diffviewbtn" 
                                                              title="<%=diff_person_heading_text%>" 
                                                              onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'<%=potDupId%>')" >
															  <%=diff_person_heading_text%>
                                                            </a>   
							                             <%}%>
													<%}%>
											    </td>												
										  	 </tr>
											<%}%>
                                             <tr> 
											         <%  
													    Operations ops = new Operations();
												        if(ops.isTransLog_SearchView()){

												         %>
                                                      <td valign="top">
                                                          <a class="viewbtn"   title="<h:outputText value="#{msgs.view_history_text}"/>" href="javascript:void(0)" onclick="javascript:showViewHistory('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoSources.size()%>','false',euidValueArrayHis)" >  
 															  <%=bundle.getString("view_history_text")%>
                                                          </a>
                                                      </td>    
													   <% } %>	 
                                              </tr> 
                                              <tr> 
                                                      <td valign="top">
                                                        <a title="<h:outputText value="#{msgs.view_sources_text}"/>" href="javascript:void(0)" onclick="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoHistory.size()%>',euidValueArraySrc)" class="viewbtn">
 														  <%=bundle.getString("view_sources_text")%>
														</a> 
                                                      </td>                                              
                                              </tr>
											  <tr><td>&nbsp;</td></tr>

											</table>
											</div>
											</td> <!-- outer TD-->
                                              <!--START  Extra tds for the sources for -->
                                                 <% for (int sCount = 0; sCount < eoSources.size(); sCount++) {%>
												 <td>
												  <div id="spacer<%=sCount%><%=countEnt%>"  style="visiblity:hidden;display:none;">
												   <table>
													 <tr>
													   <td>
														  <img src="images/spacer.gif" width="172px" height="1px" border="0">
													   </td>
													   </tr>
													 </table>
													</div>
												</td>
												<%}%>
                                        <% if (countEnt + 1 == eoArrayListObjects.length) {%>
                                        <td> <!--Displaying view sources and view history-->
										   <% if( eoMultiMergePreview == null  ) { %>
                                            <div id="mergeEuidsDiv"  style="visibility:hidden;display:none;">
                                                <table>
                                                    <tr>
                                                        <td>
                                                            <form  id="previewForm">
 							                                    <%if(operations.isEO_Merge()) {%>
																 <a href="javascript:void(0)"  class="button" tilte="<h:outputText value="#{msgs.preview_column_text}"/>"
                                                                    onclick="javascript:getFormValues('previewForm');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&mergePreview=true&rand=<%=rand%>','mainDupSource','');"   >
                                                                     <span><h:outputText value="#{msgs.preview_column_text}"/></span>
                                                                 </a>	
																 <%}%>
                                                                 <input type="hidden" id="previewForm:previewhiddenMergeEuids" title="PREVIEW_SRC_DESTN_EUIDS"/>
                                                                <h:inputHidden id="destinationEO" value="#{PatientDetailsHandler.destnEuid}" />
                                                             </form>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>  
											<% } else { %>
                                            <div id="mergeFinalEuidsDiv" style="visiblity:visible;display:block;" >
                                                <table>
                                                    <tr>
                                                        <td>
														   <%if(operations.isEO_Merge()) {%>
                                                             <a class="button"   
                                                               href="javascript:void(0)" 
															   title="<h:outputText value="#{msgs.source_submenu_merge}" />"
                                                               onclick="javascript:document.getElementById('merge_destnEuid').innerHTML= '<%=previewEuidValue%> ?';finalMultiMergeEuids('mergeDiv',event);" >
                                                                    <span><h:outputText value="#{msgs.source_submenu_merge}" /></span>
                                                            </a>
															<%}%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                             <a class="button"   
                                                               href="javascript:void(0)" 
															   title="<h:outputText value="#{msgs.cancel_but_text}"/>"
                                                               onclick="ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&cancelMultiMerge=true','mainDupSource','');" >
                                                                    <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
															</a>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>  
                                           <%}%>       
                                        </td>
                                        
                                        <%}%>

										 <%}%>

  									  </tr>
                                    </table>
                                </div>
                            </td>

						</tr>
                     <!--AFTER-->
                      </table>
                   <%}%>
 
 <%}%> <!-- if session is active -->

</html>


</f:view>
