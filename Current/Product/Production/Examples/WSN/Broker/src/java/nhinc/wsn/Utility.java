/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nhinc.wsn;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ejb.Stateless;

/**
 *
 * @author rayj
 */
@WebService()
@Stateless()
public class Utility {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "GetSubscriptionCount")
    public int GetSubscriptionCount() {
        return Brain.GetSubscriptionListCount();
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "ClearSubscriptionList")
    @Oneway
    public void ClearSubscriptionList() {
        Brain.ClearSubscriptionList();
    }
}
