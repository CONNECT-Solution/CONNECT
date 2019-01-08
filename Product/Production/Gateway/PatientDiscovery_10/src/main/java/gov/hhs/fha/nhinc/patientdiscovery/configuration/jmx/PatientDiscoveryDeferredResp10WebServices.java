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
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredResponseSecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredResponseUnsecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.NhinPatientDiscoveryDeferredResponse;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.InboundPatientDiscoveryDeferredResponse;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.OutboundPatientDiscoveryDeferredResponse;
import org.springframework.stereotype.Service;

/**
 * @author msw
 *
 */
@Service
public class PatientDiscoveryDeferredResp10WebServices extends AbstractPDDeferredResponseWebServicesMXBean {

    private final serviceEnum serviceName = serviceEnum.PatientDiscoveryDeferredResponse;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinPatientDiscoveryDeferredResponse nhinPD;
        InboundPatientDiscoveryDeferredResponse inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscoveryDeferredResponse.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscoveryDeferredResponse.class, getStandardInboundBeanName());

        nhinPD.setInboundPatientDiscoveryResponse(inboundPD);
    }


    @Override
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinPatientDiscoveryDeferredResponse nhinPD;
        InboundPatientDiscoveryDeferredResponse inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscoveryDeferredResponse.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscoveryDeferredResponse.class, getPassthroughInboundBeanName());

        nhinPD.setInboundPatientDiscoveryResponse(inboundPD);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        EntityPatientDiscoveryDeferredResponseUnsecured entityPDUnsecured;
        EntityPatientDiscoveryDeferredResponseSecured entityPDSecured;
        OutboundPatientDiscoveryDeferredResponse inboundPD;
        inboundPD = retrieveBean(OutboundPatientDiscoveryDeferredResponse.class, getStandardOutboundBeanName());
        entityPDUnsecured = retrieveBean(EntityPatientDiscoveryDeferredResponseUnsecured.class,
            getEntityUnsecuredBeanName());
        entityPDSecured = retrieveBean(EntityPatientDiscoveryDeferredResponseSecured.class, getEntitySecuredBeanName());

        entityPDUnsecured.setOutboundPatientDiscoveryResponse(inboundPD);
        entityPDSecured.setOutboundPatientDiscoveryResponse(inboundPD);
    }


    @Override
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        EntityPatientDiscoveryDeferredResponseUnsecured entityPDUnsecured;
        EntityPatientDiscoveryDeferredResponseSecured entityPDSecured;
        OutboundPatientDiscoveryDeferredResponse inboundPD;
        inboundPD = retrieveBean(OutboundPatientDiscoveryDeferredResponse.class, getPassthroughOutboundBeanName());
        entityPDUnsecured = retrieveBean(EntityPatientDiscoveryDeferredResponseUnsecured.class,
            getEntityUnsecuredBeanName());
        entityPDSecured = retrieveBean(EntityPatientDiscoveryDeferredResponseSecured.class, getEntitySecuredBeanName());

        entityPDUnsecured.setOutboundPatientDiscoveryResponse(inboundPD);
        entityPDSecured.setOutboundPatientDiscoveryResponse(inboundPD);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        NhinPatientDiscoveryDeferredResponse nhinPD = retrieveBean(NhinPatientDiscoveryDeferredResponse.class,
            getNhinBeanName());
        InboundPatientDiscoveryDeferredResponse inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
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
        EntityPatientDiscoveryDeferredResponseUnsecured entityPD = retrieveBean(
            EntityPatientDiscoveryDeferredResponseUnsecured.class, getEntityUnsecuredBeanName());
        OutboundPatientDiscoveryDeferredResponse outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
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
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityPatientDiscoveryDeferredResponseUnsecured entityPD = retrieveBean(
            EntityPatientDiscoveryDeferredResponseUnsecured.class, getEntityUnsecuredBeanName());
        OutboundPatientDiscoveryDeferredResponse outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
        if (compareClassName(outboundPatientDiscovery, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinPatientDiscoveryDeferredResponse nhinPD = retrieveBean(
            NhinPatientDiscoveryDeferredResponse.class, getNhinBeanName());
        InboundPatientDiscoveryDeferredResponse inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
        if (compareClassName(inboundPatientDiscovery, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

}
