<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://yui4jsf.sourceforge.net" prefix="yui"%>
<%@ page import="com.sun.mdm.index.edm.presentation.security.Operations"%>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchResultsConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.SearchScreenConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfigGroup"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DuplicateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.DeactivatedReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.MergeRecordHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UnmergedRecordsHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.UpdateReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AssumeMatchReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.ReportHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.AuditLogHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
 
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.application.FacesMessage" %>

<%
//Author Sridhar Narsingh
//sridhar@ligaturesoftware.com
//http://www.ligaturesoftware.com
%>
<script>
var editIndexid = "";
var thisForm;
var rand = "";
function setRand(thisrand)  {
	rand = thisrand;
}
</script>
<% 
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   int pageSize = 10;
   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();


%>
<%
//set locale value
if(session!=null ){
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
}
%>
<f:view>
    <f:loadBundle basename="#{NavigationHandler.MIDM_PROP_JSP}" var="msgs" />
    
    <html>
       <head>
<title><h:outputText value="#{msgs.application_heading}"/></title> 
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="./css/yui/yahoo/yahoo-min.js"></script>
            <!-- Additional source files go here -->
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" >
            <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css">
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
            <!--there is no custom header content for this example-->
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
         </head>
<%
  // ResourceBundle bundle = //ResourceBundle.getBundle("com.sun.mdm.index.edm.presentation.messages.midm",FacesContext.getCurrentInstance().getV//iewRoot().getLocale());
  /* String mergeText = bundle.getString("Merged_Transaction_Report_Label");
   String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
   String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
   String updateText = bundle.getString("Updated_Record_Report_Label");
   String activityText = bundle.getString("Activity_Report_Label");
   String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
   String assumedText = bundle.getString("Assumed_Matches_Report_Label"); */
%>
        
        <body class="yui-skin-sam">
             <% Operations operations=new Operations();%>
             <%@include file="./templates/header.jsp"%>
            <div id="mainContent" style="overflow:hidden;">                 
            <div id="reports">
                        <!--td><%=(String)request.getAttribute("tabName")%></td-->
                              <%

                               String mergeText = bundle.getString("Merged_Transaction_Report_Label");
                               String deactiveText = bundle.getString("Deactivated_Record_Report_Label");
                               String unmergeText = bundle.getString("Unmerged_Transaction_Report_Label");
                               String updateText = bundle.getString("Updated_Record_Report_Label");
                               String activityText = bundle.getString("Activity_Report_Label");
                               String potDupText = bundle.getString("Potential_Duplicate_Report_Label");
                               String assumedText = bundle.getString("Assumed_Matches_Report_Label");
                                ScreenObject subScreenObj = null;

								//get the Sorted Screen objects
								 ScreenObject screenObjectObj = (ScreenObject) session.getAttribute("ScreenObject");
   								ReportHandler reportHandler = new ReportHandler();
                                ScreenObject[] orderdedScreens  = reportHandler.getOrderedScreenObjects();
                                Iterator messagesIter = FacesContext.getCurrentInstance().getMessages();
  								%>
                      <%
					  if(orderdedScreens != null ) {
                                String reportTabName = (request.getAttribute("reportTabName") != null)?(String) request.getAttribute("reportTabName"):orderdedScreens[0].getDisplayTitle();
								String tabName = "";
                                String clasName = "";
						   %>
                <table border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                        <td>
                            <div id="demo" class="yui-navset">                               
                                <ul class="yui-nav">
                                 <%   for(int i=0;i<orderdedScreens.length;i++){  
                                        subScreenObj = orderdedScreens[i];
                                        tabName =  subScreenObj.getDisplayTitle(); 
										//tabName = tabName.replaceAll("Report","");
                                        if (reportTabName.equalsIgnoreCase(tabName)) { %>
                                            <li class="selected" >
										        <a title="<%=tabName%>" href="#tab<%=i+1%>"> <em><%=tabName%></em></a>
										    </li>             
                                        <%} else {%>
                                            <li>
										        <a title="<%=tabName%>" href="#tab<%=i+1%>"> <em><%=tabName%></em></a>
										    </li>             
                                        <%}	%>
                                 <%

											}%>
                                </ul>            
                                
                           <div class="yui-content">

                                 <%   for(int i=0;i<orderdedScreens.length;i++){  
                                           ArrayList screenConfigList = orderdedScreens[i].getSearchScreensConfig();
									       SearchScreenConfig objSearchScreenConfig = (SearchScreenConfig) screenConfigList.get(0);
									       ArrayList  basicSearchFieldConfigs = objSearchScreenConfig.getFieldConfigs();
                                           ValueExpression basicSearchFGValueExpression = ExpressionFactory.newInstance().createValueExpression(basicSearchFieldConfigs, basicSearchFieldConfigs.getClass());
										   boolean isActivityBoolean = (activityText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) ? true :false; 
										   Boolean isActivityReport =  new Boolean(isActivityBoolean);
                                           ValueExpression isActivityReportValueExpression = ExpressionFactory.newInstance().createValueExpression(isActivityReport, isActivityReport.getClass());
                                           Integer integerValue = new Integer(i+1);
										   ValueExpression loopIndexVE = ExpressionFactory.newInstance().createValueExpression(integerValue, integerValue.getClass());
									 %>
									     <div id="tab<%=i+1%>">
										   <form id="form<%=i%>">
										             <input id='lidmask' type='hidden' name='lidmask' value='' />
											         <div class="reportYUISearch1" >
													 <h:panelGrid rendered="<%=isActivityReportValueExpression%>">
													   <h:panelGroup>
													    <h:outputText value="Activity Type"/> &nbsp;
														<h:selectOneMenu title="activityType" styleClass="selectContent" id="viewreports" value="#{ReportHandler.frequency}" >
                                                           <f:selectItems value="#{ReportHandler.activityReportTypes}"/>
                                                        </h:selectOneMenu>      
													   </h:panelGroup>
													   </h:panelGrid>
                                                          <h:dataTable headerClass="tablehead"
                                                            id="searchScreenFieldGroupArrayId" 
                                                            var="searchScreenFieldGroup" 
                                                            value="<%=basicSearchFGValueExpression%>">
                                                       <h:column>
                                				            <div style="font-size:12px;font-weight:bold;color:#0739B6;" >
															<h:outputText value="#{searchScreenFieldGroup.description}" /></div>
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
																						value="#{ReportHandler.reportParameters[feildConfig.name]}" 
																						title="#{feildConfig.name}" 
																						rendered="#{feildConfig.name ne 'SystemCode'}" >
																							<f:selectItem itemLabel="" itemValue="" />
																							<f:selectItems  value="#{feildConfig.selectOptions}" />
																						</h:selectOneMenu>
																						
																						<h:selectOneMenu  
																						value="#{ReportHandler.reportParameters[feildConfig.name]}" 																					title="#{feildConfig.name}" 
                                                                                        onchange="javascript:setLidMaskValue(this)"  id="SystemCode" 
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
																					   value="#{ReportHandler.reportParameters[feildConfig.name]}" 

																		               title="#{feildConfig.name}"
																					   label="#{feildConfig.displayName}" 
																					   onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
																					   onkeyup="javascript:qws_field_on_key_up(this)"
                                                                                       maxlength="#{feildConfig.maxLength}" 
																					   rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                      <h:inputHidden id="SystemCodeHidden" value="#{ReportHandler.reportParameters['SystemCodeHidden']}"/>	
										<h:inputHidden id="LIDHidden" value="#{ReportHandler.reportParameters['LIDHidden']}"/>			
																						<h:inputText   required="#{feildConfig.required}" 
																									   id="LID"
																									   title="#{feildConfig.name}"
																									   value="#{ReportHandler.reportParameters[feildConfig.name]}" 
																									   label="#{feildConfig.displayName}" 
																									   onkeydown="javascript:qws_field_on_key_down(this, document.lidform.lidmask.value)"
																									   onkeyup="javascript:qws_field_on_key_up(this)"
																									   rendered="#{feildConfig.name eq 'LID'}"/>
																									   
																						<h:inputText   required="#{feildConfig.required}" 
 																						               title="#{feildConfig.name}"
																									   value="#{ReportHandler.reportParameters[feildConfig.name]}" 
																									   label="#{feildConfig.displayName}" 
																									   maxlength="#{SourceHandler.euidLength}" 
																									   rendered="#{feildConfig.name eq 'EUID'}"/>
																										   
																					</nobr>
																				</h:column>
																				
																				<!--Rendering Updateable HTML Text Area-->
																				<h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
																					<nobr>
																						<h:inputTextarea 
																						value="#{ReportHandler.reportParameters[feildConfig.name]}" 
																						label="#{feildConfig.displayName}"  
																						title="#{feildConfig.name}"
																						id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
																					</nobr>
																				</h:column>
																				
																				<h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
																				  <nobr>
	                                          <input type="text"
id = "form<%=i%><h:outputText value="#{feildConfig.name}"/>"
title = "<h:outputText value="#{feildConfig.name}"/>"
value="<h:outputText value="#{ReportHandler.reportParameters[feildConfig.name]}"/>"
required="<h:outputText value="#{feildConfig.required}"/>"
maxlength="<h:outputText value="#{feildConfig.maxLength}"/>"
onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{feildConfig.inputMask}"/>')"
onkeyup="javascript:qws_field_on_key_up(this)"
onblur="javascript:validate_date(this,'<%=dateFormat%>')">
<a href="javascript:void(0);"
title="<h:outputText value="#{feildConfig.displayName}"/>"
onclick="g_Calendar.show(event,
'form<%=i%><h:outputText value="#{feildConfig.name}"/>',
'<%=dateFormat%>',
'<%=global_daysOfWeek%>',
'<%=global_months%>',
'<%=cal_prev_text%>',
'<%=cal_next_text%>',
'<%=cal_today_text%>',
'<%=cal_month_text%>',
'<%=cal_year_text%>')"
><img border="0" title="<h:outputText value="#{feildConfig.displayName}"/> (<%=dateFormat%>)" src="./images/cal.gif"/></a>
<font class="dateFormat">(<%=dateFormat%>)</font>

																				  </nobr>
																				</h:column>
																	</h:dataTable> <!--Field config loop-->
															</h:column>
                                                       </h:dataTable> 
<!--End Field groups loop-->
												  <input type="hidden" title="tabName" name="reportName" value="<%=orderdedScreens[i].getDisplayTitle()%>"/>
<!-- Action Buttons -->   
													<table  cellpadding="0" cellspacing="0">
													 <tr>
													   <td>
													  <nobr>
													  <% if (operations.isReports_Duplicates() && potDupText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>   <!--  Potential  Duplicate Report -->										 
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"  href="javascript:void(0)" id = "deactivateReport" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
												
													 
													 <% } else if (operations.isReports_DeactivatedEUIDs() && deactiveText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Deactivated Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_MergedRecords() && mergeText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>  <!--  Merged Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "mergeText" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>

													 <% } else if (operations.isReports_UnmergedRecords() && unmergeText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  UnMerge Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_Updates() && updateText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Update Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_Activity() && activityText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %> <!--  Activity Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } else if (operations.isReports_AssumedMatches() && assumedText.equalsIgnoreCase(orderdedScreens[i].getDisplayTitle())) { %>  <!--  Assumed Match Report -->
														   <a class="button" title="<h:outputText value="#{msgs.search_button_label}"/>" id = "deactivateReport" href="javascript:void(0)" onclick="javascript:getFormValues('form<%=i%>');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/reportservices.jsf?form=form<%=i%>&random=rand'+'&'+queryStr,'results<%=i%>','<%=orderdedScreens[i].getDisplayTitle()%>')">
															 <span><h:outputText value="#{msgs.search_button_label}"/></span>
														   </a>
													 <% } %>
													 </nobr>
												    </td>
												    <td>
													   <nobr>
														  <a class="button"  title="<h:outputText value="#{msgs.clear_button_label}"/>" href="javascript:void(0)" onclick="javascript:ClearContents('form<%=i%>')" >
															<span><h:outputText value="#{msgs.clear_button_label}"/> </span>
														  </a>
													   </nobr>
													 </td>
													</tr>
													<tr>
													  <td>&nbsp;
													  </td>
													</tr>
													<tr>
													   <td colspan="2" align="center"><div class="ajaxalert" id="messages"> </div></td>									
													</tr>

				  <% if (reportHandler.isOneOfGroupExists(orderdedScreens[i].getDisplayTitle()) ) {%>
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;" colspan="2">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
					<%}%>

					<% if (reportHandler.isRequiredExists(orderdedScreens[i].getDisplayTitle()) ) {%>			
					<tr>
						<td style="font-size:10px;" colspan="2">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
					<%}%>

												  </table>
			    <table>
				</table>

												  <input type="hidden" value="<%=i%>" name="layer" title="layer" />
<!-- End Action Buttons -->
										  </form>
<!-- Results Div -->
										</div>  
                                          <div class="reportresults" id="results<%=i%>"></div>
                                     </div>                                       
                                 <%}%>
<%} else {%> 

    <div class="ajaxalert">
    <table cellpadding="0" cellspacing="0" border="0">	   
        <% int i=0;
           while (messagesIter.hasNext()) {
              FacesMessage facesMessage = (FacesMessage) messagesIter.next();
		%>
		    <% if (i == 0) {%>
			<tr> 
			  <td>		         
                   <%=bundle.getString("CONFIG_ERROR")%>&nbsp;:&nbsp;<%=facesMessage.getSummary()%>
  	          </td>
			</tr> 
			<tr>
			  <td>&nbsp;</td>
			</tr>
		   	 <tr> 
			  <td>
			<% } else { %>
			     <ul><li><%=facesMessage.getSummary()%></li></ul>
			<%} %>
			<%i++;%>
         <%}%>     	 
			   </td>
			 </tr> 
	 </table>
	 </div>
<%}%>

                            </div> <!-- End YUI content -->
							    
                            </div> <!-- demo end -->
                        </td>
                    </tr>
                </table>
            </div>        
            <form id="lidform" name="lidform">
				 <input id='lidmask' type='hidden' name='lidmask' value='' />
			 </form>
            
            <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
            
            <script>
                (function() {
                var tabView = new YAHOO.widget.TabView('demo');                
                YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
                })();
            </script>
            
            <!--END SOURCE CODE FOR EXAMPLE =============================== -->
            </div>        <!-- End mainContent -->                
<!--BEGIN YUI Table =============================== -->
            
<script>
   (function() {
       var tabView = new YAHOO.widget.TabView('demo');                
       YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
     })();
</script>



        <%
		  SourceAddHandler sourceAddHandler = new SourceAddHandler();
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
         function setLidMaskValue(field) {
			var tokens = new Array();
			thisformName = field.name.split(':');
            formName = thisformName[0]; 
			thisForm = document.getElementById(formName); //update global formName for onblur
            var  selectedValue = field.options[field.selectedIndex].value;
            var formNameValue = document.forms[formName];
            document.lidform.lidmask.value  = getLidMask(selectedValue,systemCodes,lidMasks);
         }  
         
    </script>


        </body>
    </html>
</f:view>
