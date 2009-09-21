<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2007 Sun Microsystems Inc. All Rights Reserved
  
   The contents of this file are subject to the terms
   of the Common Development and Distribution License
   (the License). You may not use this file except in
   compliance with the License.

   You can obtain a copy of the License at
   https://opensso.dev.java.net/public/CDDLv1.0.html or
   opensso/legal/CDDLv1.0.txt
   See the License for the specific language governing
   permission and limitations under the License.

   When distributing Covered Code, include this CDDL
   Header Notice in each file and include the License file
   at opensso/legal/CDDLv1.0.txt.
   If applicable, add the following below the CDDL Header,
   with the fields enclosed by brackets [] replaced by
   your own identifying information:
   "Portions Copyrighted [year] [name of copyright owner]"

   $Id: WebServiceClientEdit.jsp,v 1.9 2008/12/10 18:25:15 farble1670 Exp $

--%>

<%@ page info="WebServiceClientEdit" language="java" %>
<%@taglib uri="/WEB-INF/jato.tld" prefix="jato" %>
<%@taglib uri="/WEB-INF/cc.tld" prefix="cc" %>
<jato:useViewBean
    className="com.sun.identity.console.agentconfig.WebServiceClientEditViewBean"
    fireChildDisplayEvents="true" >

<cc:i18nbundle baseName="amConsole" id="amConsole"
    locale="<%=((com.sun.identity.console.base.AMViewBeanBase)viewBean).getUserLocale()%>"/>

<cc:i18nbundle baseName="agentService" id="agentService"
    locale="<%=((com.sun.identity.console.base.AMViewBeanBase)viewBean).getUserLocale()%>"/>
<cc:header name="hdrCommon" pageTitle="webconsole.title" bundleID="amConsole" copyrightYear="2007" fireDisplayEvents="true">

<script language="javascript" src="../console/js/am.js"></script>
<script language="javascript">
    function toggleKeyStoreComponent(radioComp) {
        frm = radioComp.form;
        disableCustomKeyStoreFields(frm, (radioComp.value == 'default'));
    }

    function disableCustomKeyStoreFields(frm, disable) {
        frm.elements['WebServiceClientEdit.keystorelocation'].disabled =
            disable;
        frm.elements['WebServiceClientEdit.keystorepassword'].disabled =
            disable;
        frm.elements['WebServiceClientEdit.keypassword'].disabled = disable;
    }

    function disableSTSFields() {
        var frm = document.forms['WebServiceClientEdit'];
        var securityMech = frm.elements['WebServiceClientEdit.SecurityMech'];
        var disableSTS = true;
        var disableDiscovery = true;
        var disableLibertyServiceURN = true;
        var disableKerberos = true;
        var disablePassThru = true;

        for (var i = 0; (i < securityMech.length); i++) {
            if (securityMech[i].checked) {
                var value = securityMech[i].value;
                disableSTS = (value != "urn:sun:wss:sts:security");
                disableDiscovery =
                    (value != "urn:sun:liberty:discovery:security");
                disableLibertyServiceURN = disableSTS && disableDiscovery;
                disableKerberos =
                    (value != "urn:sun:wss:security:null:KerberosToken");
                disablePassThru = 
                    (value != "urn:sun:wss:security:null:SAMLToken-HK") &&
                    (value != "urn:sun:wss:security:null:SAMLToken-SV") &&
                    (value != "urn:sun:wss:security:null:SAML2Token-HK") &&
                    (value != "urn:sun:wss:security:null:SAML2Token-SV") &&
                    (value != "urn:sun:wss:sts:security");
                break;
            }
        }
        frm.elements['WebServiceClientEdit.libertyservicetype'].disabled = disableLibertyServiceURN;
        frm.elements['WebServiceClientEdit.sts'].disabled = disableSTS;
        frm.elements['WebServiceClientEdit.discovery'].disabled = disableDiscovery;
        frm.elements['WebServiceClientEdit.sts'].disabled = disableSTS;
        frm.elements['WebServiceClientEdit.kerberosdomain'].disabled = disableKerberos;
        frm.elements['WebServiceClientEdit.kerberosserviceprincipal'].disabled = disableKerberos;
        frm.elements['WebServiceClientEdit.kerberosticketcachedir'].disabled = disableKerberos;
        frm.elements['WebServiceClientEdit.kerberosdomainserver'].disabled = disableKerberos;
        frm.elements['WebServiceClientEdit.ispassthroughsecuritytoken'].disabled = disablePassThru;
    }
</script>

<cc:form name="WebServiceClientEdit" method="post">
<script language="javascript">
    function confirmLogout() {
        return confirm("<cc:text name="txtLogout" defaultValue="masthead.logoutMessage" bundleID="amConsole"/>");
    }
</script>
<cc:primarymasthead name="mhCommon" bundleID="amConsole"  logoutOnClick="return confirmLogout();" locale="<%=((com.sun.identity.console.base.AMViewBeanBase)viewBean).getUserLocale()%>"/>
<cc:tabs name="tabCommon" bundleID="amConsole" submitFormData="true" />

<table border="0" cellpadding="10" cellspacing="0" width="100%">
    <tr>
	<td>
	<cc:alertinline name="ialertCommon" bundleID="amConsole" />
	</td>
    </tr>
</table>

<%-- PAGE CONTENT --------------------------------------------------------- --%>
<cc:pagetitle name="pgtitleTwoBtns" bundleID="amConsole" pageTitleText="page.title.entities.create" showPageTitleSeparator="true" viewMenuLabel="" pageTitleHelpMessage="" showPageButtonsTop="true" showPageButtonsBottom="false" />

<table border="0" cellpadding="10" cellspacing="0">
    <tr>
        <td>
            <cc:button
                name="btnInherit"
                bundleID="amConsole"
                defaultValue="agentconfig.button.inherit"
                type="primary" />
        </td>
        <td>
            <cc:button
                name="btnDump"
                bundleID="amConsole"
                defaultValue="agentconfig.button.dump"
                type="primary" />
        </td>
    </tr>
</table>

<cc:propertysheet name="propertyAttributes" bundleID="agentService" showJumpLinks="true"/>

</cc:form>

<script language="javascript">
    var frm = document.forms['WebServiceClientEdit'];
    var disabled = true;
    if (frm.elements['WebServiceClientEdit.keystoreusage']) {
        disabled = frm.elements['WebServiceClientEdit.keystoreusage'][0].checked;
    }
    disableCustomKeyStoreFields(frm, disabled);
    disableSTSFields();
</script>
</cc:header>
</jato:useViewBean>
