/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.lift.payload.builder;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.lift.utils.LiFTMessageHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author JHOPPESC
 */
public class LiFTPayloadBuilder {

    private static Log log = LogFactory.getLog(LiFTPayloadBuilder.class);

    /**
     * This method will build and insert a LiFT Payload into the request message.  At this time
     * this method will only support the transfer of a single document
     * @param msg  Original Document Submission Request Message
     * @param assertion  Original assertion information associated with this request message
     * @param fileInfo  List of information on the file(s) to be transferred.  At this time only 1 is supported.
     * @return  true if the payload was successfully inserted, otherwise it will return false if an error occurs
     */
    public boolean buildLiFTPayload(ProvideAndRegisterDocumentSetRequestType msg, AssertionType assertion, List<UrlInfoType> urlInfoList) {
        log.debug("Entering LiFTPayloadBuilder.buildLiFTPayload method...");
        boolean result = false;
        ExtrinsicObjectType extObj = null;
        Document liftPayload = new Document();

        // Validate input parameters
        if (msg != null &&
                assertion != null &&
                NullChecker.isNotNullish(urlInfoList)) {

            // Verify that the Documents Section of the Request message is empty, if not empty it.
            if (NullChecker.isNotNullish(msg.getDocument())) {
                log.warn("Document section of the input message was not null, replacing with the LiFT Payload");
                msg.getDocument().clear();
            }

            if (msg.getSubmitObjectsRequest() != null &&
                    msg.getSubmitObjectsRequest().getRegistryObjectList() != null) {
                RegistryObjectListType regObjList = msg.getSubmitObjectsRequest().getRegistryObjectList();

                // Extract the ExtrinsicObjectType from the Registry List
                extObj = LiFTMessageHelper.extractExtrinsicObject(regObjList);

                if (extObj != null) {
                    // Create the Lift slots and add it to the Extrinsic Object
                    SlotType1 transferServiceSlot = createTransferServiceSlot();
                    SlotType1 transferProtocolSlot = createTransferProtocolSlot();

                    extObj.getSlot().add(transferServiceSlot);
                    extObj.getSlot().add(transferProtocolSlot);

                    result = true;
                }
            }
            else {
                log.error("No Registry Object List was provided in the input message");
            }
        } else {
            log.error("One or more of the input paramters were invalid");
        }

        log.debug("Exiting LiFTPayloadBuilder.buildLiFTPayload method...");
        return result;
    }

    private SlotType1 createTransferServiceSlot() {
        SlotType1 transferServiceSlot = new SlotType1();
        ValueListType valueList = new ValueListType();

        valueList.getValue().add(NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_VALUE);

        transferServiceSlot.setName(NhincConstants.LIFT_TRANSPORT_SERVICE_PROTOCOL_SLOT_NAME);

        transferServiceSlot.setValueList(valueList);

        return transferServiceSlot;
    }

    private SlotType1 createTransferProtocolSlot() {
        SlotType1 transferProtocolSlot = new SlotType1();

        ValueListType valueList = new ValueListType();

        valueList.getValue().add(NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_VALUE);

        transferProtocolSlot.setName(NhincConstants.LIFT_TRANSPORT_SERVICE_SLOT_NAME);

        transferProtocolSlot.setValueList(valueList);

        return transferProtocolSlot;
    }
}
