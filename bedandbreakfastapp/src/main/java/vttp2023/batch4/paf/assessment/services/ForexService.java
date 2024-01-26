package vttp2023.batch4.paf.assessment.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {
		//https://api.frankfurter.app/latest?from=AUD&to=SGD

		 UriComponents urlCom = UriComponentsBuilder
               .fromUriString("https://api.frankfurter.app/latest")
     		  	.queryParam("from",from )
				.queryParam("to", to)
               .build();

			   String url =  urlCom.toString();
        //  System.out.println(url);
         RequestEntity<Void> req = RequestEntity.get(url).build();
        //  System.out.println(req);
         RestTemplate template = new RestTemplate();
         ResponseEntity<String> resp = null;

        try {;

            resp = template.exchange(req, String.class);
			String payload = resp.getBody();
       
         JsonReader reader = Json.createReader(new StringReader(payload));
         JsonObject result = reader.readObject();
        
         JsonObject rates =result.getJsonObject("rates");
		 Double SGD = rates.getJsonNumber("SGD").doubleValue();

		 Float SGDfloat = SGD.floatValue();

		 System.out.println(SGDfloat.toString());

		 return amount*SGDfloat;

         } catch (Exception ex) {
            ex.printStackTrace();
            return -1000f;
         }

         

	}
}

