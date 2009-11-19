package gov.hhs.fha.nhinc.policyengine.adapterpip;

import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.BinaryDocumentPolicyCriterionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.PatientPreferencesType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.ActClassClinicalDocument;
import org.hl7.v3.CE;
import org.hl7.v3.II;
import org.hl7.v3.POCDMT000040ClinicalDocument;
import org.hl7.v3.POCDMT000040InfrastructureRootTypeId;
import org.hl7.v3.STExplicit;

/**
 * This class creates CDA documents from BinaryDocumentPolicyCriteria objects.
 *
 * @author Les Westberg
 */
public class CdaPdfCreator
{
    protected Log log = null;

    /**
     * Default constructor.
     */
    public CdaPdfCreator()
    {
        log = createLogger();
    }

    /**
     * Sets up the logger object.
     */
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));

    }

    /**
     * This class creates an instance of an II with the given
     * root and extension.
     *
     * @param sRoot The root value for the II object.
     * @param sExtension The extension for the II object.
     *
     * @return The II object that was constructed.
     */
    private II createII(String sRoot, String sExtension)
    {
        II oII = new II();
        boolean bHaveData = false;

        if (sRoot != null)
        {
            oII.setRoot(sRoot);
            bHaveData = true;
        }

        if (sExtension != null)
        {
            oII.setExtension(sExtension);
            bHaveData = true;
        }

        if (bHaveData)
        {
            return oII;
        }
        else
        {
            return null;
        }
    }

    /**
     * Create the template ID tag.
     *
     * @return The Template ID tag.
     */
    private II createTemplateId()
    {
        return createII(CDAConstants.TEMPLATE_ID_ROOT_XDS_SD_DOCUMENT, null);
    }

    /**
     * Creates the ID tag for the CDA document.
     *
     * @return The ID tag for the CDA document.
     * @throws AdapterPIPException This exception is thrown if any error occurs.
     */
    private II createId(String sDocumentUniqueId)
        throws AdapterPIPException
    {
        String sHomeCommunityId = null;
        try
        {
            sHomeCommunityId = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve home community ID from gateway properties file.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
            throw new AdapterPIPException(sErrorMessage, e);
        }

        return createII(sHomeCommunityId, sDocumentUniqueId);

    }

    /**
     * This creates the type ID tag and returns it.
     *
     * @return The TypeId tag.
     */
    private POCDMT000040InfrastructureRootTypeId createTypeId()
    {
        POCDMT000040InfrastructureRootTypeId oTypeId = new POCDMT000040InfrastructureRootTypeId();

        oTypeId.setExtension(CDAConstants.TYPE_ID_EXTENSION_POCD_HD000040);
        oTypeId.setRoot(CDAConstants.TYPE_ID_ROOT);

        return oTypeId;
    }

    /**
     * Transform the CE into an HL7 CE type.
     *
     * @param oCe The policy representation of the CE.
     * @return The HL7 representation of the CE.
     */
    private CE createCode(CeType oCe)
    {
        CE oHl7Ce = new CE();
        boolean bHaveData = false;

        if (oCe != null)
        {
            if (oCe.getCode() != null)
            {
                oHl7Ce.setCode(oCe.getCode());
                bHaveData = true;
            }

            if (oCe.getDisplayName() != null)
            {
                oHl7Ce.setDisplayName(oCe.getDisplayName());
                bHaveData = true;
            }

            if (oCe.getCodeSystem() != null)
            {
                oHl7Ce.setCodeSystem(oCe.getCodeSystem());
                bHaveData = true;
            }

            if (oCe.getCodeSystemName() != null)
            {
                oHl7Ce.setCodeSystemName(oCe.getCodeSystemName());
                bHaveData = true;
            }

        }   // if (oCE != null)

        if (bHaveData)
        {
            return oHl7Ce;
        }
        else
        {
            return null;
        }

    }

    /**
     * This creates an STExplicit with the given string value.
     *
     * @param sValue The value to use when creating the node.
     * @return The STExplicit object containing the value.
     */
    private STExplicit createST(String sValue)
    {
        STExplicit oHl7St = new STExplicit();
        boolean bHaveData = false;

        if (sValue != null)
        {
            oHl7St.getContent().add(sValue);
            bHaveData = true;
        }

        if (bHaveData)
        {
            return oHl7St;
        }
        else
        {
            return null;
        }
    }



    /**
     * This method creates a single CDA document from the given BinaryDocumentPolicyCriterionType.
     *
     * @param oPtPref This contains the patient preference information. There is some infomration in this
     *                that is common to each of the criterion that need to be available when we create the CDA
     *                document.
     * @param oCriterion The binary document criterion containing the data.
     * @return The CDA document returned.
     * @throws AdapterPIPException This exception is thrown if there are any errors.
     */
    public POCDMT000040ClinicalDocument createCDA(PatientPreferencesType oPtPref, BinaryDocumentPolicyCriterionType oCriterion)
        throws AdapterPIPException
    {
        POCDMT000040ClinicalDocument oCda = new POCDMT000040ClinicalDocument();
        boolean bHaveData = false;

        if (oCriterion != null)
        {
            bHaveData = true;
            
            // Class code and mood code
            //-------------------------
            oCda.setClassCode(ActClassClinicalDocument.DOCCLIN);
            oCda.getMoodCode().add(CDAConstants.CDA_MOOD_CODE);

            // Type ID
            //---------
            oCda.setTypeId(createTypeId());

            // Template ID
            //-------------
            oCda.getTemplateId().add(createTemplateId());

            // ID
            //---
            oCda.setId(createId(oCriterion.getDocumentUniqueId()));

            // Code
            //-----
            oCda.setCode(createCode(oCriterion.getDocumentTypeCode()));

            // Title
            //-------
            oCda.setTitle(createST(oCriterion.getDocumentTitle()));
            

            



        }   // if (oCriterion != null)

        if (bHaveData)
        {
            return oCda;
        }
        else
        {
            return null;
        }

    }

    /**
     * This method creates a set of CDA documents from the given BinaryDocumentPolicyCriterion objects.
     *
     * @param oPtPref The patient preferences information for the CDA.
     * @return The list of CDA documents returned.
     * @throws AdapterPIPException This is thrown if any exception occurs in the process.
     */
    public List<POCDMT000040ClinicalDocument> createCDA(PatientPreferencesType oPtPref)
        throws AdapterPIPException
    {
        ArrayList<POCDMT000040ClinicalDocument> olCda = new ArrayList<POCDMT000040ClinicalDocument>();

        if ((oPtPref != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria() != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion() != null) &&
            (oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion().size() > 0))
        {
            for (BinaryDocumentPolicyCriterionType oCriterion : oPtPref.getBinaryDocumentPolicyCriteria().getBinaryDocumentPolicyCriterion())
            {
                POCDMT000040ClinicalDocument oCda = createCDA(oPtPref, oCriterion);
                if (oCda != null)
                {
                    olCda.add(oCda);
                }
            }
        }


        if (olCda.size() > 0)
        {
            return olCda;
        }
        else
        {
            return null;
        }
    }



}
