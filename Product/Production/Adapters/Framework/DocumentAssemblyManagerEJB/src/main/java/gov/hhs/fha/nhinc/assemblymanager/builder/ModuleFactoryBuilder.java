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
package gov.hhs.fha.nhinc.assemblymanager.builder;

import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.CDAModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.MedicationModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.AllergiesModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.modules.ProblemsModule;
import gov.hhs.fha.nhinc.assemblymanager.builder.cda.section.SectionImpl;
import gov.hhs.fha.nhinc.template.TemplateConstants;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.POCDMT000040Entry;

/**
 * 
 * @author kim
 */
public class ModuleFactoryBuilder {

    private static Log log = LogFactory.getLog(ModuleFactoryBuilder.class);

    /**
     * Returns a list of entries representing the requested clinical domain in the template.
     * 
     * @param template template information
     * @param careRecord domain clinical information
     * @return
     */
    public final static List<POCDMT000040Entry> createModule(CdaTemplate template,
            CareRecordQUPCIN043200UV01ResponseType careRecord, SectionImpl section) {
        String hitspTemplateId = template.getHitspTemplateId();
        CDAModule moduleBuilder = null;

        try {
            // medication module
            if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.MEDICATION_MODULE_HITSP_TEMPLATE_ID)) {
                moduleBuilder = new MedicationModule(template, careRecord);
            } // condition module
            else if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.CONDITION_MODULE_HITSP_TEMPLATE_ID)) {
                moduleBuilder = new ProblemsModule(template, careRecord, section);
            }
            // allergy module
            else if (hitspTemplateId.equalsIgnoreCase(TemplateConstants.ALLERGY_MODULE_HITSP_TEMPLATE_ID)) {
                moduleBuilder = new AllergiesModule(template, careRecord, section);
            } else {
                log.error("Template: \"" + hitspTemplateId + "\" - Not implemented yet.");
                return new ArrayList<POCDMT000040Entry>();
            }

            return moduleBuilder.build();
        } catch (DocumentBuilderException ex) {
            log.error("Failed to build template module \"" + hitspTemplateId + "\".", ex);
            ex.printStackTrace();
            return new ArrayList<POCDMT000040Entry>();
        }
    }
}
