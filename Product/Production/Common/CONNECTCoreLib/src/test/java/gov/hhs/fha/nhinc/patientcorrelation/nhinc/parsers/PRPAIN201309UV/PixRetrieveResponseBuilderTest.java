/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class PixRetrieveResponseBuilderTest {
    @Test
    public void createPixRetrieveResponse() {
        PRPAIN201309UV02 patCorrReq = new PRPAIN201309UV02();
        patCorrReq.setControlActProcess(createControlActProcess());
        PixRetrieveResponseBuilder response = new PixRetrieveResponseBuilder();
        PRPAIN201310UV02 message = new PRPAIN201310UV02();
        message = response.createPixRetrieveResponse(patCorrReq, createIIList());
        assertEquals(message.getControlActProcess().getQueryByParameter().getValue().getQueryId().getAssigningAuthorityName()
                , "CONNECT");
        assertEquals(message.getControlActProcess().getQueryAck().getQueryId().getExtension(), "1.16.17.19");
        assertEquals(message.getInteractionId().getExtension(), "PRPA_IN201310");
        assertEquals(message.getProcessingCode().getCode(), "P");
        assertEquals(message.getAcceptAckCode().getCode(), "AL");
        assertEquals(message.getProcessingModeCode().getCode(), "T");
        assertEquals(message.getITSVersion(),"XML_1.0");
    }

    private List<II> createIIList() {
        List<II> IIList = new ArrayList<>();
        II ii1 = new II();
        ii1.setAssigningAuthorityName("1.1");
        ii1.setExtension("1.16.17.18.19");
        ii1.setRoot("1.1");
        IIList.add(ii1);
        return IIList;
    }

    private PRPAIN201309UV02QUQIMT021001UV01ControlActProcess createControlActProcess() {
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess =
                new  PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setQueryByParameter(createQueryByParameter());
        return controlActProcess;

    }

    private JAXBElement<PRPAMT201307UV02QueryByParameter> createQueryByParameter() {
        PRPAMT201307UV02QueryByParameter parameter = new  PRPAMT201307UV02QueryByParameter();
        parameter.setQueryId(createII());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        return new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
    }

    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }


}
