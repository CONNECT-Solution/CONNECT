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

import gov.hhs.fha.nhinc.docretrieve._30.entity.EntityDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve._30.entity.EntityDocRetrieveSecured;
import gov.hhs.fha.nhinc.docretrieve.inbound.DocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.inbound.InboundDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.outbound.OutboundDocRetrieve;

import javax.servlet.ServletContext;

/**
 * The Class DocumentRetrieve30WebServices.
 *
 * @author msw
 */
public class DocumentRetrieve30WebServices extends AbstractDRWebServicesMXBean {

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docretrieve.outbound.PassthroughOutboundDocRetrieve";

    private String serviceName = "RetrieveDocuments";
    /**
     * Instantiates a new document retrieve30 web services.
     *
     * @param sc the sc
     */
    public DocumentRetrieve30WebServices(ServletContext sc) {
        super(sc);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        DocRetrieve docRetrieve = retrieveBean(DocRetrieve.class, getNhinBeanName());
        InboundDocRetrieve inboundDocRetrieve = docRetrieve.getInboundDocRetrieve();
        if (DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(inboundDocRetrieve.getClass().getName())) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru()
     */
    @Override
    public boolean isOutboundPassthru() {
        boolean isPassthru = false;
        EntityDocRetrieve entityDocRetrieve = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        OutboundDocRetrieve outboundDocRetrieve = entityDocRetrieve.getOutboundDocRetrieve();
        if (DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME.equals(outboundDocRetrieve.getClass().getName())) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getOutboundPassthruClassName()
     */
    @Override
    protected String getOutboundPassthruClassName() {
        return DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME;
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        DocRetrieve docRetrieve = retrieveBean(DocRetrieve.class, getNhinBeanName());
        InboundDocRetrieve inboundDocRetrieve = retrieveDependency(InboundDocRetrieve.class, className);

        docRetrieve.setInboundDocRetrieve(inboundDocRetrieve);
    }

    /* (non-Javadoc)
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        OutboundDocRetrieve outboundDocRetrieve = retrieveDependency(OutboundDocRetrieve.class, className);
        EntityDocRetrieve entityDocRetrieve = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        EntityDocRetrieveSecured entityDocRetrieveSecured = retrieveBean(EntityDocRetrieveSecured.class, getEntitySecuredBeanName());
        
        entityDocRetrieve.setOutboundDocRetrieve(outboundDocRetrieve);
        entityDocRetrieveSecured.setOutboundDocRetrieve(outboundDocRetrieve);
    }
    
    public String getServiceName() {
        return this.serviceName;
    }

}
