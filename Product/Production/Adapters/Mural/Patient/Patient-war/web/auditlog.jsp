<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditDataObject"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.audit.AuditSearchObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AuditLogHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.ArrayList"  %>

<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Operations operations=new Operations();
%>

<f:view>
    
    
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
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
</script>

        </head>
        
        <title><h:outputText value="#{msgs.application_heading}"/></title>  
        <%@include file="./templates/header.jsp"%>
    <body class="yui-skin-sam">
	  <table width="100%">
	    <tr>
		  <td>
             <div id="mainContent" style="width:100%">     
              <div id ="auditlog " class="basicSearch">
                        <table border="0" cellpadding="0" cellspacing="0">
                      <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText rendered="#{AuditLogHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu  id="searchType" title="#{msgs.search_Type}"
                                                      rendered="#{AuditLogHandler.possilbeSearchTypesCount gt 1}" 
                                                      onchange="submit();" 
													   style="width:220px;" 
                                                      value="#{AuditLogHandler.searchType}" 
                                                      valueChangeListener="#{AuditLogHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AuditLogHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                        </table>
                    </h:form>
                    <h:form id="advancedformData">
                        <h:inputHidden id="selectedSearchType" value="#{AuditLogHandler.selectedSearchType}"/>
                        <table border="0" cellpadding="0" cellspacing="0" >
		       	           <tr>
			    			     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{AuditLogHandler.instructionLine}" /></td>
				           </tr>
						   <tr>
                                <td>
                                    <input id='lidmask' title='lidmask' type='hidden' name='lidmask' value='DDD-DDD-DDDD' />
                            <h:dataTable headerClass="tablehead"  
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{AuditLogHandler.searchScreenFieldGroupArray}">
						    <h:column>                             
   				            <div style="font-size:12px;font-weight:bold;color:#0739B6;" ><h:outputText value="#{searchScreenFieldGroup.description}" /></div>
                            <h:dataTable headerClass="tablehead"  
                                         id="fieldConfigId" 
                                         var="feildConfig" 
                                          value="#{searchScreenFieldGroup.fieldConfigs}">
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
                                                <h:selectOneMenu 
												title='#{feildConfig.name}'
                                                                 rendered="#{feildConfig.name ne 'SystemCode'}"
	                                                             value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
                                                                  title='#{feildConfig.name}'
                                                                  id="SystemCode" 
    															  value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}"
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
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               title='#{feildConfig.name}'
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   title='#{feildConfig.name}'
															   readonly="true"
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value);javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
															   value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               title='#{feildConfig.name}'
															   label="#{feildConfig.displayName}" 
                                                               onblur="javascript:accumilateFieldsOnBlur(this,'#{feildConfig.name}')"
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea title='#{feildConfig.name}' label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" 
											       title="<h:outputText value="#{feildConfig.name}"/>"  
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
                                                   value="<h:outputText value="#{AuditLogHandler.updateableFeildsMap[feildConfig.name]}"/>"
                                                   required="<h:outputText value="#{feildConfig.required}"/>" 
                                                   maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                   onblur="javascript:validate_date(this,'<%=dateFormat%>');javascript:accumilateFieldsOnBlur(this,'<h:outputText value="#{feildConfig.name}"/>')">
                                                 
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
                                        
                                    </h:dataTable>
                                    </h:column>
                                        
                                    </h:dataTable>
                                   <table>
                                        <tr>
                                            <td>
                                                <nobr>
												<% if(operations.isAuditLog_SearchView()){%>	
                                                        <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" href="javascript:void(0)" onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/auditlogservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
                                                            <span>
                                                                <h:outputText value="#{msgs.search_button_label}"/>
                                                            </span>
                                                        </a>                                     
												<% } %>
                                                </nobr>
                                                <nobr>
                                                    <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                                        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                                    </h:outputLink>
                                                </nobr>                                                
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                                <td valign="top">
								   <div id="messages" class="ajaxalert">                                   
								   </div>
                                </td>
                            </tr>
                        </table>
                        <h:inputHidden id="enteredFieldValues" value="#{AuditLogHandler.enteredFieldValues}"/>
                    </h:form>
					<!--div class="reportresults" id="outputdiv"></div-->
			<h:panelGrid>
               <h:panelGroup rendered="#{AuditLogHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			   <h:panelGroup rendered="#{AuditLogHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			</h:panelGrid>
<div class="reportresults" style="visibility:hidden"" id="outputdiv"></div>

                </div> 
            </div>  
		  </td>
		  </tr>
		  </table>
   </body>     
        <%
           AuditLogHandler auditLogHandler = new AuditLogHandler();
           String[][] lidMaskingArray = auditLogHandler.getAllSystemCodes();
          
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
         //var selectedSearchValue = document.getElementById("searchTypeForm:searchType").options[document.getElementById("searchTypeForm:searchType").selectedIndex].value;
         //document.getElementById("advancedformData:selectedSearchType").value = selectedSearchValue;
    </script>
    </html>
</f:view>










