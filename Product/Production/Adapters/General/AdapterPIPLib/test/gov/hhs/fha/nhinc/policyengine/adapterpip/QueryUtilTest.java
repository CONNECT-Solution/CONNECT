package gov.hhs.fha.nhinc.policyengine.adapterpip;

import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.runner.RunWith;

/**
 * Unit test for the QueryUtil class
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class QueryUtilTest
{
    Mockery context = new JUnit4Mockery();

    @Test
    public void testCreateAdhocQueryRequest()
    {
        try
        {
            // Create Mock objects
            final Log mockLog = context.mock(Log.class);

            QueryUtil queryUtil = new QueryUtil()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }

            };

            // Set expectations
            context.checking(new Expectations(){{
                allowing (mockLog).isDebugEnabled();
                allowing (mockLog).isInfoEnabled();
                allowing (mockLog).debug(with(any(String.class)));
                allowing (mockLog).info(with(any(String.class)));
            }});

            String sPatientId = "123abc";
            String sAssigningAuthority = "1.1";
            String expectedPatientId = "'" + sPatientId + "^^^&" + sAssigningAuthority + "&ISO'";

            AdhocQueryRequest request = queryUtil.createAdhocQueryRequest(sPatientId, sAssigningAuthority);
            assertNotNull("Request was null", request);

            // Validate response option
            assertNotNull("Response option was null", request.getResponseOption());
            assertEquals("Return composed objects flag", true, request.getResponseOption().isReturnComposedObjects());
            assertEquals("Return type", CDAConstants.ADHOC_QUERY_REQUEST_LEAF_CLASS, request.getResponseOption().getReturnType());

            // Validate AdhocQuery
            assertNotNull("AdhocQuery was null", request.getAdhocQuery());
            assertEquals("Query id", CDAConstants.ADHOC_QUERY_REQUEST_BY_PATIENT_ID_UUID, request.getAdhocQuery().getId());
            assertNotNull("Query slot list", request.getAdhocQuery().getSlot());

            // Patient ID slot
            SlotType1 slot = retrieveFirstSlotOfName(request.getAdhocQuery().getSlot(), CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_CPP_PATIENT_ID);
            assertNotNull("Patient ID slot was null", slot);
            assertNotNull("Patient ID slot value list null", slot.getValueList());
            assertNotNull("Patient ID slot value list container null", slot.getValueList().getValue());
            assertEquals("Patient ID value", expectedPatientId, slot.getValueList().getValue().get(0));

            // Class code slot
            slot = retrieveFirstSlotOfName(request.getAdhocQuery().getSlot(), CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_DOCUMENT_CLASS_CODE);
            assertNotNull("Class code slot was null", slot);
            assertNotNull("Class code slot value list null", slot.getValueList());
            assertNotNull("Class code slot value list container null", slot.getValueList().getValue());
            assertEquals("Class code ID value", CDAConstants.ADHOC_QUERY_CLASS_CODE, slot.getValueList().getValue().get(0));

            // Status slot
            slot = retrieveFirstSlotOfName(request.getAdhocQuery().getSlot(), CDAConstants.ADHOC_QUERY_REQUEST_SLOT_NAME_STATUS);
            assertNotNull("Status slot was null", slot);
            assertNotNull("Status slot value list null", slot.getValueList());
            assertNotNull("Status slot value list container null", slot.getValueList().getValue());
            assertEquals("Status value", CDAConstants.STATUS_APPROVED_QUERY_VALUE, slot.getValueList().getValue().get(0));

        }
        catch(Throwable t)
        {
            t.printStackTrace();
            fail(t.getMessage());
        }
    }

    private SlotType1 retrieveFirstSlotOfName(List<SlotType1> olSlot, String slotName)
    {
        SlotType1 slot = null;
        if(olSlot != null)
        {
            for(SlotType1 oSlot : olSlot)
            {
                if((oSlot != null) && (slotName.equals(oSlot.getName())))
                {
                    slot = oSlot;
                    break;
                }
            }
        }

        return slot;
    }

}