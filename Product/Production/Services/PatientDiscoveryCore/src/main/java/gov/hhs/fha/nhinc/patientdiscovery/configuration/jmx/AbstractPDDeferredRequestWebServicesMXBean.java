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

import gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean;

/**
 * @author achidambaram
 *
 */
public abstract class AbstractPDDeferredRequestWebServicesMXBean extends AbstractWebServicesMXBean {

    /**
     * The Constant NHIN_PD_BEAN_NAME.
     */
    private static final String NHIN_PD_BEAN_NAME_REQ = "nhinPDReq";

    /**
     * The Constant ENTITY_UNSECURED_PD_BEAN_NAME.
     */
    private static final String ENTITY_UNSECURED_PD_BEAN_NAME_REQ = "entityPDReqUnsecured";

    /**
     * The Constant ENTITY_SECURED_PD_BEAN_NAME.
     */
    private static final String ENTITY_SECURED_PD_BEAN_NAME_REQ = "entityPDReqSecured";

    /**
     * The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME.
     */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request.StandardInboundPatientDiscoveryDeferredRequest";

    /**
     * The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME.
     */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request.PassthroughInboundPatientDiscoveryDeferredRequest";

    /**
     * The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME.
     */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request.StandardOutboundPatientDiscoveryDeferredRequest";

    /**
     * The Constant DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME.
     */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request.PassthroughOutboundPatientDiscoveryDeferredRequest";

    /**
     * The Constant Standard_OutboundOrch_PDDeferredRequest_BEAN_NAME.
     */
    private static final String STDOUTBOUND_PD_BEAN_NAME_REQ = "stdPDReqOutbound";

    /**
     * The Constant Passthrough_OutboundOrch_PDDeferredRequest_BEAN_NAME.
     */
    private static final String PTOUTBOUND_PD_BEAN_NAME_REQ = "ptPDReqOutbound";

    /**
     * The Constant Standard_InboundOrch_PDDeferredRequest_BEAN_NAME.
     */
    private static final String STDINBOUND_PD_BEAN_NAME_REQ = "stdPDReqInbound";

    /**
     * The Constant Passthrough_InboundOrch_PDDeferredRequest_BEAN_NAME.
     */
    private static final String PTINBOUND_PD_BEAN_NAME_REQ = "ptPDReqInbound";

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardOutboundBeanName()
     */
    @Override
    protected String getStandardOutboundBeanName() {
        return STDOUTBOUND_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughOutboundBeanName()
     */
    @Override
    protected String getPassthroughOutboundBeanName() {
        return PTOUTBOUND_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardInboundBeanName()
     */
    @Override
    protected String getStandardInboundBeanName() {
        return STDINBOUND_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughInboundBeanName()
     */
    @Override
    protected String getPassthroughInboundBeanName() {
        return PTINBOUND_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_PD_BEAN_NAME_REQ;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_PD_BEAN_NAME_REQ;
    }
}
