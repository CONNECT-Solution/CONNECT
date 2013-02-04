<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page2
    Created on : Aug 7, 2009, 6:03:45 PM
    Updated on : Jun 9, 2011, 12:37:15 PM
    Author     : vvickers, rwelch, richard.ettema
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                    <webuijsf:script>
                        if((navigator.userAgent.match(/iPhone/i)) || (navigator.userAgent.match(/iPod/i))) {
                            document.write('<link id="link2" rel="stylesheet" type="text/css" href="/CONNECTUniversalClientGUI/resources/stylesheet.css" />');
                            document.write('<meta content="width=320" name="viewport"/>');
                        }
                    </webuijsf:script>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <webuijsf:image height="100" id="connectImage" style="left: 0px; top: 0px; position: absolute" url="/resources/connect.GIF" width="312"/>
                        <webuijsf:tabSet binding="#{Page2.clientTabSet}" id="clientTabSet" selected="patientSearchTab" style="left: 0px; top: 144px; position: absolute; width: 694px">
                            <webuijsf:tab actionExpression="#{Page2.patientSearchTab_action}" binding="#{Page2.patientSearchTab}" id="patientSearchTab"
                                style="font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Patient Search">
                                <webuijsf:panelLayout id="patientSearchLayoutPanel" style="height: 537px; position: relative; width: 100%; -rave-layout: grid">
                                    <webuijsf:label id="searchInstruct"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 24px; position: absolute" text="Enter search criteria to search for a patient in your own MPI:"/>
                                    <webuijsf:label for="lastNameField" id="lastNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 96px; position: absolute" text="Last Name:"/>
                                    <webuijsf:textField binding="#{Page2.lastNameField}" id="lastNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 120px; top: 96px; position: absolute"/>
                                    <webuijsf:label id="firstNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 120px; position: absolute" text="First Name:"/>
                                    <webuijsf:textField binding="#{Page2.firstNameField}" id="firstNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 120px; top: 120px; position: absolute"/>
                                    <webuijsf:button actionExpression="#{Page2.patientSearchButton_action}" id="patientSearchButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 18px; left: 311px; top: 120px; position: absolute; width: 100px" text="Search"/>
                                    <webuijsf:label id="searchText"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 50px; position: absolute" text="The system will list your patients, and then you will be able to query for that patient on other systems"/>
                                    <webuijsf:label id="displayPatientSearch"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 150px; position: absolute" text="Click on the patient ID to see detailed patient information from your patient correlation table"/>    
                                    <webuijsf:table augmentTitle="false" id="patientSearchResultTable"
                                        style="left: 24px; top: 174px; position: absolute; width: 500px" title="Patient Search Results" width="500">
                                        <webuijsf:tableRowGroup id="patientSearchResultTableRowGroup" rows="4" sourceData="#{Page2.patientSearchDataList}" sourceVar="currentRow">
                                            <webuijsf:tableColumn headerText="Patient Id" id="patientSearchPatientIdColm">
                                               <webuijsf:hyperlink actionExpression="#{Page2.patientSelectIdLink_action}" 
                                                    binding="#{Page2.patientSelectIdLink}" id="patientSelectIdLink"  
                                                    style="color: blue; text-decoration: underline" text="#{currentRow.value['patientId']}"> 
                                                    <f:param name="valueAssigningAuthority" value="#{currentRow.value['assigningAuthorityID']}"/>
                                                </webuijsf:hyperlink> 
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="AA Id" id="patientSearchAaidColm">
                                                <webuijsf:staticText id="staticText0" text="#{currentRow.value['assigningAuthorityID']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Last Name" id="patientSearchLastNameColm">
                                                <webuijsf:staticText id="staticText1" text="#{currentRow.value['lastName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="First Name" id="patientSearchFirstNameColm">
                                                <webuijsf:staticText id="staticText2" text="#{currentRow.value['firstName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="SSN" id="patientSearchSSNColm" noWrap="true">
                                                <webuijsf:staticText id="staticText4" text="#{currentRow.value['ssn']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="DOB" id="patioentSearchDOBColm">
                                                <webuijsf:staticText id="staticText5" text="#{currentRow.value['dob']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Gender" id="patientSearchGenderColm">
                                                <webuijsf:staticText id="staticText6" text="#{currentRow.value['gender']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{Page2.subjectDiscoveryTab_action}" binding="#{Page2.subjectDiscoveryTab}" disabled="true"
                                id="subjectDiscoveryTab" style="color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Patient Correlations">
                                <webuijsf:panelLayout id="subjectDiscoveryLayoutPanel" style="height: 534px; position: relative; width: 100%; -rave-layout: grid">
                                    <webuijsf:staticText binding="#{Page2.subjectDiscoveryResultsInfo}" id="subjectDiscoveryResultsInfo" style="color: black; font-family: 'Times New Roman','Times',serif; font-size: 14px; font-weight: bold; left: 24px; top: 24px; position: absolute"/>
                                    <webuijsf:button actionExpression="#{Page2.broadcastSubjectDiscoveryButton_action}" id="broadcastSubjectDiscoveryButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 23px; top: 72px; position: absolute" text="Discover Patients"/>
                                    <webuijsf:label id="searchPatientDiscovery"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 95px; position: absolute" text="Click to send a patient discovery message to systems in your UDDI (and/or uddiConnectionInfo.xml file)."/>   
                                    <webuijsf:table augmentTitle="false" id="subjectDiscoveryCoorelationTable" paginateButton="true" paginationControls="true"
                                        style="left: 24px; top: 144px; position: absolute; width: 400px" title="Patient Correlations" width="400">
                                        <webuijsf:tableRowGroup id="patientCorrTableRowGoup" rows="10" sourceData="#{Page2.patientCorrelationList}" sourceVar="currentRow">
                                            <webuijsf:tableColumn headerText="Assigning Authority" id="patientCoorAssignAuthColm">
                                                <webuijsf:staticText id="correlatedAssignAuthority" text="#{currentRow.value['assignAuthorityId']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Patient Id" id="patientCorrIdColm">
                                                <webuijsf:staticText id="correlatedPatientId" text="#{currentRow.value['remotePatientId']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Organization" id="patientCorrOrgColm">
                                                <webuijsf:staticText id="correlatedOrganization" text="#{currentRow.value['organizationName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Organization Id" id="patientCorrOrgIdColm">
                                                <webuijsf:staticText id="correlatedOrganizationId" text="#{currentRow.value['organizationId']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                    <webuijsf:staticText binding="#{Page2.broadcastInfo}" id="broadcastInfo" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; font-weight: bold; left: 216px; top: 114px; position: absolute"/>
                                    <webuijsf:staticText binding="#{Page2.broadcastInfo2}" id="broadcastInfo2" style="color: red; font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 216px; top: 120px; position: absolute"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{Page2.documentTab_action}" binding="#{Page2.documentTab}" disabled="true" id="documentTab"
                                style="color: gray; font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Documents">
                                <webuijsf:panelLayout id="layoutPanel3" style="height: 582px; position: relative; width: 100%; -rave-layout: grid">
                                <webuijsf:label id="docQueryLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 48px; position: absolute" text="Click below to find Documents for Patient"/>
                                    <webuijsf:button actionExpression="#{Page2.getDocQueryResults}" id="docQueryButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 263px; top: 96px; position: absolute; width: 120px" text="Document Query"/>
                                    <webuijsf:table augmentTitle="false" id="docQueryResults" style="left: 48px; top: 130px; position: absolute; width: 600px"
                                        title="Document Search Results" width="600">
                                        <webuijsf:tableRowGroup id="docQueryResultsGroup" rows="4" sourceData="#{DocumentQueryResults.documents}" sourceVar="document">
                                            <webuijsf:tableColumn headerText="Document Id" id="documentID">
                                                <webuijsf:hyperlink actionExpression="#{Page2.displayDocument}" binding="#{Page2.selectedDocumentID}"
                                                id="documentIdLink" style="color: blue; text-decoration: underline" text="#{document.value['documentID']}" target="_new"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Creation Date" id="creationDate">
                                                <webuijsf:staticText id="staticText1" text="#{document.value['creationDate']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Title" id="title">
                                                <webuijsf:staticText id="staticText1" text="#{document.value['title']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Document Type" id="documentType">
                                                <webuijsf:staticText id="staticText2" text="#{document.value['documentType']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Institution" id="institution" noWrap="true">
                                                <webuijsf:staticText id="staticText4" text="#{document.value['institution']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                    <webuijsf:staticText binding="#{Page2.errorMessage}" id="patientInfo" style="color: blue; font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 168px; top: 24px; position: absolute"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                        </webuijsf:tabSet>
                        <webuijsf:staticText binding="#{Page2.patientInfo}" id="patientInfo" style="color: blue; font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 0px; top: 120px; position: absolute"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
