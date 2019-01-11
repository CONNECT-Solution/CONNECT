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
package gov.hhs.fha.nhinc.docquery.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.docquery._20.entity.EntityDocQuerySecured;
import gov.hhs.fha.nhinc.docquery._20.entity.EntityDocQueryUnsecured;
import gov.hhs.fha.nhinc.docquery._20.nhin.DocQuery;
import gov.hhs.fha.nhinc.docquery.inbound.InboundDocQuery;
import gov.hhs.fha.nhinc.docquery.outbound.OutboundDocQuery;
import org.springframework.stereotype.Service;

/**
 * The Class DocumentQuery30WebServices.
 *
 * @author msw
 */
@Service
public class DocumentQuery20WebServices extends AbstractDQWebServicesMXBean {

	private final serviceEnum serviceName = serviceEnum.QueryForDocuments;


	/**
	 * Configure Standard inbound implementation. The inbound orchestration
	 * implementation provided via the className param is set on the Nhin
	 * interface bean. This method uses specific types and passes them to the
	 * generic
	 * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)}
	 * and
	 * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)}
	 * methods.
	 *
	 * @param className
	 *            the class name
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImplementation(java.lang.String)
	 */
	@Override
	public void configureInboundStdImpl() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		DocQuery docQuery;
		InboundDocQuery inboundDocQuery;

		docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
		inboundDocQuery = retrieveBean(InboundDocQuery.class, getStandardInboundBeanName());
		docQuery.setInboundDocQuery(inboundDocQuery);
	}


	/**
     * Configure Passthrough inbound implementation. The inbound orchestration
     * implementation provided via the className param is set on the Nhin
     * interface bean. This method uses specific types and passes them to the
     * generic
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)}
     * and
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)}
     * methods.
     *
     * @param className
     *            the class name
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureInboundImplementation(java.lang.String)
     */
    @Override
    public void configureInboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        DocQuery docQuery;
        InboundDocQuery inboundDocQuery;

        docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
        inboundDocQuery = retrieveBean(InboundDocQuery.class, getPassthroughInboundBeanName());
        docQuery.setInboundDocQuery(inboundDocQuery);
    }

	/**
	 * Configure Standard outbound implementation. The outbound orchestration
	 * implementation provided via the className param is set on the Nhin
	 * interface bean. This method uses specific types and passes them to the
	 * generic
	 * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)}
	 * and
	 * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)}
	 * methods.
	 *
	 * @param className
	 *            the class name
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImplementation(java.lang.String)
	 */
	@Override
	public void configureOutboundStdImpl() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		EntityDocQueryUnsecured entityUnsecuredDocQuery;
		EntityDocQuerySecured entitySecuredDocQuery;
		OutboundDocQuery outboundDocQuery;

		entityUnsecuredDocQuery = retrieveBean(EntityDocQueryUnsecured.class, getEntityUnsecuredBeanName());
		entitySecuredDocQuery = retrieveBean(EntityDocQuerySecured.class, getEntitySecuredBeanName());
		outboundDocQuery = retrieveBean(OutboundDocQuery.class, getStandardOutboundBeanName() );

		entityUnsecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
		entitySecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
	}


	/**
     * Configure Passthrough outbound implementation. The outbound orchestration
     * implementation provided via the className param is set on the Nhin
     * interface bean. This method uses specific types and passes them to the
     * generic
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveBean(Class, String)}
     * and
     * {@link gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#retrieveDependency(Class, String)}
     * methods.
     *
     * @param className
     *            the class name
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws ClassNotFoundException
     *             the class not found exception
     * @see gov.hhs.fha.nhinc.configuration.jmx.AbstractWebServicesMXBean#configureOutboundImplementation(java.lang.String)
     */
    @Override
    public void configureOutboundPtImpl() throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        EntityDocQueryUnsecured entityUnsecuredDocQuery;
        EntityDocQuerySecured entitySecuredDocQuery;
        OutboundDocQuery outboundDocQuery;

        entityUnsecuredDocQuery = retrieveBean(EntityDocQueryUnsecured.class, getEntityUnsecuredBeanName());
        entitySecuredDocQuery = retrieveBean(EntityDocQuerySecured.class, getEntitySecuredBeanName());
        outboundDocQuery = retrieveBean(OutboundDocQuery.class, getPassthroughOutboundBeanName() );

        entityUnsecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
        entitySecuredDocQuery.setOutboundDocQuery(outboundDocQuery);
    }

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundPassthru()
	 */
	@Override
	public boolean isInboundPassthru() {
		boolean isPassthru = false;
		DocQuery docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
		InboundDocQuery inboundDocQuery = docQuery.getInboundDocQuery();
		if (compareClassName(inboundDocQuery, DEFAULT_INBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
			isPassthru = true;
		}
		return isPassthru;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundPassthru
	 * ()
	 */
	@Override
	public boolean isOutboundPassthru() {
		boolean isPassthru = false;
		EntityDocQueryUnsecured entityDocQuery = retrieveBean(
				EntityDocQueryUnsecured.class, getEntityUnsecuredBeanName());
		OutboundDocQuery outboundDocQuery = entityDocQuery
				.getOutboundDocQuery();
		if (compareClassName(outboundDocQuery, DEFAULT_OUTBOUND_PASSTHRU_IMPL_CLASS_NAME)) {
			isPassthru = true;
		}
		return isPassthru;
	}

	@Override
	public serviceEnum getServiceName() {
		return serviceName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isOutboundStandard
	 * ()
	 */
	@Override
	public boolean isOutboundStandard() {
		boolean isStandard = false;
		EntityDocQueryUnsecured entityDocQuery = retrieveBean(
				EntityDocQueryUnsecured.class, getEntityUnsecuredBeanName());
		OutboundDocQuery outboundDocQuery = entityDocQuery
				.getOutboundDocQuery();
		if (compareClassName(outboundDocQuery, DEFAULT_OUTBOUND_STANDARD_IMPL_CLASS_NAME)) {
			isStandard = true;
		}
		return isStandard;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gov.hhs.fha.nhinc.configuration.jmx.WebServicesMXBean#isInboundStandard()
	 */
	@Override
	public boolean isInboundStandard() {
		boolean isStandard = false;
		DocQuery docQuery = retrieveBean(DocQuery.class, getNhinBeanName());
		InboundDocQuery inboundDocQuery = docQuery.getInboundDocQuery();
		if (compareClassName(inboundDocQuery, DEFAULT_INBOUND_STANDARD_IMPL_CLASS_NAME)) {
			isStandard = true;
		}
		return isStandard;
	}

}
