/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Admin
 */
public class MpiLibTest {

    public MpiLibTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getZeroPatients() {
        System.out.println("getZeroPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        int expResult = 0;
        Patients patientlist = mpi.getPatients();
        int result = patientlist.size();
    }

    @Test
    public void getOnePatient() {
        System.out.println("getOnePatient");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void getTwoPatients() {
        System.out.println("getTwoPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("BUSTER");
        patient.getName().setFirstName("BIGS");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void searchNoData() {
        System.out.println("searchNoData");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("EASTER");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void searchWithDataNoHit() {
        System.out.println("searchWithDataNoHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("XYZ");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void searchSingleHit() {
        System.out.println("searchSingleHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("ADAMS");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());
    }

    @Test
    public void searchMulitHit() {
        System.out.println("searchMulitHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("EASTER");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(2, searchresults.size());
    }
    
    @Test
    public void searchMixCaseNoHit() {
        System.out.println("searchMixCaseNoHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("xYz");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }
    @Test
    public void searchMixCaseSingleHit()
    {
        System.out.println("searchMixCaseSingleHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("adAms");
        searchparams.getName().setFirstName("adAm");
        
        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());        
    }
    @Test
    public void searchMixCaseMulitHit() {
        System.out.println("searchMixCaseMulitHit");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getName().setLastName("eAstEr");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(2, searchresults.size());
    }
    @Test
    public void addOnePatient() {
        System.out.println("addOnePatient");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient = new Patient();
        //patient.getName().setLastName("ADAMS");
        //patient.getName().setFirstName("ADAM");
        patient.getName().setLastName("VALLURIPALLI");
        patient.getName().setFirstName("SAIBABU");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void addTwoPatients() {
        System.out.println("addTwoPatients");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("JOHN");
        patient.getName().setFirstName("SMITH");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void addSamePatientTwice() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
    }

    @Test
    public void addSamePatientTwiceWithDemographicsUpdate() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.setOptedIn(true);
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.setOptedIn(true);
        patient.getName().setLastName("MARRIED");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());

        Patient searchCriteria = new Patient();
        searchCriteria.getIdentifiers().add("id1", "A");
        Patients searchResults = mpi.Search(searchCriteria);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
    }

    @Test
    public void addSamePatientTwiceWithDemographicsGenderUpdate() {
        System.out.println("addSamePatientTwice");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        patient.setGender("M");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("SINGLE");
        patient.getName().setFirstName("LADY");
        patient.getIdentifiers().add("id1", "A");
        patient.setGender("F");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());

        Patient searchCriteria = new Patient();
        searchCriteria.getIdentifiers().add("id1", "A");
        Patients searchResults = mpi.Search(searchCriteria);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
        assertEquals(patient.getGender(), searchResult.getGender());
    }

    @Test
    public void AddTwoPatientsWithSameLastName() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("BOB");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(2, mpi.getPatients().size());
    }

    @Test
    public void AddPatientWithGender() {
        System.out.println("AddPatientWithGender");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.setGender("M");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
        
        Patients searchResults = mpi.Search(patient);
        assertEquals(1, searchResults.size());
        Patient searchResult = searchResults.get(0);
        assertEquals(patient.getName().getFirstName(), searchResult.getName().getFirstName());
        assertEquals(patient.getName().getLastName(), searchResult.getName().getLastName());
        assertEquals(patient.getGender(), searchResult.getGender());
    }

    @Test
    public void PersistIdentifiers() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;
        Patient patient2;
         
        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(1, mpi.getPatients().size());
        patient2 = mpi.getPatients().get(0);

        assertEquals(patient2.getIdentifiers().get(0).getId(), patient.getIdentifiers().get(0).getId());
        assertEquals(patient2.getIdentifiers().get(0).getOrganizationId(), patient.getIdentifiers().get(0).getOrganizationId());

        assertEquals(patient2.getIdentifiers().get(1).getId(), patient.getIdentifiers().get(1).getId());
        assertEquals(patient2.getIdentifiers().get(1).getOrganizationId(), patient.getIdentifiers().get(1).getOrganizationId());
    }

    @Test
    public void SearchByIdOneResult() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();
        searchparams.getIdentifiers().add("MRN-200", "GeneralHospital-OID");

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(1, searchresults.size());

        Patient patient = searchresults.get(0);
        assertEquals("EASTER", patient.getName().getLastName());
        assertEquals("EGG", patient.getName().getFirstName());
    }

    @Test
    public void AddPatientWithMultipleIds() {
        System.out.println("AddTwoPatientsWithSameLastName");
        MiniMpi mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(mpi.getPatients().size(), 1);

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        assertEquals(mpi.getPatients().size(), 1);

        Patient searchParams = new Patient();
        searchParams.getName().setLastName("ADAMS");
        Patients searchResults = mpi.Search(searchParams);
        assertEquals(searchResults.size(), 1);
        patient = searchResults.get(0);
        assertEquals(patient.getIdentifiers().size(), 2);
    }

    @Test
    public void searchWithNoCriteria() {
        System.out.println("searchWithNoCriteria");
        MiniMpi mpi = loadStockMpi();

        Patient searchparams = new Patient();

        Patients searchresults = mpi.Search(searchparams);

        assertNotNull(searchresults);
        assertEquals(0, searchresults.size());
    }

    @Test
    public void PersistPatientList() {
        System.out.println("PersistPatientList");
        MiniMpi mpi;

        mpi = loadStockMpi();
        int PatientListSize = mpi.getPatients().size();

        mpi = MiniMpi.GetMpiInstance("testMPI.xml");

        assertEquals(PatientListSize, mpi.getPatients().size());
    }
//    @Test
//    public void LoadCustomFile()
//    {
//        MiniMpi mpi = MiniMpi.GetMpiInstance("customMPI.xml");
//        assertEquals(mpi.getPatients().size(),6);
//    }
    
    @Test
    public void SaveCustomFile()
    {
       System.out.println("SaveCustomFile");
        MiniMpi mpi = MiniMpi.GetMpiInstance("sampleCustomMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        patient.getIdentifiers().add("id1", "A");
        patient.getIdentifiers().add("id2", "B");
        patient.setOptedIn(true);
        mpi.AddUpdate(patient);

        MiniMpi mpi2 = MiniMpi.GetMpiInstance("sampleCustomMPI.xml");
        
        assertEquals(mpi.getPatients().size(), mpi2.getPatients().size());
        Patient patient2 = mpi2.getPatients().get(0);

        assertEquals(patient.getIdentifiers().get(0).getId(), patient2.getIdentifiers().get(0).getId());
        assertEquals(patient.getIdentifiers().get(0).getOrganizationId(), patient2.getIdentifiers().get(0).getOrganizationId());

        assertEquals(patient.getIdentifiers().get(1).getId(), patient2.getIdentifiers().get(1).getId());
        assertEquals(patient.getIdentifiers().get(1).getOrganizationId(), patient2.getIdentifiers().get(1).getOrganizationId());

        java.io.File f = new java.io.File("sampleCustomMPI.xml");
        f.delete();
        
    }
    private Patient loadPatient(String lName, String fName, String mName, 
            String sex, String dob, String ssn, String add1, String add2, 
            String city, String state, String zip)
    {
        Patient patient = new Patient();
        PersonName name = new PersonName();
        Address addy = new Address();
        
        name.setFirstName(fName);
        name.setLastName(lName);
        name.setMiddleName(mName);
        patient.setName(name);
        
        patient.setDateOfBirth(dob);
        patient.setSSN(ssn);
        patient.setGender(sex);
        
        addy.setCity(city);
        addy.setState(state);
        addy.setZip(zip);
        
        
        patient.setAddress(addy);
        
        return patient;
    }
    private MiniMpi loadStandardMPI()
    {
        MiniMpi mpi;
        mpi = MiniMpi.GetMpiInstance();
        mpi.Reset();

        Patient p;
        
        try
        {
                   
            p = loadPatient("BRADFORD", "BILL", "HUGH", "M", "1/20/1975", "000-00-0001", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-123", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "KATY			", "POLK		", "F", "7/30/1974", "000-00-0002", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-124", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "JILL			", "PAULINE	", "F", "5/24/1999", "000-00-0003", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            p.getIdentifiers().add("DoD-125", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("BRADFORD", "TOMMY		", "CARL		", "M", "10/21/2003", "000-00-0004", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            mpi.AddUpdate(p); 
            p.getIdentifiers().add("DoD-126", "DoD");
            p=loadPatient("BRADFORD", "CARL			", "FRANKLIN", "M", "9/4/1941", "000-00-0005", "437 TURNER LANE			", "", "LEESBURG", "VA", "20176");
            mpi.AddUpdate(p); 
            p.getIdentifiers().add("DoD-127", "DoD");
            p=loadPatient("POLK", "ELIZABETH", "ROBERTS	", "F", "11/5/1947", "000-00-0006", "14 E WINDSOR DRIVE	", "", "BURKE		", "VA", "22015");
            p.getIdentifiers().add("CDC-123", "CDC");
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "RICHARD	", "GEORGE	", "M", "4/16/1946", "000-00-0007", "14 E WINDSOR DRIVE	", "", "BURKE		", "VA", "22015");
            p.getIdentifiers().add("CDC-124", "CDC");
            p.getIdentifiers().add("DoD-128", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "JASON		", "KENNETH	", "M", "5/16/1971", "000-00-0008", "60 LINCOLN AVE			", "	APT #3			", "FAIRFAX				", "VA", "22032");
            p.getIdentifiers().add("CDC-125", "CDC");
            p.getIdentifiers().add("DoD-129", "DoD");           
            mpi.AddUpdate(p); 
            p=loadPatient("POLK", "WALTER		", "MICHAEL	", "M", "12/5/1978", "000-00-0009", "PO BOX 1446					", "", "GERMANTOWN		", "MD", "20876");
            p.getIdentifiers().add("KAD-124", "KAD");
            p.getIdentifiers().add("CDC-126", "CDC");
            mpi.AddUpdate(p); 
            p=loadPatient("MACBETH ", "FRANK		", "EDWARD	", "M", "6/21/1963", "000-00-0010", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-125", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("JONES", "ROMEO		", "TYLER		", "M", "10/10/1992", "000-00-0011", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-126", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("SMITH", "JULIET		", "AMANDA	", "F", "4/23/1993", "000-00-0012", "997 STRATFORD STREET", "UNIT 4G", "SILVER SPRING	", "MD", "20906");
            p.getIdentifiers().add("KAD-127", "KAD");
            mpi.AddUpdate(p); 
            p=loadPatient("HARKNESS", "JACK			", "WINSTON	", "M", "9/3/1949", "000-00-0013", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-123", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("COOPER", "GWEN", "ELIZABETH","F", "8/16/1978", "000-00-0014", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-124", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("HARPER", "OWEN", "STEPHEN	", "M", "7/1/1968", "000-00-0015", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-125", "INOVA");
            mpi.AddUpdate(p); 
            p=loadPatient("SATO", "TOSHIK", "", "F", "6/14/1973", "000-00-0016", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            p.getIdentifiers().add("INOVA-126", "INOVA");
            p.getIdentifiers().add("CDC-127", "CDC");
            p.getIdentifiers().add("DoD-123", "DoD");
            mpi.AddUpdate(p); 
            p=loadPatient("JONES", "IANTO", "WALDO		", "M", "8/19/1983", "000-00-0017", "TORCHWOOD ESTATES", "1000 CARDIFF ROAD	", "GREAT FALLS		", "VA", "22066");
            mpi.AddUpdate(p); 
            p=loadPatient("MARIANO", "JULIA", "BRIDGET	", "F", "1/13/1998", "000-00-0018", "49604 MAIN STREET", "", "ELLICOTT CITY	", "MD", "21042");
            mpi.AddUpdate(p); 
            p=loadPatient("ELSTON", "MICHAEL", "BUZZ		", "M", "11/21/1928", "000-00-0019", "23 JFK DRIVE", "", "WASHINGTON		", "DC", "20004");
            p.getIdentifiers().add("GH-123", "GH");
            mpi.AddUpdate(p); 
            p=loadPatient("DANGERS", "JACK", "", "M", "12/11/1965", "000-00-0020", "99 PARADISE STREET NW", "", "WASHINGTON		", "DC", "20037");
            p.getIdentifiers().add("GH-124", "GH");
            mpi.AddUpdate(p);         
        }
        catch (Exception ex)
        {
            
        }

      
        
        return mpi;
    }
    private MiniMpi loadStockMpi() {
        MiniMpi mpi;
        mpi = MiniMpi.GetMpiInstance("testMPI.xml");
        mpi.Reset();

        Patient patient;

        patient = new Patient();
        patient.getName().setLastName("ADAMS");
        patient.getName().setFirstName("ADAM");
        System.out.println("loading identifier");
        patient.getIdentifiers().add("MRN-123", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("BUSTER");
        patient.getName().setFirstName("BIGS");
        patient.getIdentifiers().add("MRN-124", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("CHARLIE");
        patient.getName().setFirstName("CHAPLAN");
        patient.getIdentifiers().add("MRN-134", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("DONNIE");
        patient.getName().setFirstName("DARKO");
        patient.getIdentifiers().add("MRN-135", "GeneralHospital-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("EASTER");
        patient.getName().setFirstName("EGG");
        patient.getIdentifiers().add("MRN-200", "GeneralHospital-OID");
        patient.getIdentifiers().add("e123", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);

        patient = new Patient();
        patient.getName().setLastName("EASTER");
        patient.getName().setFirstName("BUNNY");
        patient.getIdentifiers().add("b7", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);
        
        patient = new Patient();
        patient.getName().setLastName("CHARLE");
        patient.getName().setFirstName("BROWN");
        patient.setOptedIn(false);
        patient.getIdentifiers().add("MRN-224", "Healthcare-r-us-OID");
        mpi.AddUpdate(patient);

        return mpi;
    }
    
}
