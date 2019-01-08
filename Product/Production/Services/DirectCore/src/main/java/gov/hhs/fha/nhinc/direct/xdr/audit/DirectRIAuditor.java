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
package gov.hhs.fha.nhinc.direct.xdr.audit;

import gov.hhs.fha.nhinc.direct.xdr.SoapEdgeContext;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import org.nhindirect.common.audit.AuditContext;
import org.nhindirect.common.audit.AuditEvent;
import org.nhindirect.common.audit.Auditor;
import org.nhindirect.common.audit.AuditorFactory;
import org.nhindirect.common.audit.DefaultAuditContext;

/**
 * @author mweaver
 *
 */
public class DirectRIAuditor implements SoapEdgeAuditor {

    private Auditor auditor = null;

    /**
     * Audits an event to the Direct RI audit logger. If a set of properties are provided, they will be audited as
     * additional contexts, otherwise only the principal, category, and message will be audited.
     *
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.direct.xdr.audit.SoapEdgeAuditor#audit(java.lang.String, java.lang.String,
     *      java.lang.String, gov.hhs.fha.nhinc.direct.xdr.audit.Auditable)
     */
    @Override
    public void audit(String principal, String category, String message, SoapEdgeContext properties) {

        AuditEvent event = new AuditEvent(category, message);

        if (properties == null) {
            getAuditor().audit(principal, event);
        } else {
            getAuditor().audit(principal, event, getContexts(properties));
        }
    }

    /**
     * Creates a set of AuditContext objects from and Auditable object.
     *
     * @param auditable A {@link Auditable} object, must not be null.
     * @return A Collection of @{link AuditContext} objects.
     */
    private static Collection<? extends AuditContext> getContexts(SoapEdgeContext auditable) {
        Collection<AuditContext> contexts = new LinkedList<>();

        if (auditable.getAuditableValues() != null) {
            for (Map.Entry<String, String> entry : auditable.getAuditableValues().entrySet()) {
                AuditContext context = new DefaultAuditContext(entry.getKey(), entry.getValue());
                contexts.add(context);
            }
        }
        return contexts;
    }

    /**
     * @return A Direct RI Auditor from the Direct RI AuditorFactory.
     */
    protected Auditor getAuditor() {
        if (auditor == null) {
            auditor = AuditorFactory.createAuditor();
        }
        return auditor;
    }

}
