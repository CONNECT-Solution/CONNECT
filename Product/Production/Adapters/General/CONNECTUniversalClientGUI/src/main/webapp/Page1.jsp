<?xml version="1.0" encoding="UTF-8"?>
<!-- 
    Document   : Page1
    Created on : Aug 7, 2009, 5:38:42 PM
    Author     : vvickers
-->
<!--
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
        <webuijsf:page binding="#{Page1.page1}" id="page1">
            <webuijsf:html id="html1">
                <webuijsf:head id="head1" title="Connect Universal Client">
                    <webuijsf:link id="link1" url="/resources/stylesheet.css"/>
                </webuijsf:head>
                <webuijsf:body id="body1" style="-rave-layout: grid">
                    <webuijsf:form id="form1">
                        <!-- Header images and text -->
                        <webuijsf:image height="80" id="connectImage" style="left: 0px; top: 0px; position: absolute" url="/resources/connect.GIF" width="270"/>
                        <webuijsf:label id="UCLabel" style="color: black; font-family: Arial,Helvetica,sans-serif; font-size: 30px; left: 200px; top: 100px; position: absolute" text="UNIVERSAL CLIENT"/>
                        <webuijsf:label id="info1" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 68px; top: 164px; position: absolute" text="Enter your account details below to login"/>
                        <!-- form fields -->
                        <webuijsf:label for="nameField" id="nameLabel" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 72px; top: 212px; position: absolute" text="Username: "/>
                        <webuijsf:textField columns="25" binding="#{Page1.nameField}" id="nameField" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 144px; top: 212px; position: absolute"/>
                        <webuijsf:label for="passField" id="passLabel" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 72px; top: 236px; position: absolute" text="Password:"/>
                        <webuijsf:passwordField columns="25" binding="#{Page1.passField}" id="passField" style="font-family: 'Times New Roman',Times,serif; font-size: 14px; left: 144px; top: 236px; position: absolute"/>
                        <webuijsf:hiddenField id="hideEID" binding="#{Page1.hideEIDField}" />
                        <webuijsf:button actionExpression="#{Page1.loginButton_action}" binding="#{Page1.loginButton}" id="loginButton" style="font-family: 'Times New Roman','Times',serif; font-size: 14px; left: 180px; top: 284px; position: absolute; width: 72px" text="Login"/>
                        <!-- validation message -->
                        <webuijsf:staticText binding="#{Page1.verifyMsg}" id="verifyMsg" style="color:red; font-family: 'Times New Roman',Times,serif; font-size: 18px; left: 48px; top: 312px; position: absolute"/>
                        <!-- footer information -->
                        <webuijsf:staticText binding="#{Page1.agencyLogo}" id="agencyLogo" style="color: gray; font-family: Arial,Helvetica,sans-serif; font-size: 30px; left: 360px; top: 0px; position: absolute"/>
                        <webuijsf:staticText binding="#{Page1.disclaimerInfo}" id="disclaimerInfo" style="color: black; width: 684px; font-family: 'Times New Roman',Times,serif; font-size: 12px; left: 5px; top: 356px; position: absolute"/>
                    </webuijsf:form>
                </webuijsf:body>
            </webuijsf:html>
        </webuijsf:page>
    </f:view>
</jsp:root>
