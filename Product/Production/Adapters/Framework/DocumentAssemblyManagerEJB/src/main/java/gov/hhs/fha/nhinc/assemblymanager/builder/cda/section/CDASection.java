/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.assemblymanager.builder.cda.section;

import gov.hhs.fha.nhinc.assemblymanager.builder.DocumentBuilderException;
import org.hl7.v3.CareRecordQUPCIN043200UV01ResponseType;
import org.hl7.v3.POCDMT000040Component3;

/**
 *
 * @author kim
 */
public interface CDASection {

   public POCDMT000040Component3 build() throws DocumentBuilderException;
   public void setCareRecordResponse(CareRecordQUPCIN043200UV01ResponseType careRecordResponse);
}
