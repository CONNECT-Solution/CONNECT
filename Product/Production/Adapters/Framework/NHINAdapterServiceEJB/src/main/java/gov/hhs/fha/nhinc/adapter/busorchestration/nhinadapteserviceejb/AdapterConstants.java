

package gov.hhs.fha.nhinc.adapter.busorchestration.nhinadapteserviceejb;

/**
 * Reference AdapterConstants - Many from ServiceNameConstants
 * 
 * Most these should be consolidated between projects into a common library
 *
 * @author Jerry Goodnough
 */
public class AdapterConstants {
    /**
     * Constant for the Document Assembler Service Name
     */
    public final static String DOCUMENT_ASSEMBLY="documentassembly";
    /**
     * Constant for the Document Manager Service Name
     */
    public final static String DOCUMENT_MANAGER="documentmanager";

    /**
     *  Constant for the Document Class Code
     */
    public final static String XDSDocumentEntryClassCode =
                                "$XDSDocumentEntryClassCode";

    /**
     * Constant for the Clinically Unique Hash metadata element
     */
    public final static String XDSClinicallyUniqueHash = "urn:gov:hhs:fha:nhinc:xds:clinicalUniqueHash";

    /**
     * Constant for the Dynamic Document Usage metadata element
     */
    public final static String XDSHasBeenAccessed = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";

    public static final String REPOSITORY_PROPERTY_FILE = "repository";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";

    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";
    
    public final static String C32_DOCUMENT = "34133-9";
}
