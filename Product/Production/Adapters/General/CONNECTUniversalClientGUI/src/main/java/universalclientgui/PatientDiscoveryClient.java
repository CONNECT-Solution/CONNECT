/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
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
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.ObjectFactory;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscovery;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7QueryParamsTransforms;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ADExplicit;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.PNExplicit;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.hl7.v3.TELExplicit;
import org.hl7.v3.TSExplicit;

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
    private static final String UC_PROPERTY_FILE_NAME = "universalClient";
    private static final String UC_PROPERTY_FILE_KEY_SSA_OID = "ssa.oid";
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

        EntityPatientDiscoveryPortType port = service.getEntityPatientDiscoveryPortSoap();

        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

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
          //  endpointAddress = ConnectionManagerCache.getEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME);
            endpointAddress = ConnectionManagerCache.getInstance().getDefaultEndpointURLByServiceName(homeCommunity, NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME);
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
        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_LOCAL_HOME_COMMUNITY);
    }

    /**
     *
     * @param assertion
     * @param patientSearchData
     */
    public void broadcastPatientDiscovery(AssertionType assertion, PatientSearchData patientSearchData, List<String> communitiesOIDs) {

        try {

            RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
            request.setAssertion(assertion);

            String localDeviceId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_LOCAL_DEVICE);
            String orgId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_HOME_COMMUNITY);


            PRPAIN201305UV02 request201305 = this.create201305(patientSearchData, orgId);

            request.setPRPAIN201305UV02(request201305);

            //check to see if user specified a targeted gateway for the PD request
            if (communitiesOIDs != null && !communitiesOIDs.isEmpty())
            {

                log.debug("A target community has been requested.  Sending PD request to the following communities: ");

                //decalare target communities parameter
                NhinTargetCommunitiesType targetType = null;

                //set parameter equal to the oid value entered by user
                Page2 pageTwoInstance = new Page2();
                targetType = pageTwoInstance.createNhinTargetCommunities(communitiesOIDs);

                //set the Target Communities value in the Entitiy PD request and target only that gateway
                request.setNhinTargetCommunities(targetType);
            }
            
            //get Entity PD port
            EntityPatientDiscoveryPortType port = getPort(getEntityPatientDiscoveryEndPointAddress());

            //invoke PD 
            RespondingGatewayPRPAIN201306UV02ResponseType response = port.respondingGatewayPRPAIN201305UV02(request);
        
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
            localDeviceId = PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME, PROPERTY_FILE_KEY_LOCAL_DEVICE);
        } catch (PropertyAccessException ex) {
            Logger.getLogger(PatientDiscoveryClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        //create patientPerson to hold multiple names, addresses, etc.
        PRPAMT201310UV02Person person201310UV02 = new PRPAMT201310UV02Person();

         //add person telephone numbers
        if (patientSearchData.getPhoneNumbers() != null)
        {
            for (int i=0; i<((List <TELExplicit>)patientSearchData.getPhoneNumbers()).size(); i++ )
            {
               TELExplicit pnTelephone = ((List <TELExplicit>)patientSearchData.getPhoneNumbers()).get(i);

               person201310UV02.getTelecom().add(pnTelephone);
            }
        }


         //add person addresses
        if (patientSearchData.getAddress() != null)
        {
            for (int i=0; i<((List <ADExplicit>)patientSearchData.getAddress()).size(); i++ )
            {

                //get ADExplict value
                ADExplicit adName = ((List <ADExplicit>)patientSearchData.getAddress()).get(i);


                //add a PatientAddress value to list
                person201310UV02.getAddr().add(adName);
            }
        }

        //add names - according to the PD spec, each name must be within it's own LivingSubjectName object
        if (patientSearchData.getNames() != null)
        {
            for (int i=0; i<((List <PNExplicit>)patientSearchData.getNames()).size(); i++ )
            {

                //get ADExplict value
                PNExplicit patientName = ((List <PNExplicit>)patientSearchData.getNames()).get(i);

                //add a PatientAddress value to list
                person201310UV02.getName().add(patientName);
            }
        }

        
        //add gender
        CE argCE = new CE();
        argCE.setCode(patientSearchData.getGender().toString());
        person201310UV02.setAdministrativeGenderCode(argCE);

        //add DOB
        TSExplicit birthTime = new TSExplicit();
        birthTime.setValue(patientSearchData.getDob().toString());
        person201310UV02.setBirthTime(birthTime);

        //add SSN
        try {
            if (patientSearchData.getSsn() != null)
            {
                II subjectId = new II();
                subjectId.setExtension(patientSearchData.getSsn().toString());
                subjectId.setRoot(PropertyAccessor.getInstance().getProperty(UC_PROPERTY_FILE_NAME, UC_PROPERTY_FILE_KEY_SSA_OID));

                PRPAMT201310UV02OtherIDs subjectIdList= new PRPAMT201310UV02OtherIDs();
                subjectIdList.getId().add(subjectId);
                
                //add a SSN value to list
                person201310UV02.getAsOtherIDs().add(subjectIdList);
            }
        } catch (PropertyAccessException ex) {
            log.error("Exception cannot access SSA OID property in gateway.properties file", ex);
        }


        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(person201310UV02);


      //  JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson(patientSearchData.getFirstName(), patientSearchData.getLastName(), patientSearchData.getGender(), patientSearchData.getDob(), patientSearchData.getSsn());
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, patientSearchData.getPatientId().toString(), localDeviceId);

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


}
