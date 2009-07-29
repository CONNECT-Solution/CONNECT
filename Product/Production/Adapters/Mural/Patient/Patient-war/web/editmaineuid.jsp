<%-- 
    Document   : editmaineuid
    Created on : Jan 4, 2008, 3:42:00 PM
    Author     : Rajani Kanth M
                 www.ligaturesoftware.com
--%>

<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@ page import="java.text.SimpleDateFormat"  %>
<%@ page import="java.util.Date"  %>
<%@ page import="java.util.HashMap"  %>
<%@ page import="java.util.ArrayList"  %>
<%@ page import="java.util.Collection"  %>
<%@ page import="java.util.Iterator"  %>
<%@ page import="javax.el.*"  %>
<%@ page import="javax.el.ValueExpression" %>

<%@ page import="com.sun.mdm.index.objects.EnterpriseObject"%>
<%@ page import="com.sun.mdm.index.objects.ObjectNode"%>
<%@ page import="com.sun.mdm.index.objects.SystemObject"%>
<%@ page import="com.sun.mdm.index.objects.SystemObjectPK"%>
<%@ page import="com.sun.mdm.index.objects.TransactionObject"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPath"%>
<%@ page import="com.sun.mdm.index.objects.epath.EPathArrayList"%>

<%@ page import="com.sun.mdm.index.edm.services.configuration.ScreenObject"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.FieldConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ObjectNodeConfig"  %>
<%@ page import="com.sun.mdm.index.edm.services.configuration.ConfigManager"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.SourceHandler"  %>
<%@ page import="com.sun.mdm.index.edm.presentation.handlers.EditMainEuidHandler"  %>


<f:view>
   
    
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
            <title><h:outputText value="#{msgs.application_heading}"/></title>
            <!-- YAHOO Global Object source file --> 
            <script type="text/javascript" src="http://yui.yahooapis.com/2.3.1/build/yahoo/yahoo-min.js" ></script>
            <!-- Additional source files go here -->
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            <script type="text/javascript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/Validation.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/newdateformat1.js"></script>
            <link rel="stylesheet" type="text/css" href="./css/yui/fonts/fonts-min.css" />
            <link rel="stylesheet" type="text/css" href="./css/yui/tabview/assets/skins/sam/tabview.css" />
            <script type="text/javascript" src="./scripts/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
            <script type="text/javascript" src="./scripts/yui/element/element-beta.js"></script>
            <script type="text/javascript" src="./scripts/yui/tabview/tabview.js"></script>
            <script type="text/javascript" src="scripts/yui4jsf/event/event.js"></script>
           <!--there is no custom header content for this example-->
           <style type="text/css">
        .squarecontainerOriginal { 
            width: 450px;
            overflow:auto;
        }

       .squarecontainer { 
            overflow:auto;
        }
        .squares {
            float: left;
            width: 5em;
            height: 5em;
            margin: .5em;
            border: 1px solid black;
        }
        
    </style>
        </head>
        <%@include file="./templates/header.jsp"%>
        <%
            EditMainEuidHandler editMainEuidHandler = (EditMainEuidHandler) session.getAttribute("EditMainEuidHandler");
            EnterpriseObject editEnterpriseObject = (EnterpriseObject) session.getAttribute("editEnterpriseObject");
            ValueExpression eoValueExpression = ExpressionFactory.newInstance().createValueExpression(editEnterpriseObject, editEnterpriseObject.getClass());
        %>
        <body class="yui-skin-sam">
            <div id="mainContent" style="overflow:hidden;"> 
                <div id="demo" class="yui-navset">
                    <div class="yui-content">
                        <h:form id="basicAddformData">
                         <h:inputHidden id="enteredFieldValues" value="#{EditMainEuidHandler.enteredFieldValues}"/>
                         <h:inputHidden id="minorFieldsEO" value="#{EditMainEuidHandler.minorFieldsEO}"/>
                         <h:inputHidden id="removeEOMinorObjectsValues" value="#{EditMainEuidHandler.removeEOMinorObjectsValues}"/>
                         <h:inputHidden id="hiddenUnLockFields" value="#{EditMainEuidHandler.hiddenUnLockFields}"/>
                            <table>
                                <tr>
                                    <td valign="top" align="left" width="50%">
                                        <h:messages  warnClass="warningMessages" infoClass="infoMessages" errorClass="errorMessages"  fatalClass="errorMessages" layout="list" />    
                                    </td>
                                </tr>
                                <tr>
                                    <td class="tablehead" align="left" colspan="2">
                                        <h:outputText value="#{msgs.edit_main_euid_label_text}" /> <%=editEnterpriseObject.getEUID()%> &nbsp;&nbsp;
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" colspan="2">
                                        <h:commandLink  styleClass="button" 
                                                        actionListener="#{EditMainEuidHandler.toUpdatedEuidDetails}"
                                                        action="#{NavigationHandler.toEuidDetails}">
                                            <span><h:outputText value="#{msgs.back_button_text}" /></span>
                                        </h:commandLink>               
                                    </td>
                                </tr>
                                <tr>
                                    <td valign="top">
                                        <!-- Start Main Euid Details-->
                                        <div id="addTab">
                                            <!-- Start EDIT Fields-->
                                            <table border="0" width="100%">
                                                <tr>
                                                    <td class="tablehead" align="right"  colspan="2">
                                                        <h:outputText value="#{msgs.main_euid_label_text}"/>                                                
                                                    </td>
                                                </tr>
                                            </table>
                                            <!--Start Displaying the root node fields -->                                        
                                             <h:dataTable  headerClass="tablehead"                                        
                                                           width="100%"
                                                           rowClasses="odd,even"                                     
                                                           id="hashIdEdit" 
                                                           var="fieldConfigPer" 
                                                           value="#{SourceHandler.rootNodeFieldConfigs}">                                                    
                                                 <h:column>
                                                     <h:outputText value="#{fieldConfigPer.displayName}"  />
                                                     <h:outputText value="*" rendered="#{fieldConfigPer.required}" />
                                                 </h:column>                                                        
                                                 <h:column>
                                                     <div id='linkSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                     <h:outputLink  rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"   
                                                                    value="javascript:void(0)" >
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] &&!EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id='linkSourceDivData:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style='visibility:hidden;display:none;'>
                                                     <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="link" styleClass="imgClass"
                                                                             url="./images/link.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                 </h:column>                                                        
                                                 <h:column>
                                                     <div id='unlockSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                     <h:outputLink  rendered="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" 
                                                                    onclick="javascript:unlockFields('#{fieldConfigPer.fullFieldName}')">
                                                            <h:graphicImage  alt="lock" styleClass="imgClass"
                                                                             url="./images/unlock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id="lockSourceDiv">
                                                     <h:outputLink  rendered="#{!EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName] }"   
                                                                    value="javascript:void(0)" >
                                                            <h:graphicImage  alt="lock" styleClass="imgClass" 
                                                                             url="./images/lock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                     <div id='lockSourceDiv:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style='visibility:hidden;display:block'>
                                                     <h:outputLink  value="javascript:void(0)" >
                                                            <h:graphicImage  alt="lock" styleClass="imgClass"
                                                                             url="./images/lock.PNG"/>               
                                                      </h:outputLink>
                                                     </div> 
                                                 </h:column>                                                        
                                                 <!--Rendering HTML Select Menu List-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                        <h:selectOneMenu value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                         disabled="#{!fieldConfigPer.updateable || (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}"
                                                                         onblur="javascript:accumilatePersonSelectFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}')"
                                                                         readonly="#{!fieldConfigPer.updateable || (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                            <f:selectItem itemLabel="" itemValue="" />
                                                            <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                        </h:selectOneMenu>
                                                    </h:column>
                                                    
                                                    <!--Rendering Updateable HTML Text boxes-->
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextbox"   
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                                      maxlength="#{fieldConfigPer.maxLength}"
																	  disabled="#{!fieldConfigPer.updateable || EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"
                                                                     readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"                                                                     
                                                                     onblur="javascript:accumilatePersonFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}')"
                                                                 />
                                                         </div>            
                                                        <div id='editableSBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style='visibility:hidden;display:none'>
                                                        <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextboxEditable"   
                                                                      maxlength="#{fieldConfigPer.maxLength}"
																	   readonly="#{!fieldConfigPer.updateable}"
																	   value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"
                                                           onblur="javascript:accumilatePersonFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}')"
                                                                     />
                                                         </div>            
                                                    </h:column>
                                                    
                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 && !(EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <nobr>
                                                            <input type="text" 
                                                                   id = "<h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilatePersonFieldsOnBlur(this,'<h:outputText value="#{fieldConfigPer.fullFieldName}" />')"
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                   value = "<h:outputText value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                   />
                                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPer.name}"/>')" > 
                                                            <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                            </a>
                                                        </nobr>
                                                        </div>            
                                                    </h:column>

                                                    <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType eq 6 && (EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName])}">
                                                        <div id='readOnlySBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>'>
                                                        <nobr>
                                                            <input type="text" 
                                                                   id = "<h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                   disabled="true"																   readonly="true"
                                                                   value = "<h:outputText value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                   />
                                                                       
                                                            <h:graphicImage  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                
                                                        </nobr>
                                                        </div>            
                                                        <div id='editableSBR:<h:outputText value="#{fieldConfigPer.fullFieldName}"/>' style="visibility:hidden;display:none;">
                                                        <nobr>
                                                            <input type="text" 
                                                                   id = "<h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilatePersonFieldsOnBlur(this,'<h:outputText value="#{fieldConfigPer.fullFieldName}" />')"
                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                   value = "<h:outputText value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                   />
                                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{fieldConfigPer.name}"/>')" > 
                                                                <h:graphicImage  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                            </a>
                                                        </nobr>
                                                        </div>            
                                                    </h:column>

                                                    <!--Rendering Updateable HTML Text boxes date fields-->
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                    <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                     id="fieldConfigIdTextArea"   
																	 disabled="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"
                                                                     readonly="#{EditMainEuidHandler.lockedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]  ||  EditMainEuidHandler.linkedFieldsHashMapFromDB[fieldConfigPer.fullFieldName]}"                                                                     
                                                                     onblur="javascript:accumilatePersonFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}')"
                                                                     value="#{EditMainEuidHandler.editSingleEOHashMap['ENTERPRISE_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                     required="#{fieldConfigPer.required}"/>
                                                </h:column>
                                                
                                            </h:dataTable>    

                                            <!--End Displaying the root  fields -->    
                                            <h:dataTable  headerClass="tablehead" 
                                                          id="allChildNodesNamesAdd" 
                                                          width="100%"
                                                          rowClasses="odd,even"                                     
                                                          var="childNodesName" 
                                                          value="#{SourceHandler.allEOChildNodesLists}">
                                                <h:column>
                                                    <table width="100%">
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <h:outputText value="#{childNodesName['NAME']}"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <a href="javascript:void(0);" 
                                                                   onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName['NAME']}"/>EODiv',event)" 
                                                                   class="button">
                                                                    <span>Add <h:outputText value="#{childNodesName['NAME']}"/></span>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="left" colspan="2">
                                                                <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODiv" style="width:100%;visibility:hidden;"></div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose" style="visibility:hidden;">
                                                                    <table>
                                                                        <tr>
                                                                            <td align="right" colspan="2">
                                                                                <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>EODiv','add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose')" class="button">
                                                                                <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>   
                                                    <h:dataTable  headerClass="tablehead" 
                                                                  width="100%"
                                                                  rowClasses="odd,even"                                     
                                                                  id="adfieldConfigId" 
                                                                  var="adressMapArrayList" 
                                                                  value="#{EditMainEuidHandler.editSingleEOHashMap[childNodesName['KEYLIST']]}">
                                                        <h:column>
                                                            
                                                            <h:dataTable 
                                                                rowClasses="odd,even"                                     
                                                                id="minorHashId" 
                                                                var="addressFieldConfig" 
                                                                value="#{childNodesName['FIELDCONFIGS']}">
                                                                
                                                                <h:column>
                                                                    <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                                    <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                                </h:column>
                                                                <!--Rendering HTML Select Menu List-->
                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                                    <h:selectOneMenu value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                         onblur="javascript:accumilateEOMinorFieldsSelectOnBlur(this,'#{fieldConfigPer.fullFieldName}','#{adressMapArrayList['MINOR_OBJECT_ID']}','#{adressMapArrayList['MINOR_OBJECT_TYP']}')">
                                                                        <f:selectItem itemLabel="" itemValue="" />
                                                                        <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                                    </h:selectOneMenu>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text boxes-->
                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                                    <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                                 id="fieldConfigIdTextbox"   
                                                                                 maxlength="#{addressFieldConfig.maxLength}"
																				 readonly="#{!addressFieldConfig.updateable}"		  value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                 onblur="javascript:accumilateEOMinorFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}','#{adressMapArrayList['MINOR_OBJECT_ID']}','#{adressMapArrayList['MINOR_OBJECT_TYP']}')"
                                                                                 required="#{addressFieldConfig.required}"/>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text boxes date fields-->
                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                                    <h:inputText label="#{addressFieldConfig.displayName}"   
                                                                                  maxlength="#{addressFieldConfig.maxLength}"
																				  readonly="#{!addressFieldConfig.updateable}"	   value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                                 id="date"
                                                                                 required="#{addressFieldConfig.required}"
                                                                                 onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                 onblur="javascript:accumilateEOMinorFieldsOnBlur(this,'#{fieldConfigPer.fullFieldName}','#{adressMapArrayList['MINOR_OBJECT_ID']}','#{adressMapArrayList['MINOR_OBJECT_TYPE']}')"
                                                                                 onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                        <h:graphicImage  id="calImgStartDate" 
                                                                                         alt="calendar Image" styleClass="imgClass"
                                                                                         url="./images/cal.gif"/>               
                                                                    </a>
                                                                </h:column>
                                                                
                                                                <!--Rendering Updateable HTML Text Area-->
                                                                <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                                    <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                                     id="fieldConfigIdTextArea"   
                                                                                     value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                     required="#{addressFieldConfig.required}"/>
                                                                </h:column>
                                                                <f:facet name="header">
                                                                    <h:column>
                                                                    <div id='<h:outputText value="#{adressMapArrayList['MINOR_OBJECT_ID']}"/><h:outputText value="#{childNodesName['NAME']}"/>'>
                                                                    </h:column>
                                                                </f:facet>
                                                                <f:facet name="footer">
                                                                    <h:column>
                                                                      <a href="javascript:accumilateEOMinorObjectsRemove('<h:outputText value="#{adressMapArrayList['MINOR_OBJECT_ID']}"/>','<h:outputText value="#{childNodesName['NAME']}"/>')" class="button">  
                                                                       <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span>
                                                                      </a>
                                                                      </div>    
                                                                  </h:column>
                                                                </f:facet>
                                                            </h:dataTable>               
                                                        </h:column>
                                                    </h:dataTable>                                                             
                                                </h:column>
                                            </h:dataTable>
                                                         
                                            <table><tr><td>&nbsp;</td></tr></table>
                                            <table>
                                                <tr>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toEuidDetails}"
                                                                        actionListener="#{PatientDetailsHandler.deactivateEO}">
                                                            <f:attribute name="eoValueExpression" value="<%=eoValueExpression%>"/>
                                                            <span><h:outputText value="Deactivate" /></span>
                                                        </h:commandLink> 
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{EditMainEuidHandler.performSubmit}">
                                                            <span><h:outputText value="Save" /></span>
                                                        </h:commandLink>
                                                        <h:commandLink  styleClass="button" 
                                                                        action="#{NavigationHandler.toEuidDetails}">
                                                            <span><h:outputText value="Cancel" /></span>
                                                        </h:commandLink>
                                                    </td>
                                                </tr>
                                            </table>
                                        </div>
                                    </td>
                                    <!-- End Main Euid Details-->
                                    <td> &nbsp;&nbsp;</td>        
                                    <td valign="top">
                                                    <div id="sourceRecordsDiv" style="overflow:auto;width:250px;">
                                        <table>
                                        <tr>
                                                        
                                                        <!-- Start Main Euid SO Details-->
                                    <%
            ArrayList eoList = editMainEuidHandler.getEoSystemObjects();
            ValueExpression eoSystemObjectsValueExpression = ExpressionFactory.newInstance().createValueExpression(eoList, eoList.getClass());
            for (int i = 0; i < eoList.size(); i++) {
                HashMap valueMap = (HashMap) eoList.get(i);
                ValueExpression eoMapValueExpression = ExpressionFactory.newInstance().createValueExpression(valueMap, valueMap.getClass());
%>

                                                        <td valign="top">
                                                            <h:dataTable  headerClass="tablehead"                                        
                                                                          width="100%"
                                                                          rowClasses="odd,even"   
                                                                          id="hashIdEditEo" 
                                                                          style="background-color:#efefef;border-top:1px solid #efefef;border-left:1px solid #efefef;border-right:1px solid #efefef;border-bottom:1px solid #efefef;"    
                                                                          var="eoSystemObjectMap"  
                                                                          value="<%=eoMapValueExpression%>">
                                                                <h:column>
                                                                    <table border="0" width="100%">
                                                                        <tr>
                                                                            <td class="tablehead" colspan="2">
                                                                                <h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE_DESC']}"/> - <h:outputText value="#{eoSystemObjectMap['LID']}" /> 
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                    <h:dataTable  headerClass="tablehead"                                        
                                                                                  width="100%"
                                                                                  rowClasses="odd,even"                                     
                                                                                  id="hashIdEdit" 
                                                                                  var="fieldConfigPer" 
                                                                                  value="#{SourceHandler.rootNodeFieldConfigs}">                                                    
                                                                        <h:column>
                                                                            <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/>:<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'
                                                                                 style="visibility:hidden;display:none;">
                                                                                <h:outputLink  value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                    <h:graphicImage  alt="link" styleClass="imgClass"
                                                                                                     url="./images/link.PNG"/>               
                                                                                </h:outputLink>
                                                                            </div> 
																			 <div id='<h:outputText value="#{fieldConfigPer.fullFieldName}"/><h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY']}"/>'
                                                                            <h:outputLink  rendered="#{EditMainEuidHandler.linkedSOFieldsHashMapFromDB[fieldConfigPer.fullFieldName] eq eoSystemObjectMap['SYSTEM_OBJECT']['LINK_KEY'] }"   
                                                                                           value="javascript:void(0)" onclick="javascript:showExtraUnLinkDivs(event,'#{fieldConfigPer.name}','#{fieldConfigPer.fullFieldName}>>#{eoSystemObjectMap['SYSTEM_CODE']}:#{eoSystemObjectMap['LID']}','#{fieldConfigPer.fullFieldName}')">
                                                                                <h:graphicImage  alt="link" styleClass="imgClass"
                                                                                                 url="./images/link.PNG"/>               
                                                                            </h:outputLink>
																			 </div> 
                                                                        </h:column>                                                        
                                                                        
                                                                        <!--Rendering HTML Select Menu List-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'MenuList' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:selectOneMenu 
																			
																			disabled="#{!fieldConfigPer.updateable}"
																			value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" >
                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                <f:selectItems  value="#{fieldConfigPer.selectOptions}"  />
                                                                            </h:selectOneMenu>
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputText label="#{fieldConfigPer.displayName}"  
                                                                                         id="fieldConfigIdTextbox"   
                                                                                         maxlength="#{fieldConfigPer.maxLength}"
																						 readonly="#{!fieldConfigPer.updateable}"	value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                         required="#{fieldConfigPer.required}"
                                                                                         />
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextBox' && fieldConfigPer.valueType eq 6 }">
                                                                            <input type="text" 
                                                                                   id = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>"  
                                                                                   onblur="javascript:validate_date(this,'MM/dd/yyyy');javascript:accumilatePersonFieldsOnBlur(this,'<h:outputText value="#{fieldConfigPer.fullFieldName}" />')"
                                                                                   onkeydown="javascript:qws_field_on_key_down(this, '<h:outputText value="#{fieldConfigPer.inputMask}"/>')"
                                                                                   onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                                   value = "<h:outputText value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}"/>"  
                                                                                   />
                                                                                       
                                                                            <a HREF="javascript:void(0);" onclick="g_Calendar.show(event,'<h:outputText value="#{eoSystemObjectMap['SYSTEM_CODE']}"/><h:outputText value="#{eoSystemObjectMap['LID']}" /><h:outputText value="#{fieldConfigPer.name}"/>')" > 
                                                                                <h:graphicImage  id="calImgDateFrom"  alt="calendar Image"  styleClass="imgClass" url="./images/cal.gif"/>               
                                                                            </a>
                                                                                
                                                                        </h:column>
                                                                        <!--Rendering Updateable HTML Text Area-->
                                                                        <h:column rendered="#{fieldConfigPer.guiType eq 'TextArea' &&  fieldConfigPer.valueType ne 6}" >
                                                                            <h:inputTextarea label="#{fieldConfigPer.displayName}"  
                                                                                             id="fieldConfigIdTextArea"   
                                                                                             value="#{eoSystemObjectMap['SYSTEM_OBJECT'][fieldConfigPer.fullFieldName]}" 
                                                                                             required="#{fieldConfigPer.required}"/>
                                                                        </h:column>
                                                                    </h:dataTable>
                                                                    
                                                                    
                                                                    <!--Minor Object fields here -->       
                                                                    <h:dataTable  headerClass="tablehead" 
                                                                                  id="allChildNodesNamesSoEdit" 
                                                                                  width="100%"
                                                                                  rowClasses="odd,even"                                     
                                                                                  var="childNodesName" 
                                                                                  value="#{SourceHandler.allSOChildNodesLists}">
                                                                        <h:column>
                                                                            <table width="100%">
                                                                                <tr>
                                                                                    <td class="tablehead" colspan="2">
                                                                                        <h:outputText value="#{childNodesName['NAME']}"/>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right" colspan="2">
                                                                                        <a href="javascript:void(0);" 
                                                                                           onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName['NAME']}"/>EODiv',event)" 
                                                                                           class="button">
                                                                                            <span>Add <h:outputText value="#{childNodesName['NAME']}"/></span>
                                                                                        </a>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="left" colspan="2">
                                                                                        <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODiv" style="width:100%;visibility:hidden;"></div>
                                                                                    </td>
                                                                                </tr>
                                                                                <tr>
                                                                                    <td align="right" colspan="2">
                                                                                        <div id="add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose" style="visibility:hidden;">
                                                                                            <table>
                                                                                                <tr>
                                                                                                    <td align="right" colspan="2">
                                                                                                        <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>EODiv','add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose')" class="button">
                                                                                                        <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                                                    </td>
                                                                                                </tr>
                                                                                            </table>
                                                                                        </div>
                                                                                    </td>
                                                                                </tr>
                                                                            </table>   
                                                                            <h:dataTable  headerClass="tablehead" 
                                                                                          width="100%"
                                                                                          rowClasses="odd,even"                                     
                                                                                          id="sofieldConfigId" 
                                                                                          var="adressMapArrayList" 
                                                                                          value="#{eoSystemObjectMap[childNodesName['KEYLIST']]}">
                                                                                <h:column>
                                                                                    <h:dataTable 
                                                                                        rowClasses="odd,even"                                     
                                                                                        id="minorHashId" 
                                                                                        var="addressFieldConfig" 
                                                                                        value="#{childNodesName['FIELDCONFIGS']}">
                                                                                        <h:column>
                                                                                            <h:outputText value="#{addressFieldConfig.displayName}"  />
                                                                                            <h:outputText value="*" rendered="#{addressFieldConfig.required}" />
                                                                                        </h:column>
                                                                                        <!--Rendering HTML Select Menu List-->
                                                                                        <h:column rendered="#{addressFieldConfig.guiType eq 'MenuList' &&  addressFieldConfig.valueType ne 6}" >
                                                                                            <h:selectOneMenu 
																							disabled="#{!addressFieldConfig.updateable}" value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" >
                                                                                                <f:selectItem itemLabel="" itemValue="" />
                                                                                                <f:selectItems  value="#{addressFieldConfig.selectOptions}"  />
                                                                                            </h:selectOneMenu>
                                                                                        </h:column>
                                                                                        
                                                                                        <!--Rendering Updateable HTML Text boxes-->
                                                                                        <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType ne 6}" >
                                                                                            <h:inputText label="#{addressFieldConfig.displayName}"  
                                                                                                         id="fieldConfigIdTextbox"   
                                                                                                         maxlength="#{addressFieldConfig.maxLength}"
																										 readonly="#{!addressFieldConfig.updateable}"	value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                                         required="#{addressFieldConfig.required}"/>
                                                                                        </h:column>
                                                                                        
                                                                                        <!--Rendering Updateable HTML Text boxes date fields-->
                                                                                        <h:column rendered="#{addressFieldConfig.guiType eq 'TextBox' &&  addressFieldConfig.valueType eq 6}">
                                                                                            <h:inputText label="#{addressFieldConfig.displayName}"  maxlength="#{addressFieldConfig.maxLength}" 
                                                                                             readonly="#{!addressFieldConfig.updateable}"            value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}"  
                                                                                                         id="date"
                                                                                                         required="#{addressFieldConfig.required}"
                                                                                                         onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                                                                         onkeyup="javascript:qws_field_on_key_up(this)" />
                                                                                            <a HREF="javascript:void(0);" 
                                                                                               onclick="g_Calendar.show(event,'topButtonsForm:fieldConfigId:6:date')" > 
                                                                                                <h:graphicImage  id="calImgStartDate" 
                                                                                                                 alt="calendar Image" styleClass="imgClass"
                                                                                                                 url="./images/cal.gif"/>               
                                                                                            </a>
                                                                                        </h:column>
                                                                                        
                                                                                        <!--Rendering Updateable HTML Text Area-->
                                                                                        <h:column rendered="#{addressFieldConfig.guiType eq 'TextArea' &&  addressFieldConfig.valueType ne 6}" >
                                                                                            <h:inputTextarea label="#{addressFieldConfig.displayName}"  
                                                                                                             id="fieldConfigIdTextArea"   
                                                                                                             value="#{adressMapArrayList[addressFieldConfig.fullFieldName]}" 
                                                                                                             required="#{addressFieldConfig.required}"/>
                                                                                        </h:column>
                                                                                        <f:facet name="footer">
                                                                                            <h:column>
                                                                                                <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>EODiv','add<h:outputText value="#{childNodesName['NAME']}"/>EODivClose')" class="button">
                                                                                                <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                                            </h:column>
                                                                                        </f:facet>
                                                                                    </h:dataTable>               
                                                                                </h:column>
                                                                            </h:dataTable>                                                             
                                                                        </h:column>
                                                                    </h:dataTable>
                                                                    <!-- End Display minor objects fields --> 
                                                                    <h:commandLink  styleClass="button" 
                                                                                    rendered="#{eoSystemObjectMap['Status'] eq 'active'}"
                                                                                    actionListener="#{EditMainEuidHandler.deactivateEOSO}">
                                                                        <f:attribute name="eoSystemObjectMapVE" value="#{eoSystemObjectMap}"/>
                                                                        <span><h:outputText value="Deactivate" /></span>
                                                                    </h:commandLink>                         
                                                                    
                                                                    <h:commandLink  styleClass="button" 
                                                                                    rendered="#{eoSystemObjectMap['Status'] eq 'inactive'}"
                                                                                    actionListener="#{EditMainEuidHandler.activateEOSO}">
                                                                        <f:attribute name="eoSystemObjectMapVE" value="#{eoSystemObjectMap}"/>
                                                                        <span><h:outputText value="Activate" /></span>
                                                                    </h:commandLink>                 
                                                                </h:column>
                                                            </h:dataTable> 
                                                        </td>
                                                        <%}%>
                                                </tr>
                                            </table>
                                                    </div>
                                            </td>
                                    <h:inputHidden  id="hiddenLinkFields"  value="#{EditMainEuidHandler.hiddenLinkFields}" />
                                    <h:inputHidden  id="hiddenUnLinkFields"  value="#{EditMainEuidHandler.hiddenUnLinkFields}" />
                                </h:form>
                                <!-- New SO fields start here -->
                                <h:form id="basicNewSOAddformData">
                                    <td valign="top">
                                        <table width="100%">
                                            <tr>
                                                <td colspan="1">
                                                    <h:selectOneMenu id="systemCode" value="#{EditMainEuidHandler.newSoSystemCode}">
                                                        <f:selectItems  value="#{EditMainEuidHandler.systemCodes}" />
                                                    </h:selectOneMenu>
                                                </td>
                                                <td>
                                                    LID : 
                                                </td>
                                                <td>
                                                    <h:inputText id="LID" value="#{EditMainEuidHandler.newSoLID}" 
                                                                 onkeydown="javascript:qws_field_on_key_down(this, document.basicNewSOAddformData.lidmask.value)"
                                                                 onkeyup="javascript:qws_field_on_key_up(this)" 
                                                                 />
                                                </td>
                                            </tr>                                            
                                        </table>
                                        <h:dataTable  headerClass="tablehead"  
                                                      id="hashIdEditNewSO" 
                                                      width="100%"
                                                      var="newSOfieldConfigPerAdd" 
                                                      value="#{SourceHandler.rootNodeFieldConfigs}">
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'MenuList' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:selectOneMenu 
												value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{newSOfieldConfigPerAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:inputText label="#{newSOfieldConfigPerAdd.displayName}"  
                                                             id="fieldConfigIdTextbox"  
                                                             maxlength="#{newSOfieldConfigPerAdd.maxLength}"										 value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}" 
                                                             required="#{newSOfieldConfigPerAdd.required}"/>
                                            </h:column>                     
                                            <!--Rendering Updateable HTML Text boxes date fields-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextBox' &&  newSOfieldConfigPerAdd.valueType eq 6 }">
                                                <h:inputText label="#{newSOfieldConfigPerAdd.name}"  id="DOB"
                                                             maxlength="#{newSOfieldConfigPerAdd.maxLength}"
															 value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}"  
                                                             required="#{newSOfieldConfigPerAdd.required}"
                                                             onkeydown="javascript:qws_field_on_key_down(this, 'DD/DD/DDDD')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             />
                                                <script> var DOB1 = getDateFieldName('basicNewSOAddformData',':DOB');</script>                                                                            
                                                <h:outputLink value="javascript:void(0);"  id="calLink"
                                                              onclick="g_Calendar.show(event,DOB1)" > 
                                                    <h:graphicImage  id="calImgStartDate" 
                                                                     alt="calendar Image" styleClass="imgClass"
                                                                     url="./images/cal.gif"/>               
                                                </h:outputLink>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text Area-->
                                            <h:column rendered="#{newSOfieldConfigPerAdd.guiType eq 'TextArea' &&  newSOfieldConfigPerAdd.valueType ne 6}" >
                                                <h:inputTextarea label="#{newSOfieldConfigPerAdd.displayName}"  
                                                                 id="fieldConfigIdTextArea"   
                                                                 value="#{EditMainEuidHandler.newSOHashMap[newSOfieldConfigPerAdd.fullFieldName]}" 
                                                                 required="#{newSOfieldConfigPerAdd.required}"
                                                                 />
                                            </h:column>
                                            
                                        </h:dataTable>
                                        


                                            <h:dataTable  headerClass="tablehead" 
                                                          id="allChildNodesNamesAdd" 
                                                          width="100%"
                                                          rowClasses="odd,even"                                     
                                                          var="childNodesName" 
                                                          value="#{SourceHandler.allEOChildNodesLists}">
                                                <h:column>
                                                    <table width="100%">
                                                        <tr>
                                                            <td class="tablehead" colspan="2">
                                                                <h:outputText value="#{childNodesName['NAME']}"/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <a href="javascript:void(0);"         
                                                                   onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName['NAME']}"/>NewSODiv',event)" 
                                                                   class="button">
                                                                    <span>Add <h:outputText value="#{childNodesName['NAME']}"/></span>
                                                                </a>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="left" colspan="2">
                                                                <div id="add<h:outputText value="#{childNodesName['NAME']}"/>NewSODiv" style="width:100%;visibility:hidden;"></div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right" colspan="2">
                                                                <div id="add<h:outputText value="#{childNodesName['NAME']}"/>NewSODivClose" style="visibility:hidden;">
                                                                    <table>
                                                                        <tr>
                                                                            <td align="right" colspan="2">
                                                                                <a href="javascript:closeExtraDivs('add<h:outputText value="#{childNodesName['NAME']}"/>NewSODiv','add<h:outputText value="#{childNodesName['NAME']}"/>NewSODivClose')" class="button">
                                                                                <span>Delete <h:outputText value="#{childNodesName['NAME']}"/></span></a>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </table>   
                                                </h:column>
                                           </h:dataTable>


                                        
                                        <table>
                                            <tr>
                                                <td>
                                                    <h:commandLink  styleClass="button" 
                                                                    action="#{EditMainEuidHandler.addNewSO}">
                                                        <span><h:outputText value="#{msgs.add_new_so_button_text}" /></span>
                                                    </h:commandLink>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>                                        
                                    <!--new SO Fields end here-->
                               </h:form>
                            </tr>
                        </table>
                    </div>         
                </div>         
   <h:dataTable  headerClass="tablehead" 
                 id="allChildNodesNames" 
                 var="childNodesName" 
                 value="#{SourceHandler.allChildNodesNames}">
        <h:column>
            <div id="extra<h:outputText value="#{childNodesName}"/>NewSODiv" 
                 class="alertSource"  
                 style="TOP:580px;LEFT:450px;HEIGHT:<h:outputText value="#{SourceHandler.allNodeFieldConfigsSizes[childNodesName]}" />px;WIDTH:400px;visibility:hidden;">
             <h:form>
                <table>
                    <tr>
                        <td align="right" colspan="2">
                            <div>
                                <a href="javascript:void(0)" rel="editballoon<h:outputText value="#{childNodesName}"/>"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                            </div> 
                        </td>
                    </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="allNodeFieldConfigsMap" 
                                       var="allNodeFieldConfigsMap" 
                                       value="#{SourceHandler.allNodeFieldConfigs}">
                             <h:column>
                                 <h:dataTable  headerClass="tablehead" 
                                               id="childFieldConfigs" 
                                               var="childFieldConfig" 
                                               width="100%"
                                               rowClasses="odd,even"                                     
                                               value="#{allNodeFieldConfigsMap[childNodesName]}">
                                     
                                     <h:column>
                                         <h:outputText value="#{childFieldConfig.displayName}"  />
                                         <h:outputText value="*" rendered="#{childFieldConfig.required}" />
                                     </h:column>
                                     <!--Rendering HTML Select Menu List-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'MenuList'}" >
                                         <h:selectOneMenu 
										 disabled="#{!childFieldConfig.updateable}"
										 value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}">
                                             <f:selectItem itemLabel="" itemValue="" />
                                             <f:selectItems  value="#{childFieldConfig.selectOptions}"  />
                                         </h:selectOneMenu>
                                     </h:column>
                                     <!--Rendering Updateable HTML Text boxes-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextBox'}" >
                                         <h:inputText label="#{childFieldConfig.displayName}"  
												      maxlength="#{childFieldConfig.maxLength}"
                                                      value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                      required="#{childFieldConfig.required}"/>
                                     </h:column>                     
                                     <!--Rendering Updateable HTML Text Area-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextArea'}" >
                                         <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                          value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                          required="#{fieldConfigAddAddress.required}"
                                                          />
                                     </h:column>
                                 </h:dataTable>                                                                                
                             </h:column>
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                          <a href="javascript:populateExtraDivs('<h:outputText value="#{childNodesName}"/>InnerDiv','add<h:outputText value="#{childNodesName}"/>Div','extra<h:outputText value="#{childNodesName}"/>AddDiv','add<h:outputText value="#{childNodesName}"/>DivClose')" class="button">
                            <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                          </a>    
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>EditDiv',event)" class="button"> 
                         <span><h:outputText value="#{msgs.cancel_but_text}"/> </span>
                          </a>    
                     </td>
                 </tr>
                 
                </table>
             </h:form>
            </div>
            <div id="extra<h:outputText value="#{childNodesName}"/>EditSODiv" 
                 class="alertSource"  
                 style="TOP:580px;LEFT:450px;HEIGHT:<h:outputText value="#{SourceHandler.allNodeFieldConfigsSizes[childNodesName]}" />px;WIDTH:400px;visibility:hidden;">
             <h:form>
                <table>
                    <tr>
                        <td align="right" colspan="2">
                            <div>
                                <a href="javascript:void(0)" rel="editballoon<h:outputText value="#{childNodesName}"/>"><h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                            </div> 
                        </td>
                    </tr>
                 <tr>
                     <td colspan="2">
                         <h:dataTable  headerClass="tablehead" 
                                       id="allNodeFieldConfigsMap" 
                                       var="allNodeFieldConfigsMap" 
                                       value="#{SourceHandler.allNodeFieldConfigs}">
                             <h:column>
                                 <h:dataTable  headerClass="tablehead" 
                                               id="childFieldConfigs" 
                                               var="childFieldConfig" 
                                               width="100%"
                                               rowClasses="odd,even"                                     
                                               value="#{allNodeFieldConfigsMap[childNodesName]}">
                                     
                                     <h:column>
                                         <h:outputText value="#{childFieldConfig.displayName}"  />
                                         <h:outputText value="*" rendered="#{childFieldConfig.required}" />
                                     </h:column>
                                     <!--Rendering HTML Select Menu List-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'MenuList'}" >
                                         <h:selectOneMenu 
										 disabled="#{!childFieldConfig.updateable}" 
										 value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}">
                                             <f:selectItem itemLabel="" itemValue="" />
                                             <f:selectItems  value="#{childFieldConfig.selectOptions}"  />
                                         </h:selectOneMenu>
                                     </h:column>
                                     <!--Rendering Updateable HTML Text boxes-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextBox'}" >
                                         <h:inputText label="#{childFieldConfig.displayName}" 
													  maxlength="#{childFieldConfig.maxLength}"
                                                      value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                      required="#{childFieldConfig.required}"/>
                                     </h:column>                     
                                     <!--Rendering Updateable HTML Text Area-->
                                     <h:column rendered="#{childFieldConfig.guiType eq 'TextArea'}" >
                                         <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                          value="#{SourceEditHandler.editSoAddressHashMap[childFieldConfig.fullFieldName]}" 
                                                          required="#{fieldConfigAddAddress.required}"
                                                          />
                                     </h:column>
                                 </h:dataTable>                                                                                
                             </h:column>
                         </h:dataTable>                                                                                
                     </td>
                 </tr>
                 <tr>
                     <td>
                          <a href="javascript:populateExtraDivs('<h:outputText value="#{childNodesName}"/>InnerDiv','add<h:outputText value="#{childNodesName}"/>Div','extra<h:outputText value="#{childNodesName}"/>AddDiv','add<h:outputText value="#{childNodesName}"/>DivClose')" class="button">
                            <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                          </a>    
                     </td>
                     <td>
                         <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>EditSODiv',event)" class="button"> 
                         <span><h:outputText value="#{msgs.cancel_but_text}"/> </span>
                          </a>    
                     </td>
                 </tr>
                 
                </table>
             </h:form>
            </div>
            <div id="extra<h:outputText value='#{childNodesName}'/>EODiv" 
                 class="alertSource"  
                 style="TOP:1800px;LEFT:700px;HEIGHT:<h:outputText value="#{SourceHandler.allNodeFieldConfigsSizes[childNodesName]}" />px;WIDTH:400px;visibility:hidden;">
                <h:form>
                <table>
                    <tr>
                        <td align="right" colspan="2">
                            <div>
                                <a href="javascript:void(0)" rel="balloon<h:outputText value="#{childNodesName}"/>">
                                <h:outputText value="#{msgs.help_link_text}"/> </a><br/>
                            </div>                               
                        </td>
                        
                    </tr>
                    <tr>
                        <td colspan="2" align="left">
                            <div id="<h:outputText value="#{childNodesName}"/>EOInnerDiv">
                                <h:dataTable  headerClass="tablehead" 
                                              id="allNodeFieldConfigsMapAdd" 
                                              var="allNodeFieldConfigsMapAdd" 
                                              width="100%"
                                              rowClasses="odd,even"                                     
                                              value="#{SourceHandler.allNodeFieldConfigs}">
                                    <h:column>
                                        <h:dataTable  headerClass="tablehead" 
                                                      id="childFieldConfigsAdd" 
                                                      var="childFieldConfigAdd" 
                                                      width="100%"
                                                      rowClasses="odd,even"                                     
                                                      value="#{allNodeFieldConfigsMapAdd[childNodesName]}">
                                            
                                            <h:column>
                                                <h:outputText value="#{childFieldConfigAdd.displayName}"  />
                                                <h:outputText value="*" rendered="#{childFieldConfigAdd.required}" />
                                            </h:column>
                                            <!--Rendering HTML Select Menu List-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'MenuList'}" >
                                                <h:selectOneMenu 
		onblur="javascript:accumilateMinorObjectSelectFieldsOnBlur('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}')"
                                                                 value="">
                                                    <f:selectItem itemLabel="" itemValue="" />
                                                    <f:selectItems  value="#{childFieldConfigAdd.selectOptions}"  />
                                                </h:selectOneMenu>
                                            </h:column>
                                            <!--Rendering Updateable HTML Text boxes-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextBox'}" >
                                                <h:inputText label="#{childFieldConfigAdd.displayName}"  
                                                             maxlength="#{childFieldConfigAdd.maxLength}"
															 onblur="javascript:accumilateMinorObjectFieldsOnBlur('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}')"
                                                             onkeydown="javascript:qws_field_on_key_down(this, '#{childFieldConfigAdd.inputMask}')"
                                                             onkeyup="javascript:qws_field_on_key_up(this)" 
                                                             value=""
                                                             required="#{childFieldConfigAdd.required}"/>
                                            </h:column>                     
                                            <!--Rendering Updateable HTML Text Area-->
                                            <h:column rendered="#{childFieldConfigAdd.guiType eq 'TextArea'}" >
                                                <h:inputTextarea label="#{fieldConfigAddAddress.displayName}"  
                                                                 onblur="javascript:accumilateMinorObjectFieldsOnBlur('#{childFieldConfigAdd.objRef}',this,'#{childFieldConfigAdd.fullFieldName}')"
                                                                 required="#{fieldConfigAddAddress.required}"
                                                                 value=""/>
                                            </h:column>
                                        </h:dataTable>                                                                                
                                    </h:column>
                                </h:dataTable>                                                                                
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <a href="javascript:populateExtraDivs('<h:outputText value="#{childNodesName}"/>EOInnerDiv','add<h:outputText value="#{childNodesName}"/>EODiv','extra<h:outputText value="#{childNodesName}"/>EOAddDiv','add<h:outputText value="#{childNodesName}"/>DivClose')" class="button">
                            <span><h:outputText value="#{msgs.ok_text_button}"/></span></a>    
                        </td>
                        <td>
                            <a HREF="javascript:void(0);" onclick="javascript:showExtraDivs('extra<h:outputText value="#{childNodesName}"/>EODiv',event)" class="button">
                            <span><h:outputText value="#{msgs.cancel_but_text}"/></span> </a>
                        </td>
                    </tr>
                </table>   
                </h:form>                 
            </div>   
        </h:column>                 
    </h:dataTable>
                
    <!-- END Extra divs for NEW  SO-->
            <div id="linkSoDiv"  class="alert" style="TOP:620px;LEFT:450px;HEIGHT:100px;WIDTH:350px;overflow:auto;VISIBILITY:hidden;">
                <form name="linkForm">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="linkedValueDiv" style="visibility:hidden"></div>
                            <nobr>Link to the '<span id="linkedDisplayValueDiv"></span>' field of :</nobr> 
                        </td>
                        <td>
                            <h:selectOneMenu id="systemCodeWithLid" value="#{EditMainEuidHandler.linkedSoWithLidByUser}">
                                <f:selectItems  value="#{EditMainEuidHandler.eoSystemObjectCodesWithLids}" />
                            </h:selectOneMenu>
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <h:outputLink  styleClass="button" rendered="#{Operations.EO_LinkSBRFields}" value="javascript:void(0)" onclick="javascript:populateLinkFields()">
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                            </h:outputLink>
                        </td>
                        <td align="left">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('linkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 
           <div id="unLinkSoDiv" class="alert" style="TOP:620px;LEFT:450px;HEIGHT:120px;WIDTH:350px;VISIBILITY:hidden;">
               <form name="unlinkForm">
                <table valign="center" style="padding-top:20px">
                    <tr>
                        <td>
                            <div id="unLinkedValueDiv" style="visibility:hidden"></div>
                            <div id="unLinkedFullFieldDiv" style="visibility:hidden"></div>
                            <nobr>Unlink from  '<span id="unLinkedDisplayValueDiv"></span>' field of Main EUID?</nobr> 
                        </td>
                    </tr>    
                    <tr>
                        <td align="right">
                            <h:outputLink  styleClass="button" value="javascript:void(0)" rendered="#{Operations.EO_UnlinkSBRFields}" onclick="javascript:populateUnLinkFields()">
                                <span><h:outputText value="#{msgs.ok_text_button}"/></span>
                            </h:outputLink>
                           <h:outputLink  styleClass="button" value="javascript:void(0)" onclick="javascript:showExtraDivs('unLinkSoDiv',event)">
                                <span><h:outputText value="#{msgs.cancel_but_text}"/></span>
                            </h:outputLink>
                        </td>
                    </tr>
                </table> 
                </form>
            </div> 
            
            <!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->
            <script>
                (function() {
                    var tabView = new YAHOO.widget.TabView('demo');
                    
                    YAHOO.log("The example has finished loading; as you interact with it, you'll see log messages appearing here.", "info", "example");
                })();
            </script>
            <!--END SOURCE CODE FOR EXAMPLE =============================== -->
         </div>         
        </body>
    </html>
    </f:view>
    
    
    
