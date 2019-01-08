/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredRequestSecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredRequestUnsecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.NhinPatientDiscoveryDeferredRequest;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request.InboundPatientDiscoveryDeferredRequest;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request.OutboundPatientDiscoveryDeferredRequest;
import org.springframework.stereotype.Service;

/**
 * @author msw
 *
 */
@Service
public class PatientDiscoveryDeferredReq10WebServices extends AbstractPDDeferredRequestWebServicesMXBean {

    private final serviceEnum serviceName = serviceEnum.PatientDiscoveryDeferredRequest;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinPatientDiscoveryDeferredRequest nhinPD;
        InboundPatientDiscoveryDeferredRequest inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscoveryDeferredRequest.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscoveryDeferredRequest.class, getStandardInboundBeanName() );

        nhinPD.setInboundPatientDiscoveryRequest(inboundPD);
    }


    @Override
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinPatientDiscoveryDeferredRequest nhinPD;
        InboundPatientDiscoveryDeferredRequest inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscoveryDeferredRequest.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscoveryDeferredRequest.class, getPassthroughInboundBeanName());

        nhinPD.setInboundPatientDiscoveryRequest(inboundPD);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        EntityPatientDiscoveryDeferredRequestUnsecured entityPDUnsecured;
        EntityPatientDiscoveryDeferredRequestSecured entityPDSecured;
        OutboundPatientDiscoveryDeferredRequest inboundPD;

        inboundPD = retrieveBean(OutboundPatientDiscoveryDeferredRequest.class, getStandardOutboundBeanName());

        entityPDUnsecured = retrieveBean(EntityPatientDiscoveryDeferredRequestUnsecured.class,
            getEntityUnsecuredBeanName());
        entityPDSecured = retrieveBean(EntityPatientDiscoveryDeferredRequestSecured.class, getEntitySecuredBeanName());

        entityPDUnsecured.setOutboundPatientDiscoveryRequest(inboundPD);
        entityPDSecured.setOutboundPatientDiscoveryRequest(inboundPD);
    }


    @Override
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        EntityPatientDiscoveryDeferredRequestUnsecured entityPDUnsecured;
        EntityPatientDiscoveryDeferredRequestSecured entityPDSecured;
        OutboundPatientDiscoveryDeferredRequest inboundPD;

        inboundPD = retrieveBean(OutboundPatientDiscoveryDeferredRequest.class, getPassthroughOutboundBeanName());

        entityPDUnsecured = retrieveBean(EntityPatientDiscoveryDeferredRequestUnsecured.class,
            getEntityUnsecuredBeanName());
        entityPDSecured = retrieveBean(EntityPatientDiscoveryDeferredRequestSecured.class, getEntitySecuredBeanName());

        entityPDUnsecured.setOutboundPatientDiscoveryRequest(inboundPD);
        entityPDSecured.setOutboundPatientDiscoveryRequest(inboundPD);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        NhinPatientDiscoveryDeferredRequest nhinPD = retrieveBean(NhinPatientDiscoveryDeferredRequest.class,
            getNhinBeanName());
        InboundPatientDiscoveryDeferredRequest inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
        if (compareClassName(inboundPatientDiscovery, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public boolean isOutboundPassthru() {
        boolean isPassthru = false;
        EntityPatientDiscoveryDeferredRequestUnsecured entityPD = retrieveBean(EntityPatientDiscoveryDeferredRequestUnsecured.class,
            getEntityUnsecuredBeanName());
        OutboundPatientDiscoveryDeferredRequest outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
        if (compareClassName(outboundPatientDiscovery, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    @Override
    public serviceEnum getServiceName() {
        return serviceName;
    }


    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundStandard()
     */
    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityPatientDiscoveryDeferredRequestUnsecured entityPD = retrieveBean(EntityPatientDiscoveryDeferredRequestUnsecured.class,
            getEntityUnsecuredBeanName());
        OutboundPatientDiscoveryDeferredRequest outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
        if (compareClassName(outboundPatientDiscovery, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundStandard()
     */
    @Override
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinPatientDiscoveryDeferredRequest nhinPD = retrieveBean(NhinPatientDiscoveryDeferredRequest.class,
            getNhinBeanName());
        InboundPatientDiscoveryDeferredRequest inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
        if (compareClassName(inboundPatientDiscovery, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }


}
