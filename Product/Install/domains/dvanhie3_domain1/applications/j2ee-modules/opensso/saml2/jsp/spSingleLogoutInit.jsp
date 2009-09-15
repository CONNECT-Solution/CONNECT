<%--
   DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
  
   Copyright (c) 2006 Sun Microsystems Inc. All Rights Reserved
  
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

   $Id: spSingleLogoutInit.jsp,v 1.8 2009/03/03 01:54:15 qcheng Exp $

--%>




<%@ page import="com.sun.identity.shared.debug.Debug" %>
<%@ page import="com.sun.identity.plugin.session.SessionManager" %>
<%@ page import="com.sun.identity.plugin.session.SessionException" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Utils" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Constants" %>
<%@ page import="com.sun.identity.saml2.meta.SAML2MetaManager" %>
<%@ page import="com.sun.identity.saml2.meta.SAML2MetaUtils" %>
<%@ page import="com.sun.identity.saml2.common.SAML2Exception" %>
<%@ page import="com.sun.identity.saml2.profile.LogoutUtil" %>
<%@ page import="com.sun.identity.saml2.profile.SPSingleLogout" %>
<%@ page import="java.util.HashMap" %>

<%--
    spSingleLogoutInit.jsp
    - initiates the LogoutRequest at the Service Provider.

    Required parameters to this jsp are :
    - binding - binding used for this request

    Some of the other optional parameters are :
    "RelayState" - the target URL on successful Single Logout
    "goto" - the target URL on successful Single Logout.
             "RelayState" takes precedence to "goto" parameter.
    "Destination" - A URI Reference indicating the address to
                    which the request has been sent.
    "Consent" - Specifies a URI a SAML defined identifier
                known as Consent Identifiers.
    "Extension" - Specifies a list of Extensions as list of
                  String objects.

    Check the SAML2 Documentation for supported parameters.
--%>
<html>

<head>
    <title>SAMLv2 Single Logout Initiation at SP</title>
</head>
<body bgcolor="#FFFFFF" text="#000000">

<%
    // Retrieves the Request Query Parameters
    // Binding are the required query parameters
    // binding - binding used for this request

    try {
        String RelayState = request.getParameter(SAML2Constants.RELAY_STATE);
        if ((RelayState == null) || (RelayState.length() == 0)) {
            RelayState = request.getParameter(SAML2Constants.GOTO);
        }

        Object ssoToken = null;
        try {
            ssoToken = SessionManager.getProvider().getSession(request);
        } catch (SessionException se) {
            if (SAML2Utils.debug.messageEnabled()) {
                SAML2Utils.debug.message("No session.");
            }
            ssoToken = null;
        }
        if (ssoToken == null) {
            SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
                "nullSSOToken", SAML2Utils.bundle.getString("nullSSOToken"));
            return;
        }
        String[] values = SessionManager.getProvider().
            getProperty(ssoToken, SAML2Constants.SP_METAALIAS);
        String metaAlias = null;
        if (values != null && values.length > 0) {
            metaAlias = values[0];
        }
        if (metaAlias == null) {
            SessionManager.getProvider().invalidateSession(
                ssoToken, request, response);
            if (RelayState != null) {
                response.sendRedirect(RelayState);
            } else {
                %>
                <jsp:forward page="/saml2/jsp/default.jsp?message=spSloSuccess"/>
                <%
            }
            return;
        }

        String idpEntityID = request.getParameter("idpEntityID");

        if ((idpEntityID == null) || (idpEntityID.length() == 0)) {
            SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
                "nullIDPEntityID", 
                SAML2Utils.bundle.getString("nullIDPEntityID"));
            return;
        }

        String binding = LogoutUtil.getSLOBindingInfo(request, metaAlias,
                                        SAML2Constants.SP_ROLE, idpEntityID);
        /**
        * Parses the request parameters and builds the Logout
        * Request to be sent to the IDP.
        *
        * @param request the HttpServletRequest.
        * @param response the HttpServletResponse.
        * @param metaAlias metaAlias of Service Provider. The format of
        *               this parameter is /realm_name/SP_name.
        * @param binding binding used for this request.
        * @param paramsMap Map of all other parameters.
        *       Following parameters names with their respective
        *       String values are allowed in this paramsMap.
        *       "RelayState" - the target URL on successful Single Logout
        *       "Destination" - A URI Reference indicating the address to
        *                       which the request has been sent.
        *       "Consent" - Specifies a URI a SAML defined identifier
        *                   known as Consent Identifiers.
        *       "Extension" - Specifies a list of Extensions as list of
        *                   String objects.
        * @throws SAML2Exception if error initiating request to IDP.
        */
        HashMap paramsMap = new HashMap();
        paramsMap.put("metaAlias", metaAlias);
        paramsMap.put("idpEntityID", idpEntityID);
        paramsMap.put(SAML2Constants.ROLE, SAML2Constants.SP_ROLE);
        paramsMap.put(SAML2Constants.BINDING, binding);
        paramsMap.put("Destination", request.getParameter("Destination"));
        paramsMap.put("Consent", request.getParameter("Consent"));
        paramsMap.put("Extension", request.getParameter("Extension"));
        if ((RelayState == null) || (RelayState.equals(""))) {
            SAML2MetaManager metaManager= new SAML2MetaManager();
            String hostEntity = metaManager.getEntityByMetaAlias(metaAlias);
            String realm = SAML2MetaUtils.getRealmByMetaAlias(metaAlias);
            RelayState = SAML2Utils.getAttributeValueFromSSOConfig(
                realm, hostEntity, SAML2Constants.SP_ROLE,
                SAML2Constants.DEFAULT_RELAY_STATE);
        }
        if (RelayState != null) {
            paramsMap.put(SAML2Constants.RELAY_STATE, RelayState);
        }

        SPSingleLogout.initiateLogoutRequest( request,response,
            binding,paramsMap);
        
        if (binding.equalsIgnoreCase(SAML2Constants.SOAP)) {
            if (RelayState != null && (!RelayState.equals(""))) {
                response.sendRedirect(RelayState);
            } else {
                %>
                <jsp:forward page="/saml2/jsp/default.jsp?message=spSloSuccess"/>
                <%
            }
        }
    } catch (SAML2Exception sse) {
        SAML2Utils.debug.error("Error sending Logout Request " , sse);
        SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
            "LogoutRequestCreationError",
            SAML2Utils.bundle.getString("LogoutRequestCreationError") + " " +
            sse.getMessage());
        return;
    } catch (Exception e) {
        SAML2Utils.debug.error("Error initializing Request ",e);
        SAML2Utils.sendError(request, response, response.SC_BAD_REQUEST,
            "LogoutRequestCreationError",
            SAML2Utils.bundle.getString("LogoutRequestCreationError") + " " +
            e.getMessage());
        return;
    }
%>

</body>
</html>
