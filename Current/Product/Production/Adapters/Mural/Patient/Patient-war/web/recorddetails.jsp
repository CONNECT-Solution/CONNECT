<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.PatientDetailsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultIterator"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchCriteria"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchOptions"%>
<%@ page import="com.sun.mdm.index.master.search.enterprise.EOSearchResultRecord"%>
<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathAPI"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.Set"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"%>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="javax.faces.context.FacesContext" %>

<f:view>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            
<!--CSS file (default YUI Sam Skin) -->
            <link  type="text/css" rel="stylesheet" href="./css/yui/datatable/assets/skins/sam/datatable.css">
            <!-- Dependencies -->
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/datasource/datasource-beta-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/dragdrop/dragdrop-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/json/json-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/calendar/calendar-min.js"></script>
            <script type="text/javascript" src="./scripts/yui/connection/connection-min.js"></script>
            <!-- Source files -->
            <script type="text/javascript" src="./scripts/yui/datatable/datatable-beta-min.js"></script>
<script>
//not used in transactions, but since its require in the script we fake it
var editIndexid = ""; 
var thisForm;
var rand = "";
function setRand(thisrand)  {
	rand = thisrand;
}

var checkedItems = new Array();
function getCheckedValues(a,v)   {
   if (a.checked == true)  {
      checkedItems.push(v);
   } else {
     for(i=0;i<checkedItems.length;i++){
       if(v == checkedItems[i]) checkedItems.splice(i, 1);
     } 
  }
}

function align(thisevent,divID) {
	divID.style.visibility= 'visible';
	divID.style.top = thisevent.clientY-180;
	divID.style.left= thisevent.clientX;
}
</script>


</head>
<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Enumeration parameterNames = request.getParameterNames();
   Operations operations=new Operations();
%>

<title><h:outputText value="#{msgs.application_heading}"/></title>  
<body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
  <table>
   <tr>
    <td>
    <div id="mainContent">
        <div id="advancedSearch" class="duplicaterecords" style="visibility:visible;display:block">
     <div id="searchType">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" title="#{msgs.search_Type}" rendered="#{PatientDetailsHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{PatientDetailsHandler.searchType}" valueChangeListener="#{PatientDetailsHandler.changeSearchType}" >
                                        <f:selectItems  value="#{PatientDetailsHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
       <tr>
         <td><div style="padding-top:100px;color:red;" id="messages"></div></td>
       </tr>
                </h:form>
            </table>
   </div>            <h:form id="advancedformData" >
                <input type="hidden" id="selectedSearchType" title='selectedSearchType' 
				value='<h:outputText value="#{PatientDetailsHandler.selectedSearchType}"/>' />

             <table border="0" cellpadding="0" cellspacing="0">
			   <tr>
			     <td align="left"><p><nobr><h:outputText  value="#{PatientDetailsHandler.instructionLine}"/></nobr></p>
				 </td>
				 

			   </tr>
                    <tr>
                        <td colspan="2">
                            <input id='lidmask' type='hidden' title='lidmask' name='lidmask' value='' />
                            <h:dataTable cellpadding="0" cellspacing="0"
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{PatientDetailsHandler.searchScreenFieldGroupArray}">
						    <h:column>
   				            <p>
							     <h:outputText value="#{searchScreenFieldGroup.description}" />
							</p>
                            <h:dataTable headerClass="tablehead"  
							             cellpadding="0" cellspacing="0"
                                         id="fieldConfigId" 										 
                                         var="feildConfig" 
                                         value="#{searchScreenFieldGroup.fieldConfigs}">

                                <!--Rendering Non Updateable HTML Text Area-->
                               <!--Rendering Non Updateable HTML Text Area-->
                                        <h:column>
                                            <nobr>											
                                                <h:outputText rendered="#{feildConfig.oneOfTheseRequired}" >
												     <span style="font-size:9px;color:blue;verticle-align:top">&dagger;&nbsp;</span>
 												</h:outputText>
                                                <h:outputText rendered="#{feildConfig.required}">
												     <span style="font-size:9px;color:red;verticle-align:top">*&nbsp;</span>
 												</h:outputText>
                                                <h:outputText value="#{feildConfig.displayName}" />
                                            </nobr>
                                        </h:column>
                                        <!--Rendering HTML Select Menu List-->
                                        <h:column rendered="#{feildConfig.guiType eq 'MenuList'}" >
                                            <nobr>
                                                <h:selectOneMenu title='#{feildConfig.name}'
                                                                 rendered="#{feildConfig.name ne 'SystemCode'}"
	                                                             value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
												                  title='#{feildConfig.name}'  
                                                                  id="SystemCode" 
    															  value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                                  rendered="#{feildConfig.name eq 'SystemCode'}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                            </nobr>
                                        </h:column>
                                        <!--Rendering Updateable HTML Text boxes-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType ne 6 }" >
                                            <nobr>
                                                <h:inputText   required="#{feildConfig.required}" 
												               title='#{feildConfig.displayName}'
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.range && feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>

												<h:inputText   required="#{feildConfig.required}" 
												               title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{!feildConfig.range && feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
															   readonly="true"
															   onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
															   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{!feildConfig.range && feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
															   title='#{feildConfig.name}'
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{!feildConfig.range && feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea label="#{feildConfig.displayName}"  title='#{feildConfig.name}'
												id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6  && !feildConfig.range}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
												   title="<h:outputText value="#{feildConfig.name}"/>"  
                                                   value="<h:outputText value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                   required="<h:outputText value="#{feildConfig.required}"/>" 
                                                   maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                   onblur="javascript:validate_date(this,'<%=dateFormat%>')">
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
										<h:column rendered="#{ feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6  &&  feildConfig.range}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.displayName}"/>"  
												   title="<h:outputText value="#{feildConfig.displayName}"/>"  
                                                   value="<h:outputText value="#{PatientDetailsHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                   required="<h:outputText value="#{feildConfig.required}"/>" 
                                                   maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                   onblur="javascript:validate_date(this,'<%=dateFormat%>')">
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
                             
                            </h:dataTable> <!--Field config loop-->
						    </h:column>
                            </h:dataTable> <!--Field groups loop-->
                            <table  cellpadding="0" cellspacing="0" style="	border:0px red solid;padding-left:20px">
                                <tr>
                                    <td align="left">
                                        <nobr>
										<% if(operations.isEO_SearchViewSBR()){%>	
                                           <a  title="<h:outputText value="#{msgs.search_button_label}"/>" class="button" href="javascript:void(0)" onclick="javascript:getRecordDetailsFormValues('advancedformData');checkedItems = new Array();setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
                                               <span>
                                                 <h:outputText value="#{msgs.search_button_label}"/>
                                               </span>
                                           </a>
										 <%}%>
                                        </nobr>
									    <nobr>
										    <h:outputLink  title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                            </h:outputLink>
                                        </nobr>                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </h:form>
			<h:panelGrid>
               <h:panelGroup rendered="#{PatientDetailsHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			   <h:panelGroup rendered="#{PatientDetailsHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			</h:panelGrid>
			<form id="collectEuidsForm">
			      <input type="hidden" id="collectEuids" title='collectEuids' />   
            </form>
<div class="reportresults" id="outputdiv"></div>     		
        </div>      
</div>
</td>
</tr>
<tr>
  <td>
  </td>
</tr>
</table>

</body>
<div class="alert" id="ajaxOutputdiv"></div> 

        <%
		  
		  HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

          PatientDetailsHandler  patientDetailsHandler = new PatientDetailsHandler();
          String[][] lidMaskingArray = patientDetailsHandler.getAllSystemCodes();
          
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
            var lidField =  getDateFieldName(formNameValue.name,'LID');
			if(lidField != null) {
             document.getElementById(lidField).value = "";
             document.getElementById(lidField).readOnly = false;
			}
			if(field.selectedIndex == 0 ) {
             document.getElementById(lidField).value = "";
			 document.getElementById(lidField).readOnly = true;
		    }
            
            formNameValue.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }   
         var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
         document.getElementById("selectedSearchType").value = selectedSearchValue;
                  

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
<!-- Added by Narayan Bhat on 22-aug-2008 to incorparte with the functionality of back button in euiddetails.jsp  -->
    <%if(request.getParameter("back")!=null){%>
    <script>
		 <% 
		  String qryString  = request.getQueryString();
	      qryString  = qryString.replaceAll("collectEuids=true","");
		%>
         var queryStr = '<%=qryString%>';
         setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/recorddetailsservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');
   
   <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);
		//replace the wild character
        attributeValue  = attributeValue.replaceAll("~~","%");
   %>
    populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
   <%}%>
   </script>
   <%
	patientDetailsHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));   
	patientDetailsHandler.setSearchType(request.getParameter("selectedSearchType")); 
	
	facesSession.setAttribute("PatientDetailsHandler",patientDetailsHandler);

   %>
   <%}%>
      
    <script type="text/javascript">
     function closeDiv()    {                            
        document.getElementById('ajaxOutputdiv').style.visibility='hidden';
        document.getElementById('ajaxOutputdiv').style.display='none';
     }
</script>
     
</html>
</f:view>








