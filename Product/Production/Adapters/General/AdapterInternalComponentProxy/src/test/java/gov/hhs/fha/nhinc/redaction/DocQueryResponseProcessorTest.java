package gov.hhs.fha.nhinc.redaction;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class DocQueryResponseProcessorTest
{
    Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);
    final PatientConsentHelper mockPatientConsentHelper = context.mock(PatientConsentHelper.class);
    final AdhocQueryRequest mockAdhocQueryRequest = context.mock(AdhocQueryRequest.class);
    final AdhocQueryResponse mockAdhocQueryResponse = context.mock(AdhocQueryResponse.class);

    @Test
    public void testCreateLogger()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };

            Log log = processor.createLogger();
            assertNotNull("Log was null", log);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testCreateLogger test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testCreateLogger test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientConsentHelper()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
            };

            PatientConsentHelper patientConsentHelper = processor.getPatientConsentHelper();
            assertNotNull("PatientConsentHelper was null", patientConsentHelper);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientConsentHelper test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientConsentHelper test: " + t.getMessage());
        }
    }

    @Test
    public void testGetPatientIdBeforeSet()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertNull("Patient identifier", processor.getPatientId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetPatientIdBeforeSet test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetPatientIdBeforeSet test: " + t.getMessage());
        }

    }

    @Test
    public void testSetPatientId()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            processor.setPatientId("test_patient_id");
            assertEquals("Patient identifier", "test_patient_id", processor.getPatientId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetPatientId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetPatientId test: " + t.getMessage());
        }

    }

    @Test
    public void testGetAssigningAuthorityIdBeforeSet()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            assertNull("Assigning authority id", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetAssigningAuthorityIdBeforeSet test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityIdBeforeSet test: " + t.getMessage());
        }

    }

    @Test
    public void testSetAssigningAuthorityId()
    {
        try
        {
            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            processor.setAssigningAuthorityId("test_assigning_authority_id");
            assertEquals("Assigning authority id", "test_assigning_authority_id", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetAssigningAuthorityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetAssigningAuthorityId test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesHappy()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNotNull("Slot values was null", slotValues);
            assertEquals("Slot values was empty", 1, slotValues.size());
            assertEquals("Slot value", "'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'", slotValues.get(0));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesNoSlotMatch()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "NoMatchSlotName");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesNoSlotMatch test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesNoSlotMatch test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesNullSlotList()
    {
        try
        {
            List<SlotType1> slots = null;

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "NoMatchSlotName");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesNullSlotList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesNullSlotList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesEmptySlotList()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesEmptySlotList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesEmptySlotList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesNullSlotName()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesNullSlotName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesNullSlotName test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesEmptySlotName()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesEmptySlotName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesEmptySlotName test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesNullValueList()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesNullValueList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesNullValueList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesEmptyValueList()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNull("Slot values was not null", slotValues);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesEmptyValueList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesEmptyValueList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractSlotValuesMultipleValues()
    {
        try
        {
            List<SlotType1> slots = new ArrayList<SlotType1>();
            SlotType1 patientIdSlot = new SlotType1();
            slots.add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.2&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            List<String> slotValues = processor.extractSlotValues(slots, "$XDSDocumentEntryPatientId");
            assertNotNull("Slot values was null", slotValues);
            assertEquals("Slot values count", 2, slotValues.size());
            assertEquals("Slot value 1", "'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'", slotValues.get(0));
            assertEquals("Slot value 2", "'ABC123321CBA^^^&9.8.7.6.5.4.3.2.2&ISO'", slotValues.get(1));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractSlotValuesMultipleValues test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractSlotValuesMultipleValues test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractIdentifiersHappy()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryType adhocQuery = new AdhocQueryType();
            adhocQueryRequest.setAdhocQuery(adhocQuery);
            SlotType1 patientIdSlot = new SlotType1();
            adhocQuery.getSlot().add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            processor.extractIdentifiers(adhocQueryRequest);
            assertNotNull("Patient identifier null", processor.getPatientId());
            assertNotNull("Assigning authority id null", processor.getAssigningAuthorityId());
            assertEquals("Patient identifier invalid", "ABC123321CBA", processor.getPatientId());
            assertEquals("Assigning authority id invalid", "9.8.7.6.5.4.3.2.1", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiers test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiers test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractIdentifiersNullAdhocQueryRequest()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = null;

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryRequest was null.");
                }
            });

            processor.extractIdentifiers(adhocQueryRequest);
            assertNull("Patient identifier", processor.getPatientId());
            assertNull("Assigning authority id", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiers test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiers test: " + t.getMessage());
        }

    }

    @Test
    public void testExtractIdentifiersNullAdhocQuery()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();

            DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
            {
                @Override
                protected Log createLogger()
                {
                    return mockLog;
                }
                @Override
                protected PatientConsentHelper getPatientConsentHelper()
                {
                    return mockPatientConsentHelper;
                }
                @Override
                protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
                {
                    return mockAdhocQueryResponse;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            processor.extractIdentifiers(adhocQueryRequest);
            assertNull("Patient identifier", processor.getPatientId());
            assertNull("Assigning authority id", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiers test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiers test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsHappy()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryType adhocQuery = new AdhocQueryType();
            adhocQueryRequest.setAdhocQuery(adhocQuery);
            SlotType1 patientIdSlot = new SlotType1();
            adhocQuery.getSlot().add(patientIdSlot);
            patientIdSlot.setName("$XDSDocumentEntryPatientId");
            ValueListType valueList = new ValueListType();
            patientIdSlot.setValueList(valueList);
            valueList.getValue().add("'ABC123321CBA^^^&9.8.7.6.5.4.3.2.1&ISO'");

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockPatientConsentHelper).retrievePatientConsentbyPatientId(with(aNonNull(String.class)), with(aNonNull(String.class)));
                }
            });

            AdhocQueryResponse response = testFilterAdhocQueryResults(adhocQueryRequest, mockAdhocQueryResponse, mockPatientConsentHelper);
            assertNotNull("AdhocQueryResponse was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsNullAdhocQueryRequest()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryRequest was null.");
                }
            });
            AdhocQueryResponse response = testFilterAdhocQueryResults(null, mockAdhocQueryResponse, mockPatientConsentHelper);
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsNullAdhocQueryResponse()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryResponse was null.");
                }
            });
            AdhocQueryResponse response = testFilterAdhocQueryResults(mockAdhocQueryRequest, null, mockPatientConsentHelper);
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsNullInputs test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsNullPatientConsentHelper()
    {
        try
        {
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("PatientConsentHelper was null.");
                }
            });
            AdhocQueryResponse response = testFilterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse, null);
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsNullPatientConsentHelper test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsNullPatientConsentHelper test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsNullPatientPreferences()
    {
        try
        {
            final PatientConsentHelper patientConsentHelper = new PatientConsentHelper()
            {
                @Override
                public PatientPreferencesType retrievePatientConsentbyPatientId(String patientId, String assigningAuthorityId)
                {
                    return null;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("PatientPreferences was null.");
                }
            });
            AdhocQueryResponse response = testFilterAdhocQueryResults(mockAdhocQueryRequest, mockAdhocQueryResponse, patientConsentHelper);
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsNullPatientPreferences test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsNullPatientPreferences test: " + t.getMessage());
        }
    }

    private AdhocQueryResponse testFilterAdhocQueryResults(AdhocQueryRequest adhocQueryRequest, AdhocQueryResponse adhocQueryResponse, final PatientConsentHelper patientConsentHelper)
    {
        AdhocQueryResponse response = null;
        DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected PatientConsentHelper getPatientConsentHelper()
            {
                return patientConsentHelper;
            }
            @Override
            protected AdhocQueryResponse filterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
            {
                return mockAdhocQueryResponse;
            }
            @Override
            protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
            {
                setPatientId("mock_patient_id");
                setAssigningAuthorityId("mock_assigning_authority_id");
            }
        };
        response = processor.filterAdhocQueryResults(adhocQueryRequest, adhocQueryResponse);
        return response;
    }





    @Test
    public void testFilterResultsHappy()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences);

            // TODO: Update after implimented
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNullInputs()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = null;
            PatientPreferencesType patientPreferences = null;

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences);

            // TODO: Update after implimented
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNullInputs test: " + t.getMessage());
        }
    }

    private AdhocQueryResponse testFilterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences)
    {
        AdhocQueryResponse response = null;
        DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected PatientConsentHelper getPatientConsentHelper()
            {
                return mockPatientConsentHelper;
            }
        };
        response = processor.filterResults(adhocQueryResponse, patientPreferences);
        return response;
    }







    @Test
    public void testFilterResultsNonPatientCentricHappy()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResultsNonPatientCentric(adhocQueryResponse);

            // TODO: Update after implimented
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNonPatientCentricHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNonPatientCentricHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNonPatientCentricNullInputs()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = null;

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResultsNonPatientCentric(adhocQueryResponse);

            // TODO: Update after implimented
            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNonPatientCentricNullInputs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNonPatientCentricNullInputs test: " + t.getMessage());
        }
    }

    private AdhocQueryResponse testFilterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse)
    {
        AdhocQueryResponse response = null;
        DocQueryResponseProcessor processor = new DocQueryResponseProcessor()
        {
            @Override
            protected Log createLogger()
            {
                return mockLog;
            }
            @Override
            protected PatientConsentHelper getPatientConsentHelper()
            {
                return mockPatientConsentHelper;
            }
        };
        response = processor.filterResultsNonPatientCentric(adhocQueryResponse);
        return response;
    }

}