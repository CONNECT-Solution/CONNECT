<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.sql.Timestamp"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.Enumeration"%>


<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
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
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
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

    <body class="yui-skin-sam">
    <%@include file="./templates/header.jsp"%>
     <div id="mainContent">   
<%
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));
   FacesContext facesContext = FacesContext.getCurrentInstance(); 
   String from = (String)facesContext.getExternalContext().getRequestParameterMap().get("where");
  AssumeMatchHandler assumeMatchHandler = new AssumeMatchHandler();
  Operations operations=new Operations();
  Enumeration parameterNames = request.getParameterNames();
%>
    <div id ="assumedmatches " class="basicSearch">
            <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" rendered="#{AssumeMatchHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{AssumeMatchHandler.searchType}" valueChangeListener="#{AssumeMatchHandler.changeSearchType}" >
                                        <f:selectItems  value="#{AssumeMatchHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                </h:form>
            </table>
            <h:form id="advancedformData" >
                <h:inputHidden id="selectedSearchType" value="#{AssumeMatchHandler.selectedSearchType}"/>
                <table border="0" cellpadding="0" cellspacing="0" >
		           <tr>
			     <td align="left" style="padding-left:60px;"><h:outputText  style="font-size:12px;font-weight:bold;color:#0739B6;"  value="#{AssumeMatchHandler.instructionLine}" /></td>
			       </tr>

                    <tr>
                        <td>
						   <%
						   int countFc = 0;
						   ArrayList sList  = assumeMatchHandler.getSearchScreenFieldGroupArray();
						   for(int i=0;i<sList.size();i++) {
							    FieldConfigGroup fcg = (FieldConfigGroup)  sList.get(i);
								countFc = fcg.getFieldConfigs().size();
						   }
                           %>
                            
                            <input id='lidmask' type='hidden' title='lidmask' name='lidmask' value='DDD-DDD-DDDD' />
                            <h:dataTable headerClass="tablehead"  
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{AssumeMatchHandler.searchScreenFieldGroupArray}">
						    <h:column>
   				            <div style="font-size:12px;font-weight:bold;color:#0739B6;"><h:outputText value="#{searchScreenFieldGroup.description}" /></div>
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
                                                <h:selectOneMenu title='#{feildConfig.name}'
                                                                 rendered="#{feildConfig.name ne 'SystemCode'}"
	                                                             value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
                                                                  title='#{feildConfig.name}'                                           id="SystemCode" 
    															  value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
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
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   readonly="true"
															   title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
															   title='#{feildConfig.name}'
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea label="#{feildConfig.displayName}"  id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
												   title="<h:outputText value="#{feildConfig.name}"/>"
                                                   value="<h:outputText value="#{AssumeMatchHandler.updateableFeildsMap[feildConfig.name]}"/>"
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
							  <f:facet name="footer">
                                     <h:column>
                                       <div>
                                        <table  cellpadding="0" cellspacing="0">
                                         <tr>
                                          <td>
                                          <nobr>
										  <% if(operations.isAssumedMatch_SearchView()){%>	
										   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"  id = "deactivateReport" href="javascript:void(0)"
										   onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=rand'+'&'+queryStr,'outputdiv','')">
												 <span><h:outputText value="#{msgs.search_button_label}"/></span>
											</a>
										 <%}%>
                                        </nobr>
                                           <nobr>
                                              <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')" >
                                                <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                              </h:outputLink>
                                           </nobr>									
                                      </td>
                                        </tr>
                                      </table>
									   </div>
                                     </h:column>
                               </f:facet>
                            </h:dataTable>
                        </td>
						<td><div id="messages" class="ajaxalert"> </div></td>
                    </tr>
					<%if(countFc == 0) {%>
					<tr>
					  <td> 
					     <h:outputText value="#{msgs.search_config_error}" />
					  </td>
					</tr>
					<%}%>
                </table>
                <h:inputHidden id="enteredFieldValues" value="#{AssumeMatchHandler.enteredFieldValues}"/>
            </h:form>
			<h:panelGrid>
               <h:panelGroup rendered="#{AssumeMatchHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			   <h:panelGroup rendered="#{AssumeMatchHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>
			</h:panelGrid>

<div class="reportresults" id="outputdiv"></div>
    </div>     
  </div>
</body>
        <%
          String[][] lidMaskingArray = assumeMatchHandler.getAllSystemCodes();          
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
     
<%
   if ("DBassumematches".equalsIgnoreCase(from))   {
   Timestamp tsCurrentTime = new Timestamp(new java.util.Date().getTime());
   //currentTime = new java.util.Date();
   String queryStr ="";
   Long currentTime = new java.util.Date().getTime();
   SimpleDateFormat simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String startDateField = simpleDateFormatFields.format(currentTime);
   queryStr = "create_end_date="+startDateField;

   SimpleDateFormat simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String startTimeField = simpletimeFormatFields.format(currentTime);
   queryStr += "&create_end_time="+startTimeField;
   long milliSecsInADay = 86400000L; //24 hours back
   Timestamp ts24HrsBack = new Timestamp(currentTime - milliSecsInADay);
   Date dt24HrsBack = new Date(currentTime - milliSecsInADay);

   simpleDateFormatFields = new SimpleDateFormat(dateFormat);
   String endDateField = simpleDateFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_date="+endDateField;

   simpletimeFormatFields = new SimpleDateFormat("HH:mm:ss");
   String  endTimeField = simpletimeFormatFields.format(ts24HrsBack);
   queryStr += "&create_start_time="+endTimeField;
%>

    <script>
	   var last24hours = "";
	     populateContents('advancedformData','create_start_date','<%=endDateField%>');
	     populateContents('advancedformData','create_start_time','<%=startTimeField%>');
	     populateContents('advancedformData','create_end_date','<%=startDateField%>');
	     populateContents('advancedformData','create_end_time','<%=endTimeField%>');
 	     ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random=<%=rand%>&<%=queryStr%>','outputdiv','')
     </script>
<% }  %>
<!-- Added by Narayan Bhat on 22-aug-2008 to incorparte with the functionality of back button in ameuiddetails.jsp  -->
    <%
    
    if(request.getParameter("back")!=null){%>
    <script>
         var queryStr = '<%=request.getQueryString()%>';
         setRand(Math.random());
		 ajaxURL('/<%=URI%>/ajaxservices/assumematchservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');
   
   <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);
   %>
    populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
   <%}%>
   </script>
   <%}%>
</html>
</f:view>              
