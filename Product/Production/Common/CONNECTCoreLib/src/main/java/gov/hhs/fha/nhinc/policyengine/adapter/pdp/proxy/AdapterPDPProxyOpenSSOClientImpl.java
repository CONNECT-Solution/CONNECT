/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.xacml.client.XACMLRequestProcessor;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Response;

/**
 *
 * AdapterPDPProxy implementation that calls an OpenSSO service.
 *
 *
 *
 * @author Neil Webb
 */

public class AdapterPDPProxyOpenSSOClientImpl implements AdapterPDPProxy
{
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_PDP_ENTITY = "PdpEntityName";
    private static final String OPENSSO_PEP_NAME = "ConnectOpenSSOPepEntity";
    private static final String PDP_ENTITY_SUFFIX = "PdpEntity";
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPDPProxyOpenSSOClientImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy.AdapterPDPProxy#processPDPRequest(com.sun.identity.xacml.context
     * .Request)
     */
    @Override
    public Response processPDPRequest(Request pdpRequest) throws PropertyAccessException, XACMLException,
            SAML2Exception

    {

        LOG.debug("Begin AdapterPDPProxyOpenSSOClientImpl.processPDPRequest(...)");

        String pdpSelection = getPDPSelcectionProperty();

        String pdpEntity = null;

        if (pdpSelection != null)

        {

            pdpEntity = pdpSelection.trim() + PDP_ENTITY_SUFFIX;

        }

        String pepEntity = OPENSSO_PEP_NAME;

        LOG.debug("Submit request for pdp entity: " + pdpEntity + " & pep entity: " + pepEntity);

        Response pdpResponse = callOpenSSO(pdpRequest, pdpEntity, pepEntity);

        LOG.debug("End AdapterPDPProxyOpenSSOClientImpl.processPDPRequest(...)");

        return pdpResponse;

    }

    /**
     *
     * Retrieve the PDP selection property from a properties file.
     *
     *
     *
     * @return PDP selection property
     *
     * @throws PropertyAccessException
     */

    protected String getPDPSelcectionProperty() throws PropertyAccessException

    {

        return PropertyAccessor.getInstance().getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_PDP_ENTITY);

    }

    /**
     *
     * Consume the OpenSSO service for PDP operations.
     *
     *
     *
     * @param pdpRequest PDP request message
     *
     * @param pdpEntity Policy decision point entity name
     *
     * @param pepEntity Policy enforcement point entity name
     *
     * @return PDP response message
     *
     * @throws XACMLException
     *
     * @throws SAML2Exception
     */

    protected Response callOpenSSO(Request pdpRequest, String pdpEntity, String pepEntity) throws XACMLException,
            SAML2Exception

    {

        return XACMLRequestProcessor.getInstance().processRequest(pdpRequest, pdpEntity, pepEntity);

    }

}
