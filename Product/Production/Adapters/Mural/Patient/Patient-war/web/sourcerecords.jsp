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
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
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
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>


<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
 URI = URI.substring(1, URI.lastIndexOf("/"));%>

 <%
if(session!=null){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>

<f:view>
 <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />       

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>
        
        <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
        <!-- Additional source files go here -->
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
                
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <script type="text/javascript" src="scripts/balloontip.js"></script>
        <script type="text/javascript" src="scripts/validation.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo/yahoo.js"></script>
        <script type="text/javascript" src="./scripts/yui/event/event.js"></script>
        <script type="text/javascript" src="./scripts/yui/dom/dom.js"></script>
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/animation/animation.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
        <script type="text/javascript" >
           var fieldNameValuesLocal="";
           var fieldNamesLocal="";
           var minorObjTypeLocal = "";
           var minorObjTypeLocalCount = 0;
           var minorArrayLocal = new Array();
  	       var editIndexid = "-1";
           var userDefinedInputMask="";
 		   // LID merge related global javascript variables
		   var lids="";
           var lidArray = [];
           var alllidsArray = [];
           var alllidsactionText = [];

           function setEditIndex(editIndex)   {
		      editIndexid = editIndex;
	       }
            
           function cancelEdit(formName, thisDiv,minorObject)   {
                ClearContents(formName); 
                enableallfields(formName);
                setEditIndex("-1");
		        document.getElementById(thisDiv).style.visibility = 'hidden';
		        document.getElementById(thisDiv).style.display  = 'none';
                document.getElementById(minorObject+'buttonspan').innerHTML = '<h:outputText value="#{msgs.source_rec_save_but}"/> '+ minorObject;
	     }
	
             var URI_VAL = '<%=URI%>';
	     var RAND_VAL = '<%=rand%>';
   </script>
        <!--there is no custom header content for this example-->
        
    </head>
    <%@include file="./templates/header.jsp"%>
   
    <% String validLid = "Not Validated";
       if(session.getAttribute("validation") != null ) {
         validLid = "Validated";
        } 
    %>
     <body class="yui-skin-sam">
    

        <div id="mainContent" style="overflow:hidden;"> 
        <div id="sourcerecords">
            <table border="0" cellspacing="0" cellpadding="0" width="90%">
                <% Operations operations=new Operations();%>
                <tr>
                    <td>
                        <div id="demo" class="yui-navset">
                            <ul class="yui-nav">
                                <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title ="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li class="selected"><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>"  href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li>
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li class="selected"><a  title ="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%} else {%>
                                <% if(operations.isSO_SearchView()){%>
                                <li class="selected">
                                    <a title="<h:outputText value="#{msgs.source_submenu_viewedit}"/>" href="#viewEditTab"><em><h:outputText value="#{msgs.source_submenu_viewedit}"/></em></a>
                                </li>
                                <%}%>
                                <% if(operations.isSO_Add()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_add}"/>" href="#addTab"><em><h:outputText value="#{msgs.source_submenu_add}"/></em></a></li>
                                <%}%>
                                <% if(operations.isSO_Merge()){%>
                                <li><a title="<h:outputText value="#{msgs.source_submenu_merge}"/>" href="#mergeTab"><em><h:outputText value="#{msgs.source_submenu_merge}"/></em></a></li>
                                <%}%>
                                <%}%>  
                            </ul>  
                            <%
                                        ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
                                        SystemObject singleSystemObject = (SystemObject) session.getAttribute("singleSystemObject");
                                        ArrayList searchResultsScreenConfigArray = (ArrayList) session.getAttribute("viewEditResultsConfigArray");
                                        ArrayList systemObjectsMapList = (ArrayList) session.getAttribute("systemObjectsMapList");
                                        SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
                                        ValueExpression LIDVaueExpression = null;
                                        ValueExpression sourceSystemVaueExpression = null;
                                        //ConfigManager.init();
                                         //ResourceBundle bundle = //ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, //FacesContext.getCurrentInstance().getViewRoot().getLocale());
                                        String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

                                        EPathArrayList ePathArrayList = new EPathArrayList();

                                        String mainDOB;
                                        SystemObject singleSystemObjectLID = (SystemObject) session.getAttribute("singleSystemObjectLID");
										HashMap systyemObjectAsHashMap  = new HashMap();
										
                                        HashMap systemObjectMap = (HashMap) session.getAttribute("systemObjectMap");
                                        String keyFunction = (String) session.getAttribute("keyFunction");
                                        SourceHandler sourceHandler = new SourceHandler();
                                        Object[] roorNodeFieldConfigs = sourceHandler.getRootNodeFieldConfigs().toArray();
                                        SourceEditHandler sourceEditHandler = (SourceEditHandler)session.getAttribute("SourceEditHandler");
                                        SourceAddHandler  sourceAddHandler   = new SourceAddHandler();

									HttpSession sessionFaces = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                             		SourceAddHandler  sourceAddHandlerFaces   = (SourceAddHandler)sessionFaces.getAttribute("SourceAddHandler");

									 CompareDuplicateManager compareDuplicateManager = new CompareDuplicateManager();
                                        int addressSize;
                                        int phoneSize;
                                        int aliasSize;
                                        int commentSize;
                                        int dateCount=99999;
                                                        HashMap resultArrayMapCompare = new HashMap();
                                                        HashMap resultArrayMapMain = new HashMap();

                                                        ValueExpression fnameExpression;
                                                        ValueExpression fvalueVaueExpression;
                                                ObjectNodeConfig[] arrObjectNodeConfig = screenObject.getRootObj().getChildConfigs();
                                                HashMap allNodefieldsMap = sourceHandler.getAllNodeFieldConfigs();
										String eoStatus= new String();
 									ValueExpression localIdDesignationVE = ExpressionFactory.newInstance().createValueExpression( localIdDesignation  ,  localIdDesignation.getClass()); 	
									%>
                            <div class="yui-content">
                              <% if(operations.isSO_SearchView()){%> 
                                <div id=viewEditTab">
                            
                                    <%if (singleSystemObjectLID != null) {
										eoStatus = compareDuplicateManager.getEnterpriseObjectStatusForSO(singleSystemObjectLID);
                                         
										systyemObjectAsHashMap = compareDuplicateManager.getSystemObjectAsHashMap(singleSystemObjectLID,objScreenObject);
										%>
                                    <%if ("viewSO".equalsIgnoreCase(keyFunction)) {%>
                                    <h:form>
                                        <div  id="sourceViewBasicSearch">                                            
                                            <table border="0" width="100%">

                                                    <tr>
                                                        <td> 
                                                            <h:commandLink title="#{msgs.source_rec_viewrecordslist_but}"  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                            </h:commandLink>                                                                     
														</td>
                                                        <th><b><h:outputText value="#{msgs.source_rec_status_but}"/></b>&nbsp;:&nbsp;<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;">
														   <%=compareDuplicateManager.getStatus(singleSystemObjectLID.getStatus())%> </font>
														</th>
                                                        <th><b><h:outputText value="#{msgs.transaction_source}"/></b>:	<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=sourceHandler.getSystemCodeDescription(singleSystemObjectLID.getSystemCode())%></font></th>
                                                        <th> <b><%=localIdDesignation%></b>:<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=singleSystemObjectLID.getLID()%></font></th>
                                                    </tr>
                                             </table>
                                            
                                            <!--Start Displaying the root node fields -->                                        
                                            <div class="minorobjects">                                                    
											   <table border="0" cellpadding="1" cellspacing="1" width="100%">
                                                 <tr><td class="tablehead" width="100%"><b><%=objScreenObject.getRootObj().getName()%></b> &nbsp; </td></tr>
 												 <tr>
												   <td>
												    <table border="0" cellpadding="1" cellspacing="1" >
                                                   <%  HashMap rootFieldValuesMap  = (HashMap) systyemObjectAsHashMap.get("SYSTEM_OBJECT");
                                                        for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
                                                                        %>  
                                                      <tr>
                                                        <th align="left">
                                                          <%=fieldConfigMap.getDisplayName()%>
                                                        </th>
                                                        <td>
															<%if(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) != null && systyemObjectAsHashMap.get("hasSensitiveData") != null && fieldConfigMap.isSensitive() && !operations.isField_VIP()){%>   
                                                               <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                            <%}else{%>
                                                               <%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : "&nbsp"%>
                                                             <%}%>
														</td>
                                                     </tr>
                                                   <%}%>
												   </table>
												</td>
												</tr>
                                                <!-- STARTDisplaying the minor object fields -->    
                                                <% String epathValue = new String();
                                                  for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                     ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                     ArrayList  minorObjectMapList =  (ArrayList) systyemObjectAsHashMap.get("SOEDIT" + childObjectNodeConfig.getName() + "ArrayList");
								                %>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td class="tablehead" width="100%"><b><%=childObjectNodeConfig.getName()%></b> &nbsp; </td></tr>
  											    <tr>
												  <td> 
												     <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
													 <table border="0" width="100%" cellpadding="0">
                                                     <%
                                                      FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                       HashMap minorObjectMap = new HashMap();
													   %>
                                                        <%if(minorObjectMapList.size() == 0) {%>
                                                        <tr class="odd">
														  <td><%=bundle.getString("source_rec_nodetails_text")%></td>
														</tr>
                                                       <%}%>
													   <%
			 					                       for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
                                                         minorObjectMap = (HashMap) minorObjectMapList.get(ar);
														 String styleClass = ((ar%2==0)?"even":"odd");
                                                     %>

													   <%if(ar == 0) {%>
                                                        <tr>			   
                                                           <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
 			                                                  <td class="tablehead">
				                                                 <%=fieldConfigArrayMinor[k].getDisplayName()%>
                                                               </td>
		                                                  <%}%>
														</tr> 
                                                      <%}%>

													 <tr class="<%=styleClass%>">
                                                        <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
                                                          <td>
														  <%if(minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()) != null ) {%>  <!--if has value-->
   															<%if( systyemObjectAsHashMap.get("hasSensitiveData") != null &&  fieldConfigArrayMinor[k].isSensitive() && !operations.isField_VIP()){%>   
                                                               <h:outputText  value="#{msgs.SENSITIVE_FIELD_MASKING}" />
                                                            <%}else{%>
                                               					<%if(fieldConfigArrayMinor[k].getValueList() != null) {%> <!-- if the field config has value list-->
																  <%if (fieldConfigArrayMinor[k].getUserCode() != null){%> <!-- if it has user defined value list-->
																	 <%=ValidationService.getInstance().getUserCodeDescription(fieldConfigArrayMinor[k].getUserCode(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
																  <%}else{%>
																	<%=ValidationService.getInstance().getDescription(fieldConfigArrayMinor[k].getValueList(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
																 <%}%>
															   <%} else {%> <!--minorObjectMap- In other cases-->
															   <%
																String value = minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()).toString();   
																if (fieldConfigArrayMinor[k].getInputMask() != null && fieldConfigArrayMinor[k].getInputMask().length() > 0) {
																  if (value != null) {
																	 //Mask the value as per the masking 
																	 value = fieldConfigArrayMinor[k].mask(value.toString());
																   }
																} 
																%> 
																 <%=value%>
															   <%}%>
                                                             <%}%>
														  <%} else {%> <!-- else print &nbsp-->
															&nbsp;
														  <%}%>
 								                          </td>
							                          <% } %>
													</tr>
														  
													 <%}%>

 													 </table>
													 </div>
												  </td>
												</tr>
												<%}%>
                                            <!--End Displaying the minor object fields -->    
 										</table>

                                       </div>

                                     <!--End Displaying the root node fields -->    

                                    
                                            <table>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <%
                                                    ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());

                                                    %>
                                                    
                                                    <td>
 													    <!--Display edit link only when the system object-->
                                                        <h:commandLink  title= "#{msgs.source_rec_edit_but}" styleClass="button" 
                                                                        action="#{NavigationHandler.toSourceRecords}" 
                                                                        rendered="#{Operations.SO_Edit}"
                                                                        actionListener="#{SourceAddHandler.editLID}" >
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>                
                                                            <span><h:outputText value="#{msgs.source_rec_edit_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
													
                                                    <td>
                                                        <h:commandLink title="#{msgs.source_rec_vieweuid_but}"  styleClass="button" 
                                                                        rendered="#{Operations.SO_SearchView}"
                                                                        action="#{NavigationHandler.toEuidDetails}" 
                                                                        actionListener="#{SourceHandler.viewEUID}" >  
                                                            <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                            <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                        </h:commandLink>   
                                                    </td>
                                                </tr> 
                                                <tr><td>&nbsp;</td></tr>
                                                
                                            </table>
                                        </div>
                                    </h:form>
                                    <%} else if ("editSO".equalsIgnoreCase(keyFunction)) {%>
                                    <%
                                     ValueExpression soValueExpression = ExpressionFactory.newInstance().createValueExpression(singleSystemObjectLID, singleSystemObjectLID.getClass());
                                    %>
                                    
                                    <div id="sourceViewBasicSearch">
                                        <!-- Start Status div-->
                                        <div id='edistatusdisplay'>
                                            <table border=0 width="100%">
                                                <tr>
                                                    <td>
                                                        <h:form>
                                                            <table border="0" cellpadding="1" cellspacing="1" width="100%">

                                                    <tr>
                                                        <td> 
                                                            <h:commandLink title="#{msgs.source_rec_viewrecordslist_but}"  styleClass="button" rendered="#{Operations.SO_SearchView}"
                                                                            action="#{NavigationHandler.toSourceRecords}" 
                                                                            actionListener="#{SourceHandler.removeSingleLID}" >  
                                                                <span><h:outputText value="#{msgs.source_rec_viewrecordslist_but}"/></span>
                                                            </h:commandLink>                                                                     
														</td>
                                                        <th><b><h:outputText value="#{msgs.source_rec_status_but}"/></b>&nbsp;:&nbsp;<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;">
														   <%=compareDuplicateManager.getStatus(singleSystemObjectLID.getStatus())%> </font>
														</th>
                                                        <th><b><h:outputText value="#{msgs.transaction_source}"/></b>:	<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=sourceHandler.getSystemCodeDescription(singleSystemObjectLID.getSystemCode())%></font></th>
                                                        <th> <b><%=localIdDesignation%></b>:<font style="font-family: Arial, Helvetica, sans-serif;font-size:12px;color:blue;text-align:left;vertical-align:middle;	   font-weight:bold;padding-left:18px;"><%=singleSystemObjectLID.getLID()%></font></th>
                                                    </tr>
																
                                                            </table>    
                                                        </h:form>
                                                    </td>
                                                    <td><div id="editFormValidate"></div>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                        <!-- Status div-->
                                                                                                 
											   <table border="0" " width="100%" >
                                                <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                <tr>
                                                    <td class="tablehead" colspan="2">
                                                        <%=objScreenObject.getRootObj().getName()%>                    
                                                    </td>
                                                </tr>
                                                <%}%>
                                                                 
                                                <tr>
                                                    <td align="left">
                                                        <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                       <table>
														<tr>
														<td style="font-size:10px;">
															 <nobr>
																 <span style="font-size:12px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
															</nobr>
														</td>
													  </tr> 
													  </table> 

														<h:form id="BasicSearchFieldsForm">
                                                          <!-- Start EDIT Fields-->
                                                          <!--Start Displaying the person fields -->                                        
                                                        <form id="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>EditSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">

                                                            <h:dataTable  id="hashIdEdit" 
                                                                          var="fieldConfigPerAdd" 
                                                                          value="#{SourceHandler.rootNodeFieldConfigs}">
                                                                <h:column>
																	 <h:outputText rendered="#{fieldConfigPerAdd.required}">
																		<span style="font-size:12px;color:red;verticle-align:top">*</span>
																	</h:outputText>													  
																	<h:outputText rendered="#{!fieldConfigPerAdd.required}">
																		<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
																	</h:outputText>													  
																	<h:outputText value="#{fieldConfigPerAdd.displayName}" />
																	<h:outputText value=":"/>
                                                                  </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive}" >
                                                                    <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" 
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
																
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive}" >
																	<h:selectOneMenu 
																	        readonly="true" 
																			disabled="true" 
																			rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP }">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                    </h:selectOneMenu>
                                                                    <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" 
																	                 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP}"
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT_EDIT'][fieldConfigPerAdd.fullFieldName]}">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                                    </h:selectOneMenu>      
                                                                </h:column>
																
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive}" >
																   
                                                                    <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                  value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                onfocus="javascript:clear_masking_on_focus()" required="#{fieldConfigPerAdd.required}"
																				rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' ||  Operations.field_VIP}"/>
																
																	<h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 value="#{msgs.SENSITIVE_FIELD_MASKING}"
																				 readonly="true"
																				 disabled="true"
 																				rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP && SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName] ne null}"/>	

																	<h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 readonly="true"
																				 disabled="true"
 																				rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' &&  !Operations.field_VIP && SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName] eq null}"/>	

                                                                </h:column>                     

																<h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive}" >
                                                                    <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                                 id="fieldConfigIdTextbox"  
                                                                                 value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
                                                                                 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                                                                                 maxlength="#{fieldConfigPerAdd.maxLength}"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                onfocus="javascript:clear_masking_on_focus()" required="#{fieldConfigPerAdd.required}"/>
                                                                </h:column>                     
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && !fieldConfigPerAdd.sensitive}">
                                                                    
                                                                    <nobr><!--Sridhar -->
                                                                        <input type="text" 
                                                                               title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                                               value="<h:outputText value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
                                                                               id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                                               required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                                               maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                                                                               onblur="javascript:validate_date(this,'<%=dateFormat%>');"
                                                                               onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                                               onkeyup="javascript:qws_field_on_key_up(this)" >
                                                                       <a href="javascript:void(0);" 
												     title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                                                    </nobr>
                                                                        
                                                                        
                                                                </h:column>

                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && fieldConfigPerAdd.sensitive && (SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP) }">
                                                                    <nobr><!--Sridhar -->
                                                                        <input type="text" 
                                                                               title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                                               value="<h:outputText value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"/>"
                                                                               id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                                               required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                                               maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                                                                               onblur="javascript:validate_date(this,'<%=dateFormat%>');"
                                                                               onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                                               onkeyup="javascript:qws_field_on_key_up(this)" >
                                                                       <a href="javascript:void(0);" 
												     title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                                                    </nobr>
                                                                </h:column>

                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6 && SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true'  && fieldConfigPerAdd.sensitive && !Operations.field_VIP }">
                                                                    <nobr><!--Sridhar -->
                                                                        <input type="text" 
                                                                                value="<h:outputText value="#{msgs.SENSITIVE_FIELD_MASKING}"/>"
                                                                               id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                                               readonly="true" 
                                                                               disabled="true" 
                                                                               maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                                                                               ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/><font class="dateFormat">(<%=dateFormat%>)</font>
                                                                    </nobr>
                                                                </h:column>

																<!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6 && !fieldConfigPerAdd.sensitive }" >
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                                                                     title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}"
 																					 id="fieldConfigIdTextArea"   
                                                                                     required="#{fieldConfigPerAdd.required}"
                                                                                      />
                                                                </h:column>
                                                                <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6 && fieldConfigPerAdd.sensitive }" >
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
																	                 readonly="true"
																					 disabled="true"
                                                                                     value="#{msgs.SENSITIVE_FIELD_MASKING}" 
                                                                                     required="#{fieldConfigPerAdd.required}"
																					 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] eq 'true' && !Operations.field_VIP}"
                                                                                     />
                                                                    <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
                                                                                     title="#{fieldConfigPerAdd.fullFieldName}"
                                                                                     value="#{SourceAddHandler.newSOHashMap['SYSTEM_OBJECT'][fieldConfigPerAdd.fullFieldName]}" 
                                                                                     required="#{fieldConfigPerAdd.required}"
																					 rendered="#{SourceAddHandler.newSOHashMap['hasSensitiveData'] ne 'true' || Operations.field_VIP}"
                                                                                     />
                                                                </h:column>
                                                                    
                                                            </h:dataTable>
                                                        </form>
                                                            
                                                        <!--End Displaying the person fields -->    
                                                        <!--Minor Object fields here -->     
                                                        <h:dataTable  id="allChildNodesNamesAdd" 
                                                                      width="100%"
                                                                      var="childNodesName" 
                                                                      value="#{SourceHandler.allChildNodesNames}">
                                                             <h:column>
                                                                <table width="100%">
                                                                    <tr>
                                                                        <td class="tablehead" colspan="2">
                                                                            <h:outputText value="#{childNodesName}"/>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
                                                                        </td>
                                                                    </tr>
                                                                        
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <a title="<h:outputText value="#{msgs.source_rec_view}"/>&nbsp;<h:outputText value="#{childNodesName}"/> " href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddDiv');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?&MOT=<h:outputText value="#{childNodesName}"/>&load=load&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv','')" class="button">
                                                                            <span>
                                                                                <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;View <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="<h:outputText value="#{msgs.source_submenu_add}"/>  <h:outputText value="#{childNodesName}"/>"/>
                                                                            </span>
                                                                                
                                                                        </td>
                                                                    </tr>
                                                                    <tr><td>
                                                                            <div id="extra<h:outputText value='#{childNodesName}'/>AddDiv"  style="visibility:hidden;display:none;">
                                                                                <table>
                                                                                    <tr>
                                                                                        <td colspan="2" align="left">
                                                                                            <form id="<h:outputText value="#{childNodesName}"/>InnerForm" name="<h:outputText value="#{childNodesName}"/>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                                                                <h:dataTable  headerClass="tablehead" 
                                                                                                              id="allNodeFieldConfigsMapAdd" 
                                                                                                              var="allNodeFieldConfigsMapAdd" 
                                                                                                              width="100%"
                                                                                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                                                                                    <h:column>
                                                                                                        <h:dataTable  headerClass="tablehead" 
                                                                                                                      id="childFieldConfigsAdd" 
                                                                                                                      var="childFieldConfigAdd" 
                                                                                                                      width="100%"
                                                                                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                                                                                                          
                                                                                                            <h:column>
 
																							 <h:outputText rendered="#{childFieldConfigAdd.required}">
																								<span style="font-size:12px;color:red;verticle-align:top">*</span>
																							</h:outputText>													  
																							<h:outputText rendered="#{!childFieldConfigAdd.required}">
																								<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
																							</h:outputText>													  
																							<h:outputText value="#{childFieldConfigAdd.displayName}" />
																							<h:outputText value=":"/>

                                                                                                            </h:column>
                                                                                                            <!--Rendering HTML Select Menu List-->
                                            <!--Rendering HTML Select Menu List-->
										  <!--user code related changes starts here-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <!-- User code fields here -->
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" onchange="getFormValues('#{childNodesName}InnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand=+RAND_VAL+&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
												rendered="#{childFieldConfigAdd.userCode ne null}">
												    <f:selectItem itemLabel="" itemValue="" />
                                                   <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>    
												
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" 
												                 rendered="#{childFieldConfigAdd.userCode eq null}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>

                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                           
                                                            <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, userDefinedInputMask)"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy ne null}"
																		 />     
																		 
																		 <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy eq null}"
																		 />

                                          </h:column>                     
										  <!--user code related changes ends here-->
                                          
										  <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                                                                                                <nobr>
                                                                                                                    <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                                                                                           id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                                                                           required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                                                                           maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                                                                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                                                                                           onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                                                           onblur="javascript:validate_date(this,'<%=dateFormat%>');">
                                                                                                                    <a href="javascript:void(0);" 
												     title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{childFieldConfigAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{childFieldConfigAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                                                                                                </nobr>
                                                                                                            </h:column>                     
                                                                                                                
                                                                                                                
                                                                                                            <!--Rendering Updateable HTML Text Area-->
                                                                                                                
                                                                                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                                                                                <h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
                                                                                                                                 required="#{childFieldConfigAdd.required}" />
                                                                                                            </h:column>
                                                                                                        </h:dataTable>                                                                                
                                                                                                    </h:column>
                                                                                                </h:dataTable>                                                                                
                                                                                                    
                                                                                            </form>
                                                                                        </td>
                                                                                    </tr>
                                                                                    <!--EDIT SO buttons START-->
                                                                                    <tr>                                                                                                                
																					  <td colspan="2">
                                                                                           <nobr>
                                                                                                <a title=" <h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/>"  href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>InnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<%=singleSystemObjectLID.getLID()%>&SYS=<%=singleSystemObjectLID.getSystemCode()%>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>NewDiv',event)">
                                                                                                     <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/> </span>
                                                                                                 </a>     
                                                                                                  <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}InnerForm');setEditIndex('-1')">
                                                                                                       <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                                                                   </h:outputLink> 
                                                                                                   <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelEdit">
                                                                                                      <a title="<h:outputText value="#{msgs.source_rec_cancel_but}"/>  <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>InnerForm', '<h:outputText value='#{childNodesName}'/>cancelEdit', '<h:outputText value='#{childNodesName}'/>')">
                                                                                                          <span><h:outputText value="#{msgs.source_rec_cancel_but}"/> <h:outputText value='#{childNodesName}'/></span>
                                                                                                       </a>     
                                                                                                    </div>
											                                                 </nobr>																		    
																					  </td>
																					</tr>
                                                                                    <!--EDIT SO buttons ENDS-->
                                                                                </table>   
                                                                            </div>  
                                                                    </td></tr>
                                                                        
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="stealth" style="visibility:hidden;display:none;"></div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="<h:outputText value="#{childNodesName}"/>NewDiv" >
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td colspan="2">
                                                                            <div id="<h:outputText value="#{childNodesName}"/>AddDiv"></div>
                                                                        </td>
                                                                    </tr>
                                                                        
                                                                </table>   
                                                            </h:column>
                                                        </h:dataTable>
                                                        <!-- End Display minor objects fields --> 
                                                        <!-- End Edit Acive SO -->
                                                         </h:form>
                                             
                                                          <!-- End EDIT Fields-->														   
                                                         <%} else if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus()) || "merged".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                                   <!-- Start READ ONY Fields-->
                                                                   <!--Start Displaying the root node fields -->                                        
                                            <!--Start Displaying the root node fields -->                                        
                                            <div class="minorobjects">                                                    
											   <table border="0" cellpadding="1" cellspacing="1" width="100%">
                                                 <tr><td class="tablehead" width="100%"><b><%=objScreenObject.getRootObj().getName()%></b> &nbsp; </td></tr>
 												 <tr>
												   <td>
												    <table border="0" cellpadding="1" cellspacing="1" >
                                                   <%  HashMap rootFieldValuesMap  = (HashMap) systyemObjectAsHashMap.get("SYSTEM_OBJECT");
                                                        for (int ifc = 0; ifc < roorNodeFieldConfigs.length; ifc++) {
                                                            FieldConfig fieldConfigMap = (FieldConfig) roorNodeFieldConfigs[ifc];
                                                                        %>  
                                                      <tr>
                                                        <th align="left">
                                                          <%=fieldConfigMap.getDisplayName()%>
                                                        </th>
                                                        <td>
														<%if(fieldConfigMap.getGuiType().equalsIgnoreCase("TextBox")) {%>
                                                          <input type="text" title="<%=fieldConfigMap.getDisplayName()%>" style="background:#efefef;border: 1px inset;" value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>"  readonly="true" disabled="true"/>
														<%}else if(fieldConfigMap.getGuiType().equalsIgnoreCase("TextArea")) {%>
 														   <textarea title="<%=fieldConfigMap.getDisplayName()%>" disabled="true" readonly="true" style="background:#efefef;border: 1px inset;" >
														      <%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>
                                                           </textarea>
														<%}else if(fieldConfigMap.getGuiType().equalsIgnoreCase("MenuList")) {%>
														  <select readonly="true" disabled="true" style="background:#efefef;border: 1px inset;"  title="<%=fieldConfigMap.getDisplayName()%>"> 
														    <option value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : "&nbsp"%>"><%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%></option>
														   </select>                                              
														 <%} else {%>
                                                          <input type="text" title="<%=fieldConfigMap.getDisplayName()%>" value="<%=(rootFieldValuesMap.get(fieldConfigMap.getFullFieldName())) != null ? rootFieldValuesMap.get(fieldConfigMap.getFullFieldName()) : " "%>"  readonly="true" disabled="true" style="background:#efefef;border: 1px inset;" />
														<%}%>
														</td>
                                                     </tr>
                                                   <%}%>
												   </table>
												</td>
												</tr>
                                                <!-- STARTDisplaying the minor object fields -->    
                                                <% String epathValue = new String();
                                                  for (int io = 0; io < arrObjectNodeConfig.length; io++) {
                                                     ObjectNodeConfig childObjectNodeConfig = arrObjectNodeConfig[io];
                                                     ArrayList  minorObjectMapList =  (ArrayList) systyemObjectAsHashMap.get("SOEDIT" + childObjectNodeConfig.getName() + "ArrayList");
								                %>
                                                <tr><td>&nbsp;</td></tr>
                                                <tr><td class="tablehead" width="100%"><b><%=childObjectNodeConfig.getName()%></b> &nbsp; </td></tr>
  											    <tr>
												  <td> 
												     <div style="BORDER-RIGHT: #91bedb 1px solid; BORDER-TOP: #91bedb 1px solid; PADDING-LEFT: 1px;BORDER-LEFT: #91bedb 1px solid; PADDING-TOP: 0px; width:100%;BORDER-BOTTOM: #91bedb 1px solid; BACKGROUND-REPEAT: no-repeat; POSITION: relative;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left; overflow:auto">
													 <table border="0" width="100%" cellpadding="0">
                                                     <%
                                                      FieldConfig[] fieldConfigArrayMinor = (FieldConfig[]) allNodefieldsMap.get(childObjectNodeConfig.getName());
                                                       HashMap minorObjectMap = new HashMap();
													   %>
                                                        <%if(minorObjectMapList.size() == 0) {%>
                                                        <tr class="odd">
														  <td><%=bundle.getString("source_rec_nodetails_text")%></td>
														</tr>
                                                       <%}%>
													   <%
			 					                       for(int ar = 0; ar < minorObjectMapList.size() ;ar ++) {
                                                         minorObjectMap = (HashMap) minorObjectMapList.get(ar);
														 String styleClass = ((ar%2==0)?"even":"odd");
                                                     %>

													   <%if(ar == 0) {%>
                                                        <tr>			   
                                                           <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
 			                                                  <td class="tablehead">
				                                                 <%=fieldConfigArrayMinor[k].getDisplayName()%>
                                                               </td>
		                                                  <%}%>
														</tr> 
                                                      <%}%>

													 <tr style="background:#efefef;" >
                                                        <% for(int k=0;k<fieldConfigArrayMinor.length;k++) {%>
                                                          <td>
														  <%if(minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()) != null ) {%>  <!--if has value-->
															   <%if(fieldConfigArrayMinor[k].getValueList() != null) {%> <!-- if the field config has value list-->
																  <%if (fieldConfigArrayMinor[k].getUserCode() != null){%> <!-- if it has user defined value list-->
																	 <%=ValidationService.getInstance().getUserCodeDescription(fieldConfigArrayMinor[k].getUserCode(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
																  <%}else{%>
																	<%=ValidationService.getInstance().getDescription(fieldConfigArrayMinor[k].getValueList(), (String) minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()))%>
																 <%}%>
															   <%} else {%> <!--minorObjectMap- In other cases-->
															   <%
																String value = minorObjectMap.get(fieldConfigArrayMinor[k].getFullFieldName()).toString();   
																if (fieldConfigArrayMinor[k].getInputMask() != null && fieldConfigArrayMinor[k].getInputMask().length() > 0) {
																  if (value != null) {
																	 //Mask the value as per the masking 
																	 value = fieldConfigArrayMinor[k].mask(value.toString());
																   }
																} 
																%> 
																 <%=value%>
															   <%}%>
														  <%} else {%> <!-- else print &nbsp-->
															&nbsp;
														  <%}%>
 								                          </td>
							                          <% } %>
													</tr>
														  
													 <%}%>

 													 </table>
													 </div>
												  </td>
												</tr>
												<%}%>
                                            <!--End Displaying the minor object fields -->    
 										</table>

                                       </div>

                                     <!--End Displaying the root node fields -->    
                                                           <!-- End Display minor objects fields --> 
                                                           <!-- End READ ONLY Fields-->
                                                          <%}%>
                                                          <h:form>
                                                        <table>  
                                                            <tr>       
                                                                <% if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>          
                                                                    
                                                                <td>
                                                                    <!-- Edit Submit button-->
                                                                    <a title="<h:outputText value="#{msgs.source_rec_save_but}"/>"  class="button" 
                                                                       href="javascript:void(0);"
                                                                       onclick="javascript:getFormValues('BasicSearchFieldsForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/editminorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','editFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.source_rec_save_but}"/></span>
                                                                    </a>                                     
                                                                        
                                                                        
                                                                </td>                                                                
                                                                <td>
                                                                    <!-- Edit CANCEL button-->
                                                                    <h:commandLink title="#{msgs.cancel_but_text}"  styleClass="button" 
                                                                                    action="#{SourceHandler.cancelEditLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                                                      
                                                                </td>
                                                                <%}%>                                                                         
                                                                <td>                                                                   
                                                                    <h:commandLink title="#{msgs.source_rec_vieweuid_but}"  styleClass="button" rendered="#{Operations.EO_SearchViewSBR}" 
                                                                                    action="#{NavigationHandler.toEuidDetails}" 
                                                                                    actionListener="#{SourceHandler.viewEUID}" >  
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_vieweuid_but}"/></span>
                                                                    </h:commandLink>                                     
                                                                </td>
                                                                <td> 
                                                                    <%if ("active".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink title="#{msgs.source_rec_deactivate_but}"  styleClass="button" rendered="#{Operations.SO_Deactivate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.deactivateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_deactivate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                    <%if ("inactive".equalsIgnoreCase(singleSystemObjectLID.getStatus())) {%>
                                                                    <h:commandLink title="#{msgs.source_rec_activate_but}"  styleClass="button" rendered="#{Operations.SO_Activate}"
                                                                                    action="#{NavigationHandler.toSourceRecords}" 
                                                                                    actionListener="#{SourceHandler.activateSO}">
                                                                        <f:attribute name="soValueExpression" value="<%=soValueExpression%>"/>
                                                                        <span><h:outputText value="#{msgs.source_rec_activate_but}" /></span>
                                                                    </h:commandLink>                         
                                                                    <%}%>            
                                                                </td>
                                                               
                                                            </tr>
                                                        </table>
                                                        </h:form>    <!-- close Action button on Edit tab -->
                                                    </td>
                                                </tr>
                                            </table>
                                                
                                    </div>
                                    <%}%>
                                    <%} else {%>
                                    <!--START SEARCH CRITERIA-->

                                    <div id="sourceViewBasicSearch">
                                        <h:form id="basicViewformData">
                                            <h:inputHidden id="enteredFieldValues" value="#{SourceHandler.enteredFieldValues}"/>
                                                
                                            <input type="hidden" name="lidmask" value="DDD-DDD-DDDD" />
                                            <table border="0" cellpadding="0" cellspacing="0">
                                                <tr><td>&nbsp;</td></tr>
                                                <tr>
                                                    <td>
                                                        <h:dataTable id="fieldConfigId" var="feildConfig" headerClass="tablehead"  value="#{SourceHandler.viewEditScreenConfigArray}">
                                                            <!--Rendering Non Updateable HTML Text Area-->
                                                            <h:column>
                                                                <h:outputText value="#{feildConfig.displayName}" />
                                                                <h:outputText  value="*"  rendered="#{feildConfig.required}" /> 
                                                            </h:column> 
                                                                
                                                            <!--Rendering HTML Select Menu List-->
                                                            <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                                                <h:selectOneMenu title="SystemCode" rendered="#{feildConfig.name eq 'SystemCode'}"
onblur="javascript:accumilateFormSelectFieldsOnBlur('basicViewformData',this,'#{feildConfig.name}')"
onchange="javascript:setLidMaskValue(this,'basicViewformData')">
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                                    
                                                                <h:selectOneMenu onblur="javascript:accumilateFormSelectFieldsOnBlur('basicViewformData',this,'#{feildConfig.name}')"
                                                     rendered="#{feildConfig.name ne 'SystemCode'}">
                                                                    <f:selectItem itemLabel="" itemValue="" />
                                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                </h:selectOneMenu>
                                                                    
                                                            </h:column>
                                                                
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 }" >
                                                                <nobr>
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                   onblur="javascript:accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                   maxlength="#{feildConfig.maxLength}" 
                                                                                   rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   id="LID" 
                                                                                   label="#{feildConfig.displayName}" 
																				   title="<%=localIdDesignationVE%>"
                                                                                   readonly="true"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, document.basicViewformData.lidmask.value)"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                   onblur="javascript:accumilateFormFieldsOnBlur(this,'#{feildConfig.name}',document.basicViewformData.lidmask.value,'#{feildConfig.valueType}','basicViewformData')"
                                                                                   rendered="#{feildConfig.name eq 'LID'}"/>
                                                                                       
                                                                    <h:inputText   required="#{feildConfig.required}" 
                                                                                   label="#{feildConfig.displayName}" 
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																				   onfocus="javascript:clear_masking_on_focus()"
                                                                                   onblur="accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                   maxlength="#{SourceHandler.euidLength}" 
                                                                                   rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                                       
                                                                                       
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                                                <nobr>
                                                                    <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   
                                                                                     onblur="accumilateFormFieldsOnBlur(this,'#{feildConfig.name}','#{feildConfig.inputMask}','#{feildConfig.valueType}','basicViewformData')"
                                                                                         
                                                                                     required="#{feildConfig.required}"/>
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                                                <nobr>
                                                                    <input type="text" 
                                                                           id = "<h:outputText value="#{feildConfig.name}"/>"  
                                                                           value="<h:outputText value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                                           required="<h:outputText value="#{feildConfig.required}"/>" 
                                                                           maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                                           onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                           onblur="javascript:validate_date(this,'<%=dateFormat%>');javascript:accumilateFormFieldsOnBlur('basicViewformData',this,'<h:outputText value="#{feildConfig.name}"/>')">
                                                                    <a href="javascript:void(0);" 
												     title="<h:outputText value="#{feildConfig.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{feildConfig.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{feildConfig.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                                                </nobr>
                                                            </h:column>
                                                                
                                                            <f:facet name="footer">
                                                                <h:column>
                                                                   
                                                                    <h:commandLink title="#{msgs.patdetails_search_button2}" styleClass="button" action="#{SourceHandler.performSubmit}" >  
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button2}"/></span>
                                                                    </h:commandLink>                                     
																	<a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>"  class="button" href="javascript:ClearContents('basicViewformData')">
                                                                        <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                    </a>
                                                                </h:column>
                                                                    
                                                            </f:facet>
                                                                
                                                        </h:dataTable>
                                                    </td>
                                                    <td valign="top">
                                                        <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                                        <h:messages  styleClass="errorMessages"  layout="list" />
                                                        <%}%>
                                                    </td>	
                                                </tr>
                                            </table>  
                                        </h:form>
                                    </div>                                                                 
                                    <!--END SEARCH CRITERIA-->
                                    <%}%>
                                    
                                </div>  
                              <%}%> 
                              <% if(operations.isSO_Add()){%> 
                                <div id="addTab">
                                    

                                        <table width="100%">
                                       <%if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                            <tr>
                                                <td>

                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
									   <%}%>
                                            <tr>
                                                <td>
				                <h:form id="basicValidateAddformData">
                                                   <table border="0" cellpadding="1" cellspacing="1" width="100%">
                                                        <tr><td>
                                                    <!--Start Add source record form-->
                                                    <input type="hidden" title="lidmask" name="lidmask" value="DDD-DDD-DDDD" />
                                                    <h:dataTable headerClass="tablehead"  
                                                                             id="fieldConfigId" 
                                                                             var="feildConfig" 
                                                                             value="#{SourceAddHandler.addScreenConfigArray}">
                                                                    <!--Rendering Non Updateable HTML Text Area-->
                                                                    <h:column>
                                                                        <nobr>
                                                                            <h:outputText value="*" rendered="#{feildConfig.required}" />
                                                                            <h:outputText value="#{feildConfig.displayName}" />
                                                                        </nobr>
                                                                    </h:column>
                                                                    <!--Rendering HTML Select Menu List-->
                                                                    <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                                                        <nobr>
                                                                            <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'basicValidateAddformData')"
                                                                                              id="SystemCode" 
																							  title="SystemCode"
                                                                                              rendered="#{feildConfig.name eq 'SystemCode'}"
                                                                                              required="true">
                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                                            </h:selectOneMenu>
                                                                        </nobr>
                                                                    </h:column>
                                                                    <!--Rendering Updateable HTML Text boxes-->
                                                                    <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 && feildConfig.name ne 'LID' }" >
                                                                        <nobr>
                                                                            <h:inputText   required="true" 
                                                                                           label="#{feildConfig.displayName}" 
                                                                                           value=""/>
                                                                                           
                                                                        </nobr>
                                                                    </h:column>
                                                                    <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 && feildConfig.name eq 'LID'}" >
                                                                        <nobr>
                                                                            <h:inputText   id="LID"
																			               title="LID"
                                                                                           required="true" 
																						   readonly="true"
																						   label="#{feildConfig.displayName}" 
																						   maxlength="#{feildConfig.maxLength}"    onkeydown="javascript:qws_field_on_key_down(this, document.basicValidateAddformData.lidmask.value)"
                                                                                           onblur="javascript:qws_field_on_key_down(this, document.basicValidateAddformData.lidmask.value);"
                                                                                           onkeyup="javascript:qws_field_on_key_up(this)"
																						   onfocus="javascript:clear_masking_on_focus()"
                                                                                           />
                                                                                           
                                                                        </nobr>
                                                                    </h:column>
                                                          </h:dataTable>
                                                     </td>
                                                     <td>
                                                        <div id="addFormValidate"> </div>    
                                                     </td>
                                                     </tr>
												</table>
                                               </h:form>
						
                                                  <div id="addFormFields" style="visibility:hidden;display:none;">
                                                    <!-- Start ADD  Fields-->
                                                    <table width="100%">
														<tr>
														<td style="font-size:10px;" colspan="2">
															 <nobr>
																 <span style="font-size:12px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
															</nobr>
														</td>
													  </tr> 
                                                       <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <b><%=objScreenObject.getRootObj().getName()%>&nbsp;Info</b>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
                                           <table width="100%" cellspacing="1"  border="0" cellpadding="1">
                                            <tr>
                                             <td colspan="2">
				                            <form id="<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm" name="<%=objScreenObject.getRootObj().getName()%>InnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                                    <h:dataTable  headerClass="tablehead"  
                                                                  id="hashIdEdit" 
                                                                   var="fieldConfigPerAdd" 
                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">
                                                        <h:column>
															 <h:outputText rendered="#{fieldConfigPerAdd.required}">
																<span style="font-size:12px;color:red;verticle-align:top">*</span>
															</h:outputText>													  
															<h:outputText rendered="#{!fieldConfigPerAdd.required}">
																<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
															</h:outputText>													  
															<h:outputText value="#{fieldConfigPerAdd.displayName}" />
														    <h:outputText value=":"/>
                                                         </h:column>
                                                        <!--Rendering HTML Select Menu List-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'MenuList' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:selectOneMenu title="#{fieldConfigPerAdd.fullFieldName}" >
                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                <f:selectItems  value="#{fieldConfigPerAdd.selectOptions}"  />
                                                            </h:selectOneMenu>
                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text boxes-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputText label="#{fieldConfigPerAdd.displayName}"  
                                                                         id="fieldConfigIdTextbox"  
																		 title="#{fieldConfigPerAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
																		  maxlength="#{fieldConfigPerAdd.maxLength}"
																		 onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
																		 onfocus="javascript:clear_masking_on_focus()"
                                                                         required="#{fieldConfigPerAdd.required}"/>
                                                        </h:column>                     
                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">

                                          <nobr>
                                            <input type="text" 
											       title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                                                   id = "<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                                                   required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
												   onblur="javascript:validate_date(this,'<%=dateFormat%>');"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                                                  onkeyup="javascript:qws_field_on_key_up(this)" >
                                                  <a href="javascript:void(0);" 
												     title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{fieldConfigPerAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                          </nobr>


                                                        </h:column>
                                                        <!--Rendering Updateable HTML Text Area-->
                                                        <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextArea' &&  fieldConfigPerAdd.valueType ne 6}" >
                                                            <h:inputTextarea label="#{fieldConfigPerAdd.displayName}"  
															                  title="#{fieldConfigPerAdd.fullFieldName}"
                                                                             id="fieldConfigIdTextArea"   
                                                                             required="#{fieldConfigPerAdd.required}"
                                                                             />
                                                        </h:column>
                                                        
                                                    </h:dataTable>
                                                    </form>
                                          </td>
										  </tr>
										  </table>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="allChildNodesNamesAdd" 
                                                                  width="100%"
                                                                  var="childNodesName" 
                                                                  value="#{SourceHandler.allChildNodesNames}">
                                                        <h:column>
                                                            <table width="100%">
                                                                <tr>
                                                                    <td class="tablehead" colspan="2">
                                                                        <h:outputText value="#{childNodesName}"/>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td colspan="2">
																    <input type="hidden" value="0" id="<h:outputText value="#{childNodesName}"/>CountValue" />
                                                                    </td>
                                                                </tr>
																
                                                                <tr>
                                                                    <td colspan="2">
																	<a title="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp; <h:outputText value="#{childNodesName}"/>" href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')" class="button">
																	<span>
							                                            <img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>&nbsp;Add <h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="Add <h:outputText value="#{childNodesName}"/>"/>
																	</span>

                                                                    </td>
                                                                </tr>
			<!--Minor objects loop starts-->
																<tr>
																<td>
              <div id="extra<h:outputText value='#{childNodesName}'/>AddNewDiv"  style="visibility:hidden;display:none;">
                <table>
                    <tr>
                        <td colspan="2" align="left">
                            <form id="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" name="<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm" method="post" enctype="application/x-www-form-urlencoded">
                                <h:dataTable  headerClass="tablehead" 
                                              id="allNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              width="100%"
                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
                                        <h:dataTable  headerClass="tablehead" 
                                                      id="childFieldConfigsAdd" 
                                                      var="childFieldConfigAdd" 
                                                      width="100%"
                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                            
                                            <h:column>
												 <h:outputText rendered="#{childFieldConfigAdd.required}">
													<span style="font-size:12px;color:red;verticle-align:top">*</span>
												</h:outputText>													  
												<h:outputText rendered="#{!childFieldConfigAdd.required}">
													<span style="font-size:12px;color:red;verticle-align:top">&nbsp;</span>
												</h:outputText>													  
												<h:outputText value="#{childFieldConfigAdd.displayName}" />
												<h:outputText value=":"/>
  											</h:column>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <!-- User code fields here -->
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" 
												onchange="getFormValues('#{childNodesName}AddNewSOInnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand='+RAND_VAL+'&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
												rendered="#{childFieldConfigAdd.userCode ne null}">
												    <f:selectItem itemLabel="" itemValue="" />
                                                   <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>    
												
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" rendered="#{childFieldConfigAdd.userCode eq null}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox' &&  childFieldConfigAdd.valueType ne 6}" >
                                           
                                                            <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, userDefinedInputMask)"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		 onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
                                                                         onkeyup="javascript:qws_field_on_key_up(this)" 
																		 onfocus="javascript:clear_masking_on_focus()"
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy ne null}"
																		 />     
																		 
																		 <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                                         title="#{childFieldConfigAdd.fullFieldName}"
                                                                         onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
																		  maxlength="#{childFieldConfigAdd.maxLength}"
																		  onfocus="javascript:clear_masking_on_focus()"
																		  onblur="javascript:validate_Integer_fields(this,'#{childFieldConfigAdd.displayName}','#{childFieldConfigAdd.valueType}')"
																		 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                         required="#{childFieldConfigAdd.required}"
																		 rendered="#{childFieldConfigAdd.constraintBy eq null}"
																		 />


                                            </h:column>                     
 
                                     <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'  &&  childFieldConfigAdd.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" title = "<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>"  
                                                   id = "<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                   required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                   maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                  onblur="javascript:validate_date(this,'<%=dateFormat%>');">
                                                 <a href="javascript:void(0);" 
												     title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>"
                                                     onclick="g_Calendar.show(event,
												          '<h:outputText value="#{childFieldConfigAdd.name}"/>',
														  '<%=dateFormat%>',
														  '<%=global_daysOfWeek%>',
														  '<%=global_months%>',
														  '<%=cal_prev_text%>',
														  '<%=cal_next_text%>',
														  '<%=cal_today_text%>',
														  '<%=cal_month_text%>',
														  '<%=cal_year_text%>')" 
														  ><img  border="0"  title="<h:outputText value="#{childFieldConfigAdd.displayName}"/> (<%=dateFormat%>)"  src="./images/cal.gif"/></a>
												  <font class="dateFormat">(<%=dateFormat%>)</font>
                                          </nobr>
                                     </h:column>                     


                                           <!--Rendering Updateable HTML Text Area-->
                                            
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea title="#{childFieldConfigAdd.fullFieldName}"  
                                                                 required="#{fieldConfigAddAddress.required}" />
                                            </h:column>
                                        </h:dataTable>                                                                                
                                    </h:column>
                                </h:dataTable>                                                                                
                                
                            </form>
                        </td>
                    </tr>
                    <!--Add New SO buttons START-->
					<tr>
					  <td>
                            <nobr>
                                <a title="<h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
                                        <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="#{msgs.source_rec_save_but}"/> <h:outputText value='#{childNodesName}'/> </span>
                                 </a>     
                                  <h:outputLink  title ="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}AddNewSOInnerForm');setEditIndex('-1')">
                                          <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                   </h:outputLink>

                                    <div style="visibility:hidden;display:none;" id="<h:outputText value='#{childNodesName}'/>cancelSOEdit">
                                         <a title="<h:outputText value="#{msgs.source_rec_cancel_but}"/>&nbsp; <h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:cancelEdit('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm', '<h:outputText value='#{childNodesName}'/>cancelSOEdit', '<h:outputText value='#{childNodesName}'/>')">
                                          <span><h:outputText value="#{msgs.source_rec_cancel_but}"/> <h:outputText value='#{childNodesName}'/></span>
                                         </a>     
                                   </div>
                             </nobr>
                      
					  </td>
					</tr>
                    <!--Add New SO buttons ENDS -->

                </table>   
            </div>   
			</td>
			<td><div id="<h:outputText value='#{childNodesName}'/>EditMessages" >   </div></td>
			</tr>
			<!--Minor objects loop ends-->

			 <tr>
				 <td colspan="2">
 					 <div id="stealth" style="visibility:hidden;heigh:0px"> </div>
				 </td>
		     </tr>
			 <tr>
 				 <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/>NewDiv" ></div>
				 </td>
			 </tr>
			 <tr>
			    <td colspan="2">
					 <div id="<h:outputText value="#{childNodesName}"/>AddNewSODiv"></div>
				 </td>
			 </tr>

              </table>   
           </h:column>
          </h:dataTable>
         </div>
		<!--End Add source record form-->
													<div id="validateButtons" style="visibility:visible;display:block">
                                                    <table>
                                                        <tr>

                                                            <td>
                                                                </nobr>
                                                                <nobr>
                                                                    <a title ="<h:outputText value="#{msgs.validate_button_text}"/>" class="button" 
																	   href="javascript:void(0);"													
																	   onclick="javascript:getFormValues('basicValidateAddformData');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&validate=true&rand=<%=rand%>','addFormValidate','');" >  
<!--- Validate Button -->
                                                                         <span><h:outputText value="#{msgs.validate_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
                                                            <td>
                                                                <a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>" class="button" href="javascript:ClearContents('basicValidateAddformData');">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                    </table>
													</div>
													<div id="saveButtons" style="visibility:hidden;display:none">
                                                    <table>
                                                        <tr>
                                                            <td>
                                                                <a title="<h:outputText value="#{msgs.patdetails_search_button1}"/>" class="button" 
																   href="javascript:void(0);"
																   onclick="javascript:ClearContents('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');setEditIndex('-1')">
                                                                    <span><h:outputText value="#{msgs.patdetails_search_button1}"/></span>
                                                                </a>
                                                            </td>
                                                            <td>
                                                                <nobr>
                                                                    <a title = "<h:outputText value="#{msgs.submit_button_text}"/>" class="button" 
																	   href="javascript:void(0);"
																	   onclick="javascript:getFormValues('<%=objScreenObject.getRootObj().getName()%>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/minorobjects.jsf?'+queryStr+'&save=true&rand=<%=rand%>','addFormValidate','');" >  
                                                                        <span><h:outputText value="#{msgs.submit_button_text}"/></span>
                                                                    </a>                                     
                                                                </nobr>
                                                            </td>
															<td>
                                                                <h:form>
																 <!-- Edit CANCEL button-->
                                                                    <h:commandLink title="#{msgs.cancel_but_text}" styleClass="button" 
                                                                                    action="#{SourceHandler.cancelSaveLID}" >  
                                                                        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                                                    </h:commandLink>                                                                      
                                                                </h:form>
                       

															</td>
                                                        </tr>
                                                    </table>
													</div>
                                             </td>   
                                          </tr>   
                                       </table>   
	                                </div>
                              <%}%> 
                              <% if(operations.isSO_Merge()){%> 
                                <div id="mergeTab">
                                        <table border="0" cellpadding="0" cellspacing="0">
                                       <%if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
                                            <tr>
                                                <td>

                                                    <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                                </td>
                                            </tr>
									   <%}%>

                                            <tr>
                                                <td>
                                                   <h:form id="basicMergeformData">
                                                     <table border="0" cellpadding="4" cellspacing="4">
                                                           <tr>
                                                               <td>
                                                                   <h:outputLabel style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   for="#{msgs.transaction_source}" value="#{msgs.transaction_source}"/>
                                                                </td>
                                                               <td>
                                                                   <h:selectOneMenu title="#{msgs.transaction_source}" onchange="javascript:setLidMaskMergeValue(this,'basicMergeformData')"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"id="sourceOption" 
                                                                   value="#{SourceMergeHandler.source}" >
																	   <f:selectItem itemLabel="" itemValue="" />
                                                                       <f:selectItems  value="#{SourceMergeHandler.selectOptions}" />
                                                                   </h:selectOneMenu>
                                                               </td>
                                                               <input id='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />           
                                                               <td>
                                                               <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 1  </font>
																   <%
																	ValueExpression mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 1" ,  localIdDesignation.getClass());   
																   %>
                                                               <h:inputText value="#{SourceMergeHandler.lid1}" 
															        id="LID1"
															        title="<%=mergeLIDVaueExpression%>" 
 																    style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                    onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                    onkeyup="javascript:qws_field_on_key_up(this)"
															        onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"
																	/>  

																	<%
																	mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 2" ,  localIdDesignation.getClass());   	
																	%>
                                                               <td>
                                                                
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 2  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid2}" id="LID2" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)"
																   onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
                                                               </td>																   <%
																	 mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 3" ,  localIdDesignation.getClass());   
																   %>
                                                               <td>
 
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%>3  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid3}" id="LID3" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
																				onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
                                                               </td>
                                                                   <%
																	 mergeLIDVaueExpression = ExpressionFactory.newInstance().createValueExpression( localIdDesignation+ " 4" ,  localIdDesignation.getClass());   
																   %>
                                                               <td>
																   <font style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                   > <%=localIdDesignation%> 4  </font>
                                                                   <h:inputText value="#{SourceMergeHandler.lid4}" id="LID4" title="<%=mergeLIDVaueExpression%>"
																   style="font-family: Arial, Helvetica, sans-serif;font-size:10px;color:#837F74;text-align:left;vertical-align:middle;"
                                                                                onkeydown="javascript:qws_field_on_key_down(this,document.basicMergeformData.lidmask.value)"
                                                                                onkeyup="javascript:qws_field_on_key_up(this)"
																				onblur="javascript:checkDuplicateFileds('basicMergeformData',this,'#{msgs.already_found_error_text}')"/>  
                                                               </td>
                                                            </tr>
                                                            <tr>
                                                               <td colspan="2">
                                                                  <nobr>
                                                                       <a title="<h:outputText value="#{msgs.source_merge_button}"/>"
                                                                          href="javascript:void(0)"
                                                                          onclick="javascript:getFormValues('basicMergeformData');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&save=true&rand=<%=rand%>','sourceRecordMergeDiv','');"  
                                                                          class="button" >
                                                                           <span><h:outputText value="#{msgs.source_merge_button}"/></span>
                                                                       </a>                                     
                                                                </nobr> 
                                                               <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('basicMergeformData')" >
                                                               <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                              </h:outputLink>
                                                               </td>
                                                               <td colspan="4">&nbsp;</td>
                                                           </tr>
                                                
                                                    </table>
							                       <input type="hidden" id="duplicateLid" title="duplicateLid" />
                                                    </h:form>
                                             <hr/>
											 <table>
											   <tr><td><div id="duplicateIdsDiv" class="ajaxalert"></div></td></tr>
											   <tr><td><div id="sourceRecordMergeDiv"></div></td></tr>
											   <tr><td><div id="sourceRecordEuidDiv"></div></td></tr>
											 </table>
 
 
                                </div>
                              <%}%>
                            </div> <!-- End YUI content -->
                        </div> <!-- demo end -->
                    </td>
                </tr>
            </table>
            
        </div> <!--end source records div -->
         <!-- START Extra divs for add  SO-->
         <div id="mergeDiv" class="alert" style="top:500px;left:560px;visibility:hidden">
             <h:form id="mergeFinalForm">
                 <table cellspacing="0" cellpadding="0" border="0" width="100%">
                     <tr><th align="left" colspan="2"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th> </tr>
                      <tr><td colspan="2">&nbsp;</td></tr>
                      <tr><td colspan="2">&nbsp;</td></tr>
                      <tr>
					     <td colspan="2" style="color:#ffffff;">&nbsp;<h:outputText value="#{msgs.source_keep_btn}"/>&nbsp;<%=localIdDesignation%>&nbsp;&nbsp;<span style="color:#ffffff;font-weight:bold;" id="soMergeConfirmContent"></span>&nbsp;&nbsp;?</td>
					 </tr>
                     <tr><td colspan="2">&nbsp;</td></tr>
                     <tr>
                         <td colspan="2"  align="center" valign="top" width="100%">
                              <a title="<h:outputText value="#{msgs.ok_text_button}"/>"
                                href="javascript:void(0)"
                                onclick="javascript:getDuplicateFormValues('basicMergeformData','mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/lidmergeservice.jsf?'+queryStr+'&mergeFinal=true&rand=<%=rand%>','sourceRecordMergeDiv','');"  
                                class="button" >
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                                </a>                                     
  							 <h:outputLink title="#{msgs.cancel_but_text}"  onclick="javascript:showExtraDivs('mergeDiv',event)" 
                                            styleClass="button"          
                                            value="javascript:void(0)">
                                 <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                             </h:outputLink>   
  							 <input type="hidden" id="mergeFinalForm:previewhiddenLid1" title="mergeFinalForm_LIDS" />
							 <input type="hidden" id="mergeFinalForm:previewhiddenLid1source" title="mergeFinalForm_SOURCE" />
							 <input type="hidden" id="mergeFinalForm:selectedMergeFields" title="mergeFinalForm_MODIFIED_VALUES" />
					     </td>
                     </tr> 
                 </table>
             </h:form>
         </div>		
	
       <!-- END Extra divs for add SO-->
       <!-- Start Extra divs for editing SO-->
  
    <!-- End Extra divs for editing SO-->
     <!-- Start Extra divs for add SO-->
 <!-- End Extra divs for add SO-->                                                                                                                                       
     <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
                                             
<script>
(function() {
    var tabView = new YAHOO.widget.TabView('demo');

    YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
})();
</script>

<!--END SOURCE CODE FOR EXAMPLE =============================== -->
</div>

   <h:dataTable  headerClass="tablehead" 
                 id="allChildNodeschildBallon" 
                 var="childNodesName" 
                 value="#{SourceHandler.allChildNodesNames}">
           <h:column>
              <div id="balloon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
              <div id="addballoon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
              <div id="editballoon<h:outputText value="#{childNodesName}"/>" class="balloonstyle">"<h:outputText  value="#{childNodesName}"/>" Help text goes here.</div>
          </h:column>                 
    </h:dataTable>
    <!--Fix for Bug : 6692060 (By Sridhar) START-->
        <%if( request.getAttribute("mergeComplete") != null) {%>
		     <script>
		      document.getElementById('confirmationButton').style.visibility = 'hidden';
		      document.getElementById('confirmationButton').style.display = 'none';
       		 </script>
        <%} else if( request.getAttribute("lids") != null) {           
        String[] srcs  = (String[]) request.getAttribute("lids");
        String  lidsSource  = (String) request.getAttribute("lidsource");		
        for(int i=0;i<srcs.length;i++) {
        %>    
        
        <script>
            collectLid('<%=srcs[i]%>'); 
            document.getElementById('confirmationButton').style.visibility = 'visible';
            document.getElementById("previewActionButton").style.visibility = "hidden";
            document.getElementById("previewActionButton").style.display = "none";                        
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "blue";
        </script>
        <%}%>
        <script>
            document.getElementById("confirmContent").innerHTML  = '<%=srcs[1]%>';
            document.getElementById("mergeFinalForm:previewhiddenLid1").value  = '<%=srcs[0]+":" + srcs[1]%>';
            document.getElementById("mergeFinalForm:previewhiddenLid1source").value  = '<%=lidsSource%>';
        </script>
        <%}%> 
	  <!--Fix for Bug : 6692060 (By Sridhar) ENDS -->

<form id="EditIndexForm" name="EditIndexForm">
		<input type="hidden" id="EditIndexFormID" value="-1" />
</form>


</body>

        <%
          String[][] lidMaskingArray = sourceAddHandler.getAllSystemCodes();
          
          
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
        function setLidMaskMergeValue(field,formName) {
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            
			var lidField1 =  getDateFieldName(formNameValue.name,'LID1');
			var lidField2 =  getDateFieldName(formNameValue.name,'LID2');
			var lidField3 =  getDateFieldName(formNameValue.name,'LID3');
			var lidField4 =  getDateFieldName(formNameValue.name,'LID4');

            document.getElementById(lidField1).value = "";
            document.getElementById(lidField2).value = "";
            document.getElementById(lidField3).value = "";
            document.getElementById(lidField4).value = "";



            /*
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField1).value = "";
			 document.getElementById(lidField1).readOnly = true;

			 document.getElementById(lidField2).value = "";
			 document.getElementById(lidField3).readOnly = true;
             
			 document.getElementById(lidField3).value = "";
			 document.getElementById(lidField3).readOnly = true;
             
			 document.getElementById(lidField4).value = "";
			 document.getElementById(lidField4).readOnly = true;
		    }
			*/

            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   

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

           function validateLidValue(formName) {
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');

            if(document.getElementById(lidField).value.length > 0 && document.getElementById(lidField).value.length != formNameValue.lidmask.value.length) {
			   alert("'" + document.getElementById(lidField).value + "' is invalid LID Format please change the value! Should be in '" + formNameValue.lidmask.value +"' Format");
			   //document.getElementById(lidField).value = "";
			   return false;
			}
			return true;
         }   

         function resetLidFields(formName,validString) {
            var formNameValue = document.forms[formName];
            var lidField =  getDateFieldName(formNameValue.name,'LID');
            if(validString == 'Not Validated') {
              document.getElementById(lidField).value = "";
            }
         }


    </script>
    <script>
          var formName ="basicAddformData";
    </script>
    <% if ("View/Edit".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%} else if ("Add".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicAddformData";
    </script>
    
    <%} else if ("Merge".equalsIgnoreCase((String) session.getAttribute("tabName"))) {%>
    <script>
          formName = "basicMergeformData";
     </script>
      
    <%} else {%>
    <script>
          formName = "BasicSearchFieldsForm";
    </script>
    <%}%>  
  <!-- Clear the merge form fields upoon load of the page -->  
  <script>
   ClearContents("basicMergeformData");
  </script>

<p>kieran</p>
</html>
</f:view>
