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
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"  %>

<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
  URI = URI.substring(1, URI.lastIndexOf("/"));
 %>
<%
//set locale value
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));

}
%>
<f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
<f:view>
    
    <html>
        <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <link rel="stylesheet" type="text/css" href="./scripts/yui4jsf/assets/tree.css"/>   
            <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
             
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script language="JavaScript" src="scripts/Validation.js"></script>
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
            <script type="text/javascript" src="scripts/yui/dragdrop/dragdrop-min.js"></script>

		   <script type="text/javascript" >
           var rand = "";
           function setRand(thisrand)  {
 	        rand = thisrand;
           }
  			 var editIndexid = "-1";
             function closeTree()    {                            
                    document.getElementById('tree').style.visibility='hidden';
                    document.getElementById('tree').style.display='none';
              }
              function setEOEditIndex(editIndex)   {
				editIndexid = editIndex;
	   		  }
              function cancelEdit(formName, thisDiv,minorObject,buttonID)   {
                ClearContents(formName); 
				enableallfields(formName);
                setEOEditIndex("-1");
				document.getElementById(thisDiv).style.visibility = 'hidden';
				document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(buttonID).innerHTML = '<h:outputText value="#{msgs.source_rec_save_but}"/>  '+ minorObject;
		      }
			 var userDefinedInputMask="";
             var URI_VAL = '<%=URI%>';
			 var RAND_VAL = '<%=rand%>';
		   var euidValueArray=[];
		   var euids="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];
		</script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}"/></title> 
        <body>

        

            <%@include file="./templates/header.jsp"%>
            <div id="mainContent1">
            <div id="ajaxContent">
                <div id="basicSearch" class="basicSearch" >
                        <table border="0" cellpadding="0" cellspacing="0" width="100%"> 
                            <tr>
                              <h:form id="potentialDupBasicForm">
                                <td>
                                    <h:outputText value="#{msgs.datatable_euid_text}"/>
                                </td>
                                <td>
								<%
								        ValueExpression requestEuidVE = null ;
										if( request.getParameter("euid") != null )  {
											String euidString  = request.getParameter("euid");
 											requestEuidVE = ExpressionFactory.newInstance().createValueExpression(euidString, euidString.getClass());
										}

								%>
                                         <h:inputText    id="euidField"
                                                         label="EUID"  
														 title="EUID"
												         maxlength="#{SourceHandler.euidLength}" 
                                                         value="<%=requestEuidVE%>" /> 
                                </td>
                                <td>                                    
                                    <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('potentialDupBasicForm');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?pageName=euiddetails.jsf&random='+rand+'&'+queryStr,'outputdiv','')"><span><h:outputText value="#{msgs.search_button_label}"/></span></a>
							    </td>
                                <td>                                    
                                     <h:commandLink  title="#{msgs.Advanced_search_text}" styleClass="button" action="#{NavigationHandler.toPatientDetails}">  
                                        <span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="Advanced search"/>
                                       </span>
                                    </h:commandLink>                                     
                                </td>
                             </h:form>
							    <%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								<!-- Added by Narayan Bhat on 22-aug-2008 to incorparte with the functionality of back button in euiddetails.jsp  -->                                                                 
								<% 
								 String pageName = request.getParameter("fromUrl");
								 String previousQuery = request.getQueryString();
								 String URL= pageName+"?"+previousQuery+"&back=true";
								 %>
 			               		<a class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>" href="<%=URL%>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>

 							 </td>
						   <%} else {%>
							 <td>	
                                <FORM>
			               		<a href="#" 
								   onclick="history.back()" 
								   class="button" 
								   title="<h:outputText  value="#{msgs.back_button_text}"/>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>
  				              </FORM>
							 </td>
 						   <%}%>
                            </tr>
 				             <tr>
                                <td colspan="5">
                                    <h:messages styleClass="errorMessages"  layout="list" />
                                </td>
                            </tr>
                            <tr>
                              <td colspan="5"><div style="color:red;" id="messages"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="5"><div id="outputdiv"></div></td>
                             </tr> 
				</table>
               </div>
 		 <%
			ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();

            //EPathArrayList ePathArrayList = compareDuplicateManager.retrieveEPathArrayList(objScreenObject);
            ArrayList objScreenObjectList = objScreenObject.getSearchResultsConfig();
			

			// Added by Anil, fix for  CR 6709864
			Operations operations=new Operations();

            EPath ePath = null;
            PatientDetailsHandler patientDetailsHandler = new PatientDetailsHandler();
            SourceHandler sourceHandler = new SourceHandler();

            SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
            ArrayList eoArrayList = new ArrayList();
            EnterpriseObject reqEnterpriseObject = new EnterpriseObject();
            if (request.getParameter("euid") != null) {
                eoArrayList = patientDetailsHandler.buildEuids(request.getParameter("euid"));
				if(eoArrayList != null && eoArrayList.size() > 0) {
                  request.setAttribute("comapreEuidsArrayList",eoArrayList);
				}
            } 
            
            if(request.getAttribute("comapreEuidsArrayList") != null) {
                session.removeAttribute("comapreEuidsArrayList");        
                eoArrayList = (ArrayList) request.getAttribute("comapreEuidsArrayList");
            }
            if (session.getAttribute("comapreEuidsArrayList") != null) {
                eoArrayList = (ArrayList) session.getAttribute("comapreEuidsArrayList");
                session.removeAttribute("comapreEuidsArrayList");        
                request.setAttribute("comapreEuidsArrayList",eoArrayList);
            } 
            String tranNo = null;
            int countEnt = 0;

            int countMain = 0;
            int totalMainDuplicates = 0;
            HashMap resultArrayMapMain = new HashMap();
            HashMap resultArrayMapCompare = new HashMap();
            SystemObject so = null;
            ValueExpression mergredHashMapVaueExpression = null;
            ArrayList eoSources = null;
            ArrayList eoHistory = null;
            ValueExpression unMergeEuidVE = null;
			int countInactive = 0;
			//variable for max minor objects count
			int maxMinorObjectsMAX = 0 ; 
			%>
                        			<%
            if (eoArrayList != null && eoArrayList.size() > 0) {%>
                <div id="mainDupSource" class="compareResults">
                    <table cellspacing="0" cellpadding="0" border="0">
                        <tr>
                            <td>
                                <div style="height:700px;overflow:auto;">
                                    <table cellspacing="0" cellpadding="0" border="0">
                                        <tr>
                                            


                
                <%
                request.setAttribute("comapreEuidsArrayList", request.getAttribute("comapreEuidsArrayList"));
                                            %>  
                                            <!-- Display the field Names first column-->
                                            <!--end displaying first column-->       
                                           <%
                                            Object[] eoArrayListObjects = eoArrayList.toArray();
                                            String dupHeading = "Main Euid";
                                            String cssMain = "maineuidpreview";
                                            String cssClass = "dynaw169";
                                            String menuClass = "menutop";
                                            String dupfirstBlue = "dupfirst";
                                            String styleClass="yellow";
                                            String subscripts[] = compareDuplicateManager.getSubscript(eoArrayListObjects.length);
                                            String mainEUID = new String();
                                            for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {

                                                 HashMap eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                                 HashMap personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                                 String eoStatus = (String) eoHashMapValues.get("EO_STATUS");
                                                if ("inactive".equalsIgnoreCase(eoStatus)) {
                                                    styleClass="deactivate";
                                                }
                                            
    
                                              //EnterpriseObject eoSource = compareDuplicateManager.getEnterpriseObject(strDataArray);

                                                if (eoArrayListObjects.length > 1) {
                                                    dupHeading = "<b>EUID " + new Integer(countEnt + 1).toString() +  "</b>";
                                                } else if (eoArrayListObjects.length == 1) {
                                                    dupHeading = "<b>EUID</b>";
                                                }
                                                mainEUID = (String) personfieldValuesMapEO.get("EUID");

                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
                                                String rootNodeName = objScreenObject.getRootObj().getName();
                                                FieldConfig[] rootFieldConfigArray = (FieldConfig[]) sourceHandler.getAllNodeFieldConfigs().get(rootNodeName);
                                                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
                   %>
                                          <%if (countEnt == 0) {%>
                                          <%

                                            %>
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" style="visibility:visible;display:block">
                                                    <div style="width:170px;overflow:auto">
                                                        <div id="mainEuidContent" class="<%=cssMain%>">
                                                            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                                                                <tr><td><b style="font-size:12px; color:blue;"><%=rootNodeName%> Info </b></td></tr>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div id="mainEuidContentButtonDiv<%=countEnt%>" class="<%=cssMain%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" style="visibility:visible;display:block;">
                                                            <div id="personassEuidDataContent" style="visibility:visible;display:block;" class="yellow">
                                                                
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
                                                                       } // If not EUID 
															         } 
                                                                    %>
                                                                   <%
                                                                   
                                                                   for (int i = 0; i < arrObjectNodeConfig.length; i++) {
                                                                    ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[i];
                                                                    FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());

                                                              maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());
                                                              int  maxMinorObjectsMinorDB =  ((Integer) eoHashMapValues.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();
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
                                                                      } //FIELD CONFIG LOOP
																	  %>
                                                                     <tr><td>&nbsp;</td></tr>

																	  <%
                                                                     }
																	 %>

																	 <%
                                                                     }
                                                                    %>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                            <%}%>     
                                            <!-- Display the field Values-->
                                            <td  valign="top">
                                                <div id="outerMainContentDivid<%=countEnt%>" >
                                                <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=dupHeading%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                        <% if ("inactive".equalsIgnoreCase(eoStatus)) {%>
                                                                           <%=personfieldValuesMapEO.get("EUID")%>
                                                                        <%} else {%>
                                                                           <span class="dupbtn" >
                                                                             <%=personfieldValuesMapEO.get("EUID")%>
                                                                           </span>
                                                                        <%} %>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>
                                                
                                                        
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="<%=styleClass%>">
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
                                                                     %>  
                                                                        <tr>
                                                                            <td>
                                                                                <%if (personfieldValuesMapEO.get(epathValue) != null) {%>
   																				  <%if(eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>                                  <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else{%>
                                                                                   <%=personfieldValuesMapEO.get(epathValue)%>
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
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                  <%}else {%>
                                                                                    <%=minorObjectHashMap.get(epathValue)%>
																				  <%}%>
																				</b>
										                                    <%}else{%> <!--if not key type-->
																		         <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>								      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
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
									
                                                <div id="mainDupSources<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                    <div style="width:170px;overflow:hidden">
											   <%if("inactive".equalsIgnoreCase(soStatus)) {
													countInactive++;
													%>
                                                    <div id="mainEuidContent<%=soHashMap.get("LID")%>" class="deactivate" >
												<%} else if("merged".equalsIgnoreCase(soStatus)) {
													countInactive++;
												%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="transaction" >
												<%} else {%>
												<div id="mainEuidContent<%=soHashMap.get("LID")%>" class="source" >
												<%}%>
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=soHashMap.get("SYSTEM_CODE")%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <b><%=soHashMap.get("LID")%></b>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
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
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="source">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td>
 																<font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(soStatus)%>
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
                                                                               
                                                                                 <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>  <!-- if sensitive fields-->
                                                                                    <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                 <%}else {%>
                                                                                    <%=soHashMapValues.get(epathValue)%>
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
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive()&& !operations.isField_VIP()){%>
                                                                                        <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                     <%}else {%>
                                                                                        <%=minorObjectHashMap.get(epathValue)%>
                                                                                     <%}%>
																				   </b>
																				  <%}else{%>
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                            <!--START displaying the History-->
                                               <% 
                                               eoHistory = (ArrayList) eoHashMapValues.get("ENTERPRISE_OBJECT_HISTORY");

                                              if(eoHistory.size() > 0) {
                                               // ArrayList soArrayList = (ArrayList) request.getAttribute("eoHistory"+(String)personfieldValuesMapEO.get("EUID"));
                                                 
                                                for(int i=0;i<eoHistory.size();i++) {
                                                    HashMap objectHistMap = (HashMap) eoHistory.get(i);
                                                    String key = (String) objectHistMap.keySet().toArray()[0];
                                                    String keyTitle = key.substring(0, key.indexOf(":"));
                                                    int ind = key.indexOf(":");
                                                    tranNo = key.substring(ind+1, key.length()); 
                                                    HashMap objectHistMapValues = (HashMap) objectHistMap.get(key);
                                                    HashMap eoValuesMap = (HashMap) objectHistMapValues.get("ENTERPRISE_OBJECT");
													String eoHistStatus = (String) objectHistMapValues.get("EO_STATUS");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>" style="visibility:hidden;display:none">
                                                  <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=personfieldValuesMapEO.get("EUID")%>" class="history" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>"><%=keyTitle%></td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=objectHistMapValues.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=personfieldValuesMapEO.get("EUID")%>" class="history">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoHistStatus)%></font></td></tr>
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
                                                                                
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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

maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

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
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> <!-- if sensitive fields-->
                                                                                      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                    <%}else {%>
                                                                                      <%=minorObjectHashMap.get(epathValue)%>
                                                                                    <%}%>
                                                                                   </b>
                                                                                  <%}else{%>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                                                     <tr><td>&nbsp;</td></tr>

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
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	 %>
                                                                     <tr><td>&nbsp;</td></tr>

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

											 <!--Start displaying merged records -->
                                         <% 
                                               ArrayList eoMergeRecords = (ArrayList) request.getAttribute("mergeEOList"+mainEUID);
                                               if(request.getAttribute("mergeEOList"+mainEUID) != null && eoMergeRecords.size() > 0) {
                                                 for(int i=0;i<eoMergeRecords.size();i++) {
                                                    HashMap mergedValuesMap = (HashMap) eoMergeRecords.get(i);
                                                    HashMap eoValuesMap = (HashMap) mergedValuesMap.get("ENTERPRISE_OBJECT");
					                                String eoMergedStatus = (String) mergedValuesMap.get("EO_STATUS");
                                            %>
                                               <td  valign="top">
                                                <div id="mainDupHistory<%=countEnt%><%=i%>">
                                                  <div style="width:170px;overflow:hidden">
                                                    <div id="mainEuidContent<%=eoValuesMap.get("EUID")%>" class="transaction" >
                                                        <table border="0" cellspacing="0" cellpadding="0" >
                                                            <tr>
                                                                <td class="<%=menuClass%>">
																  <%if(i==0) {%>
																    <h:outputText value="#{msgs.main_euid_label_text}"/>
																  <%}else {%>
																    <h:outputText value="#{msgs.merged_euid_label}"/>
																  <%}%>
																</td>
                                                            </tr> 
                                                            <h:form>
                                                                <tr>
                                                                    <td valign="top" class="dupfirst">
                                                                            <%=eoValuesMap.get("EUID")%>
                                                                    </td>
                                                                </tr>
                                                            </h:form>
                                                        </table>
                                                    </div>
                                                </div>

                                                  <div id="mainEuidContentButtonDiv<%=countEnt%>">
                                                        <div id="assEuidDataContent<%=countEnt%>" >
                                                            <div id="personEuidDataContent<%=mergedValuesMap.get("EUID")%>" class="transaction">
                                                                <table border="0" cellspacing="0" cellpadding="0">
                                                                <tr><td><font style="color:blue;font-size:12px;font-weight:bold;"><%=compareDuplicateManager.getStatus(eoMergedStatus)%></font></td></tr>
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
                                                                                
                                                                                     <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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

int  maxMinorObjectsMinorDB =  ((Integer) mergedValuesMap.get("EO" + childObjectNodeConfig.getName() + "ArrayListSize")).intValue();

maxMinorObjectsMAX  = compareDuplicateManager.getMinorObjectsMaxSize(eoArrayList,objScreenObject,childObjectNodeConfig.getName());

int maxMinorObjectsDiff  =   maxMinorObjectsMAX - maxMinorObjectsMinorDB ;


																	ArrayList  minorObjectMapList =  (ArrayList) mergedValuesMap.get("EO" + childObjectNodeConfig.getName() + "ArrayList");
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
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%> <!-- if sensitive fields-->
                                                                                      <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                                                    <%}else {%>
                                                                                      <%=minorObjectHashMap.get(epathValue)%>
                                                                                    <%}%>
                                                                                   </b>
                                                                                  <%}else{%>
                                                                                    <%if (eoHashMapValues.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>
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
                                                                     <tr><td>&nbsp;</td></tr>

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
                                                                    <tr><td>&nbsp;</td></tr>
                                                                    <%                                                                                     }//field config loop
																	 %>
                                                                     <tr><td>&nbsp;</td></tr>

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

                                              <!--End displaying merged records-->
                                           <%}%>
                                           <td valign="top"><div id="previewPane"></div></td>
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

                        <tr>
                            <td>
                                <div id="actionmainEuidContent" class="actionbuton">
                                <table width="100%" cellpadding="0" cellspacing="0" border="0">
                                    <tr>
                                        <% 

                                        HashMap eoHashMapValues = new HashMap();
                                            
                                            HashMap personfieldValuesMapEO = new HashMap();
                                            String euid = new String();
                                            String eoStatus = new String();
                                        	ValueExpression  euidValueExpression = null;        
    						             for (countEnt = 0; countEnt < eoArrayListObjects.length; countEnt++) {
                                            eoHashMapValues = (HashMap) eoArrayListObjects[countEnt];
                                            personfieldValuesMapEO = (HashMap) eoHashMapValues.get("ENTERPRISE_OBJECT");
                                            euid = (String) personfieldValuesMapEO.get("EUID");
                                            eoStatus = (String) eoHashMapValues.get("EO_STATUS");
                                            euidValueExpression = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());        
    
    							
							          %>
									  	<script>
												 euidValueArray.push('<%=euid%>');
										</script>

                                        <% if (countEnt == 0) {%>
                                        <td><img src="images/spacer.gif" width="169px" height="1px" border="0"></td>
                                        <% }%>
									   
                                        <td>
                                        <td valign="top">
                                            <div id="dynamicMainEuidButtonContent<%=countEnt%>">
                                                <table border="0" cellspacing="0" cellpadding="0" >
                                                        <%if ("active".equalsIgnoreCase(eoStatus)) {%>
                                                <tr> 
                                                    <td valign="top" width="125px">
													<!-- Start Added by Anil, fix for  CR 6709864-->
													 <% if(operations.isEO_Edit()){%>
                                                        <a  title="<h:outputText value="#{msgs.edit_euid_button_text}" />" class="button" href="javascript:void(0)"
                                                                        onclick="javascript:ajaxURL('/<%=URI%>/ajaxservices/editmaineuid.jsf?'+'&rand=<%=rand%>&euid=<%=euid%>','ajaxContent','')">
                                                            <span><h:outputText value="#{msgs.edit_euid_button_text}" /> </span>
                                                        </a>  
													 <%}%>
													 <!-- End  fix for  CR 6709864-->
                                                   <!-- Deactive/Activate button -->
                                                    <h:form>
                                                        <h:commandLink title="#{msgs.source_rec_deactivate_but}" styleClass="button" rendered="#{Operations.EO_Deactivate}"
                                                                        actionListener="#{PatientDetailsHandler.deactivateEO}">

                                                             <f:attribute name="eoValueExpression" value="<%=euidValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                        </h:commandLink> 
                                                    </h:form>
                                                    </td>
                                                </tr>
                                                        <%}%>            
                                                    <%if (countInactive != eoSources.size() && "inactive".equalsIgnoreCase(eoStatus)) {%>
                                                    <tr>
                                                         <td valign="top" width="125px">
                                                    <h:form>
                                                        <h:commandLink title="#{msgs.source_rec_activate_but}" styleClass="button" rendered="#{Operations.EO_Activate}"
                                                                        actionListener="#{PatientDetailsHandler.activateEO}">
                                                             <f:attribute name="eoValueExpression" value="<%=euidValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                        </h:commandLink>
                                                    </h:form>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <h:outputText style="color:red" value="#{msgs.euid_deactive_text}"/>
                                                         </td>
                                                      </tr>
                                                        <%}%>            

                                                  <tr> 
												  <%  
													  Operations ops = new Operations();
												     if(ops.isTransLog_SearchView()){
												  %>
                                                      <td valign="top">
                                                          <a class="viewbtn"   title="<h:outputText value="#{msgs.view_history_text}"/>" href="javascript:showViewHistory('mainDupHistory','<%=eoHistory.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoSources.size()%>','true',euidValueArray)" >  
                                                              <h:outputText value="#{msgs.view_history_text}"/>
                                                          </a>
                                                      </td>    
												  <% } %>	  
                                                  </tr> 
                                                  <tr> 
                                                      <td valign="top">
                                                          <a  title="<h:outputText value="#{msgs.view_sources_text}"/>"  href="javascript:showViewSources('mainDupSources','<%=eoSources.size()%>','<%=countEnt%>','<%=eoArrayListObjects.length%>','<%=eoHistory.size()%>',euidValueArray)" class="viewbtn"><h:outputText value="#{msgs.view_sources_text}"/></a> 
                                                      </td>                                              
                                                  </tr>

                                        <%if(eoHistory.size() > 0) {
												   %>
										
                                                     <%
											            String mergekey = new String();
                                                        for(int i=0;i<eoHistory.size();i++) {
                                                          HashMap objectHist = (HashMap) eoHistory.get(i);
                                                          mergekey = (String) objectHist.keySet().toArray()[0];
										               }
                                                        if (mergekey.startsWith("euidMerge")) {                                                  
                                                        ValueExpression tranNoValueExpressionviewmerge = ExpressionFactory.newInstance().createValueExpression(tranNo, tranNo.getClass());
                                                        ValueExpression eoArrayListVE = ExpressionFactory.newInstance().createValueExpression(eoArrayList, eoArrayList.getClass());
                                                      %>  
                                                     <tr>
                                                      <td valign="top">
                                                         <a href="javascript:void(0);" class="viewbtn" title="<h:outputText  value="#{msgs.View_MergeTree_but_text}"/>"									 onclick="javascript:ajaxURL('/<%=URI%>/viewmergetree.jsf?euid=<%=personfieldValuesMapEO.get("EUID")%>&rand=<%=rand%>','tree',event)">
                                                          <h:outputText  value="#{msgs.View_MergeTree_but_text}"/>
                                                         </a>
                                                      </td>
                                                  </tr>
                                                        <tr>
                                                            <td valign="top">
                                                                <h:form>
                                                                    <h:commandLink styleClass="activeviewbtn" rendered="#{Operations.EO_Merge}" title="#{msgs.View_Merge_Records_but_text}"
                                                                                   actionListener="#{PatientDetailsHandler.viewMergedRecords}">
                                                                        <f:attribute name="eoArrayList" value="<%=eoArrayListVE%>"/>        
                                                                        <f:attribute name="euidValueExpression" value="<%=euidValueExpression%>"/>
																		<f:attribute name="tranNoValueExpressionviewmerge" value="<%=tranNoValueExpressionviewmerge%>"/>                   
                                                                        <h:outputText  value="#{msgs.View_Merge_Records_but_text}"/>                                                            
                                                                    </h:commandLink>
                                                                </h:form>
																
                                                            </td> 
                                                        </tr>
                                                   <%}%>
										<%
                                        unMergeEuidVE = ExpressionFactory.newInstance().createValueExpression(euid, euid.getClass());
                                         %> 

                                          <%}%> 


                                               </table>                                               
  											   </div>
                                        </td>   
										<%}%>
                                    </tr>
                                </table>
                                </div>
                            </td>
                        </tr>
                 
                    </table>
                </div>
                
            </div>    
            <%}%>
 </div> <!-- end mainEuidContent -->


<div id="tree" style="LEFT:189px;TOP:350px;PADDING-LEFT: 5px; VISIBILITY: hidden; WIDTH: 180px; PADDING-TOP: 5px;  POSITION: absolute;  OVERFLOW: auto;  HEIGHT: 150px; BACKGROUND-COLOR: #c4c8e1; BORDER-RIGHT:  #000099 thin solid; BORDER-TOP: #000099 thin solid; BORDER-LEFT: #000099 thin solid; BORDER-BOTTOM:  #000099 thin solid"></div> 

                <div id="unmergePopupDiv" class="alert" style="TOP: 2250px; LEFT: 500px; HEIGHT: 150px;  WIDTH: 300px;VISIBILITY: hidden;">
                                                        <table cellpadding="0" cellspacing="0">
                                                            <h:form>
                                          <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="unmergepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td colspan="2"><h:outputText value="#{msgs.unmerge_popup_content_text}" /></td></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr><td>
                                                                        <h:commandLink styleClass="button" title="#{msgs.ok_text_button}" 
                                                                                       actionListener="#{PatientDetailsHandler.unmergeEnterpriseObject}">
                                                                            <f:attribute name="unMergeEuidVE" value="<%=unMergeEuidVE%>"/>                   
                                                                            <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                                        </h:commandLink>   
                                                                        <h:outputLink  title="#{msgs.cancel_but_text}"  onclick="javascript:showConfirm('unmergePopupDiv',event)" 
                                                                                       styleClass="button"          
                                                                                       value="javascript:void(0)">
                                                                            <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                                        </h:outputLink>   
                                                                </td></tr>
                                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                                                <tr>
                                                                    <td colspan="2">
                                                                        <h:messages style="font-size:10px; font-color:red;" layout="table" />
                                                                    </td>
                                                                </tr>        
                                                            </h:form>
                                                        </table>
                                                    </div>                                 
                       
         <div id="unmergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.unmergepopup_help}"/></div>     
		 <form id="EditIndexForm" name="EditIndexForm">
		   <input type="hidden" id="EditIndexFormID" value="-1" />
       </form>

        </body>
        <%
		 MasterControllerService masterControllerService = new MasterControllerService();
         String[][] lidMaskingArray = masterControllerService.getSystemCodes();
          
          
        %>
        <script>
            var systemCodes = new Array();
            var lidMasks = new Array();
        </script>
        
        <%
        for(int i=0;i<lidMaskingArray.length;i++) {
            String[] innerArray = lidMaskingArray[i];
            for(int j=0;j<innerArray.length;j++) {
            if(i==0) {
         %>       
         <script>
           systemCodes['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>      
         <%       
            } else {
         %>
         <script>
           lidMasks ['<%=j%>']  = '<%=lidMaskingArray[i][j]%>';
         </script>
         <%       
            }
           }
           }
        %>

<script>
		function setLidMaskValue(field,formName) {
			var  selectedValue = field.options[field.selectedIndex].value;
			
            var formNameValue = document.forms[formName];
			
            var lidField =  getDateFieldName(formName,'LID');
            //document.getElementById(lidField).value = "";
			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
             document.getElementById(lidField).disabled = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).disabled = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
</script>



<script>
         if( document.potentialDupBasicForm.elements[0]!=null) {
		var i;
		var max = document.potentialDupBasicForm.length;
		for( i = 0; i < max; i++ ) {
			if( document.potentialDupBasicForm.elements[ i ].type != "hidden" &&
				!document.potentialDupBasicForm.elements[ i ].disabled &&
				!document.potentialDupBasicForm.elements[ i ].readOnly ) {
				document.potentialDupBasicForm.elements[ i ].focus();
				break;
			}
		}
      }         

    </script>
<script type="text/javascript">
    makeDraggable("lockSBRDiv");
    makeDraggable("unLinkSoDiv");
    makeDraggable("linkSoDiv");
</script>
     </html>
    </f:view>
    
