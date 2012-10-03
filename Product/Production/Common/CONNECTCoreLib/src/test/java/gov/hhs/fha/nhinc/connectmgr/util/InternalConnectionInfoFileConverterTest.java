/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.connectmgr.util;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;

import java.io.File;

import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import org.uddi.api_v3.BusinessDetail;

import static org.junit.Assert.*;

public class InternalConnectionInfoFileConverterTest {

    @Test
    public void testConvert() throws URISyntaxException {

        String oldConnectionFilename = "/config/InternalConnectionInfoFileConverterTest/oldInternalConnectionInfo.xml";

        URL url = this.getClass().getResource(oldConnectionFilename);

        File oldConnectionFile = new File(url.toURI());

        String newConnectionFilename = System.getProperty("java.io.tmpdir") + File.separator
                + "newInternalConnection.xml";

        System.out.println(newConnectionFilename);

        File newConnectionFile = new File(newConnectionFilename);

        try {

            // Convert File

            InternalConnectionInfoFileConverter converter = new InternalConnectionInfoFileConverter();

            converter.convert(oldConnectionFile, newConnectionFile);

            // Load File

            InternalConnectionInfoDAOFileImpl newDAO = InternalConnectionInfoDAOFileImpl.getInstance();

            newDAO.setFileName(newConnectionFile.getAbsolutePath());

            BusinessDetail businessDetail = newDAO.loadBusinessDetail();

            assertEquals(1, businessDetail.getBusinessEntity().size());

            assertEquals(2, businessDetail.getBusinessEntity().get(0).getBusinessServices().getBusinessService().size());

            assertEquals("QueryForDocuments", businessDetail.getBusinessEntity().get(0).getBusinessServices()
                    .getBusinessService().get(0).getName().get(0).getValue());

        } catch (Exception e) {

            e.printStackTrace();

            fail("Error running testConvert test: " + e.getMessage());

        } finally {

            if (newConnectionFile.exists()) {

                newConnectionFile.delete();

            }

        }

    }

}
