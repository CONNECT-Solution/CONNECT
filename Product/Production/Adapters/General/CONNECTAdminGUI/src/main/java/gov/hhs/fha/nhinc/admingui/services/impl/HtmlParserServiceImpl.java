/**
 *
 */
package gov.hhs.fha.nhinc.admingui.services.impl;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.hhs.fha.nhinc.admingui.services.HtmlParserService;
import gov.hhs.fha.nhinc.admingui.event.model.PrescriptionInfo;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.stereotype.Service;

/**
 * @author mpnguyen
 *
 */
@Service
public class HtmlParserServiceImpl implements HtmlParserService {

    private static final Logger logger = LoggerFactory.getLogger(HtmlParserServiceImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see gov.hhs.fha.nhinc.pmp.services.ParserService#getAllPrescriptions(java.lang.String)
     */
    @Override
    public List<PrescriptionInfo> getAllPrescriptions(String htmlUrl) throws MalformedURLException, IOException {

        Document htmlDocument = Jsoup.parse(new URL(htmlUrl), 10000);
        return populatePrescriptionList(htmlDocument);
    }

    private List<PrescriptionInfo> populatePrescriptionList(Document htmlDocument) throws NumberFormatException {
        Element prescriptionTable = htmlDocument.getElementById("prescriptions-table");
        Elements prescriptionRows = prescriptionTable.select("tbody > tr");
        List<PrescriptionInfo> prescriptionList = new ArrayList<>();
        for (Element row : prescriptionRows) {

            prescriptionList.add(populatePrescriptionInfo(row));
        }
        return prescriptionList;
    }

    private PrescriptionInfo populatePrescriptionInfo(Element row) throws NumberFormatException {
        PrescriptionInfo prescriptionInfo = new PrescriptionInfo();
        // retrieve TD tag Cell as lists
        Elements tDCells = row.select("td");
        prescriptionInfo.setFileStrDate(tDCells.get(0).ownText());
        prescriptionInfo.setDrugName(tDCells.get(2).ownText());
        prescriptionInfo.setDrugCount(Integer.parseInt(tDCells.get(3).ownText()));
        prescriptionInfo.setDrugDuration(Integer.parseInt(tDCells.get(4).ownText()));
        prescriptionInfo.setPrescriber(tDCells.get(5).ownText());
        prescriptionInfo.setPharmacyName(tDCells.get(6).ownText());
        //prescriptionInfo.setRefill(tDCells.get(7).ownText());
        //prescriptionInfo.setMgEq(Double.parseDouble(tDCells.get(8).ownText()));
        //prescriptionInfo.setMgEdPerDay(tDCells.get(9).ownText());
        //prescriptionInfo.setPaymentType(tDCells.get(10).ownText());
        prescriptionInfo.setPmpState(tDCells.get(11).ownText());
        return prescriptionInfo;
    }

}
