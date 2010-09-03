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

package gov.hhs.fha.nhinc.hiemadapter.proxy.subscribe;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public interface HiemSubscribeAdapterProxy {
    public Element subscribe(Element subscribe, AssertionType assertion, NhinTargetSystemType target) throws Exception;
    //public UnsubscribeResponse unsubscribe(UnsubscribeRequestType request);
}
