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

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.docsubmission._20.entity.EntityDocSubmissionSecured_g1;
import gov.hhs.fha.nhinc.docsubmission._20.entity.EntityDocSubmissionUnsecured_g1;
import gov.hhs.fha.nhinc.docsubmission._20.nhin.NhinXDR_g1;
import gov.hhs.fha.nhinc.docsubmission.inbound.InboundDocSubmission;
import gov.hhs.fha.nhinc.docsubmission.outbound.OutboundDocSubmission;
import org.springframework.stereotype.Service;

/**
 * The Class DocumentSubmission20WebServices.
 *
 * @author msw
 */
@Service
public class DocumentSubmission20WebServices extends AbstractDSWebServicesMXBean {

    /** The Constant NHIN_DS_BEAN_NAME. */
    private static final String NHIN_DS_BEAN_NAME = "nhinXDR_g1";

    /** The Constant ENTITY_UNSECURED_DS_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_DS_BEAN_NAME = "entityXDRUnsecured_g1";

    /** The Constant ENTITY_SECURED_DS_BEAN_NAME. */
    private static final String ENTITY_SECURED_DS_BEAN_NAME = "entityXDRSecured_g1";

    private final serviceEnum serviceName = serviceEnum.DocumentSubmission;

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getNhinBeanName()
     */
    @Override
    protected String getNhinBeanName() {
        return NHIN_DS_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_DS_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_DS_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
     */
    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        NhinXDR_g1 nhinDS = retrieveBean(NhinXDR_g1.class, getNhinBeanName());
        InboundDocSubmission inboundDS = nhinDS.getInboundDocSubmission();
        if (compareClassName(inboundDS, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
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
        EntityDocSubmissionUnsecured_g1 entityDS = retrieveBean(EntityDocSubmissionUnsecured_g1.class, getEntityUnsecuredBeanName());
        OutboundDocSubmission outboundDS = entityDS.getOutboundDocSubmission();
        if (compareClassName(outboundDS, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        NhinXDR_g1 nhinDS = retrieveBean(NhinXDR_g1.class, getNhinBeanName());
        InboundDocSubmission inboundDS = retrieveBean(InboundDocSubmission.class, getStandardInboundBeanName());

        nhinDS.setInboundDocSubmission(inboundDS);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        NhinXDR_g1 nhinDS = retrieveBean(NhinXDR_g1.class, getNhinBeanName());
        InboundDocSubmission inboundDS = retrieveBean(InboundDocSubmission.class, getPassthroughInboundBeanName());

        nhinDS.setInboundDocSubmission(inboundDS);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        OutboundDocSubmission outboundDS = retrieveBean(OutboundDocSubmission.class, getStandardOutboundBeanName());
        EntityDocSubmissionUnsecured_g1 entityDSUnsecured = retrieveBean(EntityDocSubmissionUnsecured_g1.class,
                getEntityUnsecuredBeanName());
        EntityDocSubmissionSecured_g1 entityDSSecured = retrieveBean(EntityDocSubmissionSecured_g1.class,
                getEntitySecuredBeanName());

        entityDSSecured.setOutboundDocSubmission(outboundDS);
        entityDSUnsecured.setOutboundDocSubmission(outboundDS);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        OutboundDocSubmission outboundDS = retrieveBean(OutboundDocSubmission.class, getPassthroughOutboundBeanName());
        EntityDocSubmissionUnsecured_g1 entityDSUnsecured = retrieveBean(EntityDocSubmissionUnsecured_g1.class,
                getEntityUnsecuredBeanName());
        EntityDocSubmissionSecured_g1 entityDSSecured = retrieveBean(EntityDocSubmissionSecured_g1.class,
                getEntitySecuredBeanName());

        entityDSSecured.setOutboundDocSubmission(outboundDS);
        entityDSUnsecured.setOutboundDocSubmission(outboundDS);
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
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinXDR_g1 nhinDS = retrieveBean(NhinXDR_g1.class, getNhinBeanName());
        InboundDocSubmission inboundDS = nhinDS.getInboundDocSubmission();
        if (compareClassName(inboundDS, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
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
        EntityDocSubmissionUnsecured_g1 entityDS = retrieveBean(EntityDocSubmissionUnsecured_g1.class, getEntityUnsecuredBeanName());
        OutboundDocSubmission outboundDS = entityDS.getOutboundDocSubmission();
        if (compareClassName(outboundDS, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

}
