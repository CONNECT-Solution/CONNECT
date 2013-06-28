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
package gov.hhs.fha.nhinc.docretrieve.configuration.jmx;

import javax.servlet.ServletContext;

import gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean;

/**
 * The Class AbstractDRWebServicesMXBean. This class does not implement
 * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getOutboundPassthruClassName()} because DR has
 * different implementations for 2.0 and 3.0 versions of services.
 * 
 * @author msw
 */
public abstract class AbstractDRWebServicesMXBean extends AbstractWebServicesMXBean {

    /** The Constant NHIN_DR_BEAN_NAME. */
    private static final String NHIN_DR_BEAN_NAME = "nhinDR";

    /** The Constant ENTITY_UNSECURED_DR_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_DR_BEAN_NAME = "entityDRUnsecured";

    /** The Constant ENTITY_SECURED_DR_BEAN_NAME. */
    private static final String ENTITY_SECURED_DR_BEAN_NAME = "entityDRSecured";

    /** The Constant DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docretrieve.inbound.StandardInboundDocRetrieve";

    /** The Constant DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME. */
    public static final String DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docretrieve.inbound.PassthroughInboundDocRetrieve";

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docretrieve.outbound.StandardOutboundDocRetrieve";

    /**
     * Instantiates a new abstract dr web services mx bean.
     * 
     * @param sc the sc
     */
    public AbstractDRWebServicesMXBean(ServletContext sc) {
        super(sc);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_DR_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_DR_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_DR_BEAN_NAME;
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

}
