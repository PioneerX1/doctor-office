import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Patient {
  private String name;
  private String dob;
  private int id;

  public Patient(String name, String dob) {
    this.name = name;
    this.dob = dob;
  }
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(name, dob) VALUES (:name, :dob);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("dob", this.dob)
        .executeUpdate()
        .getKey();
    }
  }
  public static List<Patient> all() {
    String sql = "SELECT id, name, dob FROM patients";
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

  @Override
  public boolean equals(Object otherPatient) {
    if (!(otherPatient instanceof Patient)) {
      return false;
    } else {
      Patient newPatient = (Patient) otherPatient;
      return this.getName().equals(newPatient.getName()) &&
        this.getDob().equals(newPatient.getDob())  &&
        this.getId() == newPatient.getId();
    }
  }
}
