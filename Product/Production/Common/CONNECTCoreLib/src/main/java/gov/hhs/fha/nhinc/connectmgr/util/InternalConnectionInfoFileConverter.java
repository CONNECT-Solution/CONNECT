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
package gov.hhs.fha.nhinc.connectmgr.util;

import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnInfoService;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfo;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfoState;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfos;
import gov.hhs.fha.nhinc.connectmgr.data.CMInternalConnectionInfosXML;
import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import gov.hhs.fha.nhinc.util.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.uddi.api_v3.AccessPoint;
import org.uddi.api_v3.BindingTemplate;
import org.uddi.api_v3.BindingTemplates;
import org.uddi.api_v3.BusinessDetail;
import org.uddi.api_v3.BusinessEntity;
import org.uddi.api_v3.BusinessService;
import org.uddi.api_v3.BusinessServices;
import org.uddi.api_v3.CategoryBag;
import org.uddi.api_v3.IdentifierBag;
import org.uddi.api_v3.KeyedReference;
import org.uddi.api_v3.Name;

public class InternalConnectionInfoFileConverter {

    private static Map<String, String> states = null;

    static {
        states = new HashMap<String, String>();
        states.put("CA-AB", "Alberta");
        states.put("CA-BC", "British Columbia");
        states.put("CA-MB", "Manitoba");
        states.put("CA-NB", "New Brunswick");
        states.put("CA-NL", "Newfoundland");
        states.put("CA-NS", "Nova Scotia");
        states.put("CA-NU", "Nunavut");
        states.put("CA-ON", "Ontario");
        states.put("CA-PE", "Prince Edward Island");
        states.put("CA-QC", "Quebec");
        states.put("CA-SK", "Saskatchewan");
        states.put("CA-NT", "Northwest Territories");
        states.put("CA-YT", "Yukon Territory");
        states.put("US-AA", "Armed Forces Americas");
        states.put("US-AE", "Armed Forces Europe- Middle East- & Canada");
        states.put("US-AK", "Alaska");
        states.put("US-AL", "Alabama");
        states.put("US-AP", "Armed Forces Pacific");
        states.put("US-AR", "Arkansas");
        states.put("US-AS", "American Samoa");
        states.put("US-AZ", "Arizona");
        states.put("US-CA", "California");
        states.put("US-CO", "Colorado");
        states.put("US-CT", "Connecticut");
        states.put("US-DC", "District of Columbia");
        states.put("US-DE", "Delaware");
        states.put("US-FL", "Florida");
        states.put("US-FM", "Federated States of Micronesia");
        states.put("US-GA", "Georgia");
        states.put("US-GU", "Guam");
        states.put("US-HI", "Hawaii");
        states.put("US-IA", "Iowa");
        states.put("US-ID", "Idaho");
        states.put("US-IL", "Illinois");
        states.put("US-IN", "Indiana");
        states.put("US-KS", "Kansas");
        states.put("US-KY", "Kentucky");
        states.put("US-LA", "Louisiana");
        states.put("US-MA", "Massachusetts");
        states.put("US-MD", "Maryland");
        states.put("US-ME", "Maine");
        states.put("US-MH", "Marshall Islands");
        states.put("US-MI", "Michigan");
        states.put("US-MN", "Minnesota");
        states.put("US-MO", "Missouri");
        states.put("US-MP", "Northern Mariana Islands");
        states.put("US-MS", "Mississippi");
        states.put("US-MT", "Montana");
        states.put("US-NC", "North Carolina");
        states.put("US-ND", "North Dakota");
        states.put("US-NE", "Nebraska");
        states.put("US-NH", "New Hampshire");
        states.put("US-NJ", "New Jersey");
        states.put("US-NM", "New Mexico");
        states.put("US-NV", "Nevada");
        states.put("US-NY", "New York");
        states.put("US-OH", "Ohio");
        states.put("US-OK", "Oklahoma");
        states.put("US-OR", "Oregon");
        states.put("US-PA", "Pennsylvania");
        states.put("US-PR", "Puerto Rico");
        states.put("US-PW", "Palau");
        states.put("US-RI", "Rhode Island");
        states.put("US-SC", "South Carolina");
        states.put("US-SD", "South Dakota");
        states.put("US-TN", "Tennessee");
        states.put("US-TX", "Texas");
        states.put("US-UT", "Utah");
        states.put("US-VA", "Virginia");
        states.put("US-VI", "Virgin Islands");
        states.put("US-VT", "Vermont");
        states.put("US-WA", "Washington");
        states.put("US-WV", "West Virginia");
        states.put("US-WI", "Wisconsin");
        states.put("US-WY", "Wyoming");
    }

    private static String stateCodeToName(String code) {
        return states.get(code);
    }

    private String translateServiceName(String serviceName) {
        HashMap<String, String> translationMap = new HashMap<String, String>();
        translationMap.put("QueryForDocument", "QueryForDocuments");
        translationMap.put("RetrieveDocument", "RetrieveDocuments");
        translationMap.put("notificationproducer", "HIEM");
        translationMap.put("serviceXDR", "DocSubmission");
        translationMap.put("xdrrequest", "DocSubmissionDeferredReq");
        translationMap.put("xdrresponse", "DocSubmissionDeferredResp");
        translationMap.put("nhinadmindist", "AdminDistribution");

        String translatedName = translationMap.get(serviceName);
        if (translatedName == null) {
            return serviceName;
        }

        return translatedName;
    }

    private BusinessEntity convertToBusinessEntity(CMInternalConnectionInfo connectionInfo) {
        String hcid = connectionInfo.getHomeCommunityId();

        BusinessEntity businessEntity = new BusinessEntity();

        Name bEntityName = new Name();
        bEntityName.setValue(connectionInfo.getName());
        bEntityName.setLang("en");

        businessEntity.getName().add(bEntityName);
        businessEntity.setBusinessKey("uddi:localnode:" + hcid);

        BusinessServices businessServices = new BusinessServices();

        for (CMInternalConnInfoService service : connectionInfo.getServices().getService()) {
            String serviceName = translateServiceName(service.getName());

            String endpoint = service.getEndpointURL();

            BusinessService businessService = new BusinessService();
            businessService.setServiceKey("uddi:nhincnode:" + serviceName);

            Name name = new Name();
            name.setValue(serviceName);
            name.setLang("en");

            BindingTemplates bindingTemplates = new BindingTemplates();
            BindingTemplate bindingTemplate = new BindingTemplate();
            bindingTemplate.setBindingKey("uddi:nhincnode:" + serviceName);
            bindingTemplate.setServiceKey("uddi:nhincnode:" + serviceName);

            AccessPoint accessPoint = new AccessPoint();
            accessPoint.setUseType("endPoint");
            accessPoint.setValue(endpoint);

            CategoryBag serviceVersionCategoryBag = new CategoryBag();

            KeyedReference serviceVersionKeyRef = new KeyedReference();
            serviceVersionKeyRef.setTModelKey("uddi:nhin:versionofservice");
            serviceVersionKeyRef.setKeyName("");
            serviceVersionKeyRef.setKeyValue("1.0");

            CategoryBag serviceNameCategoryBag = new CategoryBag();
            KeyedReference serviceNameKeyRef = new KeyedReference();
            serviceNameKeyRef.setTModelKey("uddi:nhin:standard-servicenames");
            serviceNameKeyRef.setKeyName(serviceName);
            serviceNameKeyRef.setKeyValue(serviceName);

            businessService.getName().add(name);
            businessService.setBindingTemplates(bindingTemplates);
            bindingTemplates.getBindingTemplate().add(bindingTemplate);
            bindingTemplate.setAccessPoint(accessPoint);
            bindingTemplate.setCategoryBag(serviceVersionCategoryBag);
            serviceVersionCategoryBag.getKeyedReference().add(serviceVersionKeyRef);
            businessService.setCategoryBag(serviceNameCategoryBag);
            serviceNameCategoryBag.getKeyedReference().add(serviceNameKeyRef);

            businessServices.getBusinessService().add(businessService);
        }

        businessEntity.setBusinessServices(businessServices);

        IdentifierBag identifierBag = new IdentifierBag();
        KeyedReference hcidKeyRef = new KeyedReference();
        hcidKeyRef.setTModelKey("uddi:nhin:nhie:homecommunityid");
        hcidKeyRef.setKeyName("");
        hcidKeyRef.setKeyValue(hcid);

        identifierBag.getKeyedReference().add(hcidKeyRef);

        CategoryBag stateCategoryBag = new CategoryBag();
        if (connectionInfo.getStates() != null) {
            for (CMInternalConnectionInfoState state : connectionInfo.getStates().getState()) {
                KeyedReference stateKeyRef = new KeyedReference();
                stateKeyRef.setTModelKey("uddi:uddi.org:ubr:categorization:iso3166");
                stateKeyRef.setKeyName(stateCodeToName(state.getName()));
                stateKeyRef.setKeyValue(state.getName());

                stateCategoryBag.getKeyedReference().add(stateKeyRef);
            }
        }

        businessEntity.setIdentifierBag(identifierBag);
        businessEntity.setCategoryBag(stateCategoryBag);

        return businessEntity;
    }

    public void convert(File inFile, File outFile) {

        try {
            String inputXmlContent = StringUtil.readTextFile(inFile.getAbsolutePath());
            CMInternalConnectionInfos internalConnectionInfos = CMInternalConnectionInfosXML
                    .deserialize(inputXmlContent);

            BusinessDetail businessDetail = new BusinessDetail();
            for (CMInternalConnectionInfo internalConnectionInfo : internalConnectionInfos.getInternalConnectionInfo()) {
                BusinessEntity bEntity = convertToBusinessEntity(internalConnectionInfo);
                businessDetail.getBusinessEntity().add(bEntity);
            }

            InternalConnectionInfoDAOFileImpl newDAO = InternalConnectionInfoDAOFileImpl.getInstance();
            newDAO.setFileName(outFile.getAbsolutePath());
            newDAO.saveBusinessDetail(businessDetail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To run this from the command line, use:
     * 
     * java -cp "C:\Sun\AppServer\lib\CONNECTCoreLib.jar;C:\Sun\AppServer\lib\*"
     * gov.hhs.fha.nhinc.connectmgr.util.InternalConnectionInfoFileConverter [fromFile] [toFile]
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("\tInternalConnectionInfoFileConverter [fromFile] [toFile]");
            return;
        }
        InternalConnectionInfoFileConverter instance = new InternalConnectionInfoFileConverter();
        String inFileName = args[0];
        String outFileName = args[1];
        System.out.println("Converting from: " + inFileName);
        System.out.println("Converting   to: " + outFileName);
        try {
            instance.convert(new File(inFileName), new File(outFileName));
            System.out.println("Done.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
