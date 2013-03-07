<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page1
    Created on : Aug 7, 2009, 5:38:42 PM
    Author     : vvickers
-->
<jsp:root version="2.1" xmlns:f="http://java.sun.com/jsf/core"
   xmlns:h="http://java.sun.com/jsf/html"
   xmlns:jsp="http://java.sun.com/JSP/Page"
   xmlns:webuijsf="http://www.sun.com/webui/webuijsf">
   <jsp:directive.page contentType="text/html;charset=UTF-8"
      pageEncoding="UTF-8" />
      <webuijsf:page binding="#{Page1.page1}" id="page1">
         <webuijsf:html id="html1">
         <webuijsf:head id="head1" title="Connect Universal Client">
         
            <webuijsf:link id="link1" url="/resources/css/bootstrap.css" />
         </webuijsf:head>
         <webuijsf:body id="body1" style="-rave-layout: grid">
          <div class="container">
            <webuijsf:form id="form1">
               <webuijsf:image height="96" id="image1"
                  style="left: 0px; top: 0px; position: absolute"
                  url="/resources/connect.GIF" width="312" />
               <webuijsf:label id="info1"
                  text="Enter your account details below to login" />
               <webuijsf:label for="nameField" id="nameLabel"
                  text="Username:" />
               <webuijsf:label for="passField" id="passLabel"
                  text="Password:" />
               <webuijsf:textField binding="#{Page1.nameField}"
                  id="nameField" />
               <webuijsf:passwordField binding="#{Page1.passField}"
                  id="passField" />
               <webuijsf:button
                  actionExpression="#{Page1.loginButton_action}"
                  binding="#{Page1.loginButton}" id="loginButton"
                  text="Login" />
               <webuijsf:staticText binding="#{Page1.verifyMsg}"
                  id="verifyMsg"/>
               <webuijsf:staticText binding="#{Page1.agencyLogo}"
                  id="agencyLogo"/>
               <webuijsf:label id="UCLabel"
                  text="UNIVERSAL CLIENT" />
            </webuijsf:form>
            </div>
         </webuijsf:body>
         </webuijsf:html>
      </webuijsf:page>
</jsp:root>
