/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.hiemadapter.proxy.notify;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import org.w3c.dom.Element;

/**
 *
 * @author Jon Hoppesch
 */
public interface HiemNotifyAdapterProxy {

    /**
     * Performs a Generic HIEM notify request to an Agency
     *
     * @param request Generic Hiem Notify request.
     * @return an Acknowledgement
     */
    public Element notify(Element notify, ReferenceParametersElements referenceParametersElements,AssertionType assertion, NhinTargetSystemType target) throws Exception;

    /**
     * Performs a Document HIEM notify request to an Agency
     *
     * @param request Document Hiem Notify request.
     * @return an Acknowledgement
     */
    public Element notifySubscribersOfDocument(Element docNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception;

    /**
     * Performs a CDC HIEM notify request to an Agency
     *
     * @param request CDC Hiem Notify request.
     * @return an Acknowledgement
     */
    public Element notifySubscribersOfCdcBioPackage(Element cdcNotify, AssertionType assertion, NhinTargetSystemType target) throws Exception;

}
