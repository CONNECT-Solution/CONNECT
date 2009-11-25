/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universalclientgui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author patlollav
 */
public class DocumentQueryResults
{
    List<DocumentInformation> documents = new ArrayList<DocumentInformation>();

    public List<DocumentInformation> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentInformation> documents) {
        this.documents = documents;
    }
    
}
