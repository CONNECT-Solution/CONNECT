package gov.hhs.fha.nhinc.gateway.servlet;

import gov.hhs.fha.nhinc.docquery.entity.test.EntityDocQueryOrchImplTest;

import gov.hhs.fha.nhinc.gateway.executorservice.DQProcessor;
import gov.hhs.fha.nhinc.gateway.executorservice.DQClient;
import gov.hhs.fha.nhinc.gateway.executorservice.ResponseWrapper;
import gov.hhs.fha.nhinc.gateway.executorservice.TaskExecutor;

import gov.hhs.fha.nhinc.gateway.util.AdhocQueryRequestGenerator;
import gov.hhs.fha.nhinc.gateway.util.ExceptionDump;
import gov.hhs.fha.nhinc.gateway.util.AssertionCreator;

import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.AssigningAuthorityHomeCommunityMappingHelper;

import java.io.StringWriter;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test Generator for DQ fanout test Called from index.jsp page
 * 
 * Generates test AdhocQueryRequest and Assertion and calls EntityDocQueryOrchImpl
 * 
 * Note that apache camel is used just to dump the final response object to a file, and to marshal the response to json
 * to be displayed on web page
 * 
 * @author paul.eftis
 */
public class DQFanoutTestServlet extends HttpServlet {

    private Log log = LogFactory.getLog(getClass());

    private static final String DQContextPath = "oasis.names.tc.ebxml_regrep.xsd.query._3";

    private long startTimeMillis = 0;
    private long endTimeMillis = 0;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) {
        log.debug("DQFanoutTestServlet::doGet");
        startTimeMillis = System.currentTimeMillis();

        int requestCount = 0;

        String requestCountStr = "";
        String patientid = "";
        String localhcid = "";
        String localaaid = "";
        String targethcid = "";

        boolean runTest = false;
        boolean validRequest = false;
        if ("true".equalsIgnoreCase(req.getParameter("rundqtest"))) {
            requestCountStr = req.getParameter("requestcount");
            if (requestCountStr != null && requestCountStr.length() > 0) {
                requestCount = Integer.parseInt(requestCountStr);
            } else {
                // shouldn't get here.....just default to 20 if we do
                requestCount = 20;
            }
            targethcid = req.getParameter("targethcid");
            runTest = true;
            validRequest = true;
            log.debug("DQFanoutTestServlet rundqtest has requestCount=" + requestCountStr + " and targethcid="
                    + targethcid);
        }
        if ("true".equalsIgnoreCase(req.getParameter("rundq"))) {
            patientid = req.getParameter("patientid");
            validRequest = true;
            log.debug("DQFanoutTestServlet rundq has patientid=" + patientid);
        }

        if (validRequest) {
            try {
                localhcid = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                        NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
                List<String> localaaidList = AssigningAuthorityHomeCommunityMappingHelper
                        .lookupAssigningAuthorities(localhcid);
                // if more than one, just take first
                if (localaaidList != null) {
                    localaaid = localaaidList.get(0);
                }

                String transactionId = (UUID.randomUUID()).toString();
                AdhocQueryResponse r = null;
                AssertionType assertion = (new AssertionCreator()).createAssertion();
                EntityDocQueryOrchImplTest orchestratorTest = new EntityDocQueryOrchImplTest(
                        InitServlet.getExecutorService(), InitServlet.getLargeJobExecutorService());

                // inbound request from adapter will have associated adhocqueryrequest
                // and saml assertion.......for test we will generate these
                if (runTest) {
                    patientid = "123456789";
                    AdhocQueryRequest dq = (new AdhocQueryRequestGenerator()).generateTestRequest(1, patientid,
                            localhcid, localaaid);

                    // setting requestCount sets flag for isTest
                    orchestratorTest.setTest(requestCount, localaaid);
                    r = orchestratorTest.respondingGatewayCrossGatewayQuery(dq, assertion, null);
                } else {
                    AdhocQueryRequest dq = (new AdhocQueryRequestGenerator()).generateTestRequest(1, patientid,
                            localhcid, localaaid);
                    r = orchestratorTest.respondingGatewayCrossGatewayQuery(dq, assertion, null);
                }
                endTimeMillis = System.currentTimeMillis();
                long transTimeMillis = endTimeMillis - startTimeMillis;

                log.debug("DQFanoutTestServlet task done and received response - transTimeMillis=" + transTimeMillis);
                if (r != null) {
                    // marshall response object to string to output to web page
                    JAXBContextHandler oHandler = new JAXBContextHandler();
                    JAXBContext jc = oHandler.getJAXBContext(DQContextPath);
                    javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
                    StringWriter stringWriter = new StringWriter();
                    marshaller.marshal(r, stringWriter);
                    String responsexml = stringWriter.toString();
                    System.out.println("DQFanoutTestServlet has response=" + responsexml);
                    response.getWriter().write(
                            "<html><body>" + "<p>" + responsexml + "</p>" + "<p>DQ Transaction took " + transTimeMillis
                                    + " milliseconds</p></body></html>");
                } else {
                    response.getWriter().write(
                            "<html><body>Null Response!!!......this should never happen</body></html>");
                }
            } catch (Exception e) {
                ExceptionDump.outputCompleteException(e);
                try {
                    response.getWriter().write(
                            "<html><body><p>Exception!!!" + "......this should never happen</p><p>Exception message="
                                    + e.getMessage() + "</p></body></html>");
                } catch (Exception ex) {
                }
            }
        } else {
            try {
                response.getWriter().write("<html><body>Invalid Request!!!......try again</body></html>");
            } catch (Exception ex) {
            }
        }

    }

    @SuppressWarnings("static-access")
    private void runGenericTest(int requestCount, String patientid) {
        try {
            AssertionType assertion = (new AssertionCreator()).createAssertion();
            String transactionId = (UUID.randomUUID()).toString();
            AdhocQueryRequest dq = (new AdhocQueryRequestGenerator()).generateTestRequest(1, patientid, "1.1", "1.1.1");
            DQProcessor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse, AdhocQueryResponse> dqprocessor = new DQProcessor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse, AdhocQueryResponse>();

            List<QualifiedSubjectIdentifierType> subjectList = new ArrayList<QualifiedSubjectIdentifierType>();
            for (int i = 0; i < requestCount; i++) {
                QualifiedSubjectIdentifierType identifier = new QualifiedSubjectIdentifierType();
                String aaId = "1.1";
                identifier.setAssigningAuthorityIdentifier(aaId);
                identifier.setSubjectIdentifier(patientid);
                subjectList.add(identifier);
            }

            DQClient<QualifiedSubjectIdentifierType, AdhocQueryRequest, ResponseWrapper> dqclient = new DQClient<QualifiedSubjectIdentifierType, AdhocQueryRequest, ResponseWrapper>(
                    transactionId, assertion, dq, "1.1", patientid);

            TaskExecutor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse> dqexecutor = new TaskExecutor<QualifiedSubjectIdentifierType, AdhocQueryRequest, AdhocQueryResponse>(
                    ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(subjectList.size()) ? InitServlet.getLargeJobExecutorService()
                            : InitServlet.getExecutorService(), dqprocessor, dqclient, subjectList, dq, transactionId);

            dqexecutor.executeTask();

            AdhocQueryResponse response = dqexecutor.getFinalResponse();

        } catch (Exception e) {
            ExceptionDump.outputCompleteException(e);
        }

    }

}
