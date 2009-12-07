<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : CPPMain
    Created on : Oct 3, 2009, 8:06:29 PM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{CPPMain.page1}" id="page1">
            <webuijsf:html binding="#{CPPMain.html1}" id="html1">
                <webuijsf:head binding="#{CPPMain.head1}" id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body binding="#{CPPMain.body1}" id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form binding="#{CPPMain.form1}" id="form1">
                        <div style="left: 0px; top: 0px; position: absolute">
                            <jsp:directive.include file="CPPHeader.jspf"/>
                        </div>
                        <div style="left: 0px; top: 83px; position: absolute">
                            <jsp:directive.include file="CPPNavigator.jspf"/>
                        </div>
                        <webuijsf:panelLayout binding="#{CPPMain.layoutPanel1}" id="layoutPanel1" style="border: 1px solid gray; height: 600px; left: 164px; top: 83px; position: absolute; width: 860px">
                            <webuijsf:label binding="#{CPPMain.label1}" id="label1" style="left: 24px; top: 24px; position: absolute" text="Lastname:"/>
                            <webuijsf:textField binding="#{CPPMain.lastNameFLD}" columns="26" id="lastNameFLD" style="left: 96px; top: 24px; position: absolute"/>
                            <webuijsf:label binding="#{CPPMain.label2}" id="label2" style="left: 288px; top: 24px; position: absolute" text="Firstname:"/>
                            <webuijsf:textField binding="#{CPPMain.firstNameFLD}" id="firstNameFLD" style="left: 360px; top: 24px; position: absolute"/>
                            <webuijsf:label binding="#{CPPMain.label4}" id="label4" style="left: 504px; top: 24px; position: absolute" text="Identifier:"/>
                            <webuijsf:textField binding="#{CPPMain.idFLD}" columns="19" id="idFLD" style="left: 570px; top: 24px; position: absolute"/>
                            <webuijsf:button actionExpression="#{CPPMain.searchBTN_action}" binding="#{CPPMain.searchBTN}" id="searchBTN"
                                style="height: 19px; left: 710px; top: 24px; position: absolute; width: 120px" text="Search"/>
                            <webuijsf:table augmentTitle="false" binding="#{CPPMain.table1}" footerText="Click on Patient row to select and set context."
                                id="table1" lite="true" paginationControls="true" style="left: 0px; top: 72px; position: absolute; width: 860px" width="860">
                                <webuijsf:tableRowGroup binding="#{CPPMain.tableRowGroup1}" emptyDataMsg="No records found." id="tableRowGroup1" rows="10"
                                    sourceData="#{SessionBean1.patients}" sourceVar="currentRow">
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn1}" headerText="PatientID" id="tableColumn1">
                                        <webuijsf:hyperlink actionExpression="#{CPPMain.patientIDLink_action}" binding="#{CPPMain.patientIDLink}"
                                            id="patientIDLink" immediate="true" text="#{CPPMain.searchPatientId}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn2}" headerText="Last Name" id="tableColumn2">
                                        <webuijsf:staticText binding="#{CPPMain.staticText2}" id="staticText2" text="#{currentRow.value['lastName']}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn3}" headerText="First Name" id="tableColumn3" sort="firstName">
                                        <webuijsf:staticText binding="#{CPPMain.staticText3}" id="staticText3" text="#{currentRow.value['firstName']}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn4}" headerText="DoB" id="tableColumn4" sort="dateOfBirth">
                                        <webuijsf:staticText binding="#{CPPMain.staticText4}" id="staticText4" text="#{currentRow.value['dateOfBirth']}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn5}" headerText="Gender" id="tableColumn5" sort="gender">
                                        <webuijsf:staticText binding="#{CPPMain.staticText5}" id="staticText5" text="#{currentRow.value['gender']}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn8}" headerText="Street Address" id="tableColumn8">
                                        <webuijsf:staticText binding="#{CPPMain.streetAddressCol}" id="streetAddressCol" text="#{CPPMain.streetAddress}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn7}" headerText="Opted In" id="tableColumn7">
                                        <webuijsf:checkbox binding="#{CPPMain.checkbox1}" disabled="true" id="checkbox1" readOnly="true" selected="#{currentRow.value['optedIn']}"/>
                                    </webuijsf:tableColumn>
                                    <webuijsf:tableColumn binding="#{CPPMain.tableColumn6}" headerText="identifiers" id="tableColumn6" rendered="false"
                                        sort="identifiers" visible="false">
                                        <webuijsf:staticText binding="#{CPPMain.staticText1}" id="staticText1" text="#{currentRow.value['identifiers']}"/>
                                    </webuijsf:tableColumn>
                                </webuijsf:tableRowGroup>
                            </webuijsf:table>
                            <webuijsf:label binding="#{CPPMain.label5}" id="label5"
                                style="font-family: Arial,Helvetica,sans-serif; font-size: 8px; left: 576px; top: 45px; position: absolute" text="Patient Unit Number"/>
                        </webuijsf:panelLayout>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
