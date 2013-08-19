/**
 * 
 */
package gov.hhs.fha.nhinc.patientcorrelation.nhinc.parsers.PRPAIN201309UV;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201310UV02;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
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
        List<II> IIList = new ArrayList<II>();
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
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = new JAXBElement<PRPAMT201307UV02QueryByParameter>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        return queryByParameter;
    }
    
    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }
        

}
