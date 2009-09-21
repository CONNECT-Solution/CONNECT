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

   $Id: logout.jsp,v 1.8 2008/10/29 03:11:53 veiming Exp $

--%>

<%@page
    import="com.sun.identity.wsfederation.common.WSFederationConstants"
    import="java.util.Map"
    import="com.sun.identity.plugin.session.SessionManager"
    import="com.sun.identity.multiprotocol.MultiProtocolUtils"
    import="com.sun.identity.multiprotocol.SingleLogoutManager"
    import="com.sun.identity.wsfederation.common.WSFederationUtils"
%>
<%
    String displayName = 
        (String)request.getAttribute(WSFederationConstants.LOGOUT_DISPLAY_NAME);
    String wreply = 
        (String)request.getAttribute(WSFederationConstants.LOGOUT_WREPLY);
    Map<String, String> providerList = 
        (Map<String, String>)request.getAttribute(
        WSFederationConstants.LOGOUT_PROVIDER_LIST);
    String contextPath = request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Signing Out</title>
        <script language="JavaScript">
  <%
    if ( wreply!=null && wreply.length()>0 )
    {
  %>
            function startTimer() {
                document.getElementById("logoutPrompt").innerHTML = 
                    'Signed out of <%=displayName%>. <a href="<%=wreply%>">Click here</a> to continue or just wait a few seconds.';
                setTimeout(redirectToWReply,5000);
            }
            function redirectToWReply () {
                document.location.href="<%=wreply%>";
            } 
  <%
    } else {
  %>
            function startTimer() {
                // do nothing - nowhere to go!
            }
  <%
    }
  %>
        </script>
        <link rel="stylesheet" type="text/css" href="<%= contextPath %>/com_sun_web_ui/css/css_ns6up.css" />
        <link rel="shortcut icon" href="<%= contextPath %>/com_sun_web_ui/images/favicon/favicon.ico" type="image/x-icon" />
    </head>
    <body class="DefBdy" onload="startTimer();">
        <div class="SkpMedGry1"><a href="#SkipAnchor3860"><img src="<%= contextPath %>/com_sun_web_ui/images/other/dot.gif" alt="Jump to End of Masthead" border="0" height="1" width="1"></a></div><div class="MstDiv">
            <table class="MstTblBot" title="" border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td class="MstTdTtl" width="99%">
                        <div class="MstDivTtl"><img name="AMConfig.configurator.ProdName" src="<%= contextPath %>/console/images/PrimaryProductName.png" alt="OpenSSO" border="0"></div>
                    </td>
                    <td class="MstTdLogo" width="1%"><img name="AMConfig.configurator.BrandLogo" src="<%= contextPath %>/com_sun_web_ui/images/other/javalogo.gif" alt="Java(TM) Logo" border="0" height="55" width="31"></td>
                </tr>
            </table>
            <table class="MstTblEnd" border="0" cellpadding="0" cellspacing="0" width="100%"><tr><td><img name="RMRealm.mhCommon.EndorserLogo" src="<%= contextPath %>/com_sun_web_ui/images/masthead/masthead-sunname.gif" alt="Sun(TM) Microsystems, Inc." align="right" border="0" height="10" width="108" /></td></tr></table>
        </div>
        <table class="SkpMedGry1" border="0" cellpadding="5" cellspacing="0" width="100%"><tr><td><img src="<%= contextPath %>/com_sun_web_ui/images/other/dot.gif" alt="Jump to End of Masthead" border="0" height="1" width="1"></td></tr></table>
        <table border="0" cellpadding="10" cellspacing="0" width="100%"><tr><td></td></tr></table>
        <table cellpadding=5>
            <tr>
                <td>
                  <%
                    if ( wreply!=null && wreply.length()>0 )
                    {
                  %>
                        <script>
                            document.write("<p id=\"logoutPrompt\">Signing out of <%=displayName%></p>");
                        </script>
                        <noscript>
                            <p><a href="<%=wreply%>">Click here</a> to continue</p>
                        </noscript>
                  <%
                    }
                  %>
                  <%
                    for ( String url : providerList.keySet() )
                    {
                  %>
                        <p>Signing out from <%=providerList.get(url)%></p>
                        <iframe width="500" src="<%=url%>"></iframe>
                  <%
                    }

                    // handle multi-federation protocol case
                    Object uSession = null;
                    try {
                        uSession = 
                            SessionManager.getProvider().getSession(request);
                    } catch (Exception e) {
                        // ignore
                    }
                    if ((uSession != null) && 
                        SessionManager.getProvider().isValid(uSession) &&
                        MultiProtocolUtils.isMultipleProtocolSession(uSession, 
                            SingleLogoutManager.WS_FED)) {
                        WSFederationUtils.processMultiProtocolLogout(request, 
                            response, uSession);
                    }
                  %>
                </td>
            </tr>
        </table>
    </body>
</html>
