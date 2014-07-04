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
package gov.hhs.fha.nhinc.admingui.services;

import gov.hhs.fha.nhinc.admingui.model.direct.DirectAgent;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectCertificate;
import gov.hhs.fha.nhinc.admingui.model.direct.DirectTrustBundle;

import java.util.List;

import org.nhind.config.common.AddAnchor;
import org.nhind.config.common.AddDomain;
import org.nhind.config.common.Anchor;
import org.nhind.config.common.Domain;
import org.nhind.config.common.GetAnchorsForOwner;
import org.nhind.config.common.RemoveAnchors;
import org.nhind.config.common.UpdateDomain;

/**
 *
 * @author jasonasmith
 */
public interface DirectService {

    public List<Domain> getDomains();
    public void updateDomain(UpdateDomain domain);
    public void addDomain(AddDomain domain);
    public void deleteDomain(Domain domain);

    public List<DirectAgent> getAgents();
    public void updateAgent(DirectAgent agent);
    public void addAgent(DirectAgent agent);
    public void deleteAgent(DirectAgent agent);

    public List<DirectCertificate> getCertificates();
    public void addCertificate(DirectCertificate cert);
    public void deleteCertificate(DirectCertificate cert);

    public List<DirectTrustBundle> getTrustBundles();
    public void updateTrustBundle(DirectTrustBundle tb);
    public void addTrustBundle(DirectTrustBundle tb);
    public void deleteTrustBundle(DirectTrustBundle tb);

    public List<Anchor> getAnchorsForOwner(GetAnchorsForOwner anchors);
    public void addAnchor(AddAnchor anchor);
    public void deleteAnchor(RemoveAnchors anchor);
}
