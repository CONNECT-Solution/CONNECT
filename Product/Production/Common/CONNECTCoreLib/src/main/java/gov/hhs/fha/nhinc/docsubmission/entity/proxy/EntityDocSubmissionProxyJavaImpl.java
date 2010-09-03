/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docsubmission.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.docsubmission.entity.EntityDocSubmissionOrchImpl;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class EntityDocSubmissionProxyJavaImpl implements EntityDocSubmissionProxy
{

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg, 
            AssertionType assertion, NhinTargetCommunitiesType targets, UrlInfoType urlInfo)
    {
        RegistryResponseType response = new RegistryResponseType();

        EntityDocSubmissionOrchImpl orchImpl = new EntityDocSubmissionOrchImpl();
        response = orchImpl.provideAndRegisterDocumentSetB(msg, assertion, targets, urlInfo);

        return response;
    }
}
