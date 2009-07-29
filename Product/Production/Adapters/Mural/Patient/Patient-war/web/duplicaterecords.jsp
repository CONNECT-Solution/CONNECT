<%@ page contentType="text/html"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSearchObject"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.managers.CompareDuplicateManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SearchDuplicatesHandler"  %>
<%@ page import="com.sun.mdm.index.edm.services.masterController.MasterControllerService"  %>
<%@ page import="com.sun.mdm.index.edm.control.QwsController"  %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>


<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.math.BigDecimal"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.sql.Timestamp"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="java.util.Enumeration"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>
<%@ page import="java.util.ResourceBundle"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager" %>

<f:view>
    
   
    <html>
        <head>
            <title><h:outputText value="#{msgs.application_heading}"/></title>  
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
<script>
//not used in transactions, but since its require in the script we fake it
var editIndexid = ""; 
var thisForm;
var rand = "";

// Fields used for merge 
var rowCountMerge  ="";
var destinationEOFinalMerge ="";
var previewhiddenMergeEuidsFinalMerge ="";

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

		   //  merge related global javascript variables
		   var euids="";
		   var fromPage="";
		   var duplicateEuids = "";
           var euidArray = [];
           var alleuidsArray = [];
		   var euidValueArraySrc=[];
		   var euidValueArrayHis=[];
		   var previewEuidDivs=[];
</script>

		</head>
        <body>
            <%@include file="./templates/header.jsp"%>

<% 
   FacesContext facesContext = FacesContext.getCurrentInstance(); 
   HttpSession facesSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
   String from = (String)facesContext.getExternalContext().getRequestParameterMap().get("where");
   Integer size = 0; 
   double rand = java.lang.Math.random();
   String URI = request.getRequestURI();
   URI = URI.substring(1, URI.lastIndexOf("/"));

   ArrayList keysList  = new ArrayList();
   ArrayList labelsList  = new ArrayList();
   ArrayList fullFieldNamesList  = new ArrayList();
   StringBuffer myColumnDefs = new StringBuffer();
   Enumeration parameterNames = request.getParameterNames();
%>

<div id="mainContent" style="overflow:hidden">   
<table><tr><td>
<div id="advancedSearch" class="duplicaterecords" >
      <div id="searchType" style="background-color:blue;">
             <table border="0" cellpadding="0" cellspacing="0" align="right">
                <h:form id="searchTypeForm" >
                            <tr>
                                <td>
                                    <h:outputText  rendered="#{SearchDuplicatesHandler.possilbeSearchTypesCount gt 1}"  value="#{msgs.patdet_search_text}"/>&nbsp;
                                    <h:selectOneMenu id="searchType" title="#{msgs.search_Type}" rendered="#{SearchDuplicatesHandler.possilbeSearchTypesCount gt 1}" onchange="submit();" style="width:220px;" value="#{SearchDuplicatesHandler.searchType}" valueChangeListener="#{SearchDuplicatesHandler.changeSearchType}" >
                                        <f:selectItems  value="#{SearchDuplicatesHandler.possilbeSearchTypes}" />
                                    </h:selectOneMenu>
                                </td>
                            </tr>
                             <tr>
                           <td><div style="padding-top:100px;color:red;" id="messages"></div></td>
                     </tr>
                </h:form>
              </table>
      </div>            
     
  
             <table border="0" cellpadding="0" cellspacing="0">
		<tr>
		 <td align="left"><p><nobr><h:outputText  value="#{SearchDuplicatesHandler.instructionLine}"/></nobr></p></td>
		</tr>
               <tr>
                <td colspan="2">
              	 <h:form id="advancedformData" >
                <input type="hidden" id="selectedSearchType" title='selectedSearchType' 
				value='<h:outputText value="#{SearchDuplicatesHandler.selectedSearchType}"/>' />
                            <input id='lidmask' title="lidmask" type='hidden' name='lidmask' value='' />
                            <h:dataTable cellpadding="0" cellspacing="0"
                                         id="searchScreenFieldGroupArrayId" 
                                         var="searchScreenFieldGroup" 
                                         value="#{SearchDuplicatesHandler.searchScreenFieldGroupArray}">
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
	                                                             value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{feildConfig.selectOptions}" />
                                                </h:selectOneMenu>
                                                
                                                <h:selectOneMenu  onchange="javascript:setLidMaskValue(this,'advancedformData')"
												                  title='#{feildConfig.name}'  
                                                                  id="SystemCode" 
    															  value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
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
												               title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               maxlength="#{feildConfig.maxLength}" 
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name ne 'LID' && feildConfig.name ne 'EUID'}"/>
                                                
                                                <h:inputText   required="#{feildConfig.required}" 
												               id="LID"
															   title='#{feildConfig.name}'
                                                               label="#{feildConfig.displayName}" 
															   readonly="true"
															   onkeydown="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               onblur="javascript:qws_field_on_key_down(this, document.advancedformData.lidmask.value)"
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'LID'}"/>
                                                               
                                                <h:inputText   required="#{feildConfig.required}" 
                                                               label="#{feildConfig.displayName}" 
															   title='#{feildConfig.name}'
                                                               onkeydown="javascript:qws_field_on_key_down(this, '#{feildConfig.inputMask}')"
                                                               onkeyup="javascript:qws_field_on_key_up(this)"
                                                               maxlength="#{SourceHandler.euidLength}" 
															   value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"
                                                               rendered="#{feildConfig.name eq 'EUID'}"/>
                                                                   
                                                               
                                            </nobr>
                                        </h:column>
                                        
                                        <!--Rendering Updateable HTML Text Area-->
                                        <h:column rendered="#{feildConfig.guiType eq 'TextArea'}" >
                                            <nobr>
                                                <h:inputTextarea label="#{feildConfig.displayName}"  title='#{feildConfig.name}'
												id="fieldConfigIdTextArea"   required="#{feildConfig.required}"/>
                                            </nobr>
                                        </h:column>
                                        
                                        <h:column rendered="#{feildConfig.guiType eq 'TextBox' && feildConfig.valueType eq 6}" >
                                          <nobr>
                                            <input type="text" 
                                                   id = "<h:outputText value="#{feildConfig.name}"/>"  
												   title="<h:outputText value="#{feildConfig.name}"/>"  
                                                   value="<h:outputText value="#{SearchDuplicatesHandler.updateableFeildsMap[feildConfig.name]}"/>"
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
                            <table  cellpadding="0" cellspacing="0" style="	border:0px none solid;padding-left:20px">
                                <tr>
                                    <td align="left">
                                        <nobr>
                                           <a  class="button" title="<h:outputText value="#{msgs.search_button_label}"/>"
										       href="javascript:void(0)"
                                               onclick="javascript:getFormValues('advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','')">  
                                               <span>
                                                 <h:outputText value="#{msgs.search_button_label}"/>
                                               </span>
                                           </a>
                                        </nobr>
                                        <nobr>
                                          <h:outputLink  title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('advancedformData')">
                                           <span><h:outputText value="#{msgs.clear_button_label}"/></span></h:outputLink>
                                      </nobr>
                                    </td>
				              </tr>
                            </table>
                         </h:form>
                        </td>
                        <td><div id="messages"></div></td>
                    </tr>
                </table>

			<h:panelGrid>
               <h:panelGroup rendered="#{SearchDuplicatesHandler.oneOfGroupExists}">
					<tr> <!-- inline style required to override the class defined in CSS -->
						<td style="font-size:10px;">
						   <hr/>
							<nobr>
								 <span style="font-size:9px;color:blue;verticle-align:top;">&dagger;&nbsp;</span><h:outputText value="#{msgs.GROUP_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			   <h:panelGroup rendered="#{SearchDuplicatesHandler.requiredExists}">
					<tr>
						<td style="font-size:10px;">
							<nobr>
								 <span style="font-size:9px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
							</nobr>
						</td>
				    </tr>
 			   </h:panelGroup>

			</h:panelGrid>

    </div>  
    </td>
    </tr>
   <tr><td><div  id="outputdiv"></div></td></tr>
</table>

</div>  

  <!-- Resolve popup div starts here  -->
    <div id="resolvePopupDiv" class="alert" style="TOP:2250px; LEFT:300px; HEIGHT:195px;WIDTH: 300px;visibility:hidden; ">
       <h:form id="reportYUISearch">
         <input type="hidden" id="potentialDuplicateId" name="potentialDuplicateId" title="potentialDuplicateId" />
         <table width="100%" border="0">
           <tr><th align="left"><h:outputText value="#{msgs.pop_up_confirmation_heading}"/></th><th align="right"><a href="javascript:void(0)" rel="resolvepopuphelp"><h:outputText value="#{msgs.help_link_text}"/> </a></th></tr>
           <tr><td colspan="2"> &nbsp;</td></tr>
           <tr><td colspan="2" align="left"><b><h:outputText value="#{msgs.different_person_dailog_text}"/></b></td></tr>
           <tr>
               <td  colspan="2" align="left">
                 <div class="selectContent">
                  <h:selectOneMenu id="diffperson" title="resolveType">
                      <f:selectItem  itemValue="AutoResolve" itemLabel="Resolve Until Recalculation"/>
                      <f:selectItem  itemValue="Resolve" itemLabel="Resolve Permanently"/>
                 </h:selectOneMenu> 
                 </div> 
                </td>
          </tr>
          <tr><td colspan="2"> &nbsp;</td></tr>
          <tr>
             <td align="right"  colspan="2">
               <a  title="<h:outputText value="#{msgs.ok_text_button}" />" class="button" href="javascript:void(0)" onclick="javascript:getDuplicateFormValues('reportYUISearch','advancedformData');setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?resolveDuplicate=true&random='+rand+'&'+queryStr,'outputdiv','');document.getElementById('resolvePopupDiv').style.visibility = 'hidden';document.getElementById('resolvePopupDiv').style.display = 'none';">  
                           <span><h:outputText value="#{msgs.ok_text_button}" /></span>
               </a>
                <h:outputLink title="#{msgs.cancel_but_text}" onclick="Javascript:showResolveDivs('resolvePopupDiv',event,'123467')" 
                               styleClass="button"  
                               value="javascript:void(0)">
                      <span><h:outputText value="#{msgs.cancel_but_text}" /></span>
                </h:outputLink>   
             </td>
           </tr>
         </table>
       </h:form>

    </div>                                                
  <!-- Resolve popup div ends here  -->
   
</body>        

        <%
		 SearchDuplicatesHandler searchDuplicatesHandler = new  SearchDuplicatesHandler(); 
         String[][] lidMaskingArray = searchDuplicatesHandler.getAllSystemCodes();
         
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

<%
   if ("dashboard".equalsIgnoreCase(from))   {
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

 	    ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random=<%=rand%>&<%=queryStr%>','outputdiv','')
     </script>
<% }  %>
<!-- added by Narahari.M on 22/08/2008 for incorporate back button -->
<% if(request.getParameter("back") != null )  {%>
<script>
    var queryStr = '<%=request.getQueryString()%>'
	setRand(Math.random());ajaxURL('/<%=URI%>/ajaxservices/searchduplicatesservice.jsf?random='+rand+'&'+queryStr,'outputdiv','');
  <% while(parameterNames.hasMoreElements())   { 
        String attributeName = (String) parameterNames.nextElement();
        String attributeValue = (String) request.getParameter(attributeName);%>
        populateContents('advancedformData','<%=attributeName%>','<%=attributeValue%>');
  <%} %>
	   
</script>
 <%
	searchDuplicatesHandler.setSelectedSearchType(request.getParameter("selectedSearchType"));   
	searchDuplicatesHandler.setSearchType(request.getParameter("selectedSearchType")); 
	
	facesSession.setAttribute("SearchDuplicatesHandler",searchDuplicatesHandler);

   %>
<% } %>   
    </html> 
</f:view>
