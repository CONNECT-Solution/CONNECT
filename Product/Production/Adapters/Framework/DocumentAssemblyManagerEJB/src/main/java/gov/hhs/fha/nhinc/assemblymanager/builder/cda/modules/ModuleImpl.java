/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules;

import gov.hhs.fha.nhinc.assemblymanager.AssemblyConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.EDExplicit;
import org.hl7.v3.II;
import org.hl7.v3.ObjectFactory;

/**
 * 
 * @author kim
 */
public abstract class ModuleImpl implements CDAModule {

    protected CdaTemplate template = null;
    protected ObjectFactory objectFactory = null;
    protected CareRecordQUPCIN043200UV01ResponseType careRecordResponse = null;

    public ModuleImpl(CdaTemplate template) {
        this.template = template;
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
    }

    public ModuleImpl(CdaTemplate template, CareRecordQUPCIN043200UV01ResponseType careRecordResponse) {
        this.template = template;
        this.careRecordResponse = careRecordResponse;
        if (objectFactory == null) {
            objectFactory = new ObjectFactory();
        }
    }

    protected List<II> getConformingTemplateIds() {
        List<II> templateIdList = new ArrayList<II>();

        if (!AssemblyConstants.usePre25Templates()) {
            if (template.getHitspTemplateId() != null && template.getHitspTemplateId().length() > 0) {
                II templateId1 = new II();
                templateId1.setRoot(template.getHitspTemplateId());
                templateId1.setAssigningAuthorityName("HITSP/C83");
                templateIdList.add(templateId1);
            }

            if (template.getIheTemplateId() != null && template.getIheTemplateId().length() > 0) {
                II templateId2 = new II();
                templateId2.setRoot(template.getIheTemplateId());
                templateId2.setAssigningAuthorityName("IHE");
                templateIdList.add(templateId2);
            }
        } else {
            if (template.getHitspTemplateIdPre25() != null && template.getHitspTemplateIdPre25().length() > 0) {
                II templateId1 = new II();
                templateId1.setRoot(template.getHitspTemplateIdPre25());
                templateId1.setAssigningAuthorityName("HITSP/C32");
                templateIdList.add(templateId1);
            }
        }

        if (template.getCdaTemplateId() != null && template.getCdaTemplateId().length() > 0) {
            II templateId3 = new II();
            templateId3.setExtension(template.getCdaTemplateId());
            templateId3.setAssigningAuthorityName("CCD");
            templateIdList.add(templateId3);
        }

        return templateIdList;
    }

    protected String getEDItem(EDExplicit edObj) {
        if (edObj != null) {
            for (int i = 0; i < edObj.getContent().size(); i++) {
                // JAXBElement o = (JAXBElement) edObj.getContent().get(i);
                String o = (String) edObj.getContent().get(i);
                if (o != null) {
                    return o;
                }
            }
        }

        return "";
    }
}
