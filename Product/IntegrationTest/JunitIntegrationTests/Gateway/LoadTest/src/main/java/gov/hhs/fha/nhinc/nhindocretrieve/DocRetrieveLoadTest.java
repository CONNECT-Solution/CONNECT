package gov.hhs.fha.nhinc.nhindocretrieve;

/**
 *
 * @author Neil Webb
 */
public class DocRetrieveLoadTest
{

    public String runTest(String url, String homeCommunityId, String repositoryId, String[] documentIds)
    {
        String lastDocRetrieved = "Not Started";

        DocRetrieveClient docRetrieve = new DocRetrieveClient();

        for(String docId : documentIds)
        {
            String status = docRetrieve.retrieveDocument(url, homeCommunityId, repositoryId, docId);
            if("Success".equals(status))
            {
                lastDocRetrieved = docId;
            }
            else
            {
                System.out.println("Failure: " + status);
                break;
            }
        }

        return lastDocRetrieved;
    }

    public static void main(String[] args)
    {
        try
        {
            String url = "https://localhost:8181/CONNECTGateway/NhinService/RespondingGateway_Retrieve_Service/DocRetrieve";
            String homeCommunityId = "2.2";
            String repositoryId = "1";
            String[] documentIds = {"5MBFile", "10MBFile", "11MBFile", "12MBFile", "13MBFile", "14MBFile", "15MBFile", "16MBFile", "17MBFile", "18MBFile", "19MBFile", "20MBFile", "30MBFile", "40MBFile", "50MBFile"};
            DocRetrieveLoadTest loadTest = new DocRetrieveLoadTest();
            String lastDocId = loadTest.runTest(url, homeCommunityId, repositoryId, documentIds);
            System.out.println("Last document retrieved: " + lastDocId);
        }
        catch(Throwable t)
        {
            System.out.println("Exception: " + t.getMessage());
            t.printStackTrace();
        }
    }
}
