package vttp2023.batch4.paf.assessment.controllers;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import org.bson.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.services.ListingsService;
import vttp2023.batch4.paf.assessment.Utils;

@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BnBController {

	// You may add additional dependency injections

	@Autowired
	private ListingsService listingsSvc;
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/suburbs")
	@ResponseBody
	public ResponseEntity<String> getSuburbs() {
		List<String> suburbs = listingsSvc.getAustralianSuburbs();
		JsonArray result = Json.createArrayBuilder(suburbs).build();
		return ResponseEntity.ok(result.toString());
	}
	
	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/search")
	@ResponseBody
	public ResponseEntity<String> search(@RequestParam MultiValueMap<String, String> params) {

		String suburb = params.getFirst("suburb");
		int persons = Integer.parseInt(params.getFirst("persons"));
		int duration = Integer.parseInt(params.getFirst("duration"));
		float priceRange = Float.parseFloat(params.getFirst("price_range"));

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		listingsSvc.findAccommodatations(suburb, persons, duration, priceRange)
			.stream()
			.forEach(acc -> 
				arrBuilder.add(
					Json.createObjectBuilder()
						.add("id", acc.getId())
						.add("name", acc.getName())
						.add("price", acc.getPrice())
						.add("accommodates", acc.getAccomodates())
						.build()
				)
			);

		return ResponseEntity.ok(arrBuilder.build().toString());
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	@GetMapping("/accommodation/{id}")
	@ResponseBody
	public ResponseEntity<String> getAccommodationById(@PathVariable String id) {

		Optional<Accommodation> opt = listingsSvc.findAccommodatationById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(Utils.toJson(opt.get()).toString());
	}

	// TODO: Task 6
	@PostMapping("/accommodation")
	@ResponseBody
	public ResponseEntity<String> BookAccoms(@RequestBody String payload) {

		JsonReader reader = Json.createReader(new StringReader(payload));

		JsonObject jsonObject = reader.readObject();

		String email = jsonObject.getString("email");
		String name = jsonObject.getString("name");
		String listingId = jsonObject.getString("id");
		Integer duration = jsonObject.getInt("nights");

		Bookings bookings = new Bookings();
		bookings.setEmail(email);
		bookings.setName(name);
		bookings.setListingId(listingId);
		bookings.setDuration(duration);

		Boolean status  = listingsSvc.createBooking(bookings);

		if(status == false)
		{
			JsonObject jsonObjectError = Json.createObjectBuilder().add("message","Fail to book").build();
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(jsonObjectError.toString());

		}

		else{
			JsonObject json = Json.createObjectBuilder().build();
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(json.toString());
		}

		
	}
	
}
