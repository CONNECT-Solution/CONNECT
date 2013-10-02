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
package gov.hhs.fha.nhinc.admindistribution.configuration.jmx;

import gov.hhs.fha.nhinc.admindistribution._10.entity.EntityAdministrativeDistribution;
import gov.hhs.fha.nhinc.admindistribution._10.entity.EntityAdministrativeDistributionSecured;
import gov.hhs.fha.nhinc.admindistribution._10.nhin.NhinAdministrativeDistribution;
import gov.hhs.fha.nhinc.admindistribution.inbound.InboundAdminDistribution;
import gov.hhs.fha.nhinc.admindistribution.outbound.OutboundAdminDistribution;
import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;

import javax.servlet.ServletContext;

/**
 * The Class AdminDistribution10WebServices.
 * 
 * @author msw
 */
public class AdminDistribution10WebServices extends AbstractAdminDistributionWebServicesMXBean {

    /** The Constant NHIN_AD_BEAN_NAME. */
    private static final String NHIN_AD_BEAN_NAME = "NhinAdministrativeDistributionBean";

    /** The Constant ENTITY_UNSECURED_AD_BEAN_NAME. */
    private static final String ENTITY_UNSECURED_AD_BEAN_NAME = "EntityAdministrativeDistributionBean";

    /** The Constant ENTITY_SECURED_AD_BEAN_NAME. */
    private static final String ENTITY_SECURED_AD_BEAN_NAME = "EntityAdministrativeDistributionSecuredBean";

    private final serviceEnum serviceName = serviceEnum.AdminDistribution;

    /**
     * Instantiates a new admin distribution10 web services.
     * 
     * @param sc the sc
     */
    public AdminDistribution10WebServices(ServletContext sc) {
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
        NhinAdministrativeDistribution nhinAD = retrieveBean(NhinAdministrativeDistribution.class, getNhinBeanName());
        InboundAdminDistribution inboundAD = nhinAD.getInboundAdminDistribution();
        if (compareClassName(inboundAD, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
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
        EntityAdministrativeDistribution entityAD = retrieveBean(EntityAdministrativeDistribution.class,
                getEntityUnsecuredBeanName());
        OutboundAdminDistribution outboundAD = entityAD.getOutboundAdminDistribution();
        if (compareClassName(outboundAD, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
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
        return NHIN_AD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntityUnsecuredBeanName()
     */
    @Override
    protected String getEntityUnsecuredBeanName() {
        return ENTITY_UNSECURED_AD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#getEntitySecuredBeanName()
     */
    @Override
    protected String getEntitySecuredBeanName() {
        return ENTITY_SECURED_AD_BEAN_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImpl(java.lang.String)
     */
    @Override
    public void configureInboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        NhinAdministrativeDistribution nhinAD = retrieveBean(NhinAdministrativeDistribution.class, getNhinBeanName());
        InboundAdminDistribution inboundAD = retrieveDependency(InboundAdminDistribution.class, className);

        nhinAD.setInboundAdminDistribution(inboundAD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImpl(java.lang.String)
     */
    @Override
    public void configureOutboundImpl(String className) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        OutboundAdminDistribution outboundAD = retrieveDependency(OutboundAdminDistribution.class, className);

        EntityAdministrativeDistribution entityADUnsecured = retrieveBean(EntityAdministrativeDistribution.class,
                getEntityUnsecuredBeanName());
        EntityAdministrativeDistributionSecured entityADSecured = retrieveBean(
                EntityAdministrativeDistributionSecured.class, getEntitySecuredBeanName());

        entityADSecured.setOutboundAdminDistribution(outboundAD);
        entityADUnsecured.setOutboundAdminDistribution(outboundAD);
    }

    @Override
    public serviceEnum getServiceName() {
        return this.serviceName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundStandard()
     */
    @Override
    public boolean isOutboundStandard() {
        boolean isStandard = false;
        EntityAdministrativeDistribution entityAD = retrieveBean(EntityAdministrativeDistribution.class,
                getEntityUnsecuredBeanName());
        OutboundAdminDistribution outboundAD = entityAD.getOutboundAdminDistribution();
        if (compareClassName(outboundAD, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundStandard()
     */
    public boolean isInboundStandard() {
        boolean isStandard = false;
        NhinAdministrativeDistribution nhinAD = retrieveBean(NhinAdministrativeDistribution.class, getNhinBeanName());
        InboundAdminDistribution inboundAD = nhinAD.getInboundAdminDistribution();
        if (compareClassName(inboundAD, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
            isStandard = true;
        }
        return isStandard;
    }

}
