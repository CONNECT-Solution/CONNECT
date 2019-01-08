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
package gov.hhs.fha.nhinc.transform.policy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import gov.hhs.fha.nhinc.common.eventcommon.XDREventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRMessageType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseEventType;
import gov.hhs.fha.nhinc.common.eventcommon.XDRResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class XDRPolicyTransformHelperTest {

	private static final String MESSAGE_ID = "12345566";
	private XDRPolicyTransformHelper xdrTransformHelper;
	private HomeCommunityType hct;
	private AssertionType assertionType;
	private SlotType1 slotType1;
	private ValueListType valueListType;
	private PersonNameType pnt;
	private SlotListType rsl;
	private RegistryObjectListType rolt;
	private SubmitObjectsRequest sor;
	private static final String TEST_HC_VAL = "urn:oid:1.1.1.1.1.1.1";

	public XDRPolicyTransformHelperTest() {
	}

	@Before
	public void setUp() {
		xdrTransformHelper = new XDRPolicyTransformHelper();
		hct = new HomeCommunityType();
		hct.setHomeCommunityId(TEST_HC_VAL);
		hct.setName("hct_Name");
		hct.setDescription("hct_description");
		assertionType = new AssertionType();
		slotType1 = new SlotType1();
		valueListType = new ValueListType();
		pnt = new PersonNameType();
		rsl = new SlotListType();
		rolt = new RegistryObjectListType();
		sor = new SubmitObjectsRequest();
		slotType1.setName("$XDSDocumentEntryPatientId");

		valueListType.getValue().add("1111.4444^^^&amp;26.489.22&amp;ISO");
		slotType1.setValueList(valueListType);

	}

	@After
	public void tearDown() {
		xdrTransformHelper = null;
	}

	@Test
	public void testTransformXDRToCheckPolicy() throws Exception {

		XDREventType eventA = new XDREventType();

		assertionType.setMessageId(MESSAGE_ID);

		pnt.setFullName("Minh");
		assertionType.setPersonName(pnt);

		rsl.getSlot().add(slotType1);

		sor.setId("100");
		sor.setRequestSlotList(rsl);

		ProvideAndRegisterDocumentSetRequestType prdsrt = new ProvideAndRegisterDocumentSetRequestType();
		Document doc = new Document();
		doc.setId("223");
		prdsrt.getDocument().add(doc);
		prdsrt.setSubmitObjectsRequest(sor);

		sor.setRegistryObjectList(rolt);

		XDRMessageType xmt = new XDRMessageType();
		eventA.setMessage(xmt);
		xmt.setAssertion(assertionType);
		xmt.setProvideAndRegisterDocumentSetRequest(prdsrt);

		RequestType request = new RequestType();
		SubjectHelper subjHelp = new SubjectHelper();
		SubjectType subject = subjHelp.subjectFactory(hct, assertionType);
		request.getSubject().add(subject);

		assertionType.setHomeCommunity(hct);
		assertionType.setSSN("170112756");
		eventA.setSendingHomeCommunity(hct);
		String direction = "inbound";
		eventA.setDirection(direction);
		eventA.getMessage().setAssertion(assertionType);

		CheckPolicyRequestType result = xdrTransformHelper.transformXDRToCheckPolicy(eventA);

		verifycheckPolicy_Request(result);

	}

	@Test
	public void testTransformXDRResponseToCheckPolicy() throws Exception {

		XDRResponseEventType eventA = new XDRResponseEventType();

		assertionType.setMessageId(MESSAGE_ID);

		pnt.setFullName("Minh");
		assertionType.setPersonName(pnt);

		rsl.getSlot().add(slotType1);

		sor.setId("100");
		sor.setRequestSlotList(rsl);

		RegistryResponseType prdsrt = new RegistryResponseType();

		prdsrt.setResponseSlotList(rsl);
		prdsrt.setRequestId("1235");

		sor.setRegistryObjectList(rolt);

		XDRResponseMessageType xmt = new XDRResponseMessageType();
		eventA.setMessage(xmt);
		xmt.setAssertion(assertionType);
		xmt.setRegistryResponse(prdsrt);

		assertionType.setHomeCommunity(hct);
		assertionType.setSSN("170112756");
		eventA.setSendingHomeCommunity(hct);
		String direction = "inbound";
		eventA.setDirection(direction);
		eventA.getMessage().setAssertion(assertionType);

		RequestType request = new RequestType();
		SubjectHelper subjHelp = new SubjectHelper();
		SubjectType subject = subjHelp.subjectFactory(hct, assertionType);
		request.getSubject().add(subject);
		CheckPolicyRequestType result = xdrTransformHelper.transformXDRResponseToCheckPolicy(eventA);

		verifycheckPolicy_Request(result);

	}

	private void verifycheckPolicy_Request(CheckPolicyRequestType actualCheckPolicy) {

		assertNotNull(actualCheckPolicy);

		AssertionType assertTypeResult = actualCheckPolicy.getAssertion();

		assertEquals("messageId should not change", MESSAGE_ID, assertTypeResult.getMessageId());
		SubjectType subjectType = actualCheckPolicy.getRequest().getSubject().get(0);
		AttributeType attributeType = subjectType.getAttribute().get(1);
		assertEquals("http://www.hhs.gov/healthit/nhin#HomeCommunityId", attributeType.getAttributeId());
		assertEquals("http://www.w3.org/2001/XMLSchema#anyURI", attributeType.getDataType());
		assertEquals(TEST_HC_VAL, attributeType.getAttributeValue().get(0).getContent().get(0));

	}

	@Test
	public void testNull_Event() {

		assertNull(xdrTransformHelper.transformXDRResponseToCheckPolicy(null));
		assertNull(xdrTransformHelper.transformXDRToCheckPolicy(null));

	}

}
