/**
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services. All
 * rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met: Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or other materials provided with the
 * distribution. Neither the name of the United States Government nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
 */
package gov.hhs.fha.nhinc.admingui.services.impl.direct;

import gov.hhs.fha.nhinc.admingui.model.direct.DirectAddress;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectAgent;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectCertificate;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectDomain;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;
import gov.hhs.fha.nhinc.admingui.services.DirectService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.Domain;
import org.nhind.config.common.UpdateDomain;
import org.springframework.stereotype.Service;

/**
 *
 * @author jasonasmith
 */
public class MockDirectServiceImpl implements DirectService {
    
    private static HashMap<Integer, DirectDomain> domains;
    private static HashMap<Integer, DirectAgent> agents;
    private static HashMap<Integer, DirectCertificate> certificates;
    private static HashMap<Integer, DirectTrustBundle> trustBundles;
    
    static {
        domains = new HashMap<Integer, DirectDomain>();

        DirectDomain domain1 = new DirectDomain(1, "ENABLED", "Test Domain", "test@test", "6/3/14 4:04:00", null);
        domain1.addAddress(new DirectAddress("testEndpoint", "testType"));
        domains.put(1, domain1);
        domains.put(2, new DirectDomain(2, "ENABLED", "Connect Domain", "test@connect", "6/3/14 4:04:00", null));

        trustBundles = new HashMap<Integer, DirectTrustBundle>();

        trustBundles.put(1, new DirectTrustBundle(1, "bluebutton-providerstest", "https://bluebutton.com", "8d9121401ba2abcb16d16cb5195c360e84abedb6", "Today",
            null, null, null));
        trustBundles.put(2, new DirectTrustBundle(2, "DELETE-ME", "https://deleteme.com", "checksum", "Today",
            null, null, null));
        
        agents = new HashMap<Integer, DirectAgent>();
        
        agents.put(1, new DirectAgent(1, "OutgoingMessageSaveFolder", "/opt/direct-stock/james-2.3.2/apps/james/var/mail/directout"));
        agents.put(2, new DirectAgent(2, "IncomingMessageSaveFolder", "/opt/direct-stock/james-2.3.2/apps/james/var/mail/directin"));
        agents.put(3, new DirectAgent(3, "RawMessageSaveFolder", "/opt/direct-stock/james-2.3.2/apps/james/var/mail/directraw"));
        agents.put(4, new DirectAgent(4, "AnchorStoreType", "WS"));
        agents.put(5, new DirectAgent(5, "PublicStoreType", "WS,DNS"));
        agents.put(6, new DirectAgent(6, "BadMessageSaveFolder", "/opt/direct-stock/james-2.3.2/apps/james/var/mail/directerr"));
        agents.put(7, new DirectAgent(7, "MDNAutoResponse", "true"));
        agents.put(8, new DirectAgent(8, "PrivateStoreType", "WS"));
        agents.put(9, new DirectAgent(9, "DNSServerBindings", "0.0.0.0"));
        agents.put(10, new DirectAgent(10, "DNSServerPort", "53"));
        
        certificates = new HashMap<Integer, DirectCertificate>();
        
        certificates.put(1, new DirectCertificate(1, "ENABLED", "connectdirect.connect.org Root CA", "NO", "33b0a7f37dc0f8c3a6c0902bc3736aa35744fa58",
            "10/17/2013, 11:50", "10/17/2013, 09:55", "10/15/2023, 09:55"));
        certificates.put(2, new DirectCertificate(2, "ENABLED", "connectdirect.connect.org", "NO", "b145e498144454c16e9b9aff60b93e2f0e9839f3",
            "10/17/2013, 11:50", "10/17/2013, 09:54", "10/15/2023, 09:54"));
    }
    
    @Override
    public List<Domain> getDomains(){
        return null;
    }
    
    public List<DirectDomain> getDomainsMock() {
        return new ArrayList(domains.values());
    }

    @Override
    public void updateDomain(UpdateDomain domain) {
        //domains.put(domain.getPosition(), domain);
    }

    @Override
    public void deleteDomain(Domain domain) {
        
        Collection<DirectDomain> collectionDomains = domains.values();
        
        int i = 1;
        
        Iterator<DirectDomain> iter = collectionDomains.iterator();
        while(iter.hasNext()){
            DirectDomain temp = iter.next();
            temp.setPosition(i);
            i++;
        }
    }

    @Override
    public List<DirectAgent> getAgents() {
        return new ArrayList(agents.values());
    }

    @Override
    public void updateAgent(DirectAgent agent) {
        agents.put(agent.getPosition(), agent);
    }

    @Override
    public void deleteAgent(DirectAgent agent) {
        agents.remove(agent.getPosition());
        
        Collection<DirectAgent> collectionAgents = agents.values();
        
        int i = 1;
        
        Iterator<DirectAgent> iter = collectionAgents.iterator();
        while(iter.hasNext()){
            DirectAgent temp = iter.next();
            temp.setPosition(i);
            i++;
        }
    }

    @Override
    public List<DirectCertificate> getCertificates() {
        return new ArrayList(certificates.values());
    }

    @Override
    public void deleteCertificate(DirectCertificate cert) {
        certificates.remove(cert.getPosition());
        
        Collection<DirectCertificate> collection = certificates.values();
        
        int i = 1;
        
        Iterator<DirectCertificate> iter = collection.iterator();
        while(iter.hasNext()){
            DirectCertificate temp = iter.next();
            temp.setPosition(i);
            i++;
        }
    }

    @Override
    public List<DirectTrustBundle> getTrustBundles() {
        return new ArrayList(trustBundles.values());
    }

    @Override
    public void updateTrustBundle(DirectTrustBundle tb) {
        trustBundles.put(tb.getPosition(), tb);
    }

    @Override
    public void deleteTrustBundle(DirectTrustBundle tb) {
        trustBundles.remove(tb.getPosition());
        
        Collection<DirectTrustBundle> collection = trustBundles.values();
        
        int i = 1;
        
        Iterator<DirectTrustBundle> iter = collection.iterator();
        while(iter.hasNext()){
            DirectTrustBundle temp = iter.next();
            temp.setPosition(i);
            i++;
        }
    }

    @Override
    public void addDomain(AddDomain domain) {
        
    }

    @Override
    public void addAgent(DirectAgent agent) {
        int i = agents.size() + 1;
        agent.setPosition(i);
        agents.put(i, agent);
    }

    @Override
    public void addCertificate(DirectCertificate cert) {
        int i = certificates.size() + 1;
        cert.setPosition(i);
        certificates.put(i, cert);
    }

    @Override
    public void addTrustBundle(DirectTrustBundle tb) {
        int i = trustBundles.size() + 1;
        tb.setPosition(i);
        trustBundles.put(i, tb);
    }
    
}
