<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateIterator"  %>
<%@ page import="com.sun.mdm.index.master.search.potdup.PotentialDuplicateSummary"  %>


<f:view>
   
    <HTML>
        <HEAD>
            <title>Master Indexed Web Application </title>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link type="text/css" href="./css/styles.css"  rel="stylesheet" media="screen">
            <link type="text/css" href="./css/calpopup.css" rel="stylesheet" media="screen">
            <link type="text/css" href="./css/cal.css" rel="stylesheet" media="screen">
            <script language="JavaScript" src="scripts/edm.js"></script>
            <script type="text/javascript" src="scripts/events.js"></script>
            <script type="text/javascript" src="scripts/calpopup.js"></script>
            <script type="text/javascript" src="scripts/dateparse.js"></script>
            <script type="text/javascript" src="scripts/animatedcollapse.js"></script> 
        </HEAD>
        <BODY>
            
            <div id="main" style="width:80%;background:#f2f8fb;">   
                
            <%@include file="./templates/header.jsp"%>
                <h:form id="potentialBasicDupForm">
                    <div id="basicSearch" style="padding-top:5px;visibility:visible;">
                        <table border="0" style="font-size: 11px;"> 
                            <tr align="center">
                                <td> <h:outputText value="#{msgs.patdetails1_euid}" style="font-size: 11px;" /></td>
                                <td>                                 
                                    <h:inputText id="euid"  value="#{SearchDuplicatesHandler.euid}" required="true" />
                                </td>
                                <td align="right" >
                                    <h:commandButton  value="#{msgs.patdetails_search_button}" style="font-size: 11px;" onclick="javascript:showAdvSearch()"/>
                                </td>
                                <td align="right"> 
                                    <h:commandButton  value="#{msgs.patdetails1_button2}" style="font-size: 11px;" onclick="javascript:showAdvSearch()"/>
                                </td>
                            </tr>
                        </table>
                        <h:inputHidden rendered="true" id="basicSearchType1" value="#{SearchDuplicatesHandler.searchType}"/>
                    </div>
                    <h:message style="color: red;font-size:10px;" id="error1" for="euid"/>
                </h:form>
                <div id="advSearch" style="visibility:hidden;"></div>
                
                <div id="errorDiv" style="padding-left:350px;">
                    <h:messages style="color: red;font-size:12px" id="errorMessages" layout="table" />
                </div>
                <hr/>
            <div id="basicSearchData" style="visibility:hidden;font-size: 11px;">
                <table border="0"> 
                    <tr align="center">
                        <td> <h:outputText value="#{msgs.patdetails1_euid}" style="font-size: 11px;"/></td>
                        <td>                                 
                            <h:inputText id="euid"  value="#{SearchDuplicatesHandler.euid}" required="true"/>
                        </td>
                        <td align="right" >
                            <h:commandButton 
                                id="submitBasicSearch" 
                                action="#{SearchDuplicatesHandler.performBasicSearch}" 
                                style="font-size: 11px;"
                                value="#{msgs.patdetails_search_button}"/>                               
                        </td>
                        <td align="right"> 
                            <h:commandButton  style="font-size: 11px;" value="#{msgs.patdetails1_button2}" onclick="javascript:showAdvSearch()"/>
                        </td>
                    </tr>
                </table>
                <h:inputHidden id="basicSearchType" value="#{SearchDuplicatesHandler.searchType}"/>
            </div>
            <div id="advSearchData1" style="visibility:hidden;font-size: 11px;">
                <h:form id="potentialDupForm">
                    <thead></thead>
                    <tbody>
                        <table width="70%" style="font-size:11px;border-width: 1px;border-color:black;border-style: solid;">
                            <tr>
                                <h:outputText value="#{msgs.patdetails_search_text1}"/>
                                <h:selectOneMenu   id="resultOption" value="resultOption">
                                    <f:selectItem  itemValue="1" itemLabel="All"/>
                                    <f:selectItem  itemValue="2" itemLabel="One"/>
                                    <f:selectItem  itemValue="3" itemLabel="None"/>
                                </h:selectOneMenu>                                                       
                                <h:outputText value="#{msgs.patdetails_search_text2}"/>
                            </tr>
                            
                            <tr>
                                <td> <h:outputText value="#{msgs.patdetails_search_euid}"/></td>
                                <td> <h:inputText value="#{SearchDuplicatesHandler.euid}"/></td>
                            </tr>
                            
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_fname}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.firstName}" rendered="false"/></td>
                            </tr>
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_lname}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.lastName}"/></td>
                            </tr>
                            <tr>
                                
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_dob}"/></span>
                                </td>
                                <td align="left">
                                    <input type="text" 
                                           id="dateField"
                                           name="todays_date" 
                                           value="" 
                                           onclick="g_Calendar.show(event, 'dateField')"
                                           class="dateparse" 
                                           maxlength="32"/>
                                    <A HREF="javascript:void(0);"
                                       onclick="g_Calendar.show(event, 'dateField')">
                                        <h:graphicImage id="calImg" 
                                                        alt="calendar Image"  
                                                        url="./images/cal.gif"
                                                        style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                    </A>
                                </td>
                            </tr>
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_add1}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.addressLine1}"/></td>
                            </tr> 
                            <tr>
                            <td><h:outputText value="#{msgs.patdetails_search_text3}"/></td>
                            <tr>
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_create_start_date}"/></span>
                                </td>
                                <td align="left">
                                <h:inputText   
                                    id="createStDateField"
                                    onclick="g_Calendar.show(event, 'potentialDupForm:createStDateField')"
                                    value="#{SearchDuplicatesHandler.createStartDate}" 
                                    styleClass="dateparse" 
                                    required="true"
                                    maxlength="32"/>
                                <A HREF="javascript:void(0);"
                                   onclick="g_Calendar.show(event, 'potentialDupForm:createStDateField')">
                                    <h:graphicImage id="calImg1" 
                                                    alt="calendar Image"  
                                                    url="./images/cal.gif"
                                                    style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                </A>
                                
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_create_end_date}"/></span> 
                                </td> 
                                <td align="left">    
                                <h:inputText 
                                    id="createEndDateField"
                                    onclick="g_Calendar.show(event, 'potentialDupForm:createEndDateField')"
                                    value="#{SearchDuplicatesHandler.createEndDate}" 
                                    required="true"
                                    styleClass="dateparse" 
                                    maxlength="32"/>
                                
                                <A HREF="javascript:void(0);"
                                   onclick="g_Calendar.show(event, 'potentialDupForm:createEndDateField')">
                                    <h:graphicImage id="calImg2" 
                                                    alt="calendar Image"  
                                                    url="./images/cal.gif"
                                                    style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                </A>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td align="center" >
                                    <h:commandButton  style="font-size: 11px;" value="#{msgs.patdetails_search_button1}" onclick="javascript:action()" />
                                </td>
                                <td>&nbsp;</td> <td>&nbsp;</td>
                                <td align="center"> 
                                    <h:commandButton style="font-size: 11px;" 
                                                     id="submitSearch" 
                                                     action="#{SearchDuplicatesHandler.potentialDuplicateSearch}" 
                                                     value="#{msgs.patdetails_search_button2}" />                                                     
                                    
                                </td>
                                <td align="center">
                                    <h:commandButton   style="font-size: 11px;" value="#{msgs.patdetails_search_button3}" onclick="javascript:showBasicSearch()"/> 
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8" align="right">
                                    <h:commandButton   style="font-size: 11px;" value="#{msgs.patdetails_search_button4}" onclick="javascript:action()"/>
                                </td>
                            </tr>  
                            
                        </table>
                    </tbody>
                    <tfoot></tfoot>
                </h:form> 
            </div>
                
                <div id="datadiv" style="visibility:visible;background:#f2f8fb;">
                    <hr/>
                    <%
                    PotentialDuplicateIterator pdPageIter = (PotentialDuplicateIterator) session.getAttribute("pdPageIter");
                    PotentialDuplicateIterator asPdIter ;

                    int countMain=0;
                    PotentialDuplicateSummary mainDuplicate = null;
                    PotentialDuplicateSummary duplicateSummary = null;
                    if(pdPageIter != null && pdPageIter.count() >0 ) {
                        while (pdPageIter.hasNext()) {
                            mainDuplicate  = (PotentialDuplicateSummary) pdPageIter.next();
                    
                    %>
                    <table border="0" width="800px" bgcolor="#f2f8fb"  style="font-size:11px;">
                        <tr bgcolor="#f2f8fb"> 
                            <td>         
                                <div id="concol">                
                                    <b>Record</b> 
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_name}"/>:<%=mainDuplicate.getEUID1()%> </div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_firstNamePrompt}"/>:</div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_lastNamePrompt}"/>: </div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_SSNPrompt}"/>:</div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_DOBPrompt}"/>:</div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_AddressPrompt}"/>:</div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_Address2Prompt}"/>:</div>
                                </div>
                                <% 
                                String divId = "datadiv"+new Integer(countMain).toString();
                                String diffPerId = "diffper"+new Integer(countMain).toString();
                                String dupHeading;
                                asPdIter  = mainDuplicate.getAssociatedPotentialDuplicates();
                                int countAd=0;
                                while(asPdIter.hasNext()) {
                                    duplicateSummary  = (PotentialDuplicateSummary) asPdIter.next();
                                    
                                    switch (countAd) {
                                        case 0:  dupHeading = "<b>Main EUID</b>"; break;
                                        case 1:  dupHeading = "<b>First Dup</b>"; break;
                                        case 2:  dupHeading = "<b>Second Dup</b>"; break;
                                        case 3:  dupHeading = "<b>Third Dup</b>"; break;
                                        case 4:  dupHeading = "<b>Preview</b>"; break;
                                        default: dupHeading = "<b>Preview</b>"; break;
                                    }
                                    divId = "datadiv"+new Integer(countMain+1).toString()+new Integer(countAd).toString();
                                    diffPerId = "diffper"+new Integer(countMain+1).toString()+new Integer(countAd).toString();
                                %>
                                <div id="<%=divId%>" 
                                     style="width:120px;background: #f7f8d5;float:left; border-width:1px;border-color:#f2f8fb;border:1px solid  #ffffff;border-spacing:1px; visibility:visible">
                                    <%if(countAd<asPdIter.count()-1){%>
                                    <%=dupHeading%>
                                    <div id="incol">
                                        <a  href="javascript:populateDuplicatesPreview('<%=divId%>','${msgs.PatDetail_inp_name}')">
                                            <%=duplicateSummary.getEUID1()%>
                                        </a>
                                    </div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_inp_firstname}"/>: <%=duplicateSummary.getStatus()%></div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_inp_lastname}"/>: </div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_inp_ssn}"/></div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_inp_dob}"/>:</div>
                                    <div id="incol"><h:outputText value="#{msgs.Dup_Page_Detail_inp_address}"/>:</div>
                                    <div id="<%=diffPerId%>">
                                        <a href="javascript:makeDiffPerson('<%=divId%>','visible','<%=diffPerId%>')">
                                            <h:graphicImage  alt="Different Person" url="./images/diffper.JPG" style="border-width:0px;border-color:#ffffff;border-style:solid;"/>
                                        </a>
                                    </div>
                                    <%}else{%>
                                    <div id="preview" style="visibility:hidden;">
                                        <div id="incol">&nbsp;&nbsp;</div>
                                        <div id="incol"><b>1</b></div>
                                        <div id="incol">2</div>
                                        <div id="incol">3</div>
                                        <div id="incol">4</div>
                                        <div id="incol">5</div>
                                        <div id="incol">5</div>
                                    </div>
                                    <%}%>
                                </div>
                                <%
                                countAd++;
                                }%>
                            </td>
                        </tr>
                    </table>
                    <%
                    countMain++;
                        }
                    }
                    %>
                    
                    
                    
                    
                </div>
            </div>
            
            <div id="basicSearchData" style="visibility:hidden;font-size: 11px;">
                <table border="0"> 
                    <tr align="center">
                        <td> <h:outputText value="#{msgs.patdetails1_euid}" style="font-size: 11px;"/></td>
                        <td>                                 
                            <h:inputText id="euid"  value="#{SearchDuplicatesHandler.euid}" required="true"/>
                        </td>
                        <td align="right" >
                            <h:commandButton 
                                id="submitBasicSearch" 
                                action="#{SearchDuplicatesHandler.performBasicSearch}" 
                                style="font-size: 11px;"
                                value="#{msgs.patdetails_search_button}"/>                               
                        </td>
                        <td align="right"> 
                            <h:commandButton  style="font-size: 11px;" value="#{msgs.patdetails1_button2}" onclick="javascript:showAdvSearch()"/>
                        </td>
                    </tr>
                </table>
                <h:inputHidden id="basicSearchType" value="#{SearchDuplicatesHandler.searchType}"/>
            </div>
            <div id="advSearchData" style="visibility:hidden;font-size: 11px;">
                <h:form id="potentialDupForm">
                    <thead></thead>
                    <tbody>
                        <table width="70%" style="font-size:11px;border-width: 1px;border-color:black;border-style: solid;">
                            <tr>
                                <h:outputText value="#{msgs.patdetails_search_text1}"/>
                                <h:selectOneMenu   id="resultOption" value="resultOption">
                                    <f:selectItem  itemValue="1" itemLabel="All"/>
                                    <f:selectItem  itemValue="2" itemLabel="One"/>
                                    <f:selectItem  itemValue="3" itemLabel="None"/>
                                </h:selectOneMenu>                                                       
                                <h:outputText value="#{msgs.patdetails_search_text2}"/>
                            </tr>
                            
                            <tr>
                                <td> <h:outputText value="#{msgs.patdetails_search_euid}"/></td>
                                <td> <h:inputText value="#{SearchDuplicatesHandler.euid}"/></td>
                            </tr>
                            
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_fname}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.firstName}" rendered="false"/></td>
                            </tr>
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_lname}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.lastName}"/></td>
                            </tr>
                            <tr>
                                
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_dob}"/></span>
                                </td>
                                <td align="left">
                                    <input type="text" 
                                           id="dateField"
                                           name="todays_date" 
                                           value="" 
                                           onclick="g_Calendar.show(event, 'dateField')"
                                           class="dateparse" 
                                           maxlength="32"/>
                                    <A HREF="javascript:void(0);"
                                       onclick="g_Calendar.show(event, 'dateField')">
                                        <h:graphicImage id="calImg" 
                                                        alt="calendar Image"  
                                                        url="./images/cal.gif"
                                                        style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                    </A>
                                </td>
                            </tr>
                            <tr>
                                <td><h:outputText value="#{msgs.patdetails_search_add1}"/></td>
                                <td><h:inputText value="#{SearchDuplicatesHandler.addressLine1}"/></td>
                            </tr> 
                            <tr>
                            <td><h:outputText value="#{msgs.patdetails_search_text3}"/></td>
                            <tr>
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_create_start_date}"/></span>
                                </td>
                                <td align="left">
                                <h:inputText   
                                    id="createStDateField"
                                    onclick="g_Calendar.show(event, 'potentialDupForm:createStDateField')"
                                    value="#{SearchDuplicatesHandler.createStartDate}" 
                                    styleClass="dateparse" 
                                    required="true"
                                    maxlength="32"/>
                                <A HREF="javascript:void(0);"
                                   onclick="g_Calendar.show(event, 'potentialDupForm:createStDateField')">
                                    <h:graphicImage id="calImg1" 
                                                    alt="calendar Image"  
                                                    url="./images/cal.gif"
                                                    style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                </A>
                                
                                <td align="left" class="FieldName">
                                    <span class="FieldNameText"><h:outputText value="#{msgs.patdetails_search_create_end_date}"/></span> 
                                </td> 
                                <td align="left">    
                                <h:inputText 
                                    id="createEndDateField"
                                    onclick="g_Calendar.show(event, 'potentialDupForm:createEndDateField')"
                                    value="#{SearchDuplicatesHandler.createEndDate}" 
                                    required="true"
                                    styleClass="dateparse" 
                                    maxlength="32"/>
                                
                                <A HREF="javascript:void(0);"
                                   onclick="g_Calendar.show(event, 'potentialDupForm:createEndDateField')">
                                    <h:graphicImage id="calImg2" 
                                                    alt="calendar Image"  
                                                    url="./images/cal.gif"
                                                    style="border-width:0px;border-color:#ffffff;border-style:solid;"/>               
                                </A>
                            </tr>
                            <tr>
                                <td colspan="5">
                                    <hr/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td align="center" >
                                    <h:commandButton  style="font-size: 11px;" value="#{msgs.patdetails_search_button1}" onclick="javascript:action()" />
                                </td>
                                <td>&nbsp;</td> <td>&nbsp;</td>
                                <td align="center"> 
                                    <h:commandButton style="font-size: 11px;" 
                                                     id="submitSearch" 
                                                     action="#{SearchDuplicatesHandler.potentialDuplicateSearch}" 
                                                     value="#{msgs.patdetails_search_button2}" />                                                     
                                    
                                </td>
                                <td align="center">
                                    <h:commandButton   style="font-size: 11px;" value="#{msgs.patdetails_search_button3}" onclick="javascript:showBasicSearch()"/> 
                                </td>
                            </tr>
                            <tr>
                                <td colspan="8" align="right">
                                    <h:commandButton   style="font-size: 11px;" value="#{msgs.patdetails_search_button4}" onclick="javascript:action()"/>
                                </td>
                            </tr>  
                            
                        </table>
                    </tbody>
                    <tfoot></tfoot>
                </h:form> 
            </div>
            
        </BODY>
    </HTML>
</f:view>