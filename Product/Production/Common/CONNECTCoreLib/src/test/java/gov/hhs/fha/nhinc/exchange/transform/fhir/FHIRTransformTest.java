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
package gov.hhs.fha.nhinc.exchange.transform.fhir;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import gov.hhs.fha.nhinc.exchange.OrganizationListType;
import gov.hhs.fha.nhinc.exchange.directory.EndpointType;
import gov.hhs.fha.nhinc.exchange.directory.OrganizationType;
import gov.hhs.fha.nhinc.exchange.transform.ExchangeTransformException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.hl7.fhir.dstu3.formats.IParser;
import org.hl7.fhir.dstu3.formats.JsonParser;
import org.hl7.fhir.dstu3.formats.XmlParser;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tjafri
 */
public class FHIRTransformTest {

    private static final String JSON_FILE = "/config/FHIRTransformTest/fhirJsonResponse.json";
    private static final String XML_FILE = "/config/FHIRTransformTest/fhirXmlResponse.xml";
    private static final String XML = "xml";
    private static final String JSON = "json";
    private static final String ORG_TYPE = "Participant";
    private static final Logger LOG = LoggerFactory.getLogger(FHIRTransformTest.class);

    @Test
    public void testTransformXml() {
        try {
            Bundle bundle = readTestData(XML_FILE, XML);
            FHIRTransform transformer = new FHIRTransform();
            assertTransformedObject(transformer.transform(bundle));
        } catch (UnsupportedEncodingException | ExchangeTransformException | FHIRFormatError ex) {
            LOG.error("Unable to read test data: {}", ex.getLocalizedMessage(), ex);
        }
    }

    @Test
    public void testTransformJson() {
        try {
            Bundle bundle = readTestData(JSON_FILE, JSON);
            FHIRTransform transformer = new FHIRTransform();
            assertTransformedObject(transformer.transform(bundle));
        } catch (UnsupportedEncodingException | ExchangeTransformException | FHIRFormatError ex) {
            fail("testTransformJson failed with exception: " + ex.getLocalizedMessage());
        }
    }

    @Test
    public void testHasFHIRPropertiesInFile() throws PropertyAccessException {
        ArrayList<String> missing = new ArrayList<String>();

        NhincConstants.NHIN_SERVICE_NAMES[] nhinServices = NhincConstants.NHIN_SERVICE_NAMES.values();
        for (NhincConstants.NHIN_SERVICE_NAMES nhinService : nhinServices) {
            String serviceNames = PropertyAccessor.getInstance().getProperty(NhincConstants.FHIR_DIRECTORY_FILE,
                nhinService.getUDDIServiceName());
            if (serviceNames == null) {
                LOG.error("Service {} does not have any FHIR property defined", nhinService.getUDDIServiceName());
                missing.add(nhinService.name() + ": " + nhinService.getUDDIServiceName());
            }
        }

        if (CollectionUtils.isNotEmpty(missing)) {
            // If you are reading this, remember that the test has its own copy of the .properties file.
            // If you added the property in the Properties project and this test is still failing, be sure to update
            // this test's copy as well.

            fail("The following services defined in NhincConstants are missing property values in "
                + NhincConstants.FHIR_DIRECTORY_FILE + ".properties: " + missing.toString());
        }
    }

    private void assertTransformedObject(OrganizationListType orgListType) {
        List<OrganizationType> orglist = orgListType.getOrganization();
        assertNotNull("OrganizationListType is null", orgListType);
        assertTrue("Size of transformed Organizations must be 2", orglist.size() == 2);
        assertEquals("HCID for organization do not match", "urn:oid:2.16.840.1.113883.3.596", orglist.get(0).getHcid());
        assertEquals("Organization type do not match", ORG_TYPE, orglist.get(0).getType());
        assertTrue("Size of transformed Endpoints for Organization TestOrg1 must be 3",
            orglist.get(0).getEndpointList().getEndpoint().size() == 3);
        for (OrganizationType org : orglist) {
            assertEndpoint(org.getEndpointList().getEndpoint());
        }
    }

    private void assertEndpoint(List<EndpointType> list) {
        for (EndpointType ep : list) {
            assertNotNull("Endpoint name is null", ep.getName().get(0));
            assertNotNull("No Endpoint Configuration read ", ep.getEndpointConfigurationList());
            assertNotNull("Endpoint payload format is null", ep.getPayloadFormat());
            assertNotNull("Endpoint payload type is null", ep.getPayloadType());
        }
    }

    private Bundle readTestData(String filename, String fileType) throws FHIRFormatError, UnsupportedEncodingException {
        URL url = FHIRTransformTest.class.getClass().getResource(filename);
        LOG.info("url for the test data file {}", url);
        IParser parser = null;
        if (fileType.equals(XML)) {
            parser = new XmlParser();
        } else {
            parser = new JsonParser();
        }
        LOG.info("FIR Structures parser initialized");
        String decodedFileName = URLDecoder.decode(url.getFile(), "UTF-8");
        LOG.info("readTestData() decoded file name {}", decodedFileName);
        try (InputStream inStream = new FileInputStream(decodedFileName)) {
            return (Bundle) parser.parse(inStream);
        } catch (IOException | FHIRFormatError ex) {
            LOG.error("Unable to read test data: {}", ex.getLocalizedMessage(), ex);
        }
        return null;
    }
}
