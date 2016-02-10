/*
 * Copyright (c) 2009-2016, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.policyengine.adapter.pip;

import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * Unit test for the QueryUtil class
 *
 *
 *
 * @author Neil Webb
 */

// @RunWith(JMock.class)

public class QueryUtilTest

{

	Mockery context = new JUnit4Mockery();

	@Test
	public void testCreateAdhocQueryRequest()

	{

		try

		{

			// Create Mock objects

			QueryUtil queryUtil = new QueryUtil();

			String sPatientId = "123abc";

			String sAssigningAuthority = "1.1";

			String expectedPatientId = "'" + sPatientId + "^^^&"
					+ sAssigningAuthority + "&ISO'";

			AdhocQueryRequest request = queryUtil.createAdhocQueryRequest(
					sPatientId, sAssigningAuthority);

			assertNotNull("Request was null", request);

			// Validate response option

			assertNotNull("Response option was null",
					request.getResponseOption());

			assertEquals("Return composed objects flag", true, request
					.getResponseOption().isReturnComposedObjects());

			assertEquals("Return type",
					CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS, request
							.getResponseOption().getReturnType());

			// Validate AdhocQuery

			assertNotNull("AdhocQuery was null", request.getAdhocQuery());

			assertEquals("Query id",
					CDAConstants.ADHOC_QUERY_REQUEST_BY_PATIENT_ID_UUID,
					request.getAdhocQuery().getId());

			assertNotNull("Query slot list", request.getAdhocQuery().getSlot());

			// Patient ID slot

			SlotType1 slot = retrieveFirstSlotOfName(request.getAdhocQuery()
					.getSlot(),
					CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_CPP_PATIENT_ID);

			assertNotNull("Patient ID slot was null", slot);

			assertNotNull("Patient ID slot value list null",
					slot.getValueList());

			assertNotNull("Patient ID slot value list container null", slot
					.getValueList().getValue());

			assertEquals("Patient ID value", expectedPatientId, slot
					.getValueList().getValue().get(0));

			// Class code slot

			slot = retrieveFirstSlotOfName(
					request.getAdhocQuery().getSlot(),
					CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_DOCUMENT_CLASS_CODE);

			assertNotNull("Class code slot was null", slot);

			assertNotNull("Class code slot value list null",
					slot.getValueList());

			assertNotNull("Class code slot value list container null", slot
					.getValueList().getValue());

			assertEquals("Class code ID value",
					CDAConstants.ADHOC_QUERY_CLASS_CODE, slot.getValueList()
							.getValue().get(0));

			// Status slot

			slot = retrieveFirstSlotOfName(request.getAdhocQuery().getSlot(),
					CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);

			assertNotNull("Status slot was null", slot);

			assertNotNull("Status slot value list null", slot.getValueList());

			assertNotNull("Status slot value list container null", slot
					.getValueList().getValue());

			assertEquals("Status value",
					CDAConstants.STATUS_APPROVED_QUERY_VALUE, slot
							.getValueList().getValue().get(0));

		}

		catch (Throwable t)

		{

			t.printStackTrace();

			fail(t.getMessage());

		}

	}

	private SlotType1 retrieveFirstSlotOfName(List<SlotType1> olSlot,
			String slotName)

	{

		SlotType1 slot = null;

		if (olSlot != null)

		{

			for (SlotType1 oSlot : olSlot)

			{

				if ((oSlot != null) && (slotName.equals(oSlot.getName())))

				{

					slot = oSlot;

					break;

				}

			}

		}

		return slot;

	}

}