/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.policydteproxy;

/**
 *
 * @author jhoppesc
 */
public class PolicyDteProxyFactory {

    public static IPolicyDteProxy getPolicyDteProxy() {
        return new PolicyDteWebServiceProxy();
    }
}
