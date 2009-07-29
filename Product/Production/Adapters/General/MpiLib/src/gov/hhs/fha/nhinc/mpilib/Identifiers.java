/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class Identifiers extends ArrayList<Identifier> implements java.io.Serializable {
    private static Log log = LogFactory.getLog(Identifiers.class);
    static final long serialVersionUID = -917875998116976597L;
    
    public Identifiers() {
        log.info("Identifiers Initiated..");
    }

    public boolean add(Identifiers identifiers) {
        for (Identifier identifier : identifiers) {
            add(identifier);
        }
        return true;
    }

    @Override
    public boolean add(Identifier identifier) {
        //check to see if this id already exists
        Identifier myIdentifier = null;

        if (!doesIdentifierExist(identifier)  ) {
            myIdentifier = new Identifier(identifier.getId(), identifier.getOrganizationId());
            super.add(myIdentifier);
        }
        return true;
    }

    private boolean doesIdentifierExist(Identifier identifier) {
        boolean found = false;
        for (Identifier existingId : this) {
            if ((existingId.getOrganizationId().contentEquals(identifier.getOrganizationId()) && (existingId.getId().contentEquals(identifier.getId()))))  {
                found = true;
            }
        }
        return found;
    }

    public boolean add(String id, String organization) {
        return add(new Identifier(id, organization));
    }
}
