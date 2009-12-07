<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : ProviderProvisioning
    Created on : Oct 5, 2009, 2:08:45 AM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{ProviderProvisioning.page1}" id="page1">
            <webuijsf:html binding="#{ProviderProvisioning.html1}" id="html1">
                <webuijsf:head binding="#{ProviderProvisioning.head1}" id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body binding="#{ProviderProvisioning.body1}" id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form binding="#{ProviderProvisioning.form1}" id="form1">
                        <div style="left: 0px; top: 0px; position: absolute">
                            <jsp:directive.include file="CPPHeader.jspf"/>
                        </div>
                        <div style="left: 0px; top: 83px; position: absolute">
                            <jsp:directive.include file="CPPNavigator.jspf"/>
                        </div>
                        <webuijsf:panelLayout binding="#{ProviderProvisioning.layoutPanel1}" id="layoutPanel1" style="border: 1px solid gray; height: 600px; left: 166px; top: 83px; position: absolute; width: 860px; -rave-layout: grid">
                            <webuijsf:label binding="#{ProviderProvisioning.label1}" id="label1" style="position: absolute; left: 24px; top: 24px" text="Provider Name:"/>
                            <webuijsf:textField binding="#{ProviderProvisioning.providerNameFLD}" columns="40" id="providerNameFLD" style="position: absolute; left: 120px; top: 24px"/>
                            <webuijsf:label binding="#{ProviderProvisioning.label2}" id="label2" style="position: absolute; left: 384px; top: 24px" text="Username:"/>
                            <webuijsf:textField binding="#{ProviderProvisioning.userNameFLD}" id="userNameFLD" style="position: absolute; left: 456px; top: 24px"/>
                            <webuijsf:label binding="#{ProviderProvisioning.label3}" id="label3" style="position: absolute; left: 600px; top: 24px" text="Userid:"/>
                            <webuijsf:textField binding="#{ProviderProvisioning.userIENFLD}" id="userIENFLD" style="position: absolute; left: 648px; top: 24px"/>
                            <webuijsf:label binding="#{ProviderProvisioning.label4}" id="label4" style="left: 24px; top: 52px; position: absolute" text="Clinical Name:"/>
                            <webuijsf:textField binding="#{ProviderProvisioning.clinicalNameFLD}" columns="40" id="clinicalNameFLD" style="left: 120px; top: 52px; position: absolute"/>
                            <webuijsf:label binding="#{ProviderProvisioning.label5}" id="label5" style="left: 384px; top: 52px; position: absolute" text="FacilityNCID:"/>
                            <webuijsf:textField binding="#{ProviderProvisioning.facilityNCIDFLD}" id="facilityNCIDFLD" style="left: 456px; top: 52px; position: absolute"/>
                            <webuijsf:button binding="#{ProviderProvisioning.searchBTN}" disabled="true" id="searchBTN"
                                style="height: 19px; left: 648px; top: 52px; position: absolute; width: 120px" text="Search Provider Info."/>
                            <webuijsf:label binding="#{ProviderProvisioning.label6}" id="label6"
                                style="left: 24px; top: 144px; position: absolute; width: 237px" text="Provider will automatically have their NHIN User Role set to Physician and it's SNOMED-CT representation."/>
                            <webuijsf:label binding="#{ProviderProvisioning.label7}" id="label7"
                                style="left: 288px; top: 144px; position: absolute; width: 215px" text="NHIN Purpose of Use is statically set for this pilot to &quot;Healthcare Operations&quot;."/>
                            <webuijsf:label binding="#{ProviderProvisioning.label8}" id="label8"
                                style="left: 552px; top: 144px; position: absolute; width: 191px" text="User Organization will be automatically set based on providers FacilityNCID and Clinic."/>
                            <webuijsf:dropDown binding="#{ProviderProvisioning.astmCBX}" disabled="true" id="astmCBX"
                                items="#{ProviderProvisioning.astmCBXDefaultOptions.options}" label="ASTM Structured Role:" labelOnTop="true" style="color: #003366; left: 24px; top: 216px; position: absolute"/>
                            <webuijsf:dropDown binding="#{ProviderProvisioning.functionalCBX}" disabled="true" id="functionalCBX"
                                items="#{ProviderProvisioning.functionalCBXDefaultOptions.options}" label="Functional Roles:" labelOnTop="true" style="position: absolute; left: 24px; top: 264px"/>
                            <webuijsf:dropDown binding="#{ProviderProvisioning.hl7PermCBX}" disabled="true" id="hl7PermCBX"
                                items="#{ProviderProvisioning.hl7PermCBXDefaultOptions.options}" label="HL7 Permissions" labelOnTop="true" style="color: rgb(0, 51, 102); left: 288px; top: 216px; position: absolute"/>
                            <webuijsf:button binding="#{ProviderProvisioning.createBTN}" id="createBTN"
                                style="height: 19px; left: 24px; top: 360px; position: absolute; width: 220px" text="Create Provider Access Control Policy"/>
                        </webuijsf:panelLayout>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
