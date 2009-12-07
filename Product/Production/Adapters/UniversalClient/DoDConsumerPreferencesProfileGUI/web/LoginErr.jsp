<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : LoginErr
    Created on : Oct 12, 2009, 12:37:24 AM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{LoginErr.page1}" id="page1">
            <webuijsf:html binding="#{LoginErr.html1}" id="html1">
                <webuijsf:head binding="#{LoginErr.head1}" id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body binding="#{LoginErr.body1}" id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form binding="#{LoginErr.form1}" id="form1">
                        <div style="left: 0px; top: 0px; position: absolute">
                            <jsp:directive.include file="CPPHeader.jspf"/>
                        </div>
                        <webuijsf:label binding="#{LoginErr.label1}" id="label1" style="position: absolute; left: 384px; top: 264px" text="Login Error...return"/>
                        <webuijsf:button actionExpression="#{LoginErr.returnBTN_action}" binding="#{LoginErr.returnBTN}" id="returnBTN"
                            style="height: 19px; left: 504px; top: 264px; position: absolute; width: 120px" text="Return to Login"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
