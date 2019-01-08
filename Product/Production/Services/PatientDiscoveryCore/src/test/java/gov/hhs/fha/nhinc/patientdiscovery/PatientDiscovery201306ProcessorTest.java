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
package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.connectionmanager.dao.AssigningAuthorityHomeCommunityMappingDAO;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.COCTMT090300UV01AssignedDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000300UV01Agent;
import org.hl7.v3.MCCIMT000300UV01Device;
import org.hl7.v3.MCCIMT000300UV01Organization;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.MFMIMT700711UV01AuthorOrPerformer;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author JHOPPESC
 */
public class PatientDiscovery201306ProcessorTest {

    Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    final AssigningAuthorityHomeCommunityMappingDAO mockMappingDao = context
            .mock(AssigningAuthorityHomeCommunityMappingDAO.class);
    final PRPAIN201306UV02 mockMessage = context.mock(PRPAIN201306UV02.class);


    @Test
    public void testGetAssigningAuthorityHomeCommunityMappingDAO() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return mockMappingDao;
                }
            };

            AssigningAuthorityHomeCommunityMappingDAO mappingDao = storage
                    .getAssigningAuthorityHomeCommunityMappingDAO();
            assertNotNull("AssigningAuthorityHomeCommunityMappingDAO was null", mappingDao);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityHomeCommunityMappingDAO: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityHomeCommunityMappingDAO: " + t.getMessage());
        }
    }

    @Test
    public void testStoreMappingHappy() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return new AssigningAuthorityHomeCommunityMappingDAO() {
                        @Override
                        public boolean storeMapping(String hcid, String assigningAuthorityId) {
                            return true;
                        }
                    };
                }

                @Override
                protected String getHcid(PRPAIN201306UV02 request) {
                    return "hcid";
                }

                @Override
                protected List<String> extractAAListFrom201306(PRPAIN201306UV02 request) {
                    List<String> assigningAuthorityIds = new ArrayList<>();
                    assigningAuthorityIds.add("aa");
                    return assigningAuthorityIds;

                }
            };

            storage.storeMapping(mockMessage);
        } catch (Throwable t) {
            System.out.println("Error running testStoreMappingHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStoreMappingHappy: " + t.getMessage());
        }
    }

    @Test
    public void testStoreMappingNullHcid() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return mockMappingDao;
                }

                @Override
                protected String getHcid(PRPAIN201306UV02 request) {
                    return null;
                }

                @Override
                protected List<String> extractAAListFrom201306(PRPAIN201306UV02 request) {
                    List<String> assigningAuthorityIds = new ArrayList<>();
                    assigningAuthorityIds.add("aa");
                    return assigningAuthorityIds;

                }
            };

            storage.storeMapping(mockMessage);
        } catch (Throwable t) {
            System.out.println("Error running testStoreMappingNullHcid: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStoreMappingNullHcid: " + t.getMessage());
        }
    }

    @Test
    public void testStoreMappingNullAssigningAuthority() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return mockMappingDao;
                }

                @Override
                protected String getHcid(PRPAIN201306UV02 request) {
                    return "hcid";
                }

                @Override
                protected List<String> extractAAListFrom201306(PRPAIN201306UV02 request) {
                    List<String> assigningAuthorityIds = new ArrayList<>();
                    assigningAuthorityIds.add(null);
                    return assigningAuthorityIds;

                }
            };

            storage.storeMapping(mockMessage);
        } catch (Throwable t) {
            System.out.println("Error running testStoreMappingNullAssigningAuthority: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStoreMappingNullAssigningAuthority: " + t.getMessage());
        }
    }

    @Test
    public void testStoreMappingNullMappingDao() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return null;
                }

                @Override
                protected String getHcid(PRPAIN201306UV02 request) {
                    return "hcid";
                }

                @Override
                protected List<String> extractAAListFrom201306(PRPAIN201306UV02 request) {
                    List<String> assigningAuthorityIds = new ArrayList<>();
                    assigningAuthorityIds.add("aa");
                    return assigningAuthorityIds;

                }
            };

            storage.storeMapping(mockMessage);
        } catch (Throwable t) {
            System.out.println("Error running testStoreMappingNullMappingDao: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStoreMappingNullMappingDao: " + t.getMessage());
        }
    }

    @Test
    public void testStoreMappingFailedStorage() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor() {

                @Override
                protected AssigningAuthorityHomeCommunityMappingDAO getAssigningAuthorityHomeCommunityMappingDAO() {
                    return new AssigningAuthorityHomeCommunityMappingDAO() {
                        @Override
                        public boolean storeMapping(String hcid, String assigningAuthorityId) {
                            return false;
                        }
                    };
                }

                @Override
                protected String getHcid(PRPAIN201306UV02 request) {
                    return "hcid";
                }

                @Override
                protected List<String> extractAAListFrom201306(PRPAIN201306UV02 request) {
                    List<String> assigningAuthorityIds = new ArrayList<>();
                    assigningAuthorityIds.add("aa");
                    return assigningAuthorityIds;

                }
            };

            storage.storeMapping(mockMessage);
        } catch (Throwable t) {
            System.out.println("Error running testStoreMappingFailedStorage: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testStoreMappingFailedStorage: " + t.getMessage());
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getHcid">
    @Test
    public void testGetHcidHappy() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = new MCCIMT000300UV01Organization();
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));
            II id = new II();
            representedOrganization.getId().add(id);
            id.setRoot("test_hcid");

            String hcid = storage.getHcid(request);
            assertNotNull("HCID was null", hcid);
            assertEquals("HCID incorrect", "test_hcid", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidHappy: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNoMatch() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = new MCCIMT000300UV01Organization();
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));
            II id = new II();
            representedOrganization.getId().add(id);
            id.setRoot("test_hcid");

            String hcid = storage.getHcid(request);
            assertNotNull("HCID was null", hcid);
            assertFalse("HCID incorrect", "test_hcid_no_match".equals(hcid));
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNoMatch: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNoMatch: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullHcid() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = new MCCIMT000300UV01Organization();
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));
            II id = new II();
            representedOrganization.getId().add(id);

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullHcid: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullHcid: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullId() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = new MCCIMT000300UV01Organization();
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));
            II id = null;
            representedOrganization.getId().add(id);

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullId: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidMissingId() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = new MCCIMT000300UV01Organization();
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidMissingId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidMissingId: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullRepresentedOrganization() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));
            MCCIMT000300UV01Organization representedOrganization = null;
            asAgent.setRepresentedOrganization(hl7OjbFactory
                    .createMCCIMT000300UV01AgentRepresentedOrganization(representedOrganization));

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullRepresentedOrganization: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullRepresentedOrganization: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidMissingRepresentedOrganization() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = new MCCIMT000300UV01Agent();
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidMissingRepresentedOrganization: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidMissingRepresentedOrganization: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullAsAgent() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);
            MCCIMT000300UV01Agent asAgent = null;
            device.setAsAgent(hl7OjbFactory.createMCCIMT000300UV01DeviceAsAgent(asAgent));

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullAsAgent: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullAsAgent: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidMissingAsAgent() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);
            MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
            sender.setDevice(device);

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidMissingAsAgent: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidMissingAsAgent: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullDevice() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
            request.setSender(sender);

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullDevice: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullDevice: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullSender() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullSender: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullSender: " + t.getMessage());
        }
    }

    @Test
    public void testGetHcidNullRequest() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = null;

            String hcid = storage.getHcid(request);
            assertNull("HCID was not null", hcid);
        } catch (Throwable t) {
            System.out.println("Error running testGetHcidNullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetHcidNullRequest: " + t.getMessage());
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getAssigningAuthority">

    @Test
    public void testGetAssigningAuthorityHappy() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));
            II id = new II();
            assignedDevice.getId().add(id);
            id.setRoot("test_aa");

            String aa = storage.getAssigningAuthority(request);
            assertNotNull("AssigningAuthority was null", aa);
            assertEquals("AssigningAuthority incorrect", "test_aa", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityHappy: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityHappy: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNoMatch() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));
            II id = new II();
            assignedDevice.getId().add(id);
            id.setRoot("test_aa");

            String aa = storage.getAssigningAuthority(request);
            assertNotNull("AssigningAuthority was null", aa);
            assertFalse("HCID incorrect", "test_aa_no_match".equals(aa));
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNoMatch: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNoMatch: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullAssigningAuthority() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));
            II id = new II();
            assignedDevice.getId().add(id);

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullAssigningAuthority: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullAssigningAuthority: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullId() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));
            II id = null;
            assignedDevice.getId().add(id);

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullId: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityMissingId() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = new COCTMT090300UV01AssignedDevice();
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityMissingId: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityMissingId: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullAssignedDevice() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            org.hl7.v3.ObjectFactory hl7OjbFactory = new org.hl7.v3.ObjectFactory();
            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);
            COCTMT090300UV01AssignedDevice assignedDevice = null;
            authorOrPerformer.setAssignedDevice(hl7OjbFactory
                    .createMFMIMT700701UV01AuthorOrPerformerAssignedDevice(assignedDevice));

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullAssignedDevice: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullAssignedDevice: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityMissingAssignedDevice() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = new MFMIMT700711UV01AuthorOrPerformer();
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityMissingAssignedDevice: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityMissingAssignedDevice: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullAuthorOrPerformer() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);
            MFMIMT700711UV01AuthorOrPerformer authorOrPerformer = null;
            controlActProcess.getAuthorOrPerformer().add(authorOrPerformer);

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullAuthorOrPerformer: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullAuthorOrPerformer: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityMissingAuthorOrPerformer() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();
            PRPAIN201306UV02MFMIMT700711UV01ControlActProcess controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
            request.setControlActProcess(controlActProcess);

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityMissingAuthorOrPerformer: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityMissingAuthorOrPerformer: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullControlActProcess() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = new PRPAIN201306UV02();

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullControlActProcess: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullControlActProcess: " + t.getMessage());
        }
    }

    @Test
    public void testGetAssigningAuthorityNullRequest() {
        try {
            PatientDiscovery201306Processor storage = new PatientDiscovery201306Processor();

            PRPAIN201306UV02 request = null;

            String aa = storage.getAssigningAuthority(request);
            assertNull("AssigningAuthority was not null", aa);
        } catch (Throwable t) {
            System.out.println("Error running testGetAssigningAuthorityNullRequest: " + t.getMessage());
            t.printStackTrace();
            fail("Error running testGetAssigningAuthorityNullRequest: " + t.getMessage());
        }
    }

    /**
     * Test of createNewRequest method, of class PatientDiscovery201306Processor.
     */
    @Test
    public void testCreateNewRequest() {
        System.out.println("testCreateNewRequest");

        JAXBElement<PRPAMT201301UV02Person> queryPerson = HL7PatientTransforms.create201301PatientPerson("Joe",
                "Smith", "M", null, null);
        PRPAMT201301UV02Patient queryPatient = HL7PatientTransforms.create201301Patient(queryPerson, "1234", "1.1.1");
        PRPAIN201305UV02 query = HL7PRPA201305Transforms.createPRPA201305(queryPatient, "1.1", "2.2", "1.1.1");

        JAXBElement<PRPAMT201301UV02Person> person = HL7PatientTransforms.create201301PatientPerson("Joe", "Smith",
                "M", null, null);
        PRPAMT201301UV02Patient patient = HL7PatientTransforms.create201301Patient(person, null, null);
        PRPAIN201306UV02 msg = HL7PRPA201306Transforms.createPRPA201306(patient, "2.2", "1.1.1", "1.1", "2.2.2", query);

        String targetCommunityId = "3.3";
        PatientDiscovery201306Processor instance = new PatientDiscovery201306Processor();
        PRPAIN201306UV02 result = instance.createNewRequest(msg, targetCommunityId);

        assertEquals(targetCommunityId, result.getReceiver().get(0).getDevice().getAsAgent().getValue()
                .getRepresentedOrganization().getValue().getId().get(0).getRoot());
    }

}
