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

import com.sun.identity.xacml.common.XACMLException;
import com.sun.identity.xacml.context.Decision;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Response;
import com.sun.identity.xacml.context.Result;
import com.sun.identity.xacml.context.Status;
import com.sun.identity.xacml.policy.Obligations;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * NO-OP implementation of the adapter PDP service.
 *
 *
 *
 * @author Neil Webb
 */

public class AdapterPDPProxyNoopImpl implements AdapterPDPProxy
{
    private static final String DECISION_VALUE_PERMIT = "Permit";
    private static final Logger LOG = LoggerFactory.getLogger(AdapterPDPProxyNoopImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * gov.hhs.fha.nhinc.policyengine.adapter.pdp.proxy.AdapterPDPProxy#processPDPRequest(com.sun.identity.xacml.context
     * .Request)
     */

    @Override
    public Response processPDPRequest(Request pdpRequest)
    {
        LOG.debug("Begin AdapterPDPProxyNoopImpl.processPDPRequest(...)");
        Response resp = createResponse();
        LOG.debug("End AdapterPDPProxyNoopImpl.processPDPRequest(...)");
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

                if (results == null)

                {

                    results = new ArrayList<>();

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

            LOG.error("Error adding a result: " + e.getMessage(), e);

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

            LOG.error("Error setting decision: " + e.getMessage(), e);

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

            LOG.error("Error setting decision value: " + e.getMessage(), e);

        }

        return decision;

    }

}
