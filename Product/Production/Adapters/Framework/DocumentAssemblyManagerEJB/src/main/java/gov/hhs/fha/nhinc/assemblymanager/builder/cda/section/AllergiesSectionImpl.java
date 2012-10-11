/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
 * All rights reserved. 
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 * All other rights reserved.
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
package gov.hhs.fha.nhinc.assemblymanager.builder.cda.section;

import gov.hhs.fha.nhinc.assemblymanager.CDAConstants;
import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import gov.hhs.fha.nhinc.assemblymanager.builder.ModuleFactoryBuilder;
import gov.hhs.fha.nhinc.assemblymanager.utils.XMLUtil;
import gov.hhs.fha.nhinc.template.model.CdaTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040Component3;
import org.hl7.v3.POCDMT000040Entry;
import org.hl7.v3.POCDMT000040Section;
import org.hl7.v3.StrucDocContent;
import org.w3c.dom.Element;
import org.hl7.v3.STExplicit;

/**
 * This class performs the necessary mappings from CareRecordQUPCIN043200UV01ResponseType to POCDMT000040Component3.
 * 
 * @author kim
 */
public class AllergiesSectionImpl extends SectionImpl {

    private int reactionIndex = -1;
    private int severityIndex = -1;
    private Map<String, String> reactions = null;
    private Map<String, String> severities = null;

    public AllergiesSectionImpl(CdaTemplate template) {
        super(template);
        reactionIndex = 1;
        severityIndex = 1;
    }

    public Map<String, String> getReactions() {
        if (reactions == null) {
            reactions = new HashMap<String, String>();
        }

        return reactions;
    }

    public int getReactionIndex() {
        if (reactionIndex == -1) {
            reactionIndex = 1;
        }

        return reactionIndex++;
    }

    public int getSeverityIndex() {
        if (severityIndex == -1) {
            severityIndex = 1;
        }

        return severityIndex++;
    }

    public Map<String, String> getSeverities() {
        if (severities == null) {
            severities = new HashMap<String, String>();
        }

        return severities;
    }

    @Override
    public POCDMT000040Component3 build() throws DocumentBuilderException {

        POCDMT000040Section allergySection = new POCDMT000040Section();

        // REQUIRED! Set template ids to identify that this is a medication
        // section
        List<II> templateIdList = getConformingTemplateIds();
        for (II templateId : templateIdList) {
            allergySection.getTemplateId().add(templateId);
        }

        // REQUIRED! Must have this also: <code code="48765-2"
        // codeSystem="2.16.840.1.113883.6.1"/>
        CE loincAllergyCode = new CE();
        loincAllergyCode.setCode(CDAConstants.LOINC_ALLERGY_CODE);
        loincAllergyCode.setCodeSystem(CDAConstants.LOINC_CODE_SYS_OID);
        allergySection.setCode(loincAllergyCode);

        // REQUIRED! Set title for display
        // try {
        // Element title = XMLUtil.createElement(CDAConstants.TITLE_TAG);
        // allergySection.setTitle(title);
        // title.setTextContent(CDAConstants.ALLERGIES_SECTION_TITLE);
        // } catch (Exception e) {
        // log.error("Failed to set POCDMT000040Section.title", e);
        // }
        STExplicit title = new STExplicit();
        title.getContent().add(CDAConstants.ALLERGIES_SECTION_TITLE);
        allergySection.setTitle(title);

        // build the relevant module entries
        log.debug("********  # of ALLERGY MODULES: " + moduleTemplates.size());

        if (moduleTemplates.size() > 0) {
            CdaTemplate moduleTemplate = moduleTemplates.get(0);

            log.debug(moduleTemplate);

            List<POCDMT000040Entry> entries = ModuleFactoryBuilder.createModule(moduleTemplate, careRecordResponse, this);
            if (entries != null) {
                for (POCDMT000040Entry entry : entries) {
                    allergySection.getEntry().add(entry);
                }

                // set reaction + severity references (if any)
                try {
                    if (getReactions().size() > 0 || getSeverities().size() > 0) {
                        Element textElement = XMLUtil.createElement(CDAConstants.TEXT_TAG);
                        StringBuffer contentBuffer = new StringBuffer();
                        Set<String> keys = null;

                        if (getReactions().size() > 0) {
                            // build reaction references, i.e. <content ID='reaction-1'>some reaction</content>
                            keys = getReactions().keySet();
                            StrucDocContent reactionTextContent = null;
                            for (String key : keys) {
                                reactionTextContent = objectFactory.createStrucDocContent();
                                reactionTextContent.setID(key);
                                reactionTextContent.getContent().add(getReactions().get(key));
                                contentBuffer.append(XMLUtil.toCanonicalXML(objectFactory.createStrucDocTextContent(reactionTextContent)));
                            }
                        }

                        if (getSeverities().size() > 0) {
                            // build severity references, i.e. <content ID='severity-1'>Severe</content>
                            keys = getSeverities().keySet();
                            StrucDocContent severityTextContent = null;
                            for (String key : keys) {
                                severityTextContent = objectFactory.createStrucDocContent();
                                severityTextContent.setID(key);
                                severityTextContent.getContent().add(getSeverities().get(key));
                                contentBuffer.append(XMLUtil.toCanonicalXML(objectFactory.createStrucDocTextContent(severityTextContent)));
                            }
                        }

                        if (contentBuffer.toString().length() > 1) {
                            textElement.setTextContent(contentBuffer.toString());
                            allergySection.setText(textElement);
                        }
                    }
                } catch (Exception e) {
                    log.error("Failed to set AllergySection.text", e);
                }

                getSectionComponent().setSection(allergySection);

                return getSectionComponent();
            }
        }
        return null;
    }
}
