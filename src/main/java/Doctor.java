import java.util.ArrayList;
import java.util.List;
import org.sql2o.*;

public class Doctor {
  private String name;
  private String specialty;
  private int id;

  public Doctor(String name, String specialty) {
    this.name = name;
    this.specialty = specialty;
  }
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors(name, specialty) VALUES (:name, :specialty);";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("specialty", this.specialty)
        .executeUpdate()
        .getKey();
    }
  }
  public static List<Doctor> all() {
    String sql = "SELECT id, name, specialty FROM doctors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctor.class);
    }
  }
  public static Doctor find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT* FROM doctors where id=:id";
      Doctor doctor = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Doctor.class);
      return doctor;
    }
  }
  public void updateSpecialty(String specialty) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE doctors SET specialty = :specialty WHERE id=:id";
      con.createQuery(sql)
        .addParameter("specialty", specialty)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public String getName() {
    return name;
  }
  public String getSpecialty() {
    return specialty;
  }
  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctor)) {
      return false;
    } else {
      Doctor newDoctor = (Doctor) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
        this.getSpecialty().equals(newDoctor.getSpecialty())  &&
        this.getId() == newDoctor.getId();
    }
  }
}
