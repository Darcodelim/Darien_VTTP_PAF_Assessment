package vttp2023.batch4.paf.assessment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2023.batch4.paf.assessment.services.ForexService;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner{
	@Autowired
	ForexService forexServ;


	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		// forexServ.convert("AUD", "SGD", 1);

		// Boolean boo = bookingSvc.booking("barney@gmail.com");
		// System.out.println(boo);

		// JsonObject json = Json.createObjectBuilder().build();

		// System.out.println(json.toString());
	}
}
