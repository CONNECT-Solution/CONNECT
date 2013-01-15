<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page2
    Created on : Aug 7, 2009, 6:03:45 PM
    Updated on : Jun 9, 2011, 12:37:15 PM
    Author     : vvickers, rwelch, richard.ettema
/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
                        <webuijsf:image height="80" id="connectImage" style="left: 0px; top: 0px; position: absolute" url="/resources/connect.GIF" width="270"/>
                        <webuijsf:tabSet binding="#{Page2.clientTabSet}" id="clientTabSet" style="left: 0px; top: 194px; position: absolute; width: 694px">
                            <webuijsf:tab actionExpression="#{Page2.patientSearchTab_action}" binding="#{Page2.patientSearchTab}" id="patientSearchTab"
                                style="font-family: 'Times New Roman',Times,serif; font-size: 14px" text="Patient Search">
                                <webuijsf:panelLayout id="patientSearchLayoutPanel" style="height: 537px; position: relative; width: 100%; -rave-layout: grid">
                                    <webuijsf:label id="searchInstruct"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 24px; top: 24px; position: absolute" text="Enter the following Patient Search Criteria:"/>
                                    <webuijsf:label for="lastNameField" id="lastNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 72px; position: absolute" text="Last Name:"/>
                                    <webuijsf:textField binding="#{Page2.lastNameField}" id="lastNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 140px; top: 72px; position: absolute"/>
                                    <webuijsf:label id="firstNameLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 96px; position: absolute" text="First Name:"/>
                                    <webuijsf:textField binding="#{Page2.firstNameField}" id="firstNameField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 140px; top: 96px; position: absolute"/>
                                    <webuijsf:label id="middleInitialLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 120px; position: absolute" text="Middle Initial:"/>
                                    <webuijsf:textField binding="#{Page2.middleInitialField}" maxLength= "1" id="middleInitialField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 140px; top: 120px; position: absolute"/>
                                    <webuijsf:label id="dateOfBirthLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 48px; top: 144px; position: absolute" text="Date of Birth:"/>
                                    <webuijsf:calendar binding="#{Page2.searchDateOfBirth}" id="dateOfBirthField" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 140px; top: 144px; position: absolute"/>
                                    <webuijsf:button actionExpression="#{Page2.patientSearchButton_action}" id="patientSearchButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 18px; left: 326px; top: 144px; position: absolute; width: 72px" text="Search"/>
                                    <webuijsf:table augmentTitle="false" id="patientSearchResultTable"
                                        style="left: 24px; top: 234px; position: absolute; width: 450px" title="Patient Search Results" width="450">
                                        <webuijsf:tableRowGroup id="patientSearchResultTableRowGroup" rows="4" sourceData="#{Page2.patientSearchDataList}" sourceVar="currentRow">
                                            <webuijsf:tableColumn headerText="Patient Id" id="patientSearchPatientIdColm">
                                                <webuijsf:hyperlink actionExpression="#{Page2.patientSelectIdLink_action}"
                                                    binding="#{Page2.patientSelectIdLink}" id="patientSelectIdLink"
                                                    style="color: blue; text-decoration: underline" text="#{currentRow.value['patientId']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Last Name" id="patientSearchLastNameColm">
                                                <webuijsf:staticText id="staticText1" text="#{currentRow.value['lastName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="First Name" id="patientSearchFirstNameColm">
                                                <webuijsf:staticText id="staticText2" text="#{currentRow.value['firstName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="SSN" id="patientSearchSSNColm" noWrap="true">
                                                <webuijsf:staticText id="staticText4" text="#{currentRow.value['displaySsn']}"/>
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
                            <webuijsf:tab  actionExpression="#{Page2.subjectDiscoveryTab_action}" binding="#{Page2.subjectDiscoveryTab}" id="subjectDiscoveryTab"  text="Patient Discovery">
                                <webuijsf:panelLayout id="subjectDiscoveryLayoutPanel" style="height: 534px; position: relative; width: 100%; -rave-layout: grid">
                                    <webuijsf:staticText binding="#{Page2.subjectDiscoveryResultsInfo}" id="subjectDiscoveryResultsInfo" style="color: black; font-family: 'Times New Roman','Times',serif; font-size: 14px; font-weight: bold; left: 24px; top: 130px; position: absolute"/>
                                    <!-- Listbox to allow specific Patient Discovery Communities to be targeted -->
                                    <webuijsf:label id="pdCommsLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 13px; left: 23px; top: 30px; position: absolute;" text="Select Target Communities for PD Broadcast:"/>
                                    <h:selectManyListbox size="3" value="#{Page2.comboVal}" id="targetLBox" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 320px; top: 15px; position: absolute">
                                        <f:selectItems value="#{Page2.communitiesFromRegistry}" />
                                    </h:selectManyListbox>
                                    <webuijsf:button actionExpression="#{Page2.broadcastSubjectDiscoveryButton_action}" id="broadcastSubjectDiscoveryButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 13px; font-weight: bold; left: 23px; top: 72px; position: absolute; width: 215px" text="Click to Discover New Correlation"/>
                                    <webuijsf:table augmentTitle="false" id="subjectDiscoveryCoorelationTable" paginateButton="true" paginationControls="true"
                                        style="left: 24px; top: 155px; position: absolute; width: 400px" title="Patient Correlations" width="400">
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
                                    <!--<webuijsf:staticText binding="#{Page2.broadcastInfo}" id="broadcastInfo" style="font-family: 'Times New Roman','Times',serif; font-size: 13px; font-weight: bold; left: 216px; top: 75px; position: absolute"/>-->
                                    <webuijsf:staticText binding="#{Page2.broadcastInfo2}" id="broadcastInfo2" style="color: red; font-family: 'Times New Roman',Times,serif; font-size: 13px; left: 216px; top: 96px; position: absolute"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{Page2.documentTab_action}" binding="#{Page2.documentTab}" id="documentTab" text="Documents">
                                <webuijsf:panelLayout id="layoutPanel3" style="height: 582px; position: relative; width: 100%; -rave-layout: grid">
                                    <webuijsf:label id="documentSearchLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 168px; top: 8px; position: absolute" text="Enter the following Document Search Criteria"/>
                                    <!-- Listbox to allow specific Doc Query Communities to be targeted -->
                                    <webuijsf:label id="docQueryCommsLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 13px; left: 48px; top: 50px; position: absolute;" text="Select Target Communities for Doc Query Request:"/>
                                    <webuijsf:label id="docQueryCommsCtrlLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 48px; top: 70px; position: absolute;color:gray;" text="(Hold down Ctrl to select more than one)"/>
                                    <h:selectManyListbox size="3" value="#{Page2.docQueryComboVal}" id="queryTargetLBox" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 340px; top: 45px; position: absolute">
                                        <f:selectItems value="#{Page2.communitiesFromRegistry}" />
                                    </h:selectManyListbox>
                                    <!-- Listbox to allow specific Doc Query Document Types to be included in query request -->
                                    <webuijsf:label id="docQueryDocTypesLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 13px; left: 48px; top: 120px; position: absolute;" text="Select Document Types:"/>
                                    <webuijsf:label id="docQueryDocTypesCtrlLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 48px; top: 140px; position: absolute;color:gray;" text="(Hold down Ctrl to select more than one)"/>
                                    <h:selectManyListbox size="3" value="#{Page2.docQueryDocTypesComboVal}" id="queryDocTypesLBox" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 270px; top: 115px; position: absolute">
                                        <f:selectItems value="#{Page2.docTypesFromRegistry}" />
                                    </h:selectManyListbox>
                                    <webuijsf:label id="serviceTimeDateLabel"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 13px; left: 48px; top: 186px; position: absolute" text="Service Time Date Range:"/>
                                    <webuijsf:calendar binding="#{Page2.serviceTimeFromDate}" id="serviceTimeFromDate" style="font-family: 'Times New Roman','Times',serif; font-size: 12px; left: 198px; top: 186px; position: absolute"/>
                                    <webuijsf:calendar binding="#{Page2.serviceTimeToDate}" id="calendar1" style="font-family: 'Times New Roman','Times',serif; font-size: 12px;left: 404px; top: 186px; position: absolute"/>
                                    <webuijsf:label id="label1" style="left: 192px; top: 224px; position: absolute; vertical-align: top" text="Earliest Date"/>
                                    <webuijsf:label id="label2" style="left: 408px; top: 224px; position: absolute; vertical-align: top" text="Most Recent Date"/>
                                    <webuijsf:staticText binding="#{Page2.errorMessage}" id="patientInfo" style="color: red; font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 162px; top: 97px; position: absolute"/>
                                    <webuijsf:button actionExpression="#{Page2.getDocQueryResults}" id="docQueryButton"
                                        style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 263px; top: 248px; position: absolute; width: 120px" text="Document Query"/>
                                    <webuijsf:table augmentTitle="false" id="docQueryResults" style="left: 48px; top: 291px; position: absolute; width: 600px"
                                        title="Document Search Results" width="600">
                                        <webuijsf:tableRowGroup emptyDataMsg="#{Page2.emptyDataMsg}" id="docQueryResultsGroup" rows="4" sourceData="#{DocumentQueryResults.documents}" sourceVar="document">
                                            <webuijsf:tableColumn headerText="Dcoument Id" id="documentID"> 
                                                <webuijsf:hyperlink url="/faces/Page3.jsp?docid=#{document.value['documentID']}" binding="#{Page2.selectedDocumentID}"
                                                id="documentIdLink" style="color: blue; text-decoration: underline" text="#{document.value['documentID']}" target="_blank"/>
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
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                        </webuijsf:tabSet>
                        <webuijsf:staticText binding="#{Page2.patientInfo}" id="patientInfo" style="color: blue; font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 0px; top: 160px; position: absolute"/>
                        <webuijsf:staticText binding="#{Page2.errorMessage}" id="errorMessage" style="color: red; font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 0px; top: 176px; position: absolute"/>
                        <webuijsf:button actionExpression="#{Page2.logOutButton_action}" id="logOutButton"
                            style="font-family: 'Times New Roman','Times',serif; font-size: 14px; height: 24px; left: 610px; top: 202px; position: absolute; width: 72px" text="Log Out"/>
                        <webuijsf:hyperlink url="../resources/Universal_Client_Users_Manual.pdf" id="usersManualLink" style="outline-style:none;font-weight:bold; color: black; text-decoration: underline; top:207px; position:absolute; left: 400px; font-size: 12px;" text=" Universal Client User's Manual " onMouseOver="this.style.color='#444444';" onMouseOut="this.style.color='black';" target="_blank"/>
                        <webuijsf:label id="UCLabel1"
                            style="color: black; font-family: 'Arial','Helvetica',sans-serif; font-size: 30px; left: 200px; top: 100px; position: absolute" text="UNIVERSAL CLIENT"/>
                        <!--<webuijsf:staticText binding="#{Page2.agencyLogo}" id="agencyLogo" style="color: gray; font-family: 'Arial','Helvetica',sans-serif; font-size: 30px; left: 360px; top: 0px; position: absolute"/>-->
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
