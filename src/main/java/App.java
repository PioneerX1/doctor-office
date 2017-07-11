import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    //populate with demo default data
    //defaultData();

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctor.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/newdoctor", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String specialty = request.queryParams("specialty");
      Doctor newDoctor = new Doctor(name, specialty);
      newDoctor.save();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/newpatient", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctor doctor = Doctor.find(Integer.parseInt(request.queryParams("doctorId")));
      String patientName = request.queryParams("patient-name");
      String dob = request.queryParams("patient-dob");
      Patient newPatient = new Patient(patientName, dob, doctor.getId());
      newPatient.save();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }

  public static void defaultData() {
    Doctor doctor1 = new Doctor("Henry Hammerschmidt", "Gaping Head Wounds");
    doctor1.save();
    Doctor doctor2 = new Doctor("Sally Suffers", "Woman's Problems");
    doctor2.save();
    Patient patient1 = new Patient("Harry Yellowface", "04/30/1900", doctor1.getId());
    patient1.save();
    Patient patient2 = new Patient("Talks Much", "11/20/1889", doctor2.getId());
    patient2.save();
  }
}
