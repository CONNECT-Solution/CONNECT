<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Login
    Created on : Oct 3, 2009, 12:42:56 AM
    Author     : Duane DeCouteau
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{Login.page1}" id="page1">
            <webuijsf:html binding="#{Login.html1}" id="html1">
                <webuijsf:head binding="#{Login.head1}" id="head1">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body binding="#{Login.body1}" id="body1" imageURL="resources/images/c32Background.jpg" style="-rave-layout: grid">
                    <webuijsf:form binding="#{Login.form1}" id="form1">
                        <webuijsf:imageHyperlink actionExpression="#{Login.imageHyperlink1_action}" binding="#{Login.imageHyperlink1}" id="imageHyperlink1"
                            imageURL="/resources/images/connect.jpg" style="left: 0px; top: 0px; position: absolute"/>
                        <webuijsf:label binding="#{Login.label1}" id="label1"
                            style="color: #003366; font-family: Arial,Helvetica,sans-serif; font-size: 18px; left: 312px; top: 24px; position: absolute" text="Consumer Preferences and Policy Administration"/>
                        <webuijsf:panelLayout binding="#{Login.layoutPanel1}" id="layoutPanel1" style="border-width: 1px; border-style: solid; border-color: rgb(153, 153, 153) rgb(153, 153, 153) rgb(153, 153, 153) rgb(153, 153, 153); background-color: rgb(188, 188, 232); height: 128px; left: 312px; top: 216px; position: absolute; width: 334px; -rave-layout: grid">
                            <webuijsf:label binding="#{Login.label2}" id="label2" style="left: 25px; top: 25px; position: absolute" text="Username:"/>
                            <webuijsf:label binding="#{Login.label3}" id="label3" style="left: 24px; top: 48px; position: absolute" text="Password:"/>
                            <webuijsf:textField binding="#{Login.userNameFLD}" id="userNameFLD" style="left: 96px; top: 24px; position: absolute"/>
                            <webuijsf:passwordField binding="#{Login.passwordFLD}" id="passwordFLD" style="position: absolute; left: 96px; top: 48px"/>
                            <webuijsf:label binding="#{Login.loginLBL}" id="loginLBL"
                                style="color: gray; font-family: Arial,Helvetica,sans-serif; font-size: 10px; font-style: normal; font-weight: bold; left: 96px; top: 72px; position: absolute" text="Enter Username and Password to Login."/>
                            <webuijsf:button actionExpression="#{Login.loginBTN_action}" binding="#{Login.loginBTN}" id="loginBTN"
                                style="height: 21px; left: 225px; top: 102px; position: absolute; width: 100px" text="Login"/>
                        </webuijsf:panelLayout>
                        <webuijsf:label id="label4" style="font-family: Arial,Helvetica,sans-serif; font-size: 10px; left: 552px; top: 48px; position: absolute" text="(Developed for DoD internal use only.)"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
