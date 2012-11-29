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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import gov.hhs.fha.nhinc.generic.GenericFactory;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchFactory;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchestration;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.healthit.nhin.PatientDiscoveryFaultType;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;
import gov.hhs.fha.nhinc.aspect.InboundMessageEvent;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

/**
 * 
 * @author Neil Webb
 */

@Addressing(enabled = true)
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhinPatientDiscovery  implements ihe.iti.xcpd._2009.RespondingGatewayPortType {

    private NhinPatientDiscoveryImpl orchImpl;

   
    private WebServiceContext context;
    

    /**
     * A generic constructor.
     */
    public NhinPatientDiscovery() {
        super();
        // this is normally done with a @Resource injection
        // because we are using aop with cxf this will not
        // work.
        // see: http://mail-archives.apache.org/mod_mbox/cxf-users/200908.mbox/%3C200908131043.47706.dkulp%40apache.org%3E
      //  context = new WebServiceContextImpl();
    }

    /**
     * A constructor that takes a PD service factory.
     * 
     * @param serviceFactory the service factory.
     */
    public NhinPatientDiscovery(PatientDiscoveryServiceFactory serviceFactory) {
    }

    
    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    
    
    /**
     * The web service implementation of Patient Discovery.
     * 
     * @param body the body of the request
     * @return the Patient discovery Response
     * @throws PRPAIN201305UV02Fault a fault if there's an exception
     */
    @InboundMessageEvent(beforeBuilder = PRPAIN201305UV02EventDescriptionBuilder.class,
            afterReturningBuilder = PRPAIN201306UV02EventDescriptionBuilder.class, serviceType = "Patient Discovery",
            version = "1.0")
    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 body)
            throws PRPAIN201305UV02Fault {
        try {
            return orchImpl.respondingGatewayPRPAIN201305UV02(body, context);
        } catch (PatientDiscoveryException e) {
            PatientDiscoveryFaultType type = new PatientDiscoveryFaultType();
            type.setErrorCode("920");
            type.setMessage(e.getLocalizedMessage());
            PRPAIN201305UV02Fault fault = new PRPAIN201305UV02Fault(e.getMessage(), type);
            throw fault;
        }
    }

    public void setOrchestratorImpl(NhinPatientDiscoveryImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
    
    protected PatientDiscoveryAuditLogger getPatientDiscoveryAuditLogger() {
        return new PatientDiscoveryAuditLogger();
    }
    
    protected GenericFactory<InboundPatientDiscoveryOrchestration> getOrchestrationFactory() {
        return InboundPatientDiscoveryOrchFactory.getInstance();
    }

}
