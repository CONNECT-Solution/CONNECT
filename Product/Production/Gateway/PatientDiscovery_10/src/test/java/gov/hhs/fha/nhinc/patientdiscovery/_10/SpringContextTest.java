package gov.hhs.fha.nhinc.patientdiscovery._10;

import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.patientdiscovery.NhinPatientDiscoveryImpl;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws.NhinPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PatientDiscoveryEventAspect;
import gov.hhs.fha.nhinc.patientdiscovery.nhin.InboundPatientDiscoveryOrchFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/EventFactoryConfig.xml", "/applicationContext-patientdiscovery.xml",
        "/AuditRepositoryProxyConfig.xml" })
public class SpringContextTest {

    @Autowired
    PatientDiscoveryEventAspect pdeventAspect;

    @Autowired
    InboundPatientDiscoveryOrchFactory inboundPatientDiscoveryOrchFactory;

    @Autowired
    PatientDiscoveryAuditLogger patientDiscoveryAuditLogger;

    @Autowired
    NhinPatientDiscoveryImpl nhinPDOrchestrator;

    @Autowired
    NhinPatientDiscovery nhinPD;

    @Test
    public void checkPatientDiscoveryEventAspect() {
        assertNotNull(pdeventAspect);
    }

    @Test
    public void checkInboundPatientDiscoveryOrchFactory() {
        assertNotNull(inboundPatientDiscoveryOrchFactory);
    }

    @Test
    public void checkPatientDiscoveryAuditLogger() {
        assertNotNull(patientDiscoveryAuditLogger);
    }

    @Test
    public void checkNhinPDOrchestrator() {
        assertNotNull(nhinPDOrchestrator);
    }

    @Test
    public void checkNhinPD() {
        assertNotNull(nhinPD);
    }
}
