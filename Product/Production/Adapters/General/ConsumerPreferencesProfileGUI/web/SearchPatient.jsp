<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : SearchPatient
    Created on : Sep 10, 2009, 11:08:16 PM
    Author     : patlollav
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page id="searchPatient">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="searchPatientForm">
                        <webuijsf:staticText id="staticText1"
                            style="font-family: 'Times New Roman','Times',serif; font-size: 18px; font-weight: bold; left: 288px; top: 24px; position: absolute" text="Consumer Preferences Profile"/>
                        <webuijsf:tabSet binding="#{SearchPatient.preferencesTab}" id="preferencesTab" selected="searchPatientTab" style="height: 526px; left: 72px; top: 96px; position: absolute; width: 766px">
                            <webuijsf:tab actionExpression="#{SearchPatient.searchPatientTab_action}" binding="#{SearchPatient.searchPatientTab}"
                                id="searchPatientTab" style="font-family: 'Times New Roman',Times,serif; font-size: 12px" text="Search Patient">
                                <webuijsf:panelLayout id="patientSearchLayoutPanel" style="height: 537px; position: relative; width: 647px; -rave-layout: grid">
                                    <webuijsf:staticText id="staticText2"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; font-weight: bolder; left: 24px; top: 48px; position: absolute" text="Enter the following Patient Search Criteria:"/>
                                    <webuijsf:label id="firstNameLbl"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; height: 24px; left: 24px; top: 120px; position: absolute" text="First Name: "/>
                                    <webuijsf:label id="lastNameLbl"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 96px; position: absolute" text="Last Name:"/>
                                    <webuijsf:label id="identifierLbl"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 144px; position: absolute" text="Identifier:"/>
                                    <webuijsf:textField binding="#{SearchPatient.firstName}" columns="30" id="firstName" style="left: 96px; top: 120px; position: absolute"/>
                                    <webuijsf:textField binding="#{SearchPatient.lastName}" columns="30" id="lastName" style="left: 96px; top: 96px; position: absolute"/>
                                    <webuijsf:textField binding="#{SearchPatient.identifier}" columns="30" id="identifier" style="left: 96px; top: 144px; position: absolute"/>
                                    <webuijsf:button id="resetButton" reset="true"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 95px; top: 192px; position: absolute; width: 72px" text="Reset"/>
                                    <webuijsf:button actionExpression="#{SearchPatient.searchPatientButton_action}" id="searchPatientButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 191px; top: 192px; position: absolute; width: 72px" text="Search"/>
                                    <webuijsf:table augmentTitle="false" id="searchPatientResults"
                                        style="left: 48px; top: 240px; position: absolute; width: 600px" title="Patient Search Results" width="600">
                                        <webuijsf:tableRowGroup id="patientSearchResults" sourceData="#{UserSession.searchResults}" sourceVar="patient">
                                            <webuijsf:tableColumn headerText="Patient Id" id="patientID">
                                                <webuijsf:hyperlink actionExpression="#{SearchPatient.displayConsumerPreferences}"
                                                    binding="#{SearchPatient.selectedPatientID}" id="patientIDLink"
                                                    style="color: blue; text-decoration: underline" text="#{patient.value['patientID']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="First Name" id="firstName">
                                                <webuijsf:staticText id="staticText1" text="#{patient.value['firstName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Last Name" id="lastName">
                                                <webuijsf:staticText id="staticText1" text="#{patient.value['lastName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Organization ID" id="organizationID">
                                                <webuijsf:staticText id="staticText2" text="#{patient.value['organizationID']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{SearchPatient.patientPreferencesTab_action}" binding="#{SearchPatient.patientPreferencesTab}"
                                id="patientPreferencesTab" text="Consumer Preferences Profile">
                                <webuijsf:panelLayout id="preferencesPanel" style="height: 537px; position: relative; width: 647px; -rave-layout: grid">
                                    <webuijsf:label id="patientNameLbl"
                                        style="font-family: 'Times New Roman',Times,serif; font-size: 12px; left: 24px; top: 24px; position: absolute" text="Patient:"/>
                                    <webuijsf:staticText binding="#{SearchPatient.patientName}" id="patientName" style="left: 72px; top: 24px; position: absolute"/>
                                    <webuijsf:radioButtonGroup binding="#{SearchPatient.optIn}" id="optIn" items="#{SearchPatient.optInDefaultOptions.options}"
                                        onClick="this.form.submit()"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 48px; top: 72px; position: absolute" valueChangeListenerExpression="#{SearchPatient.optIn_processValueChange}"/>
                                    <webuijsf:table augmentTitle="false" binding="#{SearchPatient.preferencesTable}" id="preferencesTable"
                                        style="left: 24px; top: 240px; position: absolute; width: 624px" title="Fine Grained Policy Preferences" visible="false" width="624">
                                        <webuijsf:tableRowGroup id="preferencesGroup"
                                            sourceData="#{UserSession.patient.patientPreferences.fineGrainedPolicyCriteria}" sourceVar="fineGrainedPolicyCriterion">
                                            <webuijsf:tableColumn headerText="Document Type" id="documentTypeCol">
                                                <webuijsf:hyperlink actionExpression="#{SearchPatient.displayFineGrainedPreferences}" id="documentTypeLink"
                                                    style="color: blue; text-decoration: underline" text="#{fineGrainedPolicyCriterion.value['documentTypeCodeDesc']}">
                                                    <f:param binding="#{SearchPatient.userSelectedPolicyOID}" value="#{fineGrainedPolicyCriterion.value['policyOID']}"/>
                                                </webuijsf:hyperlink>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Permit/Deny" id="permitORDenyCol">
                                                <webuijsf:staticText id="staticText3" text="#{fineGrainedPolicyCriterion.value['permit']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="User Role" id="userRoleCol">
                                                <webuijsf:staticText id="staticText5" text="#{fineGrainedPolicyCriterion.value['userRoleDesc']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Purpose of Use" id="purposeOfUseCol">
                                                <webuijsf:staticText id="staticText5" text="#{fineGrainedPolicyCriterion.value['purposeOfUseDesc']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Confidentiality Code" id="confidentialityCodeCol">
                                                <webuijsf:staticText id="staticText5" text="#{fineGrainedPolicyCriterion.value['confidentialityCodeDesc']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                    <webuijsf:panelLayout binding="#{SearchPatient.fineGrainedPolicyPrefPanel}" id="fineGrainedPolicyPrefPanel"
                                        style="height: 190px; left: 288px; top: 24px; position: absolute; width: 310px" visible="false">
                                        <webuijsf:label id="fineGrainedPolicyLbl"
                                            style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 25px; top: 1px; position: absolute" text="Add/Edit Fine Grained Policy Preferences:"/>
                                        <webuijsf:label id="permitLbl"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 24px; position: absolute" text="Permission:"/>
                                        <h:selectOneRadio binding="#{SearchPatient.permission}" id="permission" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; font-weight: normal; height: 23px; left: 120px; top: 24px; position: absolute">
                                            <f:selectItems id="permissionListSelectItems" value="#{SearchPatient.permissionListDefaultItems}"/>
                                        </h:selectOneRadio>
                                        <webuijsf:label id="documentTypeLbl"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 48px; position: absolute" text="Document Type:"/>
                                        <webuijsf:dropDown binding="#{SearchPatient.documentType}" id="documentType"
                                            items="#{SearchPatient.documentTypeDefaultOptions.options}" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 144px; top: 48px; position: absolute"/>
                                        <webuijsf:label id="label1"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 72px; position: absolute" text="User Role:"/>
                                        <webuijsf:label id="label2"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 96px; position: absolute" text="Purpose of Use:"/>
                                        <webuijsf:label id="label3"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 24px; top: 120px; position: absolute" text="ConfidentialityCode:"/>
                                        <webuijsf:dropDown binding="#{SearchPatient.userRole}" id="userRole"
                                            items="#{SearchPatient.userRoleDefaultOptions.options}" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 144px; top: 72px; position: absolute"/>
                                        <webuijsf:dropDown binding="#{SearchPatient.purposeOfUse}" id="purposeOfUse"
                                            items="#{SearchPatient.purposeOfUseDefaultOptions.options}" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 144px; top: 96px; position: absolute"/>
                                        <webuijsf:dropDown binding="#{SearchPatient.confidentialityCode}" id="confidentialityCode"
                                            items="#{SearchPatient.confidentialityCodeDefaultOptions.options}" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 144px; top: 120px; position: absolute"/>
                                        <webuijsf:button actionExpression="#{SearchPatient.addPreferences}" id="addPreferences"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 23px; top: 168px; position: absolute" text="Add"/>
                                        <webuijsf:button actionExpression="#{SearchPatient.updatePreferences}" id="updatePreferences"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 95px; top: 168px; position: absolute" text="Update Selected"/>
                                        <webuijsf:button actionExpression="#{SearchPatient.deletePreferences}" id="deletePreference"
                                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 215px; top: 168px; position: absolute" text="Delete Selected"/>
                                    </webuijsf:panelLayout>
                                    <webuijsf:button actionExpression="#{SearchPatient.savePreferences}" binding="#{SearchPatient.savePreferences}"
                                        id="savePreferences"
                                        style="font-family: 'Times New Roman',Times,serif; font-size: 12px; left: 71px; top: 192px; position: absolute" text="Save Preferences"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                        </webuijsf:tabSet>
                        <webuijsf:staticText binding="#{SearchPatient.errorMessages}" id="errorMessages" style="color: red; font-family: 'Times New Roman',Times,serif; font-size: 12px; left: 72px; top: 72px; position: absolute"/>
                        <webuijsf:button actionExpression="#{SearchPatient.logOut_action}" id="logOut"
                            style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 767px; top: 72px; position: absolute" text="Log Off"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
