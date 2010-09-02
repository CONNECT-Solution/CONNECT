/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Status;
import com.sun.identity.xacml.policy.Obligations;

/**
 * NO-OP implementation of the adapter PDP service.
 * 
 * @author Neil Webb
 */
public class AdapterPDPProxyNoopImpl implements AdapterPDPProxy
{
	private static final String DECISION_VALUE_PERMIT = "Permit";
	
    private Log log = null;

    public AdapterPDPProxyNoopImpl()
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
	public Response processPDPRequest(Request pdpRequest)
	{
		log.debug("Begin AdapterPDPProxyNoopImpl.processPDPRequest(...)");
		Response resp = createResponse(); 
		log.debug("End AdapterPDPProxyNoopImpl.processPDPRequest(...)");
		return resp;
	}

	protected Response createResponse()
	{
		Response response = new Response()
		{
			@SuppressWarnings("unchecked")
			List results = null;
			
			@SuppressWarnings("unchecked")
			@Override
			public void addResult(Result arg0) throws XACMLException
			{
				if(results == null)
				{
					results = new ArrayList<Result>();
				}
				results.add(arg0);
			}

			@SuppressWarnings("unchecked")
			@Override
			public List getResults()
			{
				return results;
			}

			@Override
			public boolean isMutable()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void makeImmutable()
			{
				// TODO Auto-generated method stub
				
			}

			@SuppressWarnings("unchecked")
			@Override
			public void setResults(List arg0) throws XACMLException
			{
				results = arg0;
			}

			@Override
			public String toXMLString() throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toXMLString(boolean arg0, boolean arg1)
					throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};

		try
		{
			response.addResult(createResult());
		}
		catch (XACMLException e)
		{
			log.error("Error adding a result: " + e.getMessage(), e);
		}
		
		return response;
	}
	protected Result createResult()
	{
		Result result = new Result()
		{
			Decision decision = null;
			
			@Override
			public Decision getDecision()
			{
				return decision;
			}

			@Override
			public Obligations getObligations()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getResourceId()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Status getStatus()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isMutable()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void makeImmutable()
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setDecision(Decision arg0)
					throws XACMLException
			{
				decision = arg0;
			}

			@Override
			public void setObligations(Obligations arg0)
					throws XACMLException
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setResourceId(String arg0)
					throws XACMLException
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setStatus(Status arg0) throws XACMLException
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public String toXMLString() throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toXMLString(boolean arg0, boolean arg1)
					throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		try
		{
			result.setDecision(createDecision());
		}
		catch (XACMLException e)
		{
			log.error("Error setting decision: " + e.getMessage(), e);
		}
		
		return result;
	}
	
	protected Decision createDecision()
	{
		Decision decision = new Decision()
		{
			String value = null;

			@Override
			public String getValue()
			{
				return value;
			}

			@Override
			public boolean isMutable()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void makeImmutable()
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setValue(String arg0) throws XACMLException
			{
				value = arg0;
				
			}

			@Override
			public String toXMLString() throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toXMLString(boolean arg0, boolean arg1)
					throws XACMLException
			{
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		try
		{
			decision.setValue(DECISION_VALUE_PERMIT);
		}
		catch (XACMLException e)
		{
			log.error("Error setting decision value: " + e.getMessage(), e);
		}
		return decision;
	}
	
}
