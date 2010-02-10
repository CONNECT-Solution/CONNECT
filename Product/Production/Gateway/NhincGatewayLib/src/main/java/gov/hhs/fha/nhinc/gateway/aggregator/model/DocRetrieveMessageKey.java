package gov.hhs.fha.nhinc.gateway.aggregator.model;

import gov.hhs.fha.nhinc.gateway.aggregator.AggregatorException;


/**
 * This represents the fields that make up the message key 
 * that identifies a single document retrieve record in the 
 * aggregator message results table.
 *
 * @author Les Westberg
 */
public class DocRetrieveMessageKey
{
    
    private static final String DOC_RETRIEVE_TAG = "DocRetrieve";
    private static final String DOC_RETRIEVE_TAG_START = "<" + DOC_RETRIEVE_TAG + ">";
    private static final String DOC_RETRIEVE_TAG_END = "</" + DOC_RETRIEVE_TAG + ">";
    private static final String HOME_COMMUNITY_ID_TAG = "HomeCommunityId";
    private static final String HOME_COMMUNITY_ID_TAG_START = "<" + HOME_COMMUNITY_ID_TAG + ">";
    private static final String HOME_COMMUNITY_ID_TAG_END = "</" + HOME_COMMUNITY_ID_TAG + ">";
    private static final String REPOSITORY_ID_TAG = "RepositoryId";
    private static final String REPOSITORY_ID_TAG_START = "<" + REPOSITORY_ID_TAG + ">";
    private static final String REPOSITORY_ID_TAG_END = "</" + REPOSITORY_ID_TAG + ">";
    private static final String DOCUMENT_ID_TAG = "PatientId";
    private static final String DOCUMENT_ID_TAG_START = "<" + DOCUMENT_ID_TAG + ">";
    private static final String DOCUMENT_ID_TAG_END = "</" + DOCUMENT_ID_TAG + ">";
    
    // Private member variables
    //-------------------------
    private String homeCommunityId;
    private String repositoryId;
    private String documentId;

    /**
     * Default constructor.
     */
    public DocRetrieveMessageKey()
    {
        clear();
    }
    
    /**
     * This method takes the information in a formatted message key and
     * creates an object with that information.  This key should be one
     * that was created by this class. (or at least exactly formatted
     * that way).
     * 
     * @param sMessageKey The message key as formatted by calling the 
     *                    creatingXMLMessageKey.
     * @throws AggregatorException This is thrown if the format of the XML message
     *                             is not correct.
     */
    public DocRetrieveMessageKey(String sMessageKey)
        throws AggregatorException
    {
        // Since this is a simple XML that is in a controlled format - 
        // It is easiest to just pull out the fields by hand-parsing...
        //-------------------------------------------------------------
        parseXMLMessageKey(sMessageKey);
    }
    
    /**
     * Clear the ocntents of this object
     */
    public void clear()
    {
        homeCommunityId = "";
        repositoryId = "";
        documentId = "";
    }

    /**
     * Returns the document ID of the document.
     * 
     * @return The document ID of the document.
     */
    public String getDocumentId()
    {
        return documentId;
    }

    /**
     * Sets the document ID of the document.
     * 
     * @param documentId The document ID of the document.
     */
    public void setDocumentId(String documentId)
    {
        this.documentId = documentId;
    }

    /**
     * Returns the Home community Id where the document lis lcoated.
     * 
     * @return the hoome community Id of the location of the document.
     */
    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    /**
     * Sets the Home community Id where the document lis lcoated.
     * 
     * @param homeCommunityId the hoome community Id of the location of the document.
     */
    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Returns the repository ID for the repository that contains the document.
     * 
     * @return The repository ID for the repository that contains the document.
     */
    public String getRepositoryId()
    {
        return repositoryId;
    }

    /**
     * Sets the repository ID for the repository that contains the document.
     * 
     * @param repositoryId The repository ID for the repository that contains the document.
     */
    public void setRepositoryId(String repositoryId)
    {
        this.repositoryId = repositoryId;
    }
    
    /**
     * This method creates the XML Message Key that will be stored in the
     * MessageKey field of the AGGREGATOR.AGG_MESSAGE_RESULTS table.
     * 
     * @return The XML key that is created based on the fields in this object.
     */
    public String createXMLMessageKey()
    {
        String sHomeCommunityId = "";
        String sRepositoryId = "";
        String sDocumentId = "";
        
        if (homeCommunityId != null)
        {
            sHomeCommunityId = homeCommunityId.trim();
        }

        if (repositoryId != null)
        {
            sRepositoryId = repositoryId.trim();
        }
        
        if (documentId != null)
        {
            sDocumentId = documentId.trim();
        }

        String sKey = DOC_RETRIEVE_TAG_START + 
                        HOME_COMMUNITY_ID_TAG_START + sHomeCommunityId + HOME_COMMUNITY_ID_TAG_END +
                        REPOSITORY_ID_TAG_START + sRepositoryId + REPOSITORY_ID_TAG_END +
                        DOCUMENT_ID_TAG_START + sDocumentId + DOCUMENT_ID_TAG_END +
                      DOC_RETRIEVE_TAG_END;
        
        return sKey;
    }
       
    /**
     *  Since this is a simple XML that is in a controlled format - 
     *  It is easiest to just pull out the fields by hand-parsing...
     * 
     * @param sMessageKey The XML string key containing the data.
     * @throws AggregatorException This is thrown if the format of the XML message
     *                             is not correct.
     */
    public void parseXMLMessageKey(String sMessageKey)
        throws AggregatorException
    {
        int iStartIdx = 0;
        int iEndIdx = 0;
        
        // Get the Home community
        //------------------------
        iStartIdx = sMessageKey.indexOf(HOME_COMMUNITY_ID_TAG_START);
        if (iStartIdx >= 0)
        {
            iStartIdx += HOME_COMMUNITY_ID_TAG_START.length();
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }
        
        iEndIdx = sMessageKey.indexOf(HOME_COMMUNITY_ID_TAG_END);
        if (iEndIdx > 0)
        {
            homeCommunityId = sMessageKey.substring(iStartIdx, iEndIdx);
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }
        
        // Get the Repository ID
        //----------------------
        iStartIdx = sMessageKey.indexOf(REPOSITORY_ID_TAG_START);
        if (iStartIdx >= 0)
        {
            iStartIdx += REPOSITORY_ID_TAG_START.length();
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }
        
        iEndIdx = sMessageKey.indexOf(REPOSITORY_ID_TAG_END);
        if (iEndIdx > 0)
        {
            repositoryId = sMessageKey.substring(iStartIdx, iEndIdx);
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }

        // Get the Document Id
        //-------------------
        iStartIdx = sMessageKey.indexOf(DOCUMENT_ID_TAG_START);
        if (iStartIdx >= 0)
        {
            iStartIdx += DOCUMENT_ID_TAG_START.length();
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }
        
        iEndIdx = sMessageKey.indexOf(DOCUMENT_ID_TAG_END);
        if (iEndIdx > 0)
        {
            documentId = sMessageKey.substring(iStartIdx, iEndIdx);
        }
        else
        {
            throw new AggregatorException("Format of DocRetrieveMessageKey was invalid.  MessageKey = '" + sMessageKey + "'");
        }
    }
    
}
