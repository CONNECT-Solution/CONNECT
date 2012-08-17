/*
 * Copyright (c) 2011, Conemaugh Valley Memorial Hospital
 *
 * This source is subject to the Conemaugh public license.  Please see the
 * license.txt file for more information.
 *
 * All other rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.commondatalayer.mappers;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.FindDocumentRCMRIN000031UV01RequestType;
import org.hl7.v3.FindDocumentRCMRIN000032UV01ResponseType;
import org.hl7.v3.RCMRIN000031UV01QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.RCMRMT000003UV01QueryByParameter;
import org.hl7.v3.RCMRMT000003UV01PatientId;
/**
 *
 * @author Jason
 */
public class StaticFindDocumentQuery {

    private static Log logger = LogFactory.getLog(StaticFindDocumentQuery.class);

    public static FindDocumentRCMRIN000032UV01ResponseType createFindDocumentResponse(FindDocumentRCMRIN000031UV01RequestType request)
    {
        FindDocumentRCMRIN000032UV01ResponseType response = new FindDocumentRCMRIN000032UV01ResponseType();

        logger.info("Calling Static Find Document With Content Data...");

        // Get Provider OID from request
        String receiverOID = request.getQuery().getId().getRoot();

        // Get Patient ID from the request
        RCMRIN000031UV01QUQIMT021001UV01ControlActProcess query = request.getQuery().getControlActProcess();
        RCMRMT000003UV01QueryByParameter queryByParam = query.getQueryByParameter().getValue();
        List<RCMRMT000003UV01PatientId> patientIdList = queryByParam.getPatientId();
        String reqPatientID = patientIdList.get(0).getValue().getExtension();

        logger.debug("Retrieving Emulated Data File for : Patient ID " + reqPatientID + ",receiverOID: " + receiverOID);
        response = StaticUtil.createFindDocumentResponse(reqPatientID, receiverOID);

        return response;

    }
}
