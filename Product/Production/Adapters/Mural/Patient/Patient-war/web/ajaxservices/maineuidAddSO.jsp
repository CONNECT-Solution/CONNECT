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
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="javax.faces.context.FacesContext"  %>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceEditHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceAddHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.NavigationHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.LocaleHandler"  %>
<%@ page import="java.util.ResourceBundle"  %>

<%
String URI_Session = request.getRequestURI();URI_Session = URI_Session.substring(1, URI_Session.lastIndexOf("/"));
//remove the app name 
URI_Session = URI_Session.replaceAll("/ajaxservices","");
boolean isSessionActive = true;
%>

<% if(session!=null && session.isNew()) {
	isSessionActive = false;
%>
 <table>
   <tr>
     <td>
  <script>
   window.location = '/<%=URI_Session%>/login.jsf';
  </script>
     </td>
	 </tr>
	</table>
<%}%>
<%if (isSessionActive)  {%>



<%
            double rand = java.lang.Math.random();
            ScreenObject objScreenObject = (ScreenObject) session.getAttribute("ScreenObject");
            String URI = request.getRequestURI();
           //remove the app name 
            URI = URI.replaceAll("/ajaxservices","");
            URI = URI.substring(1, URI.lastIndexOf("/"));
			ConfigManager.init();

             String localIdDesignation = ConfigManager.getInstance().getConfigurableQwsValue(ConfigManager.LID, "Local ID");

%>
<%
//set locale value
 LocaleHandler localeHandler = new LocaleHandler();
 localeHandler.setChangedLocale((String) session.getAttribute("selectedLocale"));
%>

<!-- Global variables for the calendar-->
<%
 ResourceBundle bundle = ResourceBundle.getBundle(NavigationHandler.MIDM_PROP, FacesContext.getCurrentInstance().getViewRoot().getLocale());
 String global_daysOfWeek  = bundle.getString("global_daysOfWeek");
 String global_months = bundle.getString("global_months");
 String cal_prev_text = bundle.getString("cal_prev_text");
 String cal_next_text = bundle.getString("cal_next_text");
 String cal_today_text = bundle.getString("cal_today_text");
 String cal_month_text = bundle.getString("cal_month_text");
 String cal_year_text = bundle.getString("cal_year_text");
 String  dateFormat = ConfigManager.getDateFormat();
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
            <link type="text/css" href="../css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="../css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="../css/DatePicker.css" rel="stylesheet" media="screen">
            
			<script type="text/javascript" src="/scripts/edm.js"></script>
            <script type="text/javascript" src="/scripts/Validation.js"></script>
            <script type="text/javascript" src="/scripts/calpopup.js"></script>
            <script type="text/javascript" src="/scripts/Control.js"></script>
            <script type="text/javascript" src="/scripts/dateparse.js"></script>
            <script type="text/javascript" src="/scripts/newdateformat1.js"></script>

            <link rel="stylesheet" type="text/css" href="/css/yui/fonts/fonts-min.css" />
            <link rel="stylesheet" type="text/css" href="/css/yui/tabview/assets/skins/sam/tabview.css" />
            <script type="text/javascript" src="/scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="/scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="/scripts/yui/tabview/tabview.js"></script>
            <script type="text/javascript" src="/scripts/yui4jsf/event/event.js"></script>
           <!--there is no custom header content for this example-->
        </head>
<body>
<div>
<table valign="top" border="0" cellpadding="0" cellspacing="0" >
<tr>
<td valign="top">
<form id="RootNodeInnerForm" name="RootNodeInnerForm" method="post" enctype="application/x-www-form-urlencoded">
<table valign="top" border="0" cellpadding="0" cellspacing="0" style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;">
    <input type="hidden" title="lidmask" name="lidmask" value="DDD-DDD-DDDD" />
	 <tr>
 			 <td style="font-size:10px;">
					 <nobr>
 					   <span style="font-size:12px;color:red;verticle-align:top; FONT-WEIGHT: normal; FONT-FAMILY: Arial, Helvetica,sans-serif">*&nbsp;</span><h:outputText value="#{msgs.REQUIRED_FIELDS}"/>
 					  </nobr>
 			 </td>
 	</tr> 

     <tr height="22px" valign="top">
         <td align="left">
		     <h:outputText value="#{msgs.transaction_source}"/>:
         </td>
    	  <td align="left">
             <h:selectOneMenu id="SystemCode" title="SystemCode"
			 style="font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 10px; text-align: left;width:220px"
			 onchange="javascript:setLidMaskValue(this,'RootNodeInnerForm')" >
              <f:selectItem itemLabel="Select Source" itemValue="" />
               <f:selectItems  value="#{EditMainEuidHandler.systemCodes}" />
             </h:selectOneMenu>
         </td>
		 <td>&nbsp;</td>
    </tr>
    <tr>
        <td align="left" nowrap>
		        <%=localIdDesignation%>:
   	    </td>
   	    <td align="left">
                <h:inputText id="LID" 
				             title="LID" 
				             readonly="true"
                             onkeydown="javascript:qws_field_on_key_down(this, document.RootNodeInnerForm.lidmask.value)"
                             onkeyup="javascript:qws_field_on_key_up(this)"/>
         </td>
   	    <td align="left">
	       <a class="button" title="<h:outputText value="#{msgs.validate_button_text}"/>" href="javascript:void(0)" onclick="javascript:getFormValues('RootNodeInnerForm');javascript:ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&validateLID=true','validateMessages','');">
           <span><h:outputText value="#{msgs.validate_button_text}"/></span></a>
         </td>
	</tr>
    <tr>
         <td colspan="3"><div id="validateMessages"></div></td>
    </tr>                                            
<tr>
<td valign="top" colspan="3">
    <!-- Start ADD  Fields-->
        <h:dataTable  headerClass="tablehead"  
                      id="hashIdEdit" 
                      width="100%"
   				      style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                      var="fieldConfigPerAdd" 
                      value="#{SourceHandler.rootNodeFieldConfigs}">
        <h:column>
           <nobr>
		    <h:outputText rendered="#{fieldConfigPerAdd.required}">
                <span style="font-size:12px;color:red;verticle-align:top">*</span>
             </h:outputText>													  
 			 <h:outputText value="#{fieldConfigPerAdd.displayName}" />
             <h:outputText value=":"/>
			</nobr>
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
							onfocus="javascript:clear_masking_on_focus()" onblur="javascript:validate_Integer_fields(this,'#{fieldConfigPerAdd.displayName}','#{fieldConfigPerAdd.valueType}')"
                             onkeydown="javascript:qws_field_on_key_down(this, '#{fieldConfigPerAdd.inputMask}')"
                             maxlength="#{fieldConfigPerAdd.maxLength}"
                             onkeyup="javascript:qws_field_on_key_up(this)" 
                             required="#{fieldConfigPerAdd.required}"/>
            </h:column>                     
            <!--Rendering Updateable HTML Text boxes date fields-->
            <h:column rendered="#{fieldConfigPerAdd.guiType eq 'TextBox' &&  fieldConfigPerAdd.valueType eq 6}">
            
                <nobr>
                    <input type="text" 
                           title="<h:outputText value="#{fieldConfigPerAdd.fullFieldName}"/>"
                           id = "NEWSO<h:outputText value="#{fieldConfigPerAdd.name}"/>"  
                           required="<h:outputText value="#{fieldConfigPerAdd.required}"/>" 
                           maxlength="<h:outputText value="#{fieldConfigPerAdd.maxLength}"/>"
                           onblur="javascript:validate_date(this,'<%=dateFormat%>');"
                           onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPerAdd.inputMask}"/>')"
                           onkeyup="javascript:qws_field_on_key_up(this)" >
                           <a href="javascript:void(0);" 
							title="<h:outputText value="#{fieldConfigPerAdd.displayName}"/>"
                            onclick="g_Calendar.show(event,
							'NEWSO<h:outputText value="#{fieldConfigPerAdd.name}"/>',
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
</td>
</tr>
</table>
</form>
</td>
</tr>
<!-- Root node form End -->

<tr>
<td valign="top">    
    <h:dataTable  headerClass="tablehead" 
                  id="allChildNodesNamesAdd" 
                  width="100%"
        		  style="width:100%;background-color:#c4c8e1;;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"
                  var="childNodesName" 
                  value="#{SourceHandler.allChildNodesNames}">
        <h:column>
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
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
                        <a title="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp;<h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" href="javascript:void(0)" onclick="javascript:showMinorObjectsDiv('extra<h:outputText value='#{childNodesName}'/>AddNewDiv')" class="button">
                        <span>
                            <img src="./images/down-chevron-button.png" border="0" alt="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp;<h:outputText value="#{childNodesName}"/>"/>&nbsp;<h:outputText value="#{msgs.source_submenu_add}"/> &nbsp;<h:outputText value="#{childNodesName}"/>&nbsp;<img src="./images/down-chevron-button.png" border="0" alt="<h:outputText value="#{msgs.source_submenu_add}"/>&nbsp;<h:outputText value="#{childNodesName}"/>"/>
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
                                                          value="#{SourceHandler.allNodeFieldConfigs}">
                                                <h:column>
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  id="childFieldConfigsAdd" 
                                                                  var="childFieldConfigAdd" 
                               				      style="width:100%;background-color:#c4c8e1;font-family: Arial, Helvetica, sans-serif; color: #6B6D6B; font-size: 12px; text-align: left;"                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                           <h:column>
                                              <nobr>
                                          		 <h:outputText rendered="#{childFieldConfigAdd.required}">
                                                    <span style="font-size:12px;color:red;verticle-align:top">*</span>
                                                 </h:outputText>													  
                                          		 <h:outputText value="#{childFieldConfigAdd.displayName}" />
                                                 <h:outputText value=":"/>
                                          	 </nobr>
                                           </h:column>                                                        
                                                        <!--Rendering HTML Select Menu List-->
                  

									      <!--user code related changes starts here-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <!-- User code fields here -->
												<h:selectOneMenu title="#{childFieldConfigAdd.fullFieldName}" onchange="getFormValues('#{childNodesName}AddNewSOInnerForm');ajaxMinorObjects('/'+URI_VAL+'/ajaxservices/usercodeservices.jsf?'+queryStr+'&MOT=#{childNodesName}&Field=#{childFieldConfigAdd.fullFieldName}&userCode=#{childFieldConfigAdd.userCode}&rand=+RAND_VAL+&userCodeMasking=true','#{childNodesName}AddNewSODiv',event)"
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
                                                                       id = "NewSO<h:outputText value="#{childFieldConfigAdd.name}"/>"  
                                                                       required="<h:outputText value="#{childFieldConfigAdd.required}"/>" 
                                                                       maxlength="<h:outputText value="#{childFieldConfigAdd.maxLength}"/>"
                                                                       onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{childFieldConfigAdd.inputMask}"/>')"
                                                                       onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                       onblur="javascript:validate_date(this,'<%=dateFormat%>');javascript:accumilateMinorObjectFieldsOnBlurLocal('<h:outputText value="#{childFieldConfigAdd.objRef}"/>',this,'<h:outputText value="#{childFieldConfigAdd.fullFieldName}"/>','<h:outputText value="#{childFieldConfigAdd.inputMask}"/>','<h:outputText value="#{childFieldConfigAdd.valueType}"/>')">
                                                                 <a href="javascript:void(0);" 
							                                      title="<h:outputText value="#{childFieldConfigAdd.displayName}"/>"
                                                                  onclick="g_Calendar.show(event,
							                                      'NewSO<h:outputText value="#{childFieldConfigAdd.name}"/>',
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
                                                                             onblur="javascript:accumilateMinorObjectFieldsOnBlurLocal('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}','#{childFieldConfigAdd.inputMask}','#{childFieldConfigAdd.valueType}')"
                                                                             required="#{fieldConfigAddAddress.required}" />
                                                        </h:column>
                                                    </h:dataTable>                                                                                
                                                </h:column>
                                            </h:dataTable>                                                                                
                                            
                                        </form>
                                    </td>
                                </tr>
								<tr>
					             <td>
                 					<a title="<h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp;<h:outputText value='#{childNodesName}'/>" href="javascript:void(0);" class="button" onclick="javascript:getFormValues('<h:outputText value="#{childNodesName}"/>AddNewSOInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&MOT=<h:outputText value="#{childNodesName}"/>&LID=<h:outputText value="#{sourceAddHandler.LID}"/>&SYS=<h:outputText value="#{sourceAddHandler.SystemCode}"/>&rand=<%=rand%>&minorObjSave=save','<h:outputText value="#{childNodesName}"/>AddNewSODiv',event)">
								   <span id="<h:outputText value='#{childNodesName}'/>buttonspan"><h:outputText value="#{msgs.source_rec_save_but}"/>&nbsp;<h:outputText value='#{childNodesName}'/> </span>
                                 </a>     
                                 <h:outputLink  title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('#{childNodesName}AddNewSOInnerForm')">
                                        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
                                 </h:outputLink>
								 </td>
								</tr>
                            </table>   
                        </div>   
                    </td>
                    <td><div id="<h:outputText value='#{childNodesName}'/>EditMessages" >   </div></td>
                </tr>
                <!--Minor objects loop ends-->

                <tr>
                    <td>
                        <div id="stealth" style="visibility:hidden;height:0px"> </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="<h:outputText value="#{childNodesName}"/>NewDiv" ></div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div id="<h:outputText value="#{childNodesName}"/>AddNewSODiv"></div>
                    </td>
                </tr>
            </table>   
        </h:column>
    </h:dataTable>
</td>
</tr>
<tr><td><div id="AddNewSODivMessages"></div></td></tr>                

<tr>
   <td>
   <nobr>
    <a href="javascript:void(0);" title="<h:outputText value="#{msgs.source_submenu_add}"/>" class="button" onclick="javascript:getFormValues('RootNodeInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&save=save','AddNewSODivMessages',event)">
	  <span id="buttonspan"><h:outputText value="#{msgs.source_submenu_add}"/>&nbsp;</span>
    </a>     
     <h:outputLink title="#{msgs.clear_button_label}" styleClass="button"  value="javascript:void(0)" onclick="javascript:ClearContents('RootNodeInnerForm')">
        <span><h:outputText value="#{msgs.clear_button_label}"/></span>
     </h:outputLink>    
	 <a class="button" title="<h:outputText value="#{msgs.cancel_but_text}"/>"  href="javascript:void(0)" onclick="javascript:ClearContents('RootNodeInnerForm');ajaxMinorObjects('/<%=URI%>/ajaxservices/euidSOAddminorobjects.jsf?'+queryStr+'&rand=<%=rand%>&cancel=cancel','AddNewSODivMessages',event);">
        <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
     </a>    
	 </nobr>
     </td>
</tr>

</table>
</div>


</body>
</html>
</f:view>
<%} %>  <!-- Session check -->
<!--End Add source record form-->
