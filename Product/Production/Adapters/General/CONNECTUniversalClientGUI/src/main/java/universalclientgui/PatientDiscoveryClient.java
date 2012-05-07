/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met: 
 *     * Redistributions of source code must retain the above 
 *       copyright notice, this list of conditions and the following disclaimer. 
 *     * Redistributions in binary form must reproduce the above copyright 
 *       notice, this list of conditions and the following disclaimer in the documentation 
 *       and/or other materials provided with the distribution. 
 *     * Neither the name of the United States Government nor the 
 *       names of its contributors may be used to endorse or promote products 
 *       derived from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package universalclientgui;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;

import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author patlollav
 */
public class PatientDiscoveryClient {

    private static final String PROPERTY_FILE_NAME = "gateway";
    private static final String PROPERTY_FILE_KEY_LOCAL_DEVICE = "localDeviceId";
    private static final String PROPERTY_FILE_KEY_HOME_COMMUNITY = "localHomeCommunityId";
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscovery";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscovery";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoveryPortSoap";
    private static final String WSDL_FILE = "EntityPatientDiscovery.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:RespondingGateway_PRPA_IN201305UV02";
    private static final String SERVICE_NAME = NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME;

    private static Log log = null;
    private static Service cachedService = null;
    private WebServiceProxyHelper oProxyHelper = new WebServiceProxyHelper();

    private Log getLog() {
        if (log == null) {
            log = LogFactory.getLog(getClass());
        }
        return log;
    }

    
    /**
     * Retrieve the service class for this web service.
     *
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                getLog().error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    /**
     * Retrieve the local home community id
     * 
     * @return Local home community id
     * 
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */

    private String getHomeCommunityId() throws PropertyAccessException {
        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_HOME_COMMUNITY);
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    protected String getEndpointURL() throws ConnectionManagerException, PropertyAccessException {
        return ConnectionManagerCache.getInstance().getInternalEndpointURLByServiceName(SERVICE_NAME);
    }

    /**
     * This method retrieves and initializes the port.
     *
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected EntityPatientDiscoveryPortType getPort(String url, String wsAddressingAction, AssertionType assertion) {
        EntityPatientDiscoveryPortType port = null;
        Service service = getService();
        if (service != null) {
            getLog().debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoveryPortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            getLog().error("Unable to obtain serivce - no port created.");
        }
        return port;
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

            String orgId = getHomeCommunityId();

            PRPAIN201305UV02 request201305 = this.create201305(patientSearchData, orgId);

            request.setPRPAIN201305UV02(request201305);

            String url = getEndpointURL();
            if (NullChecker.isNotNullish(url)) {
                EntityPatientDiscoveryPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);
                oProxyHelper.invokePort(port, EntityPatientDiscoveryPortType.class, "respondingGatewayPRPAIN201305UV02", request);
            } else {
                log.error("Error getting URL for: " + SERVICE_NAME + "url is null");
            }

        } catch (Exception ex) {
            getLog().error("Exception in patient discovery", ex);
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
            localDeviceId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_LOCAL_DEVICE);
        } catch (PropertyAccessException ex) {
            getLog().error(ex);
        }

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientSearchData.getFirstName(), patientSearchData.getLastName(), patientSearchData.getGender(), patientSearchData.getDob(), patientSearchData.getSsn());
        //PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientSearchData.getPatientId(), localDeviceId);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientSearchData.getPatientId(), patientSearchData.getAssigningAuthorityID());


        //resp = HL7PRPA201305Transforms.createPRPA201305(patient, patientSearchData.getAssigningAuthorityID(), receiverOID, localDeviceId);
        resp = HL7PRPA201305Transforms.createPRPA201305(patient, localDeviceId, receiverOID, patientSearchData.getAssigningAuthorityID());

        return resp;
    }
 }
