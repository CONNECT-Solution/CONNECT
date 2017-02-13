/**
 *
 */
package gov.hhs.fha.nhinc.admingui.services;


import gov.hhs.fha.nhinc.admingui.event.model.PrescriptionInfo;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * @author mpnguyen
 *
 */
public interface HtmlParserService {

    public List<PrescriptionInfo> getAllPrescriptions(String htmlUrl) throws MalformedURLException, IOException;

}