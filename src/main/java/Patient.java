import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Patient {
  private String name;
  private String dob;
  private int id;
  private int doctorId;

  public Patient(String name, String dob, int doctorId) {
    this.name = name;
    this.dob = dob;
    this.doctorId = doctorId;
  }
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(name, dob, doctorId) VALUES (:name, :dob, :doctorId);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("dob", this.dob)
        .addParameter("doctorId", this.doctorId)
        .executeUpdate()
        .getKey();
    }
  }
  public static List<Patient> all() {
    String sql = "SELECT id, name, dob, doctorId FROM patients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patient.class);
    }
  }
  public static Patient find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT* FROM patients where id=:id";
      Patient patient = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patient.class);
      return patient;
    }
  }
  public void updateDob(String dob) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patients SET dob = :dob WHERE id=:id";
      con.createQuery(sql)
        .addParameter("dob", dob)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public String getName() {
    return name;
  }
  public String getDob() {
    return dob;
  }
  public int getId() {
    return id;
  }
  public int getDoctorId() {
    return doctorId;
  }

  @Override
  public boolean equals(Object otherPatient) {
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
        this.getDob().equals(newPatient.getDob())  &&
        this.getDoctorId() == newPatient.getDoctorId() &&
        this.getId() == newPatient.getId();
    }
  }
}
