/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.marshallers;

import org.w3c.dom.Element;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

/**
 *
 * @author rayj
 */
public class CheckPolicyRequestMarshaller {
    private static final String CheckPolicyRequestContextPath = "gov.hhs.fha.nhinc.common.nhinccommonadapter";

    public Element marshalCheckPolicyRequest(CheckPolicyRequestType object) {
        return new Marshaller().marshal(object, CheckPolicyRequestContextPath);
    }

    public CheckPolicyRequestType unmarshalCheckPolicyRequest(Element element) {
        return (CheckPolicyRequestType) new Marshaller().unmarshal(element, CheckPolicyRequestContextPath);
    }
}
