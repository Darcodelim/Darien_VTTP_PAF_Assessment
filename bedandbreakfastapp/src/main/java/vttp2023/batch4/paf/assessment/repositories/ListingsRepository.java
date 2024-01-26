package vttp2023.batch4.paf.assessment.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.Utils;
import vttp2023.batch4.paf.assessment.models.Accommodation;
import vttp2023.batch4.paf.assessment.models.AccommodationSummary;

@Repository
public class ListingsRepository {
	
	// You may add additional dependency injections

	@Autowired
	private MongoTemplate template;

	/*
	 *db.listings.aggregate([

{
    $match: { 
        "address.country":{
        $regex:"australia",$options:"i"
            }      
        }
    },

 
    {
        $group:
        {
            _id:"$address.suburb"
        }
    },
    
    {
        $project:{_id:1}
    }

]);
	 */
	public List<String> getSuburbs(String country) {
		MatchOperation matchCountry = Aggregation.match(Criteria.where("address.country").regex(country,"i"));
		GroupOperation groupOperation = Aggregation.group("address.suburb");
		 ProjectionOperation displaySuburb = Aggregation.project("_id");
		 Aggregation pipeline = Aggregation.newAggregation(matchCountry,groupOperation,displaySuburb);

		AggregationResults<Document> results = template.aggregate(pipeline, "listings",Document.class);

        List<Document> resultDoc = results.getMappedResults();

		List<String> suburbList = new ArrayList<>();
		
		for(Document d : resultDoc)
		{
			String suburb = d.getString("_id");
			if(suburb.isEmpty())
			{continue;}
			suburbList.add(suburb);

		}

		return suburbList;
	}

	/*
db.listings.aggregate([

{
    $match: { 
        "address.suburb":{
        $regex:"north sydney",$options:"i"
            } ,price:{$lte:1000}
            , accommodates:{$gte:1},min_nights:{$lte:10}
        }
    },{
            $sort:{"price":-1}
        
    },

    
    {
        $project:{_id:1,name:1,price:1,accommodates:1}
    }

]);
	 */
	public List<AccommodationSummary> findListings(String suburb, int persons, int duration, float priceRange) {
		MatchOperation matchCountry = Aggregation.match(Criteria.where("address.suburb").regex(suburb,"i")
		.and("price").lte(priceRange)
		.and("accommodates").gte(persons)
		.and("min_nights").lte(duration));

		SortOperation sortOperation= Aggregation.sort(Sort.by(Direction.DESC, "price"));
		
		 ProjectionOperation displayListing = Aggregation.project("name","price","accommodates","_id");
		 Aggregation pipeline = Aggregation.newAggregation(matchCountry,displayListing,sortOperation);

		AggregationResults<Document> results = template.aggregate(pipeline, "listings",Document.class);

        List<Document> resultDoc = results.getMappedResults();

		List<AccommodationSummary> listingSummary = new ArrayList<>();
		
		for(Document d : resultDoc)
		{
			String id = d.getString("_id");
			String name = d.getString("name");
			Integer accommodates = d.getInteger("accommodates");
			float price = d.get("price",Number.class).floatValue();
			AccommodationSummary accomSumm = new AccommodationSummary();
			accomSumm.setId(id);
			accomSumm.setName(name);
			accomSumm.setPrice(price);
			accomSumm.setAccomodates(accommodates);
			
			listingSummary.add(accomSumm);
		}

		return listingSummary;
	}

	// IMPORTANT: DO NOT MODIFY THIS METHOD UNLESS REQUESTED TO DO SO
	// If this method is changed, any assessment task relying on this method will
	// not be marked
	public Optional<Accommodation> findAccommodatationById(String id) {
		Criteria criteria = Criteria.where("_id").is(id);
		Query query = Query.query(criteria);

		List<Document> result = template.find(query, Document.class, "listings");
		if (result.size() <= 0)
			return Optional.empty();

		return Optional.of(Utils.toAccommodation(result.getFirst()));
	}

}
