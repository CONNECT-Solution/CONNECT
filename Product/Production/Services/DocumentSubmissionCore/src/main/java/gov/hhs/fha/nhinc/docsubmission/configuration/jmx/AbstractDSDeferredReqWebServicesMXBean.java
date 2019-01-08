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
package gov.hhs.fha.nhinc.docsubmission.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean;

/**
 * The Class AbstractDSDeferredReqWebServicesMXBean.
 *
 * @author msw
 */
public abstract class AbstractDSDeferredReqWebServicesMXBean extends AbstractWebServicesMXBean {

    /** The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request.StandardInboundDocSubmissionDeferredRequest";

    /** The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request.PassthroughInboundDocSubmissionDeferredRequest";

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request.StandardOutboundDocSubmissionDeferredRequest";

    /** The Constant DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request.PassthroughOutboundDocSubmissionDeferredRequest";

    /** The Constant Standard_OutboundOrch_DS_BEAN_NAME. */
    private static final String StdOutbound_DS_Bean_Name = "stdDSReqOutbound";

    /** The Constant Passthrough_OutboundOrch_DS_BEAN_NAME. */
    private static final String PtOutbound_DS_Bean_Name = "ptDSReqOutbound";

    /** The Constant Standard_InboundOrch_DS_BEAN_NAME. */
    private static final String StdInbound_DS_Bean_Name = "stdDSReqInbound";

    /** The Constant Passthrough_InboundOrch_DS_BEAN_NAME. */
    private static final String PtInbound_DS_Bean_Name = "ptDSReqInbound";

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardOutboundBeanName()
     */
    @Override
    protected String getStandardOutboundBeanName() {
        return StdOutbound_DS_Bean_Name;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughOutboundBeanName()
     */
    @Override
    protected String getPassthroughOutboundBeanName() {
        return PtOutbound_DS_Bean_Name;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getStandardInboundBeanName()
     */
    @Override
    protected String getStandardInboundBeanName() {
        return StdInbound_DS_Bean_Name;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getPassthroughInboundBeanName()
     */
    @Override
    protected String getPassthroughInboundBeanName() {
        return PtInbound_DS_Bean_Name;
    }

    /**
     * Configure outbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure outbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure Inbound Standard implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /**
     * Configure Inbound Passthrough implementation. This method is abstract because subclass implementations must use
     * actual types as opposed to the type parameters use in {@link #retrieveBean(Class, String)} and
     *
     * @param className the class name
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws ClassNotFoundException the class not found exception {@link #retrieveDependency(Class, String)}.
     */
    @Override
    public abstract void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public abstract boolean isInboundPassthru();

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public abstract boolean isOutboundPassthru();

}
