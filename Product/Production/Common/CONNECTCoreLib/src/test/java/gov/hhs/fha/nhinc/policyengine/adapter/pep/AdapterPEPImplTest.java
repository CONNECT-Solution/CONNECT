package gov.hhs.fha.nhinc.policyengine.adapter.pep;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.MissingResourceException;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;

import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeValueType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;

import org.junit.Test;

import com.sun.identity.xacml.context.Attribute;
import com.sun.identity.xacml.context.Request;
import com.sun.identity.xacml.context.Subject;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

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
