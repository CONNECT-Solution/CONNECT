/*
 * Copyright (c) 2009-2013, United States Government, as represented by the Secretary of Health and Human Services. 
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

import gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean;
import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredResponseSecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.EntityPatientDiscoveryDeferredResponseUnsecured;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.NhinPatientDiscoveryDeferredResponse;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.InboundPatientDiscoveryDeferredResponse;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.OutboundPatientDiscoveryDeferredResponse;

import javax.servlet.ServletContext;

/**
 * @author msw
 * 
 */
public class PatientDiscoveryDeferredResp10WebServices extends AbstractWebServicesMXBean {

    /** The Constant NHIN_PD_BEAN_NAME. */
    private static final String NHIN_PD_BEAN_NAME = "nhinPDResp";

    /** The Constant ENTITY_UNSECURED_PD_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_PD_BEAN_NAME = "entityPDRespUnsecured";

    /** The Constant ENTITY_SECURED_PD_BEAN_NAME. */
    private static final String ENTITY_SECURED_PD_BEAN_NAME = "entityPDRespSecured";

    /** The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.StandardInboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response.PassthroughInboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.StandardOutboundPatientDiscoveryDeferredResponse";

    /** The Constant DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response.PassthroughOutboundPatientDiscoveryDeferredResponse";

    private final serviceEnum serviceName = serviceEnum.PatientDiscoveryDeferredResponse;
    
    /**
     * @param sc
     */
    public PatientDiscoveryDeferredResp10WebServices(ServletContext sc) {
        super(sc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        NhinPatientDiscoveryDeferredResponse nhinPD = null;
        InboundPatientDiscoveryDeferredResponse inboundPD = null;

        nhinPD = retrieveBean(NhinPatientDiscoveryDeferredResponse.class, getNhinBeanName());
        inboundPD = retrieveDependency(InboundPatientDiscoveryDeferredResponse.class, className);

        nhinPD.setInboundPatientDiscoveryResponse(inboundPD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        EntityPatientDiscoveryDeferredResponseUnsecured entityPDUnsecured = null;
        EntityPatientDiscoveryDeferredResponseSecured entityPDSecured = null;
        OutboundPatientDiscoveryDeferredResponse inboundPD = null;

        inboundPD = retrieveDependency(OutboundPatientDiscoveryDeferredResponse.class, className);
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
        if (DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(inboundPatientDiscovery.getClass().getName())) {
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
        if (DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(outboundPatientDiscovery.getClass().getName())) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_PD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_PD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_PD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getInboundStandardClassName()
     */
    @Override
    protected String getInboundStandardClassName() {
        return DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getInboundPassthruClassName()
     */
    @Override
    protected String getInboundPassthruClassName() {
        return DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getOutboundStandardClassName()
     */
    @Override
    protected String getOutboundStandardClassName() {
        return DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getOutboundPassthruClassName()
     */
    @Override
    protected String getOutboundPassthruClassName() {
        return DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME;
    }

    public serviceEnum getServiceName() {
        return this.serviceName;
    }
}
