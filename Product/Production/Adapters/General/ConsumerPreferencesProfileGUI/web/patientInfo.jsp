<%@ page import="java.util.*" %>
<%@ page import="gov.hhs.fha.nhinc.mpilib.*" %>
<%@ page import="gov.hhs.fha.nhinc.properties.*" %>
<%@ page import="gov.hhs.fha.nhinc.policyengine.adapterpip.proxy.*" %>
<%@ page import="gov.hhs.fha.nhinc.common.nhinccommonadapter.*" %>
<%@ page import="gov.hhs.fha.nhinc.transform.subdisc.*" %>
<%@ page import="org.hl7.v3.*" %>
<%@ page import="gov.hhs.fha.nhinc.mpi.proxy.*" %>
<%@ page import="gov.hhs.fha.nhinc.nhinclib.*" %>
<%@ page import="gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Patient Authorization</title>
    </head>
    <body>
        <img src="connect.gif" width="650" height="250" alt="connect"/>
        <h2>Opt In/Opt Out Utility</h2>

        <%
        String paramToken = request.getParameter("token");
        //out.println("PatientInfo has token: " + paramToken);
        if (paramToken == null || paramToken.isEmpty()) {
            RequestDispatcher disp = request.getRequestDispatcher("login.jsp");
            disp.forward(request, response);
        } else {
            String orgId = PropertyAccessor.getProperty("gateway", "localHomeCommunityId");
            String assigningAuthId = PropertyAccessor.getProperty("adapter", "assigningAuthorityId");
        %>

        <b><big>Home Community Id: <% out.println(orgId);%></big></b>

        <br><br>Patient search criteria:<br>
        <form method="POST">
            <input type="hidden" name="token" value=<%= paramToken%> />
            <table border="0" cellspacing="5">
                <tbody>
                    <tr>
                        <td>First Name:</td>
                        <td><input type="text" name="firstName" value="" size="20"/></td>
                    </tr>
                    <tr>
                        <td>Last Name:</td>
                        <td><input type="text" name="lastName" value="" size="20"/></td>
                    </tr>
                    <tr>
                        <td>Identifier:</td>
                        <td><input type="text" name="patientId" value="" size="20"/></td>
                    </tr>
                </tbody>
            </table>
            <br>
            <input type="submit" value="Submit"/>
            <input type="reset" value="Reset"/>
            <br><br>
        </form>
        <hr><br>
        <%
            String searchFirstName = request.getParameter("firstName");
            String searchLastName = request.getParameter("lastName");
            String searchId = request.getParameter("patientId");

            if ((searchFirstName != null && !searchFirstName.isEmpty()) ||
                    (searchLastName != null && !searchLastName.isEmpty()) ||
                    (searchId != null && !searchId.isEmpty())) {

                II patId = new II();
                patId.setExtension(searchId);
                patId.setRoot(assigningAuthId);
                PRPAMT201301UVPatient patient = HL7PatientTransforms.create201301Patient(HL7PatientTransforms.create201301PatientPerson(searchFirstName, searchLastName, null, null, null), patId);
                PRPAIN201305UV searchPat = HL7PRPA201305Transforms.createPRPA201305(patient, orgId, orgId, assigningAuthId);

                AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
                AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
                PRPAIN201306UV patients = mpiProxy.findCandidates(searchPat);

                Patients pats = new Patients();
                Patient searchPatient = new Patient();

                if (patients != null &&
                        patients.getControlActProcess() != null &&
                        NullChecker.isNotNullish(patients.getControlActProcess().getSubject()) &&
                        patients.getControlActProcess().getSubject().get(0) != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent() != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1() != null &&
                        patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient() != null) {
                     PRPAMT201310UVPatient mpiPatResult = patients.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();

                     if (NullChecker.isNotNullish(mpiPatResult.getId()) &&
                             mpiPatResult.getId().get(0) != null &&
                             NullChecker.isNotNullish(mpiPatResult.getId().get(0).getExtension()) &&
                             NullChecker.isNotNullish(mpiPatResult.getId().get(0).getRoot())) {
                        searchPatient.getIdentifiers().add(mpiPatResult.getId().get(0).getExtension(), mpiPatResult.getId().get(0).getRoot());
                     }
                     
                     if (mpiPatResult.getPatientPerson() != null &&
                             mpiPatResult.getPatientPerson().getValue() != null &&
                             mpiPatResult.getPatientPerson().getValue().getName() != null) {
                         PersonNameType name = HL7Extractors.translatePNListtoPersonNameType(mpiPatResult.getPatientPerson().getValue().getName());
                         PersonName personName = new PersonName();
                         personName.setFirstName(name.getGivenName());
                         personName.setLastName(name.getFamilyName());
                         searchPatient.setName(personName);
                     }

                     pats.add(searchPatient);
                }

                if (!pats.isEmpty()) {
                    session.setAttribute("patientList", pats);
        %>

        <form method="POST" action="update.jsp">
            <input type="hidden" name="token" value=<%= paramToken%> />
            <table cellpadding="10" border="1">
                <tr>
                    <th>Last Name</th>
                    <th>First Name</th>
                    <th>Organization Id:</th>
                    <th>Patient Id:</th>
                    <th>Opt In:</th>
                </tr>

                <%

                for (int x = 0; x < pats.size(); x++) {
                    Patient pat = pats.get(x);
                    PersonName name = pat.getName();

                    out.println("<tr> ");
                    if (name != null && name.getLastName() != null && !name.getLastName().isEmpty()) {
                        out.println("<td>" + name.getLastName() + "</td>");
                    } else {
                        out.println("<td>&nbsp;</td>");
                    }

                    if (name != null && name.getFirstName() != null && !name.getFirstName().isEmpty()) {
                        out.println("<td>" + name.getFirstName() + "</td>");
                    } else {
                        out.println("<td>&nbsp;</td>");
                    }

                    String checked = "";
                    pat.setOptedIn(false);
                    AdapterPIPProxyObjectFactory factory = new AdapterPIPProxyObjectFactory();
                    AdapterPIPProxy adapterPIPProxy = factory.getAdapterPIPProxy();
                    RetrievePtConsentByPtIdRequestType consentReq = new RetrievePtConsentByPtIdRequestType();

                    Identifiers newIds = new Identifiers();
                    for (Identifier id : pat.getIdentifiers()) {
                        if (id.getOrganizationId().equals(assigningAuthId)) {
                            out.println("<td>" + id.getOrganizationId() + "</td>");
                            out.println("<td>" + id.getId() + "</td>");

                            consentReq.setAssigningAuthority(id.getOrganizationId());
                            consentReq.setPatientId(id.getId());
                            RetrievePtConsentByPtIdResponseType consentResp = adapterPIPProxy.retrievePtConsentByPtId(consentReq);
                            if (consentResp.getPatientPreferences().isOptIn()) {
                                checked = "checked";
                                pat.setOptedIn(true);
                            }
                            newIds.add(id);
                        }
                    }
                    if (newIds.isEmpty()) {
                        out.println("<td>&nbsp;</td>");
                        out.println("<td>&nbsp;</td>");
                        out.println("<td>&nbsp;</td>");
                    } else {
                        out.println("<td><input type='checkbox' name='chk_" + x + "' " + checked + "/></td>");
                    }
                    pat.setIdentifiers(newIds);

                    out.println("</tr> ");
                }
                %>
            </table>
            <br>
            <button name="submit" type="submit" value="submit">Update Patient Authorization</button>
        </form>
        <%
            } else {%>
        <br>
        <b><big>No matching patients found</big></b>
        <% }
            }
        }
        %>
    </body>
</html>

