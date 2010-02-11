package gov.hhs.fha.nhinc.adapter.reidentification.data;

import java.util.ArrayList;
import java.util.List;



/**
 * This class represents the root node of the reidentification XML file.
 */
public class PseudonymMaps {
    
    /**
     * This represents a collection of pseudonym maps. By design, a pseudonym 
     * can only map to one real ID, which serves as the key to the object look-up.
     */
     private List<PseudonymMap>pseudonymMaps = new ArrayList<PseudonymMap>();
     
     /**
      * This retrieves a collection of pseudonym maps.
      */
    public List<PseudonymMap> getPseudonymMap(){
        return pseudonymMaps;
    }
    
    /**
     * This sets an collection of pseudonym maps.
     */
    public void setPseudonymMap(List<PseudonymMap> pseudonymMap){
        this.pseudonymMaps = pseudonymMap;
    }
}
