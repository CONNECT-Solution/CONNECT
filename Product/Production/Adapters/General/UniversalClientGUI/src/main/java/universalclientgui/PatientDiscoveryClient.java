/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscovery;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author patlollav
 */
public class PatientDiscoveryClient {

    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_LOCAL_HOME_COMMUNITY = "localHomeCommunityId";
    private static Log log = LogFactory.getLog(PatientDiscoveryClient.class);
    private static final String PROPERTY_FILE_KEY_ASSIGN_AUTH = "assigningAuthorityId";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    /**
     * Entity Patient Discovery service
     */
    private static EntityPatientDiscovery service = new EntityPatientDiscovery();

    /**
     *
     * @param url
     * @return
     */
    private EntityPatientDiscoveryPortType getPort(String url) {

        if (service == null) {
            service = new EntityPatientDiscovery();
        }

        EntityPatientDiscoveryPortType port = service.getEntityPatientDiscoveryPortSoap11();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

    /**
     *
     * @return
     */
    private String getEntityPatientDiscoveryEndPointAddress() {

        String endpointAddress = null;

        try {
            // Lookup home community id
            String homeCommunity = getHomeCommunityId();
            // Get endpoint url
            endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME);
            log.debug("Entity Patient Discovery endpoint address: " + endpointAddress);
        } catch (PropertyAccessException pae) {
            log.error("Exception encountered retrieving the local home community: " + pae.getMessage(), pae);
        } catch (Exception cme) {
            log.error("Exception encountered retrieving the entity doc query connection endpoint address: " + cme.getMessage(), cme);
        }
        return endpointAddress;
    }

    /**
     * Retrieve the local home community id
     *
     * @return Local home community id
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }

    /**
     *
     * @param assertion
     * @param patientSearchData
     */
    public void broadcastPatientDiscovery(AssertionType assertion, PatientSearchData patientSearchData) {

        try {

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setAssertion(assertion);

            String localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_LOCAL_DEVICE);
            String orgId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_HOME_COMMUNITY);


            PRPAIN201305UV02 request201305 = this.create201305(patientSearchData, orgId);

            request.setPRPAIN201305UV02(request201305);
            
            EntityPatientDiscoveryPortType port = getPort(getEntityPatientDiscoveryEndPointAddress());

            RespondingGatewayPRPAIN201306UV02ResponseType response = null;
						
		int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
		int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
        String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
        javax.xml.ws.WebServiceException catchExp = null;
        if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
            int i = 1;
            while (i <= retryCount) {
                try {
                    response = port.respondingGatewayPRPAIN201305UV02(request);
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
                            log.error("Thread Got Interrupted while waiting on EntityPatientDiscovery call :" + iEx);
                        } catch (IllegalArgumentException iaEx) {
                            log.error("Thread Got Interrupted while waiting on EntityPatientDiscovery call :" + iaEx);
                        }
                        retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                    } else {
                        log.error("Unable to call EntityPatientDiscovery Webservice due to  : " + e);
                        throw e;
                    }
                }
            }

            if (i > retryCount) {
                log.error("Unable to call EntityPatientDiscovery Webservice due to  : " + catchExp);
                throw catchExp;
            }

        } else {
            response = port.respondingGatewayPRPAIN201305UV02(request);
        }
		        
        } catch (PropertyAccessException ex) {
            log.error("Exception in patient discovery", ex);
        }
    }

    /**
     * 
     * @param first
     * @param last
     * @param gender
     * @param birthdate
     * @param ssn
     * @param senderOID
     * @param receiverOID
     * @return
     */
    public PRPAIN201305UV02 create201305(PatientSearchData patientSearchData, String receiverOID) {
        PRPAIN201305UV02 resp = new PRPAIN201305UV02();

        String localDeviceId = null;

        try {
            localDeviceId = PropertyAccessor.getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_LOCAL_DEVICE);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(PatientDiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientSearchData.getFirstName(), patientSearchData.getLastName(), patientSearchData.getGender(), patientSearchData.getDob(), patientSearchData.getSsn());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientSearchData.getPatientId(), localDeviceId);

        resp = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchData.getAssigningAuthorityID(), receiverOID, localDeviceId);
        return resp;
    }

    /**
     *
     * @param first
     * @param last
     * @param gender
     * @param birthdate
     * @param ssn
     * @return
     */
    private PRPAMT201301UV02Patient createPatient(String first, String last, String gender, String birthdate, String ssn) {
        PRPAMT201301UV02Patient patient = new PRPAMT201301UV02Patient();
        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(first, last, gender, birthdate, ssn);
        II id = new II();

        patient = HL7PatientTransforms.create201301Patient(person, id);

        return patient;
    }


//    /**
//     * This will be removed once the broadcast PD is implented
//     */
//    private NhinTargetCommunitiesType createNhinTargetCommunities(){
//        NhinTargetCommunitiesType nhinTargetCommunities = new NhinTargetCommunitiesType();
//        NhinTargetCommunityType nhinTargetCommunity = new NhinTargetCommunityType();
//        nhinTargetCommunity.setList("List");
//        nhinTargetCommunity.setRegion("Region");
//
//        HomeCommunityType homeCommunity = new HomeCommunityType();
//        homeCommunity.setDescription("Description");
//        homeCommunity.setHomeCommunityId("2.2");
//
//        nhinTargetCommunity.setHomeCommunity(homeCommunity);
//        nhinTargetCommunities.getNhinTargetCommunity().add(nhinTargetCommunity);
//
//        return nhinTargetCommunities;
//    }

}
