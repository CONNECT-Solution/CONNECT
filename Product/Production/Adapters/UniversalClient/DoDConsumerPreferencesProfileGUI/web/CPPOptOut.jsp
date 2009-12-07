<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : CPPOptOut
    Created on : Oct 5, 2009, 1:06:40 AM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <div style="left: 0px; top: 0px; position: absolute">
                            <jsp:directive.include file="CPPHeader.jspf"/>
                        </div>
                        <div style="left: 0px; top: 83px; position: absolute">
                            <jsp:directive.include file="CPPNavigator.jspf"/>
                        </div>
                        <div style="left: 168px; top: 100px; position: absolute">
                            <jsp:directive.include file="CPPPatientContext.jspf"/>
                        </div>
                        <div style="left: 168px; top: 216px; position: absolute">
                            <jsp:directive.include file="CPPOptOutNav.jspf"/>
                        </div>
                        <webuijsf:label id="label1" style="color: rgb(0, 51, 102); left: 182px; top: 83px; position: absolute" text="Selected Patient"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
