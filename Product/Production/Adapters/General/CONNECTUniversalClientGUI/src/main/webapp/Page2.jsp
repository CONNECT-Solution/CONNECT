<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page2
    Created on : Aug 7, 2009, 6:03:45 PM
    Updated on : Jun 9, 2011, 12:37:15 PM
    Author     : vvickers, rwelch, richard.ettema
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core"
   xmlns:h="http://java.sun.com/jsf/html"
   xmlns:jsp="http://java.sun.com/JSP/Page"
   xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
   <jsp:directive.page contentType="text/html;charset=UTF-8"
      pageEncoding="UTF-8" />
   <webuijsf:page id="page1">
      <webuijsf:html id="html1">
      <webuijsf:head id="head1">
         <webuijsf:link id="link1" url="/resources/css/bootstrap.css" />
         <webuijsf:link id="link2"
            url="/resources/css/bootstrap-responsive.css" />

      </webuijsf:head>
      <webuijsf:body id="body1">
         <div class="container">
            <webuijsf:image height="100" id="connectImage"
               url="/resources/connect.GIF" width="312" />
               
            <webuijsf:panelLayout>
               <webuijsf:panelGroup
                  rendered="#{not empty Page2.patientInfo.text}">
                  <div class="alert alert-info">
                     <a class="close" data-dismiss="alert">x</a>
                     <webuijsf:staticText binding="#{Page2.patientInfo}"
                        id="patientInfo" />
                  </div>
               </webuijsf:panelGroup>

               <webuijsf:panelGroup
                  rendered="#{not empty Page2.errorMessage.text}">
                  <div class="alert alert-error">
                     <a class="close" data-dismiss="alert">x</a>
                     <webuijsf:staticText
                        binding="#{Page2.errorMessage}"
                        id="errorMessage" />
                  </div>
               </webuijsf:panelGroup>
            </webuijsf:panelLayout>

            <ul class="nav nav-tabs">
               <li class="active"><a href="#patientSearchTab"
                  data-toggle="tab">Patient Search</a></li>
               <li><a href="#subjectDiscoveryTab" data-toggle="tab">Patient
                     Correlations</a></li>
               <li><a href="#documentTab" data-toggle="tab">Documents</a></li>
            </ul>


            <webuijsf:form id="form1">
               <div id="tabContent" class="tab-content">
                  <div class="tab-pane active" id="patientSearchTab">
                     <webuijsf:panelLayout id="patientSearchLayoutPanel">
                        <fieldset>
                           <span class="help-block"> Enter search
                              criteria to search for a patient in your
                              own MPI: </span>
                           <webuijsf:label for="lastNameField"
                              id="lastNameLabel" text="Last Name:" />
                           <webuijsf:textField
                              binding="#{Page2.lastNameField}"
                              id="lastNameField" />
                           <webuijsf:label id="firstNameLabel"
                              text="First Name:" />
                           <webuijsf:textField
                              binding="#{Page2.firstNameField}"
                              id="firstNameField" />
                        </fieldset>
                        <webuijsf:button styleClass="btn"
                           actionExpression="#{Page2.patientSearchButton_action}"
                           id="patientSearchButton" text="Search" />
                        <webuijsf:label id="searchText"
                           text="The system will list your patients, and then you will be able to query for that patient on other systems" />
                        <webuijsf:label id="displayPatientSearch"
                           text="Click on the patient ID to see detailed patient information from your patient correlation table" />
                        <webuijsf:table augmentTitle="false"
                           id="patientSearchResultTable"
                           title="Patient Search Results"
                           styleClass="table table-hover">
                           <webuijsf:tableRowGroup
                              id="patientSearchResultTableRowGroup"
                              rows="4"
                              sourceData="#{Page2.patientSearchDataList}"
                              sourceVar="currentRow">
                              <webuijsf:tableColumn
                                 headerText="Patient Id"
                                 id="patientSearchPatientIdColm">
                                 <webuijsf:hyperlink
                                    actionExpression="#{Page2.patientSelectIdLink_action}"
                                    binding="#{Page2.patientSelectIdLink}"
                                    id="patientSelectIdLink"
                                    text="#{currentRow.value['patientId']}">
                                    <f:param
                                       name="valueAssigningAuthority"
                                       value="#{currentRow.value['assigningAuthorityID']}" />
                                 </webuijsf:hyperlink>
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn headerText="AA Id"
                                 id="patientSearchAaidColm">
                                 <webuijsf:staticText id="staticText0"
                                    text="#{currentRow.value['assigningAuthorityID']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Last Name"
                                 id="patientSearchLastNameColm">
                                 <webuijsf:staticText id="staticText1"
                                    text="#{currentRow.value['lastName']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="First Name"
                                 id="patientSearchFirstNameColm">
                                 <webuijsf:staticText id="staticText2"
                                    text="#{currentRow.value['firstName']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn headerText="SSN"
                                 id="patientSearchSSNColm" noWrap="true">
                                 <webuijsf:staticText id="staticText4"
                                    text="#{currentRow.value['ssn']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn headerText="DOB"
                                 id="patioentSearchDOBColm">
                                 <webuijsf:staticText id="staticText5"
                                    text="#{currentRow.value['dob']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn headerText="Gender"
                                 id="patientSearchGenderColm">
                                 <webuijsf:staticText id="staticText6"
                                    text="#{currentRow.value['gender']}" />
                              </webuijsf:tableColumn>
                           </webuijsf:tableRowGroup>
                        </webuijsf:table>
                     </webuijsf:panelLayout>
                  </div>
                  <div class="tab-pane" id="subjectDiscoveryTab">
                     <webuijsf:panelLayout
                        id="subjectDiscoveryLayoutPanel">
                        <webuijsf:staticText
                           binding="#{Page2.subjectDiscoveryResultsInfo}"
                           id="subjectDiscoveryResultsInfo" />
                        <webuijsf:button
                           actionExpression="#{Page2.broadcastSubjectDiscoveryButton_action}"
                           id="broadcastSubjectDiscoveryButton"
                           text="Discover Patient" />
                        <webuijsf:label id="searchPatientDiscovery"
                           text="Click to send a patient discovery message to systems in your UDDI (and/or uddiConnectionInfo.xml file)." />
                        <webuijsf:table augmentTitle="false"
                           id="subjectDiscoveryCoorelationTable"
                           paginateButton="true"
                           paginationControls="true"
                           title="Patient Correlations"
                           styleClass="table">
                           <webuijsf:tableRowGroup
                              id="patientCorrTableRowGoup" rows="10"
                              sourceData="#{Page2.patientCorrelationList}"
                              sourceVar="currentRow">
                              <webuijsf:tableColumn
                                 headerText="Assigning Authority"
                                 id="patientCoorAssignAuthColm">
                                 <webuijsf:staticText
                                    id="correlatedAssignAuthority"
                                    text="#{currentRow.value['assignAuthorityId']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Patient Id"
                                 id="patientCorrIdColm">
                                 <webuijsf:staticText
                                    id="correlatedPatientId"
                                    text="#{currentRow.value['remotePatientId']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Organization"
                                 id="patientCorrOrgColm">
                                 <webuijsf:staticText
                                    id="correlatedOrganization"
                                    text="#{currentRow.value['organizationName']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Organization Id"
                                 id="patientCorrOrgIdColm">
                                 <webuijsf:staticText
                                    id="correlatedOrganizationId"
                                    text="#{currentRow.value['organizationId']}" />
                              </webuijsf:tableColumn>
                           </webuijsf:tableRowGroup>
                        </webuijsf:table>
                        <webuijsf:staticText
                           binding="#{Page2.broadcastInfo}"
                           id="broadcastInfo" />
                        <webuijsf:staticText
                           binding="#{Page2.broadcastInfo2}"
                           id="broadcastInfo2" />
                     </webuijsf:panelLayout>
                  </div>
                  <div class="tab-pane" id="documentTab">
                     <webuijsf:panelLayout id="layoutPanel3">
                        <webuijsf:label id="docQueryLabel"
                           text="Click below to find Documents for Patient" />
                        <webuijsf:button
                           actionExpression="#{Page2.getDocQueryResults}"
                           id="docQueryButton" text="Document Query" />
                        <webuijsf:table augmentTitle="false"
                           id="docQueryResults"
                           title="Document Search Results"
                           styleClass="table">
                           <webuijsf:tableRowGroup
                              id="docQueryResultsGroup" rows="4"
                              sourceData="#{DocumentQueryResults.documents}"
                              sourceVar="document">
                              <webuijsf:tableColumn
                                 headerText="Document Id"
                                 id="documentID">
                                 <webuijsf:hyperlink
                                    actionExpression="#{Page2.displayDocument}"
                                    binding="#{Page2.selectedDocumentID}"
                                    id="documentIdLink"
                                    text="#{document.value['documentID']}"
                                    target="_new" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Creation Date"
                                 id="creationDate">
                                 <webuijsf:staticText id="staticText1"
                                    text="#{document.value['creationDate']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn headerText="Title"
                                 id="title">
                                 <webuijsf:staticText id="staticText1"
                                    text="#{document.value['title']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Document Type"
                                 id="documentType">
                                 <webuijsf:staticText id="staticText2"
                                    text="#{document.value['documentType']}" />
                              </webuijsf:tableColumn>
                              <webuijsf:tableColumn
                                 headerText="Institution"
                                 id="institution" noWrap="true">
                                 <webuijsf:staticText id="staticText4"
                                    text="#{document.value['institution']}" />
                              </webuijsf:tableColumn>
                           </webuijsf:tableRowGroup>
                        </webuijsf:table>
                        <webuijsf:staticText
                           binding="#{Page2.errorMessage}"
                           id="patientInfo" />
                     </webuijsf:panelLayout>
                  </div>
               </div>


            </webuijsf:form>

         </div>
      </webuijsf:body>
      </webuijsf:html>
      <webuijsf:script url="http://code.jquery.com/jquery.js"></webuijsf:script>
      <webuijsf:script url="/resources/js/bootstrap.js"></webuijsf:script>
   </webuijsf:page>
</jsp:root>
