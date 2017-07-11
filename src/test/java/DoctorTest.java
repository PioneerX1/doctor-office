import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DoctorTest{
  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doc_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM patients *;";
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
    }
  }

  @Test
  public void Doctor_instantiatesCorrectly_true() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    assertEquals(true, testDoctor instanceof Doctor);
  }
  @Test
  public void getName_retrievesDoctorName_John_Doe() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    assertEquals("John Doe", testDoctor.getName());
  }
  @Test
  public void getId_doctorInstantiatesWithID_true() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    assertTrue(testDoctor.getId() > 0);
  }
  @Test
  public void save_assignsIdToObject() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    Doctor savedDoctor = Doctor.all().get(0);
    assertEquals(testDoctor.getId(), savedDoctor.getId());
  }
  //two doctor objects with same info should still be unique cause of ID
  @Test
  public void equals_returnsTrueIfDoctorsAreTheSame_false() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    Doctor testDoctor2 = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor2.save();
    assertFalse(testDoctor.equals(testDoctor2));
  }
  @Test
  public void all_returnsAllInstancesOfDoctor_true() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    Doctor testDoctor2 = new Doctor("Jane Doe", "Blood Letting");
    testDoctor2.save();
    assertEquals(true, Doctor.all().get(0).equals(testDoctor));
    assertEquals(true, Doctor.all().get(1).equals(testDoctor2));
  }
  @Test
  public void find_returnsDoctorWithSameId_testDoctor2() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    Doctor testDoctor2 = new Doctor("Frank", "Blood Letting");
    testDoctor2.save();
    assertEquals(Doctor.find(testDoctor2.getId()), testDoctor2);
  }
  @Test
  public void updateSpecialty_updatesDoctorSpecialty_true() {
    Doctor testDoctor = new Doctor("John Doe", "Gaping Head Wound");
    testDoctor.save();
    testDoctor.updateSpecialty("Blood Letting");
    assertEquals("Blood Letting", Doctor.find(testDoctor.getId()).getSpecialty());
  }

}
