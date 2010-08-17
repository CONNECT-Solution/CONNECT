package gov.hhs.fha.nhinc.redactionengine.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.policyengine.adapter.pip.CDAConstants;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
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
    public void testGetHomeCommunityIdBeforeSet()
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
            assertNull("Home Community id", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testGetHomeCommunityIdBeforeSet test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHomeCommunityIdBeforeSet test: " + t.getMessage());
        }

    }

    @Test
    public void testSetHomeCommunityIdId()
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
            processor.setHomeCommunityId("test_homeCommunity_id");
            assertEquals("Home community id", "test_homeCommunity_id", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testSetHomeCommunityIdId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testSetHomeCommunityIdId test: " + t.getMessage());
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
            adhocQuery.setHome("urn:oid:7.7");
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
            assertNotNull("HomeCommunity id was null", processor.getHomeCommunityId());
            assertEquals("Patient identifier invalid", "ABC123321CBA", processor.getPatientId());
            assertEquals("Assigning authority id invalid", "9.8.7.6.5.4.3.2.1", processor.getAssigningAuthorityId());
            assertEquals("Home community id invalid", "7.7", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiersHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiersHappy test: " + t.getMessage());
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
                    one(mockLog).warn("AdhocQueryRequest was null.");
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
            AdhocQueryType adhocQuery = null;
            adhocQueryRequest.setAdhocQuery(adhocQuery);

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
    public void testExtractIdentifiersEmptySlots()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryType adhocQuery = new AdhocQueryType();
            adhocQueryRequest.setAdhocQuery(adhocQuery);
            adhocQuery.setHome("urn:oid:7.7");

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
            assertNull("Patient identifier was not null", processor.getPatientId());
            assertNull("Assigning authority id was not null", processor.getAssigningAuthorityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiersEmptySlots test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiersEmptySlots test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractIdentifiersNullHomeCommunityId()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryType adhocQuery = new AdhocQueryType();
            adhocQueryRequest.setAdhocQuery(adhocQuery);

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
            assertNull("HomeCommunity id was not null", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiersNullHomeCommunityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiersNullHomeCommunityId test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractIdentifiersUnformattedHomeCommunityId()
    {
        try
        {
            AdhocQueryRequest adhocQueryRequest = new AdhocQueryRequest();
            AdhocQueryType adhocQuery = new AdhocQueryType();
            adhocQueryRequest.setAdhocQuery(adhocQuery);
            adhocQuery.setHome("7.7");

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
            assertNotNull("HomeCommunity id was null", processor.getHomeCommunityId());
            assertEquals("Home community id", "7.7", processor.getHomeCommunityId());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractIdentifiersUnformattedHomeCommunityId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractIdentifiersUnformattedHomeCommunityId test: " + t.getMessage());
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
                    one(mockPatientConsentHelper).retrievePatientConsentbyPatientId(with(aNonNull(String.class)), with(aNonNull(String.class)));
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
                    one(mockLog).warn("AdhocQueryRequest was null.");
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
                    one(mockLog).warn("AdhocQueryResponse was null.");
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
                    one(mockLog).warn("PatientConsentHelper was null.");
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
                    one(mockLog).warn("PatientPreferences was null.");
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
    public void filterAdhocQueryResultsNullPatientId()
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
                    one(mockLog).info("Not a patient-centric query.");
                }
            });

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
                @Override
                protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
                {
                    setPatientId(null);
                    setAssigningAuthorityId("mock_assigning_authority_id");
                }
                @Override
                protected AdhocQueryResponse filterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse)
                {
                    return new AdhocQueryResponse();
                }
            };
            AdhocQueryResponse response = processor.filterAdhocQueryResults(adhocQueryRequest, mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsNullPatientId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsNullPatientId test: " + t.getMessage());
        }
    }

    @Test
    public void filterAdhocQueryResultsEmptyPatientId()
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
                    one(mockLog).info("Not a patient-centric query.");
                }
            });

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
                @Override
                protected void extractIdentifiers(AdhocQueryRequest adhocQueryRequest)
                {
                    setPatientId("");
                    setAssigningAuthorityId("mock_assigning_authority_id");
                }
                @Override
                protected AdhocQueryResponse filterResultsNonPatientCentric(AdhocQueryResponse adhocQueryResponse)
                {
                    return new AdhocQueryResponse();
                }
            };
            AdhocQueryResponse response = processor.filterAdhocQueryResults(adhocQueryRequest, mockAdhocQueryResponse);
            assertNotNull("AdhocQueryResponse was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running filterAdhocQueryResultsEmptyPatientId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running filterAdhocQueryResultsEmptyPatientId test: " + t.getMessage());
        }
    }





    @Test
    public void testFilterResultsHappy()
    {
        try
        {
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObject));
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

            adhocQueryResponse.setTotalResultCount(BigInteger.ONE);

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNotNull("AdhocQueryResponse was null", response);
            assertNotNull("RegistryObjectList was null", response.getRegistryObjectList());
            assertFalse("Identifiable list was empty", response.getRegistryObjectList().getIdentifiable().isEmpty());
            assertEquals("Result count", 1L, response.getTotalResultCount().longValue());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsMultipleDocs()
    {
        try
        {
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObject));
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");
            ExtrinsicObjectType extObject2 = new ExtrinsicObjectType();
            registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObject2));
            ExternalIdentifierType extId2 = new ExternalIdentifierType();
            extObject2.getExternalIdentifier().add(extId2);
            extId2.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId2.setValue("test_doc_id2");

            adhocQueryResponse.setTotalResultCount(BigInteger.valueOf(2));

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNotNull("AdhocQueryResponse was null", response);
            assertNotNull("RegistryObjectList was null", response.getRegistryObjectList());
            assertFalse("Identifiable list was empty", response.getRegistryObjectList().getIdentifiable().isEmpty());
            assertEquals("Result count", 2L, response.getTotalResultCount().longValue());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsMultipleDocs test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsMultipleDocs test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNotAllowed()
    {
        try
        {
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObject));
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

            adhocQueryResponse.setTotalResultCount(BigInteger.ONE);

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, false);

            assertNotNull("AdhocQueryResponse was null", response);
            assertNull("RegistryObjectList was not null", response.getRegistryObjectList());
            assertEquals("Result count", 0L, response.getTotalResultCount().longValue());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNotAllowed test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNotAllowed test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNullAdhocQueryResponse()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = null;
            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).warn("AdhocQueryResponse was null.");
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNullAdhocQueryResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNullAdhocQueryResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNullRegistryObjectList()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info("RegistryObjectList was null.");
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNotNull("AdhocQueryResponse was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNullRegistryObjectList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNullRegistryObjectList test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsEmptyExtrinsicObjectList()
    {
        try
        {
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);

            adhocQueryResponse.setTotalResultCount(BigInteger.ONE);

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNotNull("AdhocQueryResponse was null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsEmptyExtrinsicObjectList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsEmptyExtrinsicObjectList test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNullPatientPreferences()
    {
        try
        {
            oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory rimObjFact = new oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory();

            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            registryObjectList.getIdentifiable().add(rimObjFact.createExtrinsicObject(extObject));
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

            adhocQueryResponse.setTotalResultCount(BigInteger.ONE);

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    allowing(mockLog).info(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResults(adhocQueryResponse, patientPreferences, true);

            assertNotNull("AdhocQueryResponse was null", response);
            assertNotNull("RegistryObjectList was null", response.getRegistryObjectList());
            assertFalse("Identifiable list was empty", response.getRegistryObjectList().getIdentifiable().isEmpty());
            assertEquals("Result count", 1L, response.getTotalResultCount().longValue());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsHappy test: " + t.getMessage());
        }
    }

    private AdhocQueryResponse testFilterResults(AdhocQueryResponse adhocQueryResponse, PatientPreferencesType patientPreferences, final boolean docAllowedFlag)
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
            @Override
            protected PatientPreferencesType retrievePatientPreferencesForDocument(ExtrinsicObjectType oExtObj)
            {
                return new PatientPreferencesType();
            }
            @Override
            protected boolean documentAllowed(ExtrinsicObjectType extObject, PatientPreferencesType patientPreferences)
            {
                return docAllowedFlag;
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
            RegistryObjectListType registryObjectList = new RegistryObjectListType();
            adhocQueryResponse.setStatus("test_status");
            adhocQueryResponse.setRegistryObjectList(registryObjectList);

            adhocQueryResponse.setTotalResultCount(BigInteger.ONE);

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            AdhocQueryResponse response = testFilterResultsNonPatientCentric(adhocQueryResponse);

            assertNotNull("AdhocQueryResponse was not null", response);
            assertEquals("Status", "test_status", response.getStatus());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNonPatientCentricHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNonPatientCentricHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNonPatientCentricNullInput()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = null;

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockLog).warn("AdhocQueryResponse was null.");
                }
            });
            AdhocQueryResponse response = testFilterResultsNonPatientCentric(adhocQueryResponse);

            assertNull("AdhocQueryResponse was not null", response);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNonPatientCentricNullInput test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNonPatientCentricNullInput test: " + t.getMessage());
        }
    }

    @Test
    public void testFilterResultsNonPatientCentricNullRegistryObjectList()
    {
        try
        {
            AdhocQueryResponse adhocQueryResponse = new AdhocQueryResponse();

            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockLog).info("RegistryObjectList was null.");
                }
            });
            AdhocQueryResponse response = testFilterResultsNonPatientCentric(adhocQueryResponse);

            assertNotNull("AdhocQueryResponse was null", response);
            assertNull("RegistryObjectList was not null", response.getRegistryObjectList());
        }
        catch(Throwable t)
        {
            System.out.println("Error running testFilterResultsNonPatientCentricNullRegistryObjectList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testFilterResultsNonPatientCentricNullRegistryObjectList test: " + t.getMessage());
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







    @Test
    public void testExtractDocumentIdHappy()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNotNull("Extracted document id was null", extractedDocumentId);
            assertEquals("Document id", "test_doc_id", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentIdEmptyExtIdentifierList()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNull("Extracted document id was not null", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdEmptyExtIdentifierList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdEmptyExtIdentifierList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentIdNullIdentificationScheme()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setValue("test_doc_id");

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNull("Extracted document id was not null", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdNullIdentificationScheme test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdNullIdentificationScheme test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentIdBadIdentificationScheme()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME + "bad");
            extId.setValue("test_doc_id");

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNull("Extracted document id was not null", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdBadIdentificationScheme test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdBadIdentificationScheme test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentIdNullDocId()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNull("Extracted document id was not null", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdNullDocId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdNullDocId test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentIdEmptyDocId()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("");

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
            String extractedDocumentId = processor.extractDocumentId(extObject);

            assertNull("Extracted document id was not null", extractedDocumentId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentIdEmptyDocId test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentIdEmptyDocId test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractRepositoryIdHappy()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            SlotType1 slot = new SlotType1();
            extObject.getSlot().add(slot);
            slot.setName(CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID);
            ValueListType valueList = new ValueListType();
            slot.setValueList(valueList);
            valueList.getValue().add("5");

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
            String repositoryId = processor.extractRepositoryId(extObject);

            assertNotNull("Extracted repository id was null", repositoryId);
            assertEquals("Repository id", "5", repositoryId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractRepositoryIdHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractRepositoryIdHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractRepositoryIdNoValueList()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            SlotType1 slot = new SlotType1();
            extObject.getSlot().add(slot);
            slot.setName(CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID);

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
            String repositoryId = processor.extractRepositoryId(extObject);

            assertNull("Extracted repository id was not null", repositoryId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractRepositoryIdNoValueList test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractRepositoryIdNoValueList test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractRepositoryIdBadSlotName()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            SlotType1 slot = new SlotType1();
            extObject.getSlot().add(slot);
            slot.setName(CDAConstants.SLOT_NAME_REPOSITORY_UNIQUE_ID + "bad");
            ValueListType valueList = new ValueListType();
            slot.setValueList(valueList);
            valueList.getValue().add("5");

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
            String repositoryId = processor.extractRepositoryId(extObject);

            assertNull("Extracted repository id was not null", repositoryId);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractRepositoryIdBadSlotName test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractRepositoryIdBadSlotName test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentTypeHappy()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ClassificationType typeCodeClassification = new ClassificationType();
            extObject.getClassification().add(typeCodeClassification);
            typeCodeClassification.setClassificationScheme("urn:uuid:f0306f51-975f-434e-a61c-c59651d33983");
            typeCodeClassification.setNodeRepresentation("123-abc");

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
            String documentType = processor.extractDocumentType(extObject);

            assertNotNull("Extracted document type was null", documentType);
            assertEquals("Document type code", "123-abc", documentType);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentTypeHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentTypeHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentTypeNoClassifications()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
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
            String documentType = processor.extractDocumentType(extObject);

            assertNull("Extracted document type was not null", documentType);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentTypeNoClassifications test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentTypeNoClassifications test: " + t.getMessage());
        }
    }

    @Test
    public void testExtractDocumentTypeBadScheme()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ClassificationType typeCodeClassification = new ClassificationType();
            extObject.getClassification().add(typeCodeClassification);
            typeCodeClassification.setClassificationScheme("urn:uuid:f0306f51-975f-434e-a61c-c59651d33983z");
            typeCodeClassification.setNodeRepresentation("123-abc");

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
            String documentType = processor.extractDocumentType(extObject);

            assertNull("Extracted document type was not null", documentType);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testExtractDocumentTypeBadScheme test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testExtractDocumentTypeBadScheme test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientPreferencesForDocumentHappy()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

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
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockPatientConsentHelper).retrievePatientConsentbyDocumentId(with(any(String.class)), with(any(String.class)), with(any(String.class)));
                }
            });
            PatientPreferencesType patientPreferences = processor.retrievePatientPreferencesForDocument(extObject);

            assertNotNull("Extracted patient preferences was null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientPreferencesForDocumentHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientPreferencesForDocumentHappy test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientPreferencesForDocumentNullExtObject()
    {
        try
        {
            ExtrinsicObjectType extObject = null;

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
                    allowing(mockLog).error("Extrinsic Object was null.");
                }
            });
            PatientPreferencesType patientPreferences = processor.retrievePatientPreferencesForDocument(extObject);

            assertNull("Extracted patient preferences was not null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientPreferencesForDocumentNullExtObject test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientPreferencesForDocumentNullExtObject test: " + t.getMessage());
        }
    }

    @Test
    public void testRetrievePatientPreferencesForDocumentNullResponse()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

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
                    PatientConsentHelper patientConsentHelper = new PatientConsentHelper()
                    {
                        @Override
                        public PatientPreferencesType retrievePatientConsentbyDocumentId(String homeCommunityId, String repositoryId, String documentId)
                        {
                            return null;
                        }
                    };
                    return patientConsentHelper;
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                }
            });
            PatientPreferencesType patientPreferences = processor.retrievePatientPreferencesForDocument(extObject);

            assertNull("Extracted patient preferences was not null", patientPreferences);
        }
        catch(Throwable t)
        {
            System.out.println("Error running testRetrievePatientPreferencesForDocumentNullResponse test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testRetrievePatientPreferencesForDocumentNullResponse test: " + t.getMessage());
        }
    }

    @Test
    public void testDocumentAllowedHappy()
    {
        try
        {
            ExtrinsicObjectType extObject = new ExtrinsicObjectType();
            ExternalIdentifierType extId = new ExternalIdentifierType();
            extObject.getExternalIdentifier().add(extId);
            extId.setIdentificationScheme(CDAConstants.DOCUMENT_ID_IDENT_SCHEME);
            extId.setValue("test_doc_id");

            PatientPreferencesType patientPreferences = new PatientPreferencesType();

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
                protected String extractDocumentType(ExtrinsicObjectType oExtObj)
                {
                    return "mock_doc_type";
                }
            };
            context.checking(new Expectations()
            {
                {
                    allowing(mockLog).debug(with(any(String.class)));
                    one(mockPatientConsentHelper).documentSharingAllowed(with(aNonNull(String.class)), with(aNonNull(PatientPreferencesType.class)));
                }
            });
            
            assertFalse("DocumentAllowed", processor.documentAllowed(extObject, patientPreferences));
        }
        catch(Throwable t)
        {
            System.out.println("Error running testDocumentAllowedHappy test: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testDocumentAllowedHappy test: " + t.getMessage());
        }
    }

}