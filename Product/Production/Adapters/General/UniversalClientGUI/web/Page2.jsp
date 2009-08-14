<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page2
    Created on : Aug 7, 2009, 6:03:45 PM
    Author     : vvickers
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <webuijsf:image height="100" id="image1" style="left: 0px; top: 0px; position: absolute" url="/resources/connect.GIF" width="312"/>
                        <webuijsf:tabSet binding="#{Page2.subjectDiscoveryTab}" id="subjectDiscoveryTab" selected="patientSearchTab" style="left: 0px; top: 144px; position: absolute; width: 694px">
                            <webuijsf:tab actionExpression="#{Page2.patientSearchTab_action}" binding="#{Page2.patientSearchTab}" id="patientSearchTab"
                                style="font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Patient Search">
                                <webuijsf:panelLayout id="layoutPanel1" style="height: 537px; position: relative; width: 647px; -rave-layout: grid">
                                    <webuijsf:label id="searchInstruct"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 24px; position: absolute" text="Enter the following Patient Search Criteria:"/>
                                    <webuijsf:label binding="#{Page2.lastNameLabel}" for="lastNameField" id="lastNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 72px; position: absolute" text="Last Name:"/>
                                    <webuijsf:textField binding="#{Page2.lastNameField}" id="lastNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 120px; top: 72px; position: absolute"/>
                                    <webuijsf:label binding="#{Page2.firstNameLabel}" id="firstNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 96px; position: absolute" text="First Name:"/>
                                    <webuijsf:textField binding="#{Page2.firstNameField}" id="firstNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 120px; top: 96px; position: absolute"/>
                                    <webuijsf:button actionExpression="#{Page2.patientSearchButton_action}" id="patientSearchButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 18px; left: 311px; top: 96px; position: absolute" text="Search"/>
                                    <webuijsf:table augmentTitle="false" binding="#{Page2.table1}" id="table1"
                                        style="left: 24px; top: 144px; position: absolute; width: 450px" title="Patient Search Results" width="450">
                                        <webuijsf:tableRowGroup binding="#{Page2.tableRowGroup1}" id="tableRowGroup1" rows="10"
                                            selected="#{Page2.selectedState}" sourceData="#{Page2.patientSearchDataList}" sourceVar="currentRow">
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn7}" headerText="Patient Select" id="tableColumn7"
                                                onClick="setTimeout(function(){document.getElementById('form1:subjectDiscoveryTab:patientSearchTab:layoutPanel1:table1').initAllRows()}, 0);" selectId="radioButton1">
                                                <!--<webuijsf:checkbox id="checkbox1" selected="#{Page2.selected}" selectedValue="#{Page2.selectedValue}"/>-->
                                                <webuijsf:radioButton binding="#{Page2.radioButton1}" id="radioButton1" label="" name="patientSelectGroup"
                                                    selected="#{Page2.selected}" selectedValue="#{Page2.selectedValue}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn1}" headerText="Last Name" id="tableColumn1">
                                                <webuijsf:staticText id="staticText1" text="#{currentRow.value['lastName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn2}" headerText="First Name" id="tableColumn2">
                                                <webuijsf:staticText id="staticText2" text="#{currentRow.value['firstName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn3}" headerText="Patient Id" id="tableColumn3">
                                                <webuijsf:staticText id="staticText3" text="#{currentRow.value['patientId']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn4}" headerText="SSN" id="tableColumn4">
                                                <webuijsf:staticText id="staticText4" text="#{currentRow.value['ssn']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn5}" headerText="DOB" id="tableColumn5">
                                                <webuijsf:staticText id="staticText5" text="#{currentRow.value['dob']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{Page2.tableColumn6}" headerText="Gender" id="tableColumn6">
                                                <webuijsf:staticText id="staticText6" text="#{currentRow.value['gender']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                        <f:facet name="actionsBottom">
                                            <webuijsf:button actionExpression="#{Page2.subjectDiscoveryButton_action}" binding="#{Page2.subjectDiscoveryButton}"
                                                id="subjectDiscoveryButton"
                                                style="font-family: 'Times New Roman',Times,serif; font-size: 18px; left: 120px; top: 240px; position: absolute" text="Perform Subject Discovery"/>
                                        </f:facet>
                                    </webuijsf:table>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab binding="#{Page2.subjectDiscoveryTab2}" disabled="true" id="subjectDiscoveryTab2"
                                style="color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Subject Discovery">
                                <webuijsf:panelLayout id="layoutPanel2" style="height: 534px; position: relative; width: 100%; -rave-layout: grid"/>
                            </webuijsf:tab>
                            <webuijsf:tab disabled="true" id="documentDiscoveryTab"
                                style="color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Document Discovery">
                                <webuijsf:panelLayout id="layoutPanel3" style="height: 582px; position: relative; width: 100%; -rave-layout: grid"/>
                            </webuijsf:tab>
                        </webuijsf:tabSet>
                        <webuijsf:staticText binding="#{Page2.patientInfo}" id="patientInfo" style="color: blue; font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 0px; top: 120px; position: absolute"/>
                        <webuijsf:button actionExpression="#{Page2.logOutButton_action}" id="logOutButton"
                            style="font-family: 'Times New Roman','Times',serif; font-size: 14px; height: 24px; left: 623px; top: 120px; position: absolute" text="Log Out"/>
                        <webuijsf:label id="UCLabel1"
                            style="color: gray; font-family: 'Arial','Helvetica',sans-serif; font-size: 30px; left: 360px; top: 48px; position: absolute" text="UNIVERSAL CLIENT"/>
                        <webuijsf:staticText binding="#{Page2.agencyLogo1}" id="agencyLogo1" style="color: gray; font-family: 'Arial','Helvetica',sans-serif; font-size: 30px; left: 360px; top: 0px; position: absolute"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
