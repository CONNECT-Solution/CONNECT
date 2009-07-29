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
<%@ page import="com.sun.mdm.index.master.MergeResult"  %>
 
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
 <%@ page import="com.sun.mdm.index.edm.presentation.handlers.TransactionHandler"  %>
  
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
 <% String message = request.getParameter("msg");
	if (message != null && message.length() > 0)    {%>
     <script>
	    document.getElementById('messages').innerHTML = "<%=message%>";
     </script>
 <%}%>
<%          Enumeration parameterNames = request.getParameterNames();
            ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
			
			MasterControllerService masterControllerService = new MasterControllerService();

			Iterator messagesIter = FacesContext.getCurrentInstance().getMessages(); 
	
			ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
            TransactionHandler transactionHandler = new TransactionHandler();
            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();

            EPath ePath = null;
            PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();

            Operations operations = new Operations();
            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(ConfigManager.getDateFormat());
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            String transactionId = (String) (request.getParameter("transactionId")==null?request.getAttribute("transactionId"):request.getParameter("transactionId"));
            String function  = (request.getParameter("function") != null)? request.getParameter("function") : "euidMerge";

			String unMergeFinalStr = request.getParameter("unMergeFinal");
            boolean isUnMergeFinal = (null == unMergeFinalStr?false:true);

			//unMergePreview
			String unMergePreviewStr = request.getParameter("unMergePreview");
            boolean isUnMergePreview = (null == unMergePreviewStr?false:true);

            String  unmergeTransactionId = request.getParameter("unmergeTransactionId");
			String mainEuid  = request.getParameter("mainEuid");
            String mainEUID = new String();
			
 

            HashMap unmergeHashMap  = new HashMap();
            HashMap unmergePreviewHashMap  = new HashMap();
			if(isUnMergePreview) {
                //eoArrayList = transactionHandler.getTranscationDetails(unmergeTransactionId);
 				unmergePreviewHashMap  = transactionHandler.previewUnmergeEnterpriseObject(unmergeTransactionId, mainEuid);
				messagesIter = FacesContext.getCurrentInstance().getMessages(); 
                if(unmergePreviewHashMap != null ) {
				  if(unmergePreviewHashMap.get("CONCURRENT_MOD_ERROR") != null ) {%> <!-- If concurrent modification-->
				  <table>
				    <tr>
				    <td>

             	   <script>
 					   alert("<%=unmergeHashMap.get("CONCURRENT_MOD_ERROR")%>");
                 	   ajaxURL('/<%=URI%>/ajaxservices/transactiondetailsservice.jsf?'+'&rand=<%=rand%>&transactionId=<%=unmergeTransactionId%>&function=euidMerge','mainDupSource','');
              	   </script>
				   </td>
				   </tr>
				 </td>
				 </table>

				<%} else {
					eoArrayList.add(unmergePreviewHashMap);

 					%>
 
				<%}%>

				<%} else {%> <!-- If unmerge fails modification-->
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

                
<%                request.setAttribute("comapreEuidsArrayList", eoArrayList);

			}

			%>
 
  
<%
				//Variables for euid merge  
  			 messagesIter = FacesContext.getCurrentInstance().getMessages(); 

            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
			int maxMinorObjectsMAX = 0;
            ValueExpression sourceEUIDVaueExpression = null;
            ValueExpression destinationEUIDVaueExpression = null;
            ValueExpression mergredHashMapVaueExpression = null;
            EnterpriseObject sourceEO = null;
            EnterpriseObject destinationEO = null;
      		
			HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;

            if ( eoArrayList != null && eoArrayList.size() > 0 ) {
            %>  
               <table cellspacing="0" cellpadding="0" border="0">
                         <tr>
                            <td>
                                <div>
                    
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            <%
                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                            String dupHeading = "Main Euid";
                                            String cssMain = "maineuidpreview";
                                            String cssClass = "dynaw169";
                                            String cssHistory = "differentHistoryColour";
                                            String cssSources = "differentSourceColour";
                                            String cssDiffPerson = "differentPersonColour";
                                            String styleTransaction = "transaction";
                                            String menuClass = "menutop";
                                            String dupfirstBlue = "dupfirst";
                                            String styleClass="yellow";
                                            String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                             if (eoArrayListObjects.length == 1) {
                                              styleClass = "blue";
                                            }
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                String eoStatus = (String) eoHashMapValues.get("EO_STATUS");

                                                if ("Add".equalsIgnoreCase(function)) {
                                                        dupHeading = "<b>" + function + "</b>";
                                                } else if ("euidMerge".equalsIgnoreCase(function) || "lidMerge".equalsIgnoreCase(function) ){
                                                    if (countEnt == 1) {
														String mergedLabel = bundle.getString("merged_euid_label");
														dupHeading ="<b>"+mergedLabel+"</b>";

                                                        //dupHeading = "<b>Merged EUID</b>";
                                                    } else  if (countEnt >= 2) {
														String afterMergedLabel = bundle.getString("after_merged_euid_label");
                                                       // dupHeading = "<b>After Merged MAIN EUID</b>";
													   dupHeading = "<b>"+afterMergedLabel+"</b>";
                                                    } else 
                                                        if (countEnt == 0) {
														String beforeMergedLabel = bundle.getString("before_merged_euid_label");
                                                        //dupHeading = "<b>Before Merged MAIN  EUID</b>";
														dupHeading = "<b>"+beforeMergedLabel+"</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                } else if ("euidUnMerge".equalsIgnoreCase(function) || "lidUnMerge".equalsIgnoreCase(function)) {
                                                    if (countEnt == 1) {
														String afterUnmergedLabel = bundle.getString("after_unmerged_euid_label");
														dupHeading = "<b>"+afterUnmergedLabel +"</b>";
                                                        //dupHeading = "<b>After UnMerge MAIN EO </b>";
                                                    } else if (countEnt >=2) {
														String unmergedLabel = bundle.getString("unmerged_euid_label");
														dupHeading ="<b>"+unmergedLabel+"</b>";
                                                        //dupHeading = "<b> UnMerged EO  </b>";
                                                    }
													else if (countEnt == 0) {
														String beforeUnmergedLabel = bundle.getString("before_unmerged_euid_label");                                                        
														dupHeading = "<b>"+beforeUnmergedLabel+"</b>";

                                                       // dupHeading = "<b>Before UnMerge MAIN EO</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                }
                                                else {
                                                    if (countEnt > 0) {
                                                        String afterLabel = bundle.getString("after_label");    
                                                        dupHeading = "<b> " + afterLabel + "  "+function+ "</b>";                                                                                                           
                                                        //dupHeading = "<b>After " + function + "</b>";
                                                    } else if (countEnt == 0) {
                                                        String beforeLabel = bundle.getString("before_label");  
                                                       // dupHeading = "<b>Before " + function + "</b>";
                                                        
                                                        dupHeading = "<b> " +beforeLabel+" "+ function + "</b>";
                                                        mainEUID = (String) personfieldValuesMapEO.get("EUID");
                                                    }
                                                }
                                               

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = objScreenObject.getRootObj().getChildConfigs();
                   %>
                                             <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="blue" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="menutop"><h:outputText  value="#{msgs.Unmerge_but_text}"/>&nbsp;<h:outputText  value="#{msgs.preview_column_text}"/></td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td>
                                                                       <font style="text-decoration:none;color:#000000;"><b>
                                                                            <%=personfieldValuesMapEO.get("EUID")%>
                                                                        </b></font>
                                                                    </td>
                                                                </tr>
                                                         </table>
                                                    </div>
                                                </div>
                                                
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="blue">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="blue">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoStatus)%>
																</font></td></tr>
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
                                        if (countEnt > 0) {
                                            resultArrayMapCompare.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                        } else {
                                            resultArrayMapMain.put(epathValue, personfieldValuesMapEO.get(epathValue));
                                        }
                                                                    %>  
                                                                        <tr>
                                                                            <td>
 
                                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
                                                                                
                                                                                <%if ((countEnt > 0 && resultArrayMapCompare.get(epathValue) != null && resultArrayMapMain.get(epathValue) != null) &&
            !resultArrayMapCompare.get(epathValue).toString().equalsIgnoreCase(resultArrayMapMain.get(epathValue).toString())) {
        fnameExpression = ExpressionFactory.newInstance().createValueExpression(epathValue, epathValue.getClass());
        fvalueVaueExpression = ExpressionFactory.newInstance().createValueExpression(personfieldValuesMapEO.get(epathValue), personfieldValuesMapEO.get(epathValue).getClass());

                                                                                %>
                                                                                    <font class="highlight">
											<%if(eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
                                                                                            <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                      <%}else{%>
                                                                                       <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                     <%}%>
                                                                                    </font>
                                                                                  <%} else {%>
 										<%if(resultArrayMapMain.get(epathValue) == null ) { %>
                                                                                    <font class="highlight">
											 <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
													       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                    </font>
																					 <%}else {%>
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
                                                                                        <%=personfieldValuesMapEO.get(epathValue)%>
                                                                                       <%}%>
                                                                                     <%}%>
                                                                                   <%}%>

                                                                                <%} else {%>
 																					<%if(resultArrayMapMain.get(epathValue) != null ) { %>
                                                                                    <font class="highlight">
																				         <%if(eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> 
																					       <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                        <%}else{%>
																						 <img src="./images/calup.gif" border="0" alt="Blank Value"/>
                                                                                       <%}%>
                                                                                    </font>
																					 <%}else {%>
                                                                                       &nbsp; 
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
maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
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
										                                     <%if(fieldConfigMap.isKeyType()) {%> <!--if key type-->
                                                                                <b>
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else {%>
                                                                                    <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
																				</b>
										                                    <%}else{%> <!--if not key type-->
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
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
                                                                  <tr><td>&nbsp;</td></tr>

                                                                  <%  } // TOTAL MINOR OBJECTS LOOP
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
                                            <!--Start displaying the sources-->
                                              <!--Start displaying the sources-->
                                               <% 
                                               eoSources = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_SOURCES");

                                              if(eoSources.size() > 0 ) {
                                                //ArrayList soArrayList = (ArrayList) request.getAttribute("eoSources"+(String)personfieldValuesMapEO.get("EUID"));
                                                HashMap soHashMap = new HashMap();
                                                for(int i=0;i<eoSources.size();i++) {
                                                    soHashMap = (HashMap) eoSources.get(i);
                                                    HashMap soHashMapValues = (HashMap) soHashMap.get("SYSTEM_OBJECT");
												    String soStatus = (String) soHashMap.get("Status");
                                               %>
                                              
                                               <td  valign="top">

                                                <div id="unmergepreviewmainDupSources<%=i%>" style="visibility:hidden;display:none">
                                                    <div style="width:170px;overflow:hidden;">
                                                    
											   <%if("inactive".equalsIgnoreCase(soStatus)) {%>
                                                    <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="deactivate" >
												<%} else if("merged".equalsIgnoreCase(soStatus)) {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="transaction" >
												<%} else {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source" >
												<%}%>											  


                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=soHashMap.get("SYSTEM_CODE")%></td>
                                                            </tr> 
                                                                 <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <b><%=soHashMap.get("LID")%></b>
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
                                                                <table border="0" cellspacing="0" cellpadding="0">
																<tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(soStatus)%>
																</font></td></tr>

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
                                                                                <%if (soHashMapValues.get(epathValue) != null) {%>
                                                                                
                                                                                <%=soHashMapValues.get(epathValue)%>
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
                                                                    ArrayList  minorObjectMapList =  (ArrayList) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayList");

int  maxMinorObjectsMinorDB =  ((Integer) soHashMap.get("SO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

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
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null &&  fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%} // MINOR OBJECTS LOOP FOR THE SO
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
                                            
                                           <%}%>
                                         </tr>
                                    </table>
                                </div>
                            </td>
                        </tr>   
                       <!-- eo ArrayList NOT NULL CONDITION  -->  
                    </table>
                    <%} else {%> <!-- If the transaction details are not found -->
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
  <%}%> <!-- if session is active -->


</html>


</f:view>
