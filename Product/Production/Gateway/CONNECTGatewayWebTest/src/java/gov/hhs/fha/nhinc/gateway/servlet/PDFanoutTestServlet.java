package gov.hhs.fha.nhinc.gateway.servlet;

import gov.hhs.fha.nhinc.patientdiscovery.entity.test.EntityPatientDiscoveryOrchImplTest;

import gov.hhs.fha.nhinc.gateway.util.PatientDiscoveryRequestGenerator;
import gov.hhs.fha.nhinc.gateway.util.ExceptionDump;
import gov.hhs.fha.nhinc.gateway.util.AssertionCreator;

import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import gov.hhs.fha.nhinc.transform.marshallers.JAXBContextHandler;
import javax.xml.bind.JAXBContext;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV.helpers.AssigningAuthorityHomeCommunityMappingHelper;

import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Test Generator for PD fanout test Called from index.jsp page
 * 
 * Generates test RespondingGatewayPRPAIN201305UV02RequestType and Assertion and calls EntityPatientDiscoveryOrchImpl
 * 
 * Note that apache camel is used just to dump the final response object to a file, and to marshal the response to json
 * to be displayed on web page
 * 
 * @author paul.eftis
 */
public class PDFanoutTestServlet extends HttpServlet {

    private Log log = LogFactory.getLog(getClass());

    private static final String PDContextPath = "org.hl7.v3";

    private long startTimeMillis = 0;
    private long endTimeMillis = 0;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) {
        log.debug("PDFanoutTestServlet::doGet");
        startTimeMillis = System.currentTimeMillis();

        int requestCount = 0;

        String requestCountStr = "";
        String serviceHcid = "";
        String patientid = "";
        String localhcid = "";
        String localaaid = "";
        String fname = "";
        String lname = "";
        String gender = "";
        String dob = "";

        boolean runTest = false;
        boolean validRequest = false;
        if ("true".equalsIgnoreCase(req.getParameter("runpdtest"))) {
            requestCountStr = req.getParameter("requestcount");
            if (requestCountStr != null && requestCountStr.length() > 0) {
                requestCount = Integer.parseInt(requestCountStr);
            } else {
                // shouldn't get here.....just default to 20 if we do
                requestCount = 20;
            }
            serviceHcid = req.getParameter("targethcid");
            runTest = true;
            validRequest = true;
            log.debug("PDFanoutTestServlet runpdtest has requestCount=" + requestCountStr + " and serviceHcid="
                    + serviceHcid);
        }

        if ("true".equalsIgnoreCase(req.getParameter("runpd"))) {
            patientid = req.getParameter("patientid");
            fname = req.getParameter("fname");
            lname = req.getParameter("lname");
            gender = req.getParameter("gender");
            dob = req.getParameter("dob");
            validRequest = true;
            log.debug("PDFanoutTestServlet runpd has patientid=" + patientid + " and fname=" + fname + " and lname="
                    + lname + " and gender=" + gender + " and dob=" + dob);
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
                // inbound request from adapter will have associated pdrequest
                // and saml assertion.......for test we will generate these
                String transactionId = (UUID.randomUUID()).toString();
                RespondingGatewayPRPAIN201306UV02ResponseType r = null;
                AssertionType assertion = (new AssertionCreator()).createAssertion();
                RespondingGatewayPRPAIN201305UV02RequestType pd = new RespondingGatewayPRPAIN201305UV02RequestType();
                pd.setNhinTargetCommunities(null);
                pd.setAssertion(assertion);
                EntityPatientDiscoveryOrchImplTest orchestrator = new EntityPatientDiscoveryOrchImplTest(
                        InitServlet.getExecutorService(), InitServlet.getLargeJobExecutorService());

                if (runTest) {
                    patientid = "123456789";
                    pd.setPRPAIN201305UV02(PatientDiscoveryRequestGenerator.create201305(patientid, serviceHcid,
                            "test fname", "test lname", "male", "01-01-1990", "111-11-1111", serviceHcid, null));

                    // lookup serviceUrl using hcid
                    NhinTargetSystemType target = new NhinTargetSystemType();
                    HomeCommunityType home = new HomeCommunityType();
                    home.setHomeCommunityId(serviceHcid);
                    target.setHomeCommunity(home);
                    String serviceUrl = getEndPointFromConnectionManager(target,
                            NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
                    // setTest sets flag for isTest
                    orchestrator.setTest(requestCount, serviceUrl);
                    r = orchestrator.respondingGatewayPRPAIN201305UV02(pd, assertion);
                } else {
                    pd.setPRPAIN201305UV02(PatientDiscoveryRequestGenerator.create201305(patientid, localaaid, fname,
                            lname, gender, dob, "111-11-1111", localhcid, null));
                    r = orchestrator.respondingGatewayPRPAIN201305UV02(pd, assertion);
                }
                endTimeMillis = System.currentTimeMillis();
                long transTimeMillis = endTimeMillis - startTimeMillis;

                log.debug("PDFanoutTestServlet taskexecutor done and received response - transTimeMillis="
                        + transTimeMillis);

                if (r != null) {
                    List<CommunityPRPAIN201306UV02ResponseType> responseList = r.getCommunityResponse();
                    String responsexml = "";
                    for (CommunityPRPAIN201306UV02ResponseType c : responseList) {
                        // marshall response object to string to output to web page
                        JAXBContextHandler oHandler = new JAXBContextHandler();
                        JAXBContext jc = oHandler.getJAXBContext(PDContextPath);
                        javax.xml.bind.Marshaller marshaller = jc.createMarshaller();
                        StringWriter stringWriter = new StringWriter();
                        marshaller.marshal(c.getPRPAIN201306UV02(), stringWriter);
                        responsexml += "<p>" + stringWriter.toString() + "</p>";
                    }
                    System.out.println("PDFanoutTestServlet has response=" + responsexml);
                    response.getWriter().write(
                            "<html><body>" + "<p>" + responsexml + "</p>" + "<p>PD Transaction took " + transTimeMillis
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

    protected String getEndPointFromConnectionManager(NhinTargetSystemType oTargetSystem, String sServiceName)
            throws ConnectionManagerException {
        return ConnectionManagerCache.getInstance().getEndpontURLFromNhinTarget(oTargetSystem, sServiceName);
    }

    // @SuppressWarnings("static-access")
    // private void runGenericTest(int requestCount){
    // try{
    // AssertionType assertion = (new AssertionCreator()).createAssertion();
    // String transactionId = (UUID.randomUUID()).toString();
    // RespondingGatewayPRPAIN201305UV02RequestType pd = new RespondingGatewayPRPAIN201305UV02RequestType();
    // pd.setNhinTargetCommunities(null);
    // pd.setAssertion(assertion);
    //
    // PDProcessor<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02,
    // RespondingGatewayPRPAIN201306UV02ResponseType> pdprocessor =
    // new PDProcessor<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, PRPAIN201306UV02,
    // RespondingGatewayPRPAIN201306UV02ResponseType>(assertion);
    // PDClient<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper> pdclient =
    // new PDClient<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType, ResponseWrapper>(assertion);
    //
    // List<UrlInfo> pdlist = new ArrayList<UrlInfo>();
    // for(int i = 0; i < requestCount; i++){
    // UrlInfo urlInfo = new UrlInfo();
    // urlInfo.setUrl("https://medvasrv.teambi.com:8181/gatewaysimulator/RespondingGateway_Service");
    // urlInfo.setHcid("1.1." + i);
    // pdlist.add(urlInfo);
    // }
    //
    //
    // TaskExecutor<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType,
    // RespondingGatewayPRPAIN201306UV02ResponseType> pdexecutor =
    // new TaskExecutor<UrlInfo, RespondingGatewayPRPAIN201305UV02RequestType,
    // RespondingGatewayPRPAIN201306UV02ResponseType>(
    // ExecutorServiceHelper.getInstance().checkExecutorTaskIsLarge(pdlist.size()) ?
    // InitServlet.getLargeJobExecutorService() : InitServlet.getExecutorService(),
    // pdprocessor, pdclient, pdlist, pd, transactionId);
    //
    // pdexecutor.executeTask();
    // RespondingGatewayPRPAIN201306UV02ResponseType response = pdexecutor.getFinalResponse();
    // }catch(Exception e){
    // ExceptionDump.outputCompleteException(e);
    // }
    //
    // }
}
