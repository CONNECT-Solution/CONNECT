<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page1
    Created on : Oct 20, 2009, 10:19:33 AM
    Author     : dcannon
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
                        <webuijsf:label for="tfHomeCommunityID" id="label1" style="left: 48px; top: 216px; position: absolute" text="Home Community ID"/>
                        <webuijsf:label for="tfEndPoint" id="label2" style="left: 48px; top: 264px; position: absolute" text="Endpoint"/>
                        <webuijsf:button actionExpression="#{Page1.btnSubmit_action}" id="btnSubmit" style="left: 551px; top: 312px; position: absolute" text="Submit"/>
                        <webuijsf:textField columns="75" id="tfHomeCommunityID" style="left: 192px; top: 216px; position: absolute" text="#{ApplicationBean1.homeCommunityID}"/>
                        <webuijsf:textField columns="75" id="tfEndPoint" style="left: 192px; top: 264px; position: absolute" text="#{ApplicationBean1.endPoint}"/>
                        <webuijsf:image height="12" id="image2" style="left: 48px; top: 192px; position: absolute" url="/resources/redpx.JPG" width="720"/>
                        <webuijsf:staticText id="staticTextEndpointExample" style="left: 192px; top: 288px; position: absolute; width: 336px" text="e.g. https://nhinhealthlinc.org/nhinsubjectdiscovery/service"/>
                        <webuijsf:staticText id="staticTextHomeCommunityID" style="left: 192px; top: 240px; position: absolute; width: 336px" text="e.g. 2.16.840.1.113883.13.25"/>
                        <webuijsf:image id="image1" style="left: 48px; top: 0px; position: absolute" url="/resources/CONNECT.JPG"/>
                        <webuijsf:staticText id="staticText1" style="left: 48px; top: 96px; position: absolute" text="This GUI was created to facilitate the addition of the Subject Discovery endpoints to the Service Registry so that the CONNECT Reference Test Gateway is aware of your gateway to begin testing.  Please ensure to be accurate on entering your Home Community ID and Subject Discovery endpoint.  If you have already entered your Home Community ID previously, you will not need to re-enter.  If you try to enter the Home Community ID and it already exists in the Service Registry then you will receive an error message.  You will need to contact us if any edits are required to existing Service Registry entries relating to this CONNECT Reference Test Gateway."/>
                        <webuijsf:staticText id="staticText2"
                            style="font-family: 'Verdana','Arial','Helvetica',sans-serif; font-size: 18px; height: 22px; left: 288px; top: 24px; position: absolute; width: 262px" text="Reference Test Gateway"/>
                        <webuijsf:staticText id="staticText3"
                            style="font-family: 'Verdana','Arial','Helvetica',sans-serif; font-size: 18px; left: 216px; top: 48px; position: absolute" text="  Subject Discovery Endpoint Addition Screen"/>
                        <webuijsf:staticText id="errorText" style="color: #a60000; height: 24px; left: 72px; top: 336px; position: absolute; width: 576px" text="#{ApplicationBean1.errorText}"/>
                        <webuijsf:button actionExpression="#{AddUDDIEntry.button1_action}" id="button1" style="left: 599px; top: 312px; position: absolute" text="Clear"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
