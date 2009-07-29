<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*" %>
<%@ page import="gov.hhs.fha.nhinc.mpilib.*" %>
<%@ page import="gov.hhs.fha.nhinc.policyengine.adapterpip.proxy.*" %>
<%@ page import="gov.hhs.fha.nhinc.common.nhinccommonadapter.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Patient Update</title>
    </head>
    <body>
        <img src="connect.gif" width="650" height="250" alt="connect"/>

        <%
        String paramToken = request.getParameter("token");
        if (paramToken == null || paramToken.isEmpty()) {
            RequestDispatcher disp = request.getRequestDispatcher("login.jsp");
            disp.forward(request, response);
        } else {

            Object patListObj = session.getAttribute("patientList");

            if (patListObj instanceof Patients) {
                Patients patList = (Patients) patListObj;

                for (int x = 0; x < patList.size(); x++) {
                    Patient pat = patList.get(x);
                    String reqOptIn = request.getParameter("chk_" + x) + "";

                    boolean changedFlag = false;
                    if ((pat.isOptedIn() && !"on".equals(reqOptIn)) ||
                            (!pat.isOptedIn() && "on".equals(reqOptIn))) {
                        changedFlag = true;
                    }

                    String patDesc = pat.getName().getFirstName() + " " +
                            pat.getName().getLastName();
                    if (changedFlag) {
                        boolean isGoodStore = false;
                        Identifiers ids = pat.getIdentifiers();
                        for (Identifier id : pat.getIdentifiers()) {

                            PatientPreferencesType patientPrefs = new PatientPreferencesType();
                            patientPrefs.setAssigningAuthority(id.getOrganizationId());
                            patientPrefs.setPatientId(id.getId());
                            //toggle current setting
                            patientPrefs.setOptIn(!pat.isOptedIn());

                            AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                            AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                            StorePtConsentRequestType storeReq = new StorePtConsentRequestType();
                            storeReq.setPatientPreferences(patientPrefs);
                            StorePtConsentResponseType storeResp = adapterPIPProxy.storePtConsent(storeReq);
                            
                            if ("SUCCESS".equalsIgnoreCase(storeResp.getStatus())) {
                                isGoodStore = true;
        %>
        <h2>
            Updated patient: <% out.println(patDesc + " id: " + id.getId() + " of community: " + id.getOrganizationId());%>
        </h2>
        <%
                            }
                        }
                        if (!isGoodStore) {
        %>
        <h2>
            Failed to update patient: <% out.println(patDesc);%>
        </h2>
        <%
                        }
                    } else {
        %>
        <h2>
            Patient: <% out.println(patDesc + " does not require an update");%>
        </h2>
        <%
                    }
                }
            }
        }
        %>
        <hr>
        <form name="patientInfo" action="patientInfo.jsp" method="POST">
            <input type="hidden" name="token" value=<%= paramToken%> />
            <input type="submit" value="Define Patient Authorization" />
        </form>
    </body>
</html>
