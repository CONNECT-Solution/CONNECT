/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.routing.RoutingObjectFactory;
import gov.hhs.fha.nhinc.docsubmission.adapter.component.routing.XDRRouting;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class AdapterComponentDocSubmissionOrchImpl {
    private static Log log = null;
    public AdapterComponentDocSubmissionOrchImpl()
    {
        log = createLogger();
    }

    public RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion) {
        log.debug("Begin provideAndRegisterDocumentSetb()");
        XDRHelper helper = new XDRHelper();
        RegistryErrorList errorList = helper.validateDocumentMetaData(msg);

        RegistryResponseType result = null;

        if(errorList.getHighestSeverity().equals(helper.XDS_ERROR_SEVERITY_ERROR))
        {
            result = helper.createErrorResponse(errorList);
        }
        else
        {
            log.info(" Request contained " + msg.getDocument().size() + " documents.");
            log.info(" Request Id: " + msg.getSubmitObjectsRequest().getId());

            List<String> recips = helper.getIntendedRecepients(msg);

            if(recips != null)
            {
                List<String>  xdrBeans = helper.getRoutingBeans(recips);
                RoutingObjectFactory factory = new RoutingObjectFactory();

                for(String bean : xdrBeans)
                {
                    log.debug("Bean name = " + bean);
                    XDRRouting proxy = factory.getNhinXDRRouting(bean);
                    result = proxy.provideAndRegisterDocumentSetB(msg, assertion);
                }
            }
            else
            {
                log.debug("No beans to forward the message to");
                result = helper.createPositiveAck();
            }


        }
        return result;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    private String getRoutingBeanName(List<String> recips)
    {
        String result = "";

        return RoutingObjectFactory.BEAN_REFERENCE_IMPLEMENTATION;
    }
    private XDRRouting getRoutingBean(String beanName)
    {
        XDRRouting result = null;

        result = new RoutingObjectFactory().getNhinXDRRouting(beanName);

        return result;
    }

}
