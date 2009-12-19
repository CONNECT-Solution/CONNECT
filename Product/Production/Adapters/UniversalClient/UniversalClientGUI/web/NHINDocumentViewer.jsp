<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : NHINDocumentViewer
    Created on : Oct 12, 2009, 4:40:10 PM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{NHINDocumentViewer.page1}" id="page1">
            <webuijsf:html binding="#{NHINDocumentViewer.html1}" id="html1">
                <webuijsf:head binding="#{NHINDocumentViewer.head1}" id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                    <webuijsf:script type="text/javascript">
                        function showProgressBar() {
                            var domNode = document.getElementById("form1:layoutPanel1:progressBar");
                            if (domNode != null) {
                                domNode.setProps({"visible":true});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:script type="text/javascript">
                        function closeProgressBar() {
                            var domNode = document.getElementById("form1:layoutPanel1:progressBar");
                            if (domNode != null) {
                                domNode.setProps({"visible":false});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:script type="text/javascript">
                        function showRetrieveComplete() {
                            var domNode = document.getElementById("form1:layoutPanel1:retrieveCompletePanel");
                            if (domNode != null) {
                                domNode.setProps({"visible":true});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:script type="text/javascript">
                        function showNoNewDocuments() {
                            var domNode = document.getElementById("form1:layoutPanel1:noNewDocumentsPanel");
                            if (domNode != null) {
                                domNode.setProps({"visible":true});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:script type="text/javascript">
                        function closeNoNewDocuments() {
                            var domNode = document.getElementById("form1:layoutPanel1:noNewDocumentsPanel");
                            if (domNode != null) {
                                domNode.setProps({"visible":false});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:script type="text/javascript">
                        function closeRetrieveComplete() {
                            var domNode = document.getElementById("form1:layoutPanel1:retrieveCompletePanel");
                            if (domNode != null) {
                                domNode.setProps({"visible":false});
                            }
                        }
                    </webuijsf:script>
                    <webuijsf:meta binding="#{NHINDocumentViewer.meta1}" content="#{SessionBean1.refreshRate}" httpEquiv="refresh" id="meta1"/>
                </webuijsf:head>
                <webuijsf:body binding="#{NHINDocumentViewer.body1}" id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form binding="#{NHINDocumentViewer.form1}" id="form1">
                        <webuijsf:tabSet binding="#{NHINDocumentViewer.tabSet1}" id="tabSet1" lite="true" mini="true" selected="inboxTab" style="height: 550px; left: 0px; top: 0px; position: absolute; width: 100%">
                            <webuijsf:tab binding="#{NHINDocumentViewer.inboxTab}" id="inboxTab" text="Document Inbox">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel1}" id="layoutPanel1" panelLayout="flow" style="height: 492px; position: relative; width: 100%;">
                                    <webuijsf:progressBar binding="#{NHINDocumentViewer.progressBar}" id="progressBar" refreshRate="70000"
                                        rendered="#{NHINDocumentViewer.progressBarVisible}"
                                        style="background-position: 50% 50%; height: 40px; left: 0px; top: 0px; position: relative; width: 40px"
                                        taskState="running" type="BUSY" visible="#{NHINDocumentViewer.progressBarVisible}"/>
                                    <br/>
                                    <webuijsf:table augmentTitle="false" binding="#{NHINDocumentViewer.table1}" id="table1" lite="true"
                                        paginationControls="true" style="position: relative; width: 100%" width="100%">
                                        <webuijsf:tableRowGroup binding="#{NHINDocumentViewer.tableRowGroup1}" emptyDataMsg="No documents currently available."
                                            id="tableRowGroup1" rows="5" sourceData="#{SessionBean1.availableDocuments}" sourceVar="currentRow">
                                            <webuijsf:tableColumn binding="#{NHINDocumentViewer.tableColumn1}" headerText="Select" id="tableColumn1" width="80">
                                                <webuijsf:checkbox binding="#{NHINDocumentViewer.displaySelectedCHK}"
                                                    disabled="#{NHINDocumentViewer.checkBoxEnabled}" id="displaySelectedCHK"
                                                    onClick="webui.suntheme4_2.common.timeoutSubmitForm(this.form, 'tabSet1:inboxTab:layoutPanel1:table1:tableRowGroup1:tableColumn1:displaySelectedCHK');"
                                                    selected="#{currentRow.value['selected']}" valueChangeListenerExpression="#{NHINDocumentViewer.displaySelectedCHK_processValueChange}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{NHINDocumentViewer.tableColumn2}" headerText="Organization" id="tableColumn2" sort="organizationName">
                                                <webuijsf:hyperlink actionExpression="#{NHINDocumentViewer.docOrgLink_action}"
                                                    binding="#{NHINDocumentViewer.docOrgLink}" disabled="#{NHINDocumentViewer.checkBoxEnabled}" id="docOrgLink" text="#{currentRow.value['organizationName']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{NHINDocumentViewer.tableColumn3}" headerText="Title" id="tableColumn3" sort="documentTitle">
                                                <webuijsf:staticText binding="#{NHINDocumentViewer.staticText3}" id="staticText3" text="#{currentRow.value['documentTitle']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn binding="#{NHINDocumentViewer.tableColumn4}" headerText="Document Type" id="tableColumn4" sort="documentType">
                                                <webuijsf:staticText binding="#{NHINDocumentViewer.staticText4}" id="staticText4" text="#{currentRow.value['documentType']}"/>
                                            </webuijsf:tableColumn>
                                            <webuijsf:tableColumn headerText="Document Status" id="tableColumn6" sort="documentStatus" width="80">
                                                <webuijsf:staticText binding="#{NHINDocumentViewer.staticText1}" id="staticText1" text="#{currentRow.value['documentStatus']}"/>
                                            </webuijsf:tableColumn>
                                        </webuijsf:tableRowGroup>
                                    </webuijsf:table>
                                    <br/>
                                    <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel12}" id="layoutPanel12" panelLayout="flow" style="position: relative; text-align: right; width: 100%">
                                        <webuijsf:button actionExpression="#{NHINDocumentViewer.viewSelectedBTN_action}"
                                            binding="#{NHINDocumentViewer.viewSelectedBTN}" id="viewSelectedBTN" onClick="showProgressBar()"
                                            style="height: 19px; width: 120px" text="View Selected"/>
                                        <webuijsf:button actionExpression="#{NHINDocumentViewer.selectAllAndViewBTN_action}"
                                            binding="#{NHINDocumentViewer.selectAllAndViewBTN}" id="selectAllAndViewBTN" style="height: 19px; width: 120px" text="Select All &amp; View"/>
                                        <webuijsf:button actionExpression="#{NHINDocumentViewer.resetDisplayBTN_action}"
                                            binding="#{NHINDocumentViewer.resetDisplayBTN}" id="resetDisplayBTN" style="height: 19px; width: 120px" text="Reset Selections"/>
                                    </webuijsf:panelLayout>
                                    <webuijsf:panelLayout binding="#{NHINDocumentViewer.retrieveCompletePanel}" id="retrieveCompletePanel"
                                        style="border: 1px solid gray; background-color: rgb(204, 204, 204); height: 70px; left: 168px; top: 96px; position: absolute; width: 600px; -rave-layout: grid" visible="false">
                                        <webuijsf:label id="label2"
                                            style="color: blue; font-family: Arial,Helvetica,sans-serif; font-size: 12px; font-weight: bold; left: 24px; top: 24px; position: absolute" text="Retrieval complete - Documents are available below."/>
                                        <webuijsf:hyperlink actionExpression="#{NHINDocumentViewer.retrievalCompleteCloseLink_action}"
                                            binding="#{NHINDocumentViewer.retrievalCompleteCloseLink}" id="retrievalCompleteCloseLink"
                                            onClick="closeRetrieveComplete()" style="left: 552px; top: 0px; position: absolute" text="Close"/>
                                    </webuijsf:panelLayout>
                                    <webuijsf:panelLayout binding="#{NHINDocumentViewer.noNewDocumentsPanel}" id="noNewDocumentsPanel"
                                        style="border: 1px solid gray; background-color: rgb(204, 204, 204); height: 70px; left: 157px; top: 84px; position: absolute; width: 600px" visible="false">
                                        <webuijsf:label id="label1"
                                            style="color: blue; font-family: 'Arial','Helvetica',sans-serif; font-size: 12px; font-weight: bold; left: 24px; top: 24px; position: absolute" text="No new document(s) are available on NHIN.  The most recent are available below."/>
                                        <webuijsf:hyperlink actionExpression="#{NHINDocumentViewer.closeNoNewDocsLink_action}"
                                            binding="#{NHINDocumentViewer.closeNoNewDocsLink}" id="closeNoNewDocsLink" onClick="closeNoNewDocuments()"
                                            style="left: 552px; top: 0px; position: absolute" text="Close"/>
                                    </webuijsf:panelLayout>
                                    <webuijsf:label binding="#{NHINDocumentViewer.progressBarStatusLBL}" id="progressBarStatusLBL"
                                        style="color: navy; left: 0px; top: 0px; position: absolute" text="No new document(s) are available on NHIN.  The most recent are available below."/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab1_action}" binding="#{NHINDocumentViewer.tab1}" id="tab1" rendered="false"
                                text="Tab 1" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel2}" id="layoutPanel2" panelLayout="flow" style="border: 1px solid black; height: 100%; position: relative; width: 100%;  overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea1}" escape="false" id="displayArea1"
                                        style="height: 100%; left: 0px; top: 0px; position: absolute; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab2_action}" binding="#{NHINDocumentViewer.tab2}" id="tab2" rendered="false"
                                text="Tab 2" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel5}" id="layoutPanel5" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%;  overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea2}" escape="false" id="displayArea2"
                                        style="height: 100%; left: 0px; top: 0px; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab3_action}" binding="#{NHINDocumentViewer.tab3}" id="tab3" rendered="false"
                                text="Tab 3" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel6}" id="layoutPanel6" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%;  overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea3}" escape="false" id="displayArea3"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab4_action}" binding="#{NHINDocumentViewer.tab4}" id="tab4" rendered="false"
                                text="Tab 4" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel3}" id="layoutPanel3" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea4}" escape="false" id="displayArea4"
                                        style="height: 100%; left: 0px; top: 0px; position: absolute; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab5_action}" binding="#{NHINDocumentViewer.tab5}" id="tab5" rendered="false"
                                text="Tab 5" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel4}" id="layoutPanel4" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea5}" escape="false" id="displayArea5"
                                        style="height: 100%; left: 0px; top: 0px; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab6_action}" binding="#{NHINDocumentViewer.tab6}" id="tab6" rendered="false"
                                text="Tab 6" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel7}" id="layoutPanel7" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea6}" escape="false" id="displayArea6"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab7_action}" binding="#{NHINDocumentViewer.tab7}" id="tab7" rendered="false"
                                text="Tab 7" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel8}" id="layoutPanel8" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea7}" escape="false" id="displayArea7"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab8_action}" binding="#{NHINDocumentViewer.tab8}" id="tab8" rendered="false"
                                text="Tab 8" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel9}" id="layoutPanel9" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea8}" escape="false" id="displayArea8"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab9_action}" binding="#{NHINDocumentViewer.tab9}" id="tab9" rendered="false"
                                text="Tab 9" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel10}" id="layoutPanel10" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea9}" escape="false" id="displayArea9"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                            <webuijsf:tab actionExpression="#{NHINDocumentViewer.tab10_action}" binding="#{NHINDocumentViewer.tab10}" id="tab10"
                                rendered="false" text="Tab 10" visible="false">
                                <webuijsf:panelLayout binding="#{NHINDocumentViewer.layoutPanel11}" id="layoutPanel11" panelLayout="flow" style="border: 1px solid black; height: 100%; width: 100%; overflow-y: scroll;overflow-x: hidden;">
                                    <h:outputText binding="#{NHINDocumentViewer.displayArea10}" escape="false" id="displayArea10"
                                        style="height: 100%; width: 100%" value="#{SessionBean1.convertedNHINDocument}"/>
                                </webuijsf:panelLayout>
                            </webuijsf:tab>
                        </webuijsf:tabSet>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
