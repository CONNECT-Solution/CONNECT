/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.xacml.client.XACMLRequestProcessor;
import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Response;

/**
 * AdapterPDPProxy implementation that calls an OpenSSO service.
 * 
 * @author Neil Webb
 */
public class AdapterPDPProxyOpenSSOClientImpl implements AdapterPDPProxy
{
    private static final String PROPERTY_FILE_NAME_GATEWAY = "gateway";
    private static final String PROPERTY_FILE_KEY_PDP_ENTITY = "PdpEntityName";
    private static final String OPENSSO_PEP_NAME = "ConnectOpenSSOPepEntity";
    private static final String PDP_ENTITY_SUFFIX = "PdpEntity";

    private Log log = null;

    public AdapterPDPProxyOpenSSOClientImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

	/* (non-Javadoc)
	 * @see gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy.AdapterPDPProxy#processPDPRequest(com.sun.identity.xacml.context.Request)
	 */
	@Override
	public Response processPDPRequest(Request pdpRequest) throws PropertyAccessException, XACMLException, SAML2Exception
	{
		log.debug("Begin AdapterPDPProxyOpenSSOClientImpl.processPDPRequest(...)");

        String pdpSelection = getPDPSelcectionProperty();
        String pdpEntity = null;
        if(pdpSelection != null)
        {
        	pdpEntity = pdpSelection.trim() + PDP_ENTITY_SUFFIX;
        }
        String pepEntity = OPENSSO_PEP_NAME;
        log.debug("Submit request for pdp entity: " + pdpEntity + " & pep entity: " + pepEntity);
        Response pdpResponse = callOpenSSO(pdpRequest, pdpEntity, pepEntity);
		
		log.debug("End AdapterPDPProxyOpenSSOClientImpl.processPDPRequest(...)");
		return pdpResponse;
	}
	
	/**
	 * Retrieve the PDP selection property from a properties file.
	 * 
	 * @return PDP selection property
	 * @throws PropertyAccessException
	 */
	protected String getPDPSelcectionProperty() throws PropertyAccessException
	{
		return PropertyAccessor.getProperty(PROPERTY_FILE_NAME_GATEWAY, PROPERTY_FILE_KEY_PDP_ENTITY);
	}
	
	/**
	 * Consume the OpenSSO service for PDP operations.
	 * 
	 * @param pdpRequest PDP request message
	 * @param pdpEntity Policy decision point entity name
	 * @param pepEntity Policy enforcement point entity name
	 * @return PDP response message
	 * @throws XACMLException
	 * @throws SAML2Exception
	 */
	protected Response callOpenSSO(Request pdpRequest, String pdpEntity, String pepEntity) throws XACMLException, SAML2Exception
	{
		return XACMLRequestProcessor.getInstance().processRequest(pdpRequest, pdpEntity, pepEntity);
	}

}
