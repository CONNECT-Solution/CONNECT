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
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryUnsecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.NhinPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.InboundPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.OutboundPatientDiscovery;
import org.springframework.stereotype.Service;

/**
 * The Class PatientDiscovery10WebServices.
 *
 * @author msw
 */
@Service
public class PatientDiscovery10WebServices extends AbstractPDWebServicesMXBean {

    private final serviceEnum serviceName = serviceEnum.PatientDiscovery;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        NhinPatientDiscovery nhinPD = retrieveBean(NhinPatientDiscovery.class, getNhinBeanName());
        InboundPatientDiscovery inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
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
        EntityPatientDiscoveryUnsecured entityPD = retrieveBean(EntityPatientDiscoveryUnsecured.class,
                getEntityUnsecuredBeanName());
        OutboundPatientDiscovery outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
        if (compareClassName(outboundPatientDiscovery, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /**
     * Configure inbound implementation. The inbound orchestration implementation provided via the className param is
     * set on the Nhin interface bean. This method uses specific types and passes them to the generic
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)} and
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)} methods.
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImplementation(java.lang.String)
     */
    @Override
    public void configureInboundStdImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        NhinPatientDiscovery nhinPD;
        InboundPatientDiscovery inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscovery.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscovery.class, getStandardInboundBeanName());

        nhinPD.setInboundPatientDiscovery(inboundPD);
    }


    @Override
    public void configureInboundPtImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        NhinPatientDiscovery nhinPD;
        InboundPatientDiscovery inboundPD;

        nhinPD = retrieveBean(NhinPatientDiscovery.class, getNhinBeanName());
        inboundPD = retrieveBean(InboundPatientDiscovery.class, getPassthroughInboundBeanName());

        nhinPD.setInboundPatientDiscovery(inboundPD);
    }

    /**
     * Configure outbound implementation. The outbound orchestration implementation provided via the className param is
     * set on the Nhin interface bean. This method uses specific types and passes them to the generic
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)} and
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)} methods.
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImplementation(java.lang.String)
     */
    @Override
    public void configureOutboundStdImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        EntityPatientDiscoveryUnsecured entityPD;
        OutboundPatientDiscovery outboundPD;

        entityPD = retrieveBean(EntityPatientDiscoveryUnsecured.class, getEntityUnsecuredBeanName());
        outboundPD = retrieveBean(OutboundPatientDiscovery.class, getStandardOutboundBeanName());

        entityPD.setOutboundPatientDiscovery(outboundPD);
    }

    @Override
    public void configureOutboundPtImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        EntityPatientDiscoveryUnsecured entityPD;
        OutboundPatientDiscovery outboundPD;

        entityPD = retrieveBean(EntityPatientDiscoveryUnsecured.class, getEntityUnsecuredBeanName());
        outboundPD = retrieveBean(OutboundPatientDiscovery.class, getPassthroughOutboundBeanName());

        entityPD.setOutboundPatientDiscovery(outboundPD);
    }

    @Override
    public serviceEnum getServiceName() {
        return serviceName;
    }

    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityPatientDiscoveryUnsecured entityPD = retrieveBean(EntityPatientDiscoveryUnsecured.class,
                getEntityUnsecuredBeanName());
        OutboundPatientDiscovery outboundPatientDiscovery = entityPD.getOutboundPatientDiscovery();
        if (compareClassName(outboundPatientDiscovery, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    @Override
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinPatientDiscovery nhinPD = retrieveBean(NhinPatientDiscovery.class,
                getNhinBeanName());
        InboundPatientDiscovery inboundPatientDiscovery = nhinPD.getInboundPatientDiscovery();
        if (compareClassName(inboundPatientDiscovery, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

}
