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

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.docretrieve._20.entity.EntityDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve._20.inbound.DocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.inbound.InboundDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.outbound.OutboundDocRetrieve;

import javax.servlet.ServletContext;

/**
 * The Class DocumentRetrieve30WebServices.
 * 
 * @author msw
 */
public class DocumentRetrieve20WebServices extends AbstractDRWebServicesMXBean {

    /** The Constant DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME. */
    public static final String DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME = "gov.hhs.fha.nhinc.docretrieve._20.outbound.PassthroughOutboundDocRetrieve";
  
    private final serviceEnum serviceName = serviceEnum.RetrieveDocuments;

    /**
     * Instantiates a new document retrieve30 web services.
     * 
     * @param sc the sc
     */
    public DocumentRetrieve20WebServices(ServletContext sc) {
        super(sc);
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        DocRetrieve docRetrieve = retrieveBean(DocRetrieve.class, getNhinBeanName());
        InboundDocRetrieve inboundDocRetrieve = docRetrieve.getInboundDocRetrieve();
        if (compareClassName(inboundDocRetrieve, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }
    @Override
    public boolean isOutboundPassthru() {
        boolean isPassthru = false;
        EntityDocRetrieve docRetrieve = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        OutboundDocRetrieve outboundDocRetrieve = docRetrieve.getOutboundDocRetrieve();
        if (compareClassName(outboundDocRetrieve, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
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
        DocRetrieve nhinDR = null;
        InboundDocRetrieve inboundDR = null;

        nhinDR = retrieveBean(DocRetrieve.class, getNhinBeanName());
        inboundDR = retrieveBean(InboundDocRetrieve.class, getStandardInboundBeanName());

        nhinDR.setInboundDocRetrieve(inboundDR);
    }
    
    
    @Override
    public void configureInboundPtImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        DocRetrieve nhinDR = null;
        InboundDocRetrieve inboundDR = null;

        nhinDR = retrieveBean(DocRetrieve.class, getNhinBeanName());
        inboundDR = retrieveBean(InboundDocRetrieve.class, getPassthroughInboundBeanName());

        nhinDR.setInboundDocRetrieve(inboundDR);
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
        EntityDocRetrieve entityDR = null;
        OutboundDocRetrieve outboundDR = null;

        entityDR = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        outboundDR = retrieveBean(OutboundDocRetrieve.class, getStandardOutboundBeanName());

        entityDR.setOutboundDocRetrieve(outboundDR);
    }
    
    @Override
    public void configureOutboundPtImpl() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        EntityDocRetrieve entityDR = null;
        OutboundDocRetrieve outboundDR = null;

        entityDR = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        outboundDR = retrieveBean(OutboundDocRetrieve.class, getPassthroughOutboundBeanName());

        entityDR.setOutboundDocRetrieve(outboundDR);
    }


    @Override
    public serviceEnum getServiceName() {
        return this.serviceName;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundStandard()
     */
    @Override
    public boolean isInboundStandard() {
        boolean isStandard = false;
        DocRetrieve docRetrieve = retrieveBean(DocRetrieve.class, getNhinBeanName());
        InboundDocRetrieve inboundDocRetrieve = docRetrieve.getInboundDocRetrieve();
        if (compareClassName(inboundDocRetrieve, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundStandard()
     */
    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityDocRetrieve entityDocRetrieve = retrieveBean(EntityDocRetrieve.class, getEntityUnsecuredBeanName());
        OutboundDocRetrieve outboundDocRetrieve = entityDocRetrieve.getOutboundDocRetrieve();
        if (compareClassName(outboundDocRetrieve, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }
}
