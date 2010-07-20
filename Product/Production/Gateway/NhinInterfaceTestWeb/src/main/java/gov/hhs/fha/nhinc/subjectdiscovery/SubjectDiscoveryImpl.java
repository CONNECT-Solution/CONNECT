/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subjectdiscovery;

import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhincsubjectdiscovery.NhincSubjectDiscoveryPortType;
import gov.hhs.fha.nhinc.nhincsubjectdiscovery.NhincSubjectDiscoveryService;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractorHelper;
import java.util.StringTokenizer;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author jhoppesc
 */
public class SubjectDiscoveryImpl {

    private static Log log = LogFactory.getLog(SubjectDiscoveryImpl.class);
    private static final String SERVICE_NAME = "mocksubjectdiscovery";

    public static MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201301UV");

        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();
        PIXConsumerPRPAIN201301UVRequestType pix201301Request = new PIXConsumerPRPAIN201301UVRequestType();

        // Determine the receiving home community id
        String homeCommunityId = null;
        if (message.getReceiver() != null &&
                message.getReceiver().size() > 0 &&
                message.getReceiver().get(0) != null &&
                message.getReceiver().get(0).getDevice() != null &&
                message.getReceiver().get(0).getDevice().getId() != null &&
                message.getReceiver().get(0).getDevice().getId().size() > 0 &&
                message.getReceiver().get(0).getDevice().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            homeCommunityId = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }

        pix201301Request.setPRPAIN201301UV02(message);
        pix201301Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        NhincSubjectDiscoveryService service = new NhincSubjectDiscoveryService();
        NhincSubjectDiscoveryPortType port = service.getNhincSubjectDiscoveryPortSoap11();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    ackMsg = port.pixConsumerPRPAIN201301UV(pix201301Request);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    int flag = 0;
                    StringTokenizer st = new StringTokenizer(exceptionText, ",");
                    while (st.hasMoreTokens()) {
                        if (e.getMessage().contains(st.nextToken())) {
                            flag = 1;
                        }
                    }
                    if (flag == 1) {
                        log.warn("Exception calling ... web service: " + e.getMessage());
                        System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                        log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                        i++;
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException iEx) {
                            log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call mocksubjectdiscovery Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call mocksubjectdiscovery Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            ackMsg = port.pixConsumerPRPAIN201301UV(pix201301Request);
        }
		
        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201301UV");
        return ackMsg;
    }

    public static MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201302UV");

        MCCIIN000002UV01 ackMsg = new MCCIIN000002UV01();
        PIXConsumerPRPAIN201302UVRequestType pix201302Request = new PIXConsumerPRPAIN201302UVRequestType();

        pix201302Request.setPRPAIN201302UV02(message);
        pix201302Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Determine the receiving home community id
        String homeCommunityId = null;
        if (message.getReceiver() != null &&
                message.getReceiver().size() > 0 &&
                message.getReceiver().get(0) != null &&
                message.getReceiver().get(0).getDevice() != null &&
                message.getReceiver().get(0).getDevice().getId() != null &&
                message.getReceiver().get(0).getDevice().getId().size() > 0 &&
                message.getReceiver().get(0).getDevice().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            homeCommunityId = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }

        NhincSubjectDiscoveryService service = new NhincSubjectDiscoveryService();
        NhincSubjectDiscoveryPortType port = service.getNhincSubjectDiscoveryPortSoap11();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    ackMsg = port.pixConsumerPRPAIN201302UV(pix201302Request);
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    log.warn("Exception calling ... web service: " + e.getMessage());
                    System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                    log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                    i++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException iEx) {
                        log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call mocksubjectdiscovery Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            ackMsg = port.pixConsumerPRPAIN201302UV(pix201302Request);
        }
		


        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201302UV");
        return ackMsg;
    }


    public static MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV02 message, WebServiceContext context) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public static PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PRPAIN201309UV02 message, WebServiceContext context) {
        log.debug("Entering SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV");

        PRPAIN201310UV02 ret310 = new PRPAIN201310UV02();
        PIXConsumerPRPAIN201309UVRequestType pix201309Request = new PIXConsumerPRPAIN201309UVRequestType();

        pix201309Request.setPRPAIN201309UV02(message);
        pix201309Request.setAssertion(SamlTokenExtractor.GetAssertion(context));

        // Determine the receiving home community id
        String homeCommunityId = null;
        if (message.getReceiver() != null &&
                message.getReceiver().size() > 0 &&
                message.getReceiver().get(0) != null &&
                message.getReceiver().get(0).getDevice() != null &&
                message.getReceiver().get(0).getDevice().getId() != null &&
                message.getReceiver().get(0).getDevice().getId().size() > 0 &&
                message.getReceiver().get(0).getDevice().getId().get(0) != null &&
                NullChecker.isNotNullish(message.getReceiver().get(0).getDevice().getId().get(0).getRoot())) {
            homeCommunityId = message.getReceiver().get(0).getDevice().getId().get(0).getRoot();
        }

        NhincSubjectDiscoveryService service = new NhincSubjectDiscoveryService();
        NhincSubjectDiscoveryPortType port = service.getNhincSubjectDiscoveryPortSoap11();
		gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, SamlTokenExtractorHelper.getEndpointURL(homeCommunityId, SERVICE_NAME));
        
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
        int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        if (retryCount > 0 && retryDelay > 0) {
            int i = 1;
            javax.xml.ws.WebServiceException catchExp = null;
            while (i <= retryCount) {
                try {
                    ret310 = port.pixConsumerPRPAIN201309UV(pix201309Request).getPRPAIN201310UV02();
                    break;
                } catch (javax.xml.ws.WebServiceException e) {
                    catchExp = e;
                    log.warn("Exception calling ... web service: " + e.getMessage());
                    System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                    log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                    i++;
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException iEx) {
                        log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iEx);
                    } catch (IllegalArgumentException iaEx) {
                        log.error("Thread Got Interrupted while waiting on mocksubjectdiscovery call :" + iaEx);
                    }
                    retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                }
            }

            if (retryCount == 0 && catchExp != null) {
                log.error("Unable to call mocksubjectdiscovery Webservice due to  : " + catchExp);
                throw catchExp;
            }
        } else {
            ret310 = port.pixConsumerPRPAIN201309UV(pix201309Request).getPRPAIN201310UV02();
        }
		
        log.debug("Exiting SubjectDiscoveryImpl.pixConsumerPRPAIN201309UV");
        return ret310;
    }
}
