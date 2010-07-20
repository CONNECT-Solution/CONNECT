/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adaptercomponentmpi;

import gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerService;
import gov.hhs.fha.nhinc.adapter.commondatalayer.CommonDataLayerPortType;
import gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers.HL7Parser201305;
import gov.hhs.fha.nhinc.adaptercomponentmpi.hl7parsers.HL7Parser201306;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.*;

/**
 *
 * @author Jon Hoppesch
 */
public class PatientChecker {

    private static Log log = LogFactory.getLog(PatientChecker.class);

    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    private static final String SERVICE_NAME_COMMON_DATA_LAYER_SERVICE = "adaptercommondatalayerservice";

    public static PRPAIN201306UV02 FindPatient(PRPAIN201305UV02 query) {
        log.debug("Entering PatientChecker.FindPatient method...");
        //declare request and response
        PRPAIN201306UV02 result = null;
        FindPatientsPRPAIN201305UV02RequestType request=null;
        FindPatientsPRPAMT201310UV02ResponseType findPatientsResponse = null;

        if (HL7Parser201305.ExtractHL7QueryParamsFromMessage(query) == null) {
            log.error("no query parameters were supplied");
        } else {
            // Get the Home community ID for this box...
            //------------------------------------------
            String sHomeCommunityId = "";
            String sEndpointURL = "";
            try {
               sHomeCommunityId = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, HOME_COMMUNITY_ID_PROPERTY);
            }
            catch (Exception e) {
                log.error("Failed to read " + HOME_COMMUNITY_ID_PROPERTY +
                          " property from the " + GATEWAY_PROPERTY_FILE + ".properties  file.  Error: " +
                          e.getMessage(), e);
            }

            // Get the endpoint URL for the common data layer service
            //------------------------------------------
            CommonDataLayerService service = new CommonDataLayerService ();
            CommonDataLayerPortType port = service.getCommonDataLayerPort();

            if ((sHomeCommunityId != null) && (sHomeCommunityId.length() > 0)) {
                try {
                    sEndpointURL = ConnectionManagerCache.getEndpointURLByServiceName(sHomeCommunityId, SERVICE_NAME_COMMON_DATA_LAYER_SERVICE);
                }
                catch (Exception e) {
                    log.error("Failed to retrieve endpoint URL for service:" + SERVICE_NAME_COMMON_DATA_LAYER_SERVICE +
                              " from connection manager.  Error: " + e.getMessage(), e);
                }
            }

            if ((sEndpointURL != null) &&
                (sEndpointURL.length() > 0)) {
				gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, sEndpointURL);
            }
            else {
                // Just a way to cover ourselves for the time being...  - assume port 8080
                //-------------------------------------------------------------------------
				gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, "http://localhost:8080/CommonDataLayerService/AdapterCommonDataLayer");

                log.warn("Did not find endpoint URL for service: " + SERVICE_NAME_COMMON_DATA_LAYER_SERVICE + " and " +
                         "Home Community: " + sHomeCommunityId + ".  Using default URL: " +
                         "'http://localhost:8080/CommonDataLayerService/AdapterCommonDataLayer'");
            }

            //message
            PRPAIN201305UV02MCCIMT000100UV01Message patientMessage = new org.hl7.v3.PRPAIN201305UV02MCCIMT000100UV01Message();
            patientMessage.setControlActProcess(query.getControlActProcess());

            //request and query
            log.info("perform patient lookup via common data layer service");
            request = new org.hl7.v3.FindPatientsPRPAIN201305UV02RequestType();
            request.setQuery(patientMessage);
            
        int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    findPatientsResponse = port.findPatients(request);
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
                            log.error("Thread Got Interrupted while waiting on PatientChecker call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on PatientChecker call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call PatientChecker Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call PatientChecker Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            findPatientsResponse = port.findPatients(request);
        }
		
            if (findPatientsResponse != null) {

                if ((findPatientsResponse.getSubject() == null) || 
                     findPatientsResponse.getSubject().isEmpty() ||
                     (findPatientsResponse.getSubject().size() == 0)) {
                    log.info("patient not found in MPI");
                    result = null;
                } else if (findPatientsResponse.getSubject().size() > 1) {
                    log.info("multiple patients found in MPI [findPatientsResponse.getSubject().size()=" + findPatientsResponse.getSubject().size() + "]");
                    result = null;
                } else {
                    log.info("single patient found in MPI");
                    PRPAMT201310UV02Patient searchResultPatient = findPatientsResponse.getSubject().get(0);

                    result = HL7Parser201306.BuildMessageFromMpiPatient (searchResultPatient, query);
                }
            }
        }

        log.debug("Exiting PatientChecker.FindPatient method...");
        return result;
    }
}
