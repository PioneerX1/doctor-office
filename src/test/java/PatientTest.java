import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class PatientTest{
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doc_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM patients *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
    }
  }

  @Test
  public void Patient_instantiatesCorrectly_true() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    assertEquals(true, testPatient instanceof Patient);
  }
  @Test
  public void getName_retrievesPatientName_John_Doe() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    assertEquals("John Doe", testPatient.getName());
  }
  @Test
  public void getId_patientInstantiatesWithID_true() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    assertTrue(testPatient.getId() > 0);
  }
  @Test
  public void save_assignsIdToObject() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    Patient savedPatient = Patient.all().get(0);
    assertEquals(testPatient.getId(), savedPatient.getId());
  }
  //two patient objects with same info should still be unique cause of ID
  @Test
  public void equals_returnsTrueIfPatientsAreTheSame_false() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    Patient testPatient2 = new Patient("John Doe", "12/08/1982");
    testPatient2.save();
    assertFalse(testPatient.equals(testPatient2));
  }
  @Test
  public void all_returnsAllInstancesOfPatient_true() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    Patient testPatient2 = new Patient("Jane Doe", "01/05/1979");
    testPatient2.save();
    assertEquals(true, Patient.all().get(0).equals(testPatient));
    assertEquals(true, Patient.all().get(1).equals(testPatient2));
  }
  @Test
  public void find_returnsPatientWithSameId_testPatient2() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    Patient testPatient2 = new Patient("Frank", "02/05/1979");
    testPatient2.save();
    assertEquals(Patient.find(testPatient2.getId()), testPatient2);
  }
  @Test
  public void updateDob_updatesPatientDOB_true() {
    Patient testPatient = new Patient("John Doe", "12/08/1982");
    testPatient.save();
    testPatient.updateDob("11/16/1979");
    assertEquals("11/16/1979", Patient.find(testPatient.getId()).getDob());
  }

}
