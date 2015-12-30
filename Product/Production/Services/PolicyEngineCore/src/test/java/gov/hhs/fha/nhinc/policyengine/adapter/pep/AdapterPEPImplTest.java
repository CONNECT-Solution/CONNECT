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
package gov.hhs.fha.nhinc.policyengine.adapter.pep;

import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Subject;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import java.util.Iterator;
import java.util.MissingResourceException;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import static org.junit.Assert.*;
import org.junit.Test;

public class AdapterPEPImplTest {

    @Test
    public void testCreatePdpRequest_emptyValueRemovalCheck() {

        CheckPolicyRequestType request = createPolicyRequest();

        addAttributeToRequest(

        request,

        createAttribute(

        "urn:oasis:names:tc:xspa:1.0:subject:organization-id",

        "http://www.w3.org/2001/XMLSchema#string"));

        try {

            AdapterPEPImpl adapter = new AdapterPEPImpl();

            Request req = adapter.createPdpRequest(request, null);

            assertTrue(!doesRequestContainAttribute(req,

            "urn:oasis:names:tc:xspa:1.0:subject:organization-id"));

        } catch (MissingResourceException ex) {

            fail("MissingResourceException has been thrown.");

        }

    }

    @SuppressWarnings("rawtypes")
    private boolean doesRequestContainAttribute(Request request, String id) {

        Iterator i = request.getSubjects().iterator();

        if (i.hasNext()) {

            Subject subj = (Subject) i.next();

            for (Iterator j = subj.getAttributes().iterator(); j.hasNext();) {

                Attribute attr = (Attribute) j.next();

                if (attr.getAttributeId().toString().equals(id)) {

                    return true;

                }

            }

        }

        return false;

    }

    private void addAttributeToRequest(CheckPolicyRequestType request,

    AttributeType attr) {

        SubjectType st = new SubjectType();

        st.setSubjectCategory("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject");

        st.getAttribute().add(attr);

        request.getRequest().getSubject().add(st);

    }

    private CheckPolicyRequestType createPolicyRequest() {

        CheckPolicyRequestType request = new CheckPolicyRequestType();

        HomeCommunityType hc = new HomeCommunityType();

        AssertionType at = new AssertionType();

        RequestType rt = new RequestType();

        request.setRequest(rt);

        request.setAssertion(at);

        hc.setHomeCommunityId("2.16.840.1.113883.3.333");

        at.setHomeCommunity(hc);

        return request;

    }

    private AttributeType createAttribute(String id, String type) {

        AttributeType attr = new AttributeType();

        AttributeValueType av = new AttributeValueType();

        attr.getAttributeValue().add(av);

        attr.setDataType(type);

        attr.setAttributeId(id);

        return attr;

    }

}
