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
