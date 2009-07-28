<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.objects.ObjectField"  %>

<f:view>
    
 
    <html>
        <head>
            <title>EDM Driven Screen</title>  
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <LINK REL="STYLESHEET" HREF="./css/styles.css"  TYPE="text/css">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/DatePicker.css" rel="stylesheet" media="screen">
            
            <script type="text/javascript" src="scripts/yui/yahoo-dom-event.js"></script>             
            <script type="text/javascript" src="scripts/yui/animation.js"></script>            
            <script type="text/javascript" src="scripts/events.js"></script>            
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/Control.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/newdateformat1.js"></script>
            
        </head>
        <body>
            
            <div id="main" style="width:80%;height:600px;background:#f2f8fb;">   
                <div id="summaryDiv"  style="width:100%;border:0px solid #334baf;padding-top:0px;">
                    <div id="headerDiv" style="padding-top:0px;">
                        <table style="width:100%;background:#e7ecf5;border:1px solid #c2c2c2;">
                            <thead align="left" style="background:#0266f9;color:#black; font-size:10px" >
                                <tr>
                                    <th><h:outputText value="EDM"/></th> 
                                </tr> 
                            </thead>
                            <tbody>
                                <tr align="left" style="background:#e7ecf5;color:#black; font-size:30px">
                                    <th><h:outputText value="#{msgs.login_heading}"/></th> 
                                </tr>
                            </tbody>
                            <tfoot style="font-size:9px">
                                <tr><td align="right"><a href="#"><h:outputText value="#{msgs.login_help}"/></a></td></tr>
                            </tfoot>
                        </table>  
                    </div>  
                    <br>
                    <div id="basicSearch" class="basicSearch" style="visibility:hidden;display:none">
                        <h:form id="basicformData">
                            <h:dataTable id="basicScreenConfigId" value="#{HashMapHandler.screenConfigArray}" var="screenConfig" >
                                <h:column rendered="#{screenConfig.screenTitle eq 'Basic Search'}">
                                    <h:dataTable id="fieldConfigGrpId" var="feildConfigGrp" value="#{screenConfig.fieldConfigs}">
                                        <h:column>
                                            <h:dataTable id="basicFieldConfigId" var="feildConfig" value="#{feildConfigGrp.fieldConfigs}">
                                                <!--Rendering Non Updateable HTML Text Area-->
                                                <h:column rendered="#{feildConfig.updateable}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                                                    <h:inputText id="fieldConfigIdText" value="#{HashMapHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                                                    <h:inputTextarea id="fieldConfigIdTextArea"   value="#{HashMapHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                
                                                
                                                <!--Rendering Non Updateable HTML Text boxes-->
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                                        <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <h:inputText id="SystemCode" value="#{HashMapHandler.SystemCode}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="EUID" value="#{HashMapHandler.EUID}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="LID" value="#{HashMapHandler.LID}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                                    <h:column>
                                                       <h:outputText value="#{feildConfig.displayName}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    </h:column>
                                                    <h:inputText value="#{HashMapHandler.create_start_date}" 
                                                                 required="#{feildConfig.required}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'advancedformData:advScreenConfigId:1:fieldConfigGrpId:0:fieldConfigId:3:create_start_date')" > 
                                                        <h:graphicImage  id="calImgStartDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                                    <h:outputText value="#{feildConfig.displayName}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText value="#{HashMapHandler.create_end_date}" 
                                                                 required="#{feildConfig.required}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'advancedformData:advScreenConfigId:1:fieldConfigGrpId:0:fieldConfigId:4:create_end_date')" > 
                                                        <h:graphicImage  id="calImgEndDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                                 value="#{HashMapHandler.create_start_time}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="create_end_time" value="#{HashMapHandler.create_end_time}" 
                                                                 required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="Status"  value="#{HashMapHandler.Status}" required="#{feildConfig.required}"/>
                                                </h:column>
                                            </h:dataTable>
                                        </h:column>
                                    </h:dataTable>
                                </h:column>
                                <f:facet name="footer">
                                    <h:column>
                                        <h:commandLink  styleClass="button" action="#{HashMapHandler.performSubmit}">  
                                            <h:outputText value="Search"/>  
                                        </h:commandLink>                                     
                                        &nbsp;
                                         <a class="button" href="javascript:showAdvSearch()">
                                             &nbsp;&dArr;&nbsp;Advanced Search&nbsp;&dArr;&nbsp;
                                         </a>                                     
                                        
                                    </h:column>
                                </f:facet>
                            </h:dataTable>
                           
                        </h:form>
                    </div>  
                    <br>
                    <div id="advancedSearch" class="basicSearch" style="visibility:visible;display:block">
                        <h:form id="advancedformData">
                            <h:dataTable id="advScreenConfigId" value="#{HashMapHandler.screenConfigArray}" var="screenConfig" >
                                <h:column rendered="#{screenConfig.screenTitle eq 'Advanced Search'}">
                                    <h:outputText value="#{screenConfig.instruction}" /> <br>
                                    <h:dataTable id="fieldConfigGrpId" var="feildConfigGrp" value="#{screenConfig.fieldConfigs}">
                                        <h:column>
                                            <h:dataTable id="fieldConfigId" var="feildConfig" value="#{feildConfigGrp.fieldConfigs}">
                                                <!--Rendering Non Updateable HTML Text Area-->
                                                <h:column rendered="#{feildConfig.updateable}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text boxes-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextBox'}" >
                                                    <h:inputText id="fieldConfigIdText" value="#{HashMapHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <!--Rendering Updateable HTML Text Area-->
                                                <h:column rendered="#{feildConfig.updateable && feildConfig.guiType eq 'TextArea'}" >
                                                    <h:inputTextarea id="fieldConfigIdTextArea"   value="#{HashMapHandler.updateableFeildsMap[feildConfig.name]}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <!--Rendering HTML Select Menu List-->
                                                
                                                
                                                <!--Rendering Non Updateable HTML Text boxes-->
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'SystemCode'}" >
                                                        <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <h:inputText id="SystemCode" value="#{HashMapHandler.SystemCode}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'EUID' }" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="EUID" value="#{HashMapHandler.EUID}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'LID'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="LID" value="#{HashMapHandler.LID}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_start_date'}">
                                                    <h:column>
                                                       <h:outputText value="#{feildConfig.displayName}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    </h:column>
                                                    <h:inputText value="#{HashMapHandler.create_start_date}" 
                                                                 required="#{feildConfig.required}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'advancedformData:advScreenConfigId:1:fieldConfigGrpId:0:fieldConfigId:3:create_start_date')" > 
                                                        <h:graphicImage  id="calImgStartDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{ !feildConfig.updateable && feildConfig.guiType eq 'TextBox' &&  feildConfig.name eq 'create_end_date'}">
                                                    <h:outputText value="#{feildConfig.displayName}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText value="#{HashMapHandler.create_end_date}" 
                                                                 required="#{feildConfig.required}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a HREF="javascript:void(0);" onclick="g_Calendar.show(event, 
                                                       'advancedformData:advScreenConfigId:1:fieldConfigGrpId:0:fieldConfigId:4:create_end_date')" > 
                                                        <h:graphicImage  id="calImgEndDate" 
                                                                         alt="calendar Image" styleClass="imgClass"
                                                                         url="./images/cal.gif"/>               
                                                    </a>
                                                </h:column>
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_start_time'}">
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText rendered="#{ feildConfig.name eq 'create_start_time'}" id="create_start_time" 
                                                                 value="#{HashMapHandler.create_start_time}" required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq 'create_end_time'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="create_end_time" value="#{HashMapHandler.create_end_time}" 
                                                                 required="#{feildConfig.required}"/>
                                                </h:column>
                                                
                                                <h:column rendered="#{!feildConfig.updateable && feildConfig.guiType eq 'TextBox' && feildConfig.name eq  'Status'}" >
                                                    <h:outputText value="#{feildConfig.displayName}" />&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <h:inputText id="Status"  value="#{HashMapHandler.Status}" required="#{feildConfig.required}"/>
                                                </h:column>
                                            </h:dataTable>
                                        </h:column>
                                    </h:dataTable>
                                </h:column>
                                <f:facet name="footer">
                                    <h:column>
                                            <a class="button" href="javascript:action()">
                                            <h:outputText value="#{msgs.patdetails_search_button1}"/>
                                           </a>
                                            &nbsp;
                                            <h:commandLink  id="submitSearch" styleClass="button" 
                                                            action="#{HashMapHandler.performSubmit}" >  
                                                <h:outputText value="#{msgs.patdetails_search_button2}"/>
                                            </h:commandLink>                                     
                                            &nbsp;
                                            <a class="button" href="javascript:showBasicSearch()">&nbsp;&uArr;&nbsp;Basic Search&nbsp;&uArr;&nbsp;</a>                                     
                                        
                                    </h:column>
                                </f:facet>
                            </h:dataTable>
                            
                        </h:form>
                    </div>  
                    
                    <div id="loginDiv" style="padding-top:100px;padding-bottom:100px;">
                        <h:form id="loginform">
                            <table  style="background:#e0ebf9;border:2px solid #c2c2c2;font-size:11px;border-style:solidgroove;border-bottom:4px solid #dbe4f2;border-right:4px solid #dbe4f2;" 
                                    cellpadding="4px" cellspacing="4px" align="center">
                                <thead align="left" style="font-size: 11px;">
                                    <tr><th colspan="2"><h:outputText value="#{msgs.login_table_header_title}"/></th></tr>
                                </thead>
                                <tbody style="font-size: 11px;">
                                </tbody>
                                <tfoot style="font-size: 11px;" align="center">
                                    <tr>
                                        <td colspan="3">
                                            <h:commandButton style="font-size: 11px;" id="submit" action="#{HashMapHandler.performSubmit}" value="#{msgs.login_submit_button_prompt}" />
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </h:form>
                    </div>
                    <div id="errorDiv" style="padding-left:350px;">
                      <h:messages style="color: red;font-size:12px" id="errorMessages" layout="table" />
                   </div>
                </div>
            </div>
        </body>
    </html> 
</f:view>