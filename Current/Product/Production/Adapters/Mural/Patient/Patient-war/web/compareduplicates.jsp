<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ValidationService"  %>

<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"  %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%
 double rand = java.lang.Math.random();
 String URI = request.getRequestURI();
  URI = URI.substring(1, URI.lastIndexOf("/"));
  Operations operations = new Operations();
 %>

<f:view>
   
    <html>
        <head>
           <!-- YAHOO Global Object source file --> 
        <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
        
        <!-- Additional source files go here -->
        
        <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
        <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
        <link type="text/css" href="./css/balloontip.css"  rel="stylesheet" media="screen">    
        <script type="text/javascript" src="scripts/balloontip.js"></script>       
        <script type="text/javascript" src="scripts/edm.js"></script>
        <script type="text/javascript" src="scripts/Validation.js"></script>
        <script type="text/javascript" src="scripts/calpopup.js"></script>
        <script type="text/javascript" src="scripts/Control.js"></script>
        <script type="text/javascript" src="scripts/dateparse.js"></script>
        <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
        <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
        <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
        <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>  
		<script type="text/javascript" >
    	   var previewEuidDivs=[];
           var rand = "";
           function setRand(thisrand)  {
 	        rand = thisrand;
           }
           var editIndexid = "-1";
           function setEOEditIndex(editIndex)   {
				editIndexid = editIndex;
	   	   }
		   // LID merge related global javascript variables
		   var euids="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];

         </script>
        </head>
        <title><h:outputText value="#{msgs.application_heading}" /></title>
        <body>
            <%@include file="./templates/header.jsp"%>
             <div id="mainContent">
  			   <table width="100%"><tr><td>
                <div id="basicSearch" class="basicSearch" style="visibility:visible;display:block;">
                    <h:form id="advancedformData">
                        <table border="0" cellpadding="0" cellspacing="0" > 
                            <tr>
                                <td align="left">
                                    <h:outputText value="#{msgs.datatable_euid_text}"/>
                                </td>
                                <td align="left">
                                    <h:inputText   
                                        id="euidField"
                                        title="EUID"
                                        value="#{PatientDetailsHandler.singleEUID}" 
								        maxlength="#{SourceHandler.euidLength}" 
										/>
                                </td>
								 <!-- added by Narahari.M on 22/08/2008 for incorporate back button -->
								 
                                  <% if(request.getParameter("euids") != null) { %>
                                <td>                                    
                                     <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?pageName=compareduplicates.jsf&random='+rand+'&'+queryStr,'outputdiv','')"><span><h:outputText value="#{msgs.search_button_label}"/></span></a>
 
                                </td>                                    
                                <td>                                    
									 <h:commandLink  title="#{msgs.Advanced_search_text}"  styleClass="button" action="#{NavigationHandler.toPatientDetails}">  
                                        <span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                       </span>
                                    </h:commandLink>                          
							</td>
							<%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								                                                            
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
                                   
                            <%}else {%>
                                <td>                                    
                                       <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"
										       href="javascript:void(0)"
                                               onclick="javascript:getFormValues('advancedformData');setRand(Math.random());
ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?compareEuids=true&random='+rand+'&'+queryStr,'outputdiv','')">  
                                                <span><h:outputText value="#{msgs.search_button_label}"/></span></a>
                                 </td>                                    
                                <td>                             
                                        <a  href="duplicaterecords.jsf" 
									       class="button" 
										   title="<h:outputText value="#{msgs.Advanced_search_text}"/>" >  
  										<span>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                            <h:outputText  value="#{msgs.Advanced_search_text}"/>
                                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText  value="#{msgs.Advanced_search_text}"/>"/>
                                       </span>
									   </a>
                                </td> 
								<%if(request.getParameter("fromUrl") != null ) {%>
							 <td>
								
								<% 
								 String pageName = request.getParameter("fromUrl");
								 String previousQuery = request.getQueryString();
								 String URL= pageName+"?"+previousQuery+"&back=true";
								 %>
 			               		<a class="button" title="<h:outputText  value="#{msgs.back_button_text}"/>" href="<%=URL%>" >
						          <span><h:outputText  value="#{msgs.back_button_text}"/></span>
					            </a>

 							 </td><%} else {%>
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
	<%}%>
                             </tr>
                            <tr>
                                <td colspan="2">                           
                                           <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" /> 
                                </td>
                            </tr>
                             <tr>
                              <td colspan="2"><div style="color:red;" id="messages"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="2"><div id="outputdiv"></div></td>
                             </tr> 
				             <tr>
                                <td colspan="2"><div id="populatePreviewDiv"></div></td>
                             </tr> 
                        </table>
                    </h:form>
                 </div>
             </td></tr></table>
  			 <table width="100%">
			        <tr><td><div id="mainDupSource" class="compareResults"></div></td></tr>
 			 </table>
             </div> 
              <%if(request.getParameter("euids") != null) {%>
             	<script>
               	  ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&euids=<%=request.getParameter("euids")%>','mainDupSource','');
              	</script>
             <%} else {%>
             	<script>
               	  ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+'&rand=<%=rand%>&fromPage=<%=request.getParameter("fromPage")%>&duplicateEuids=<%=request.getParameter("duplicateEuids")%>','mainDupSource','');
                  fromPage ="<%=request.getParameter("fromPage")%>";
                  duplicateEuids = "<%=request.getParameter("duplicateEuids")%>";

              	</script>
             <%}%>

                         <div id="mergeDiv" class="alert" style="TOP:2050px; LEFT:300px; VISIBILITY: hidden; ">
                             <table cellspacing="0" cellpadding="0" border="0">
                                    <tr><th colspan="2" align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th></tr>
                                    <tr><td colspan="2"> &nbsp;</td></tr>
                                     <tr>
									   <td colspan="2" align="left"> 
									      <nobr>
									      <table border="0" align="left">
										     <tr>
											    <td align="left"><b><h:outputText value="#{msgs.mergediv_popup_text}"/></b></td>
											    <td align="left"><b><div id="merge_destnEuid"></div></b></td>
 											  </tr>
										  </table> 
										  </nobr>
									   </td>
									 </tr>
                                     <tr><td colspan="2"> &nbsp;</td></tr>
                                     <tr>
                                        <td colspan="2">
                                            <h:form  id="mergeFinalForm">
 							                      <%if(operations.isEO_Merge()) {%>
														<a href="javascript:void(0)"  
														   class="button" 
														    title="<h:outputText value="#{msgs.ok_text_button}" />"
                                                            onclick="javascript:getFormValues('mergeFinalForm');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&mergeFinal=true&rand=<%=rand%>','mainDupSource','');showExtraDivs('mergeDiv',event)"   >
                                                                     <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </a>	
												 <%}%>
                                                  <input type="hidden" id="mergeFinalForm:srcDestnEuids" title="MERGE_SRC_DESTN_EUIDS" />
                                                <h:inputHidden id="destinationEO" value="#{PatientDetailsHandler.destnEuid}" />
                                                <h:inputHidden id="selectedMergeFields" value="#{PatientDetailsHandler.selectedMergeFields}" />
                                                <h:outputLink styleClass="button"  title="#{msgs.cancel_but_text}"
                                                          value="javascript:void(0)" 
                                                          onclick="javascript:showExtraDivs('mergeDiv',event)" >
                                                 <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                                            </h:outputLink>
                                            </h:form>
                                        </td>
                                    </tr>
                                    
                                </table>
                        </div>  
                                   <div id="resolvePopupDiv" class="alert" style="TOP:2250px; LEFT:300px; visibility:hidden; ">
                                     
                                       <h:form id="reportYUISearch">
 										   <input type="hidden" title="resolvePotentialDuplicateId" id="resolvePotentialDuplicateId" />
                                           <table width="100%">
                                               <tr><th colspan="2" align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th></tr>
                                               <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr><td  colspan="2" align="center"><b><h:outputText value="#{msgs.different_person_dailog_text}"/></b></td></tr>
                                               <tr>
                                                   <td  colspan="2">
                                                         <h:selectOneMenu id="diffperson" title="resolveType">
                                                           <f:selectItem  itemValue="AutoResolve" itemLabel="#{msgs.AutoResolve_Label}"/>
                                                           <f:selectItem  itemValue="Resolve"     itemLabel="#{msgs.Resolve_Perm_Label}"/>
                                                        </h:selectOneMenu>                                                        
                                                   </td>
                                               </tr>
                                                <tr><td colspan="2"> &nbsp;</td></tr>
                                               <tr>
                                                   <td colspan="2">
                  										<a class="button" title="<h:outputText value="#{msgs.ok_text_button}" />"
                                                            href="javascript:void(0)"
                                                            onclick="javascript:getFormValues('reportYUISearch');ajaxURL('/<%=URI%>/ajaxservices/euidmergeservice.jsf?'+queryStr+'&resolveDuplicate=true&rand=<%=rand%>','mainDupSource','');"   >
                                                                <span><h:outputText value="#{msgs.ok_text_button}" /></span>
                                                       </a>   

                                                       <h:outputLink  onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'123467')" title="#{msgs.cancel_but_text}"
                                                                      styleClass="button"  
                                                                      value="javascript:void(0)">
                                                         <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                                                       </h:outputLink>   
                                                   </td>
                                               </tr>
                                           </table>
                                           
                                       </h:form>
                                   </div>                                                

             <div id="resolvepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.resolvepopup_help}"/></div>    
             <div id="mergepopuphelp" class="balloonstyle"><h:outputText  value="#{msgs.mergepopup_help}"/></div>    
             
        </body>
        <%
        if( request.getAttribute("eoMultiMergePreview") != null) {
            String destnEuid  = (String) request.getAttribute("destnEuid");
            String[] srcs  = (String[]) request.getAttribute("sourceEUIDs");
		%>
		<script>
			document.getElementById('mainEuidContent<%=destnEuid%>').className = "blue";
            document.getElementById('personEuidDataContent<%=destnEuid%>').className = "blue";
		</script>
		<%
        for(int i=0;i<srcs.length;i++) {
        %>    
            
        <script>
			document.getElementById('mainEuidContent<%=srcs[i]%>').className = "blue";
            document.getElementById('personEuidDataContent<%=srcs[i]%>').className = "blue";
         </script>
        <%}%>
        <%}%>

<script>
         if( document.advancedformData.elements[0]!=null) {
		var i;
		var max = document.advancedformData.length;
		for( i = 0; i < max; i++ ) {
			if( document.advancedformData.elements[ i ].type != "hidden" &&
				!document.advancedformData.elements[ i ].disabled &&
				!document.advancedformData.elements[ i ].readOnly ) {
				document.advancedformData.elements[ i ].focus();
				break;
			}
		}
      }         

    </script>
<script type="text/javascript">
    makeDraggable("resolvePopupDiv");
    makeDraggable("mergeDiv");
</script>

    </html>
    </f:view>
    
