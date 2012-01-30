package gov.hhs.fha.nhinc.connectmgr.util;

import gov.hhs.fha.nhinc.connectmgr.persistance.dao.InternalConnectionInfoDAOFileImpl;
import java.io.File;
import java.net.URL;
import org.junit.Test;
import org.uddi.api_v3.BusinessDetail;
import static org.junit.Assert.*;

public class InternalConnectionInfoFileConverterTest {

    @Test
    public void testConvert() {

        String oldConnectionFilename = "/config/InternalConnectionInfoFileConverterTest/oldInternalConnectionInfo.xml";
        URL url = this.getClass().getResource(oldConnectionFilename);
        File oldConnectionFile = new File(url.getFile());

        String newConnectionFilename = System.getProperty("java.io.tmpdir") + File.separator + "newInternalConnection.xml";
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
            assertEquals("QueryForDocuments", businessDetail.getBusinessEntity().get(0).getBusinessServices().getBusinessService().get(0).getName().get(0).getValue());

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
