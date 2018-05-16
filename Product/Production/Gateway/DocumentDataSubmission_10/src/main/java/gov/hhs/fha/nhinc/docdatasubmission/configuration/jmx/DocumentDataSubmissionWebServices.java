/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docdatasubmission.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.docdatasubmission._10.entity.EntityDocDataSubmissionSecured;
import gov.hhs.fha.nhinc.docdatasubmission._10.entity.EntityDocDataSubmissionUnsecured;
import gov.hhs.fha.nhinc.docdatasubmission._10.nhin.NhinDocDataSubmission;
import gov.hhs.fha.nhinc.docdatasubmission.inbound.InboundDocDataSubmission;
import gov.hhs.fha.nhinc.docdatasubmission.outbound.OutboundDocDataSubmission;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.AbstractDSWebServicesMXBean;
import javax.servlet.ServletContext;

public class DocumentDataSubmissionWebServices extends AbstractDSWebServicesMXBean {

    private static final String NHIN_DS_BEAN_NAME = "nhinDDS";
    private static final String ENTITY_UNSECURED_DS_BEAN_NAME = "entityDDSUnsecured";
    private static final String ENTITY_SECURED_DS_BEAN_NAME = "entityDDSSecured";
    private static final serviceEnum serviceName = serviceEnum.DocumentDataSubmission;

    public DocumentDataSubmissionWebServices(ServletContext sc) {
        super(sc);
    }

    @Override
    protected String getNhinBeanName() {
        return NHIN_DS_BEAN_NAME;
    }

    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_DS_BEAN_NAME;
    }

    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_DS_BEAN_NAME;
    }

    @Override
    public boolean isInboundPassthru() {
        boolean isPassthru = false;
        NhinDocDataSubmission nhinDDS = retrieveBean(NhinDocDataSubmission.class, getNhinBeanName());
        InboundDocDataSubmission inboundDS = nhinDDS.getInboundDocDataSubmission();
        if (compareClassName(inboundDS, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    @Override
    public boolean isOutboundPassthru() {
        boolean isPassthru = false;
        EntityDocDataSubmissionUnsecured entityDS = retrieveBean(EntityDocDataSubmissionUnsecured.class, getEntityUnsecuredBeanName());
        OutboundDocDataSubmission outboundDS = entityDS.getOutboundDocDataSubmission();
        if (compareClassName(outboundDS, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
            isPassthru = true;
        }
        return isPassthru;
    }

    @Override
    public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinDocDataSubmission nhinDS = retrieveBean(NhinDocDataSubmission.class, getNhinBeanName());
        InboundDocDataSubmission inboundDS = retrieveBean(InboundDocDataSubmission.class, getStandardInboundBeanName());

        nhinDS.setInboundDocDataSubmission(inboundDS);
    }

    @Override
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        NhinDocDataSubmission nhinDS = retrieveBean(NhinDocDataSubmission.class, getNhinBeanName());
        InboundDocDataSubmission inboundDS = retrieveBean(InboundDocDataSubmission.class,
            getPassthroughInboundBeanName());

        nhinDS.setInboundDocDataSubmission(inboundDS);
    }

    @Override
    public void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        OutboundDocDataSubmission outboundDS = retrieveBean(OutboundDocDataSubmission.class,
            getStandardOutboundBeanName());
        EntityDocDataSubmissionUnsecured entityDSUnsecured = retrieveBean(EntityDocDataSubmissionUnsecured.class,
            getEntityUnsecuredBeanName());
        EntityDocDataSubmissionSecured entityDSSecured = retrieveBean(EntityDocDataSubmissionSecured.class,
            getEntitySecuredBeanName());

        entityDSSecured.setOutboundDocDataSubmission(outboundDS);
        entityDSUnsecured.setOutboundDocDataSubmission(outboundDS);
    }

    @Override
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
    ClassNotFoundException {
        OutboundDocDataSubmission outboundDS = retrieveBean(OutboundDocDataSubmission.class,
            getPassthroughOutboundBeanName());
        EntityDocDataSubmissionUnsecured entityDSUnsecured = retrieveBean(EntityDocDataSubmissionUnsecured.class,
            getEntityUnsecuredBeanName());
        EntityDocDataSubmissionSecured entityDSSecured = retrieveBean(EntityDocDataSubmissionSecured.class,
            getEntitySecuredBeanName());

        entityDSSecured.setOutboundDocDataSubmission(outboundDS);
        entityDSUnsecured.setOutboundDocDataSubmission(outboundDS);
    }

    @Override
    public serviceEnum getServiceName() {
        return serviceName;
    }

    @Override
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinDocDataSubmission nhinDS = retrieveBean(NhinDocDataSubmission.class, getNhinBeanName());
        InboundDocDataSubmission inboundDS = nhinDS.getInboundDocDataSubmission();
        if (compareClassName(inboundDS, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityDocDataSubmissionUnsecured entityDS = retrieveBean(EntityDocDataSubmissionUnsecured.class, getEntityUnsecuredBeanName());
        OutboundDocDataSubmission outboundDS = entityDS.getOutboundDocDataSubmission();
        if (compareClassName(outboundDS, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

}
