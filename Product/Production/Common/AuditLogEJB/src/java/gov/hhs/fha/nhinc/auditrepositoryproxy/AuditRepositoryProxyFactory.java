/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditrepositoryproxy;

/**
 *
 * @author jhoppesc
 */
public class AuditRepositoryProxyFactory {

    public static IAuditRepositoryProxy getAuditRepositoryProxy() {
        return new AuditRepositoryWebServiceProxy();
    }
}
