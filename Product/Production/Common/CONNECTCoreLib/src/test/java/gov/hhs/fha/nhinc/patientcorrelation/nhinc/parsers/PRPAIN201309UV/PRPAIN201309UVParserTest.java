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

import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201307UV02DataSource;
import org.hl7.v3.PRPAMT201307UV02ParameterList;
import org.hl7.v3.PRPAMT201307UV02PatientIdentifier;
import org.hl7.v3.PRPAMT201307UV02QueryByParameter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * @author achidambaram
 *
 */
public class PRPAIN201309UVParserTest {

    @Test
    public void testParseHL7ParameterListFrom201309MessageWhenMessageNull() {
        PRPAIN201309UV02 message = null;
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7ParameterListFrom201309Message(message));
    }

    @Test
    public void testParseHL7ParameterListFrom201309MessageWhenControlActProcessNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = null;
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7ParameterListFrom201309Message(message));
    }

    @Test
    public void testParseHL7ParameterListFrom201309MessageWhenQueryByParameterNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = null;
        controlActProcess.setTypeId(createII());
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7ParameterListFrom201309Message(message));
    }

    @Test
    public void testParseHL7ParameterListFrom201309MessageWhenParameterListNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter parameter = new  PRPAMT201307UV02QueryByParameter();
        PRPAMT201307UV02ParameterList parameterList = null;
        parameter.setTypeId(createII());
        parameter.setParameterList(parameterList);
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter =
                new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        controlActProcess.setQueryByParameter(queryByParameter);
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7ParameterListFrom201309Message(message));
    }

    @Test
    public void testParseHL7ParameterListFrom201309MessageWhenQueryByParameterValueNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter parameter = null;
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter =
                new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        controlActProcess.setQueryByParameter(queryByParameter);
        controlActProcess.setTypeId(createII());
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7ParameterListFrom201309Message(message));
    }

    @Test
    public void testParseHL7ParameterListFrom201309Message() {
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNotNull(parser.parseHL7ParameterListFrom201309Message(createMessage()));
    }

    @Test
    public void testParseHL7PatientPersonFrom201309Message() {
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        PRPAMT201307UV02PatientIdentifier patientIdentifier = parser.parseHL7PatientPersonFrom201309Message(createMessage());
        assertEquals(patientIdentifier.getTypeId().getAssigningAuthorityName(), "CONNECT");
    }

    @Test
    public void testParseHL7PatientPersonFrom201309MessageWhenPatientIdentifierNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter parameter = new PRPAMT201307UV02QueryByParameter();
        PRPAMT201307UV02PatientIdentifier patIdentifier = null;
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        parameter.setParameterList(parameterList);
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter =
                new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        controlActProcess.setQueryByParameter(queryByParameter);
        controlActProcess.setTypeId(createII());
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        assertNull(parser.parseHL7PatientPersonFrom201309Message(message));
    }

    @Test
    public void testBuildAAInclusionFilterList() {
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        List<II> list;
        list = parser.buildAssigningAuthorityInclusionFilterList(createMessage());
        assertEquals(list.get(0).getAssigningAuthorityName(), "CONNECT");
        assertEquals(list.get(0).getExtension(), "1.16.17.19");
    }

    @Test
    public void testBuildAAInclusionFilterListWhenParameterListNull() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        PRPAMT201307UV02QueryByParameter parameter = new  PRPAMT201307UV02QueryByParameter();
        PRPAMT201307UV02ParameterList parameterList = null;
        parameter.setTypeId(createII());
        parameter.setParameterList(parameterList);
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter =
                new JAXBElement<>(xmlqname,
                PRPAMT201307UV02QueryByParameter.class, parameter);
        controlActProcess.setQueryByParameter(queryByParameter);
        message.setControlActProcess(controlActProcess);
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        List<II> list;
        list = parser.buildAssigningAuthorityInclusionFilterList(message);
        assertTrue(list.isEmpty());
    }


    @Test
    public void testExtractQueryId() {
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        JAXBElement<PRPAMT201307UV02QueryByParameter> queryByParameter = parser.extractQueryId(createMessage());
        assertEquals(queryByParameter.getValue().getQueryId().getAssigningAuthorityName(),"CONNECT");
        assertEquals(queryByParameter.getValue().getQueryId().getExtension(),"1.16.17.19");
        assertEquals(queryByParameter.getValue().getQueryId().getRoot(), "1.1");
    }

    @Test
    public void testExtractQueryIdWhenQueryByParameterNull() {
        PRPAIN201309UVParser parser = new PRPAIN201309UVParser();
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess = null;
        message.setControlActProcess(controlActProcess);
        assertNull(parser.extractQueryId(message));
    }

    private PRPAIN201309UV02 createMessage() {
        PRPAIN201309UV02 message = new PRPAIN201309UV02();
        message.setControlActProcess(createControlActProcess());
        return message;
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
        parameter.setParameterList(createPRPAMT201307UV02ParameterList());
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "parameter");
        return new JAXBElement<>(xmlqname,
        PRPAMT201307UV02QueryByParameter.class, parameter);
    }


    private PRPAMT201307UV02ParameterList createPRPAMT201307UV02ParameterList() {
        PRPAMT201307UV02ParameterList parameterList = new PRPAMT201307UV02ParameterList();
        parameterList.getPatientIdentifier().add(createPRPAMT201307UV02PatientIdentifier());
        parameterList.getDataSource().add(createPRPAMT201307UV02DataSource());
        return parameterList;
    }

    private PRPAMT201307UV02DataSource createPRPAMT201307UV02DataSource() {
        PRPAMT201307UV02DataSource datasource = new PRPAMT201307UV02DataSource();
        datasource.setTypeId(createII());
        datasource.getValue().add(createII());
        return datasource;
    }

    private PRPAMT201307UV02PatientIdentifier createPRPAMT201307UV02PatientIdentifier() {
        PRPAMT201307UV02PatientIdentifier patIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patIdentifier.setTypeId(createII());
        return patIdentifier;
    }


    private II createII() {
        II ii = new II();
        ii.setAssigningAuthorityName("CONNECT");
        ii.setExtension("1.16.17.19");
        ii.setRoot("1.1");
        return ii;
    }
}
