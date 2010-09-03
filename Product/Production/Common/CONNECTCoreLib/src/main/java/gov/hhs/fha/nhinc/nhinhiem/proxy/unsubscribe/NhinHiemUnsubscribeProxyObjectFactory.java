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

package gov.hhs.fha.nhinc.nhinhiem.proxy.unsubscribe;

/**
 *
 * @author rayj
 */
public class NhinHiemUnsubscribeProxyObjectFactory {
    public NhinHiemUnsubscribeProxy getNhinHiemSubscribeProxy() {
        NhinHiemUnsubscribeProxy nhinHiemSubscribe =  new NhinHiemUnsubscribeWebServiceProxy();
        return nhinHiemSubscribe;
    }

}
