<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : UserLogin
    Created on : Sep 10, 2009, 10:29:56 PM
    Author     : patlollav
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
    <jsp:directive.page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"/>
    <f:view>
        <webuijsf:page binding="#{UserLogin.userLoginPage}" id="UserLogin">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1" title="Connect Consumer Preferences Profile Management">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <webuijsf:image height="96" id="image1" style="left: 0px; top: 0px; position: absolute" url="/resources/connect.gif" width="264"/>
                        <webuijsf:label id="info1"
                            style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 48px; top: 144px; position: absolute" text="Enter your account details below to login"/>
                        <webuijsf:label for="nameField" id="nameLabel"
                            style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 72px; top: 192px; position: absolute" text="Username:"/>
                        <webuijsf:label for="passField" id="passLabel"
                            style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 72px; top: 216px; position: absolute" text="Password:"/>
                        <webuijsf:textField binding="#{UserLogin.userName}" id="nameField" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 144px; top: 192px; position: absolute"/>
                        <webuijsf:passwordField binding="#{UserLogin.password}" id="passField" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 144px; top: 216px; position: absolute"/>
                        <webuijsf:button actionExpression="#{UserLogin.loginButton_action}" binding="#{UserLogin.loginButton}" id="loginButton"
                            style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 215px; top: 264px; position: absolute" text="Login"/>
                        <webuijsf:staticText binding="#{UserLogin.verifyMsg}" id="verifyMsg" style="font-family: 'Times New Roman',Times,serif; font-size: 18px; left: 48px; top: 312px; position: absolute"/>
                        <webuijsf:staticText binding="#{UserLogin.agencyLogo}" id="agencyLogo" style="color: gray; font-family: Arial,Helvetica,sans-serif; font-size: 30px; left: 360px; top: 0px; position: absolute"/>
                        <webuijsf:label id="UCLabel"
                            style="color: gray; font-family: 'Times New Roman','Times',serif; font-size: 24px; left: 264px; top: 48px; position: absolute" text="Consumer Preferences Profile Management"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
