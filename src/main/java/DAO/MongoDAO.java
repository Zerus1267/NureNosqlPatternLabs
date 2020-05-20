package DAO;

import Model.Country;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDAO implements IMyDAO {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> planeCollection;
	private MongoCollection<Document> tripCollection;
	private MongoCollection<Document> companyCollection;
	private MongoCollection<Document> countryCollection;

	public MongoDAO() {
		mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27001,localhost:27002,localhost:27003/?replicaset=myreplica&socketTimeoutMS=10000"));
		mongoClient.setWriteConcern(WriteConcern.MAJORITY);
		mongoDatabase = mongoClient.getDatabase("airtrans");
		planeCollection = mongoDatabase.getCollection("plane");
		tripCollection = mongoDatabase.getCollection("trip");
		companyCollection = mongoDatabase.getCollection("company");
		countryCollection = mongoDatabase.getCollection("country");
		System.out.println("Plane count in db = " + planeCollection.countDocuments());
	}

	public void testRead() {
		//List<Document> results = new ArrayList<>();
		Document doc = planeCollection.find(eq("plane_modification", "PH-XLK")).first();
		System.out.println(doc.getObjectId("_id"));
	}

	@Override
	public int getTripsByCountry(String country) {
		Bson queryFilter = Filters.all("countryOut.countryName", country);
		List<Document> documents = new ArrayList<>();
		tripCollection.find(queryFilter).iterator().forEachRemaining(documents::add);
		System.out.println("List size " + documents.size());
		return documents.size();
	}

	@Override
	public void addNewPlaneFunc(String name, int capacity, String company) throws Exception {

		//TODO> Add plane and determine the company of the plane, if company doesn't exist. If company exist add the plane into the plane field of company.

		if (!hasPlane(name)) {
			//planeCollection.insertOne(new Document().append("planeName", name).append("planeCapacity", capacity));
			if (hasCompany(company)) {
				addNewPlane(name, capacity);
				companyCollection.updateOne(eq("companyName", company), new Document("$addToSet", new Document("companyPlane", new Document("planeName", name))));
			} else {
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(System.in));
				System.out.println("Enter the company`s country please!");
				String countryName = reader.readLine();
				if (countryName == "") countryName = "TestCountry";
				addNewCompany(company, countryName, name);
				addNewPlane(name, capacity);
			}
		} else {
			System.out.println("This plane already exists!");
		}
	}

	public void addNewPlane(String name, int capacity) {
		Document document = new Document("planeName", name);
		document.append("planeCapacity", capacity);
		planeCollection.insertOne(document);
	}

	private void addNewCompany(String companyName, String countryName, String planeName) {
		if (!hasCountry(countryName)) {
			addNewCountry(countryName);
		}
		companyCollection.insertOne(Document.parse("{companyName:'" + companyName + "', companyCountry:{countryName:'" + countryName + "'}, companyPlane:[{planeName:'" + planeName + "'}]}"));
	}

	public void addNewCountry(String countryName) {
		Document document = new Document();
		document.append("countryName", countryName);
		countryCollection.insertOne(document);
	}

	private boolean hasCountry(String countryName) {
		long count = countryCollection.count(eq("countryName", countryName));
		if (count == 0) return false;
		else return true;
	}

	private boolean hasPlane(String planeName) {
		Document document = planeCollection.find(eq("planeName", planeName)).first();
		if (document != null) return true;
		else return false;
	}

	private boolean hasCompany(String companyName) {
		long count = companyCollection.count(eq("companyName", companyName));
		if (count == 0) return false;
		else return true;
	}

	@Override
	public void deletePlaneByName(String plane_name) throws SQLException {

		try {
			long existingCount = planeCollection.count(eq("planeName", plane_name));
			if (existingCount != 1) {
				throw new Exception("Not appropriate count of founded rows, actual= " + existingCount + " but should be 1");
			} else {
				companyCollection.findOneAndUpdate(Filters.elemMatch("companyPlane", eq("planeName", plane_name)), new Document("$pull", new Document("companyPlane", new Document("planeName", plane_name))));
			}

			System.out.println("Successfully deleted the plane: " + plane_name);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Something get wrog while deleting the plane: " + plane_name);
		}
	}

	public int getPlaneCollectionSize() {
		return (int) planeCollection.countDocuments();
	}

	public int getCompanyCollectionSize() {
		return (int) companyCollection.countDocuments();
	}

	public int getCompanyPlaneCount(String companyName) {
		int res = -1;
		Document projectFields = new Document("_id", 0);
		projectFields.put("count", new BasicDBObject("$size", "$companyPlane"));
		AggregateIterable<Document> document = companyCollection.aggregate(Arrays.asList(Aggregates.match(new Document("companyName", companyName)), Aggregates.project(projectFields)));
		for (Document document1 : document) {
			res = document1.getInteger("count");
		}
		return res;
	}

	public MongoCollection<Document> getPlaneCollection() {
		return planeCollection;
	}

	public MongoCollection<Document> getTripCollection() {
		return tripCollection;
	}

	public MongoCollection<Document> getCompanyCollection() {
		return companyCollection;
	}

	public MongoCollection<Document> getCountryCollection() {
		return countryCollection;
	}


	@Override
	public void updateCompanyPlaneByNames(String companyName, String planeName) throws SQLException {

	}

	@Override
	public void addNewTrip(String countryIn, String countryOut, int minDuration, int minPrice, String companyName) throws SQLException, IOException {

	}

	public List<Country> getCountryList() {
		List<Country> countryList = new ArrayList<>();
		MongoCursor<Document> cursor = countryCollection.find().iterator();
		while (cursor.hasNext()) {
			countryList.add(new Country(cursor.next().getString("countryName")));
		}
		return countryList;
	}

	public List<PlaneDto> getPlaneList() {
		List<PlaneDto> planeList = new ArrayList<>();
		MongoCursor<Document> cursor = planeCollection.find().iterator();
		Document document = null;
		while (cursor.hasNext()) {
			document = cursor.next();
			planeList.add(new PlaneDto(document.getString("planeName"), document.getInteger("planeCapacity"), document.get("planeCompany", Document.class).getString("companyName")));
		}
		return planeList;
	}

	public List<TripDto> getTripList() {
		List<TripDto> tripList = new ArrayList<>();
		MongoCursor<Document> cursor = tripCollection.find().iterator();
		Document document = null;
		while (cursor.hasNext()) {
			document = cursor.next();
			tripList.add(new TripDto(
					document.get("countryIn", Document.class).getString("countryName"),
					document.get("countryOut", Document.class).getString("countryName"),
					document.getInteger("tripMinDuration"),
					document.getInteger("tripMinPrice"),
					document.get("tripCompany", Document.class).getString("companyName")));
		}
		return tripList;
	}

	public List<CompanyDto> getCompanyList() {
		List<CompanyDto> companyList = new ArrayList<>();
		MongoCursor<Document> cursor = companyCollection.find().iterator();
		Document document = null;
		while (cursor.hasNext()) {
			document = cursor.next();
			companyList.add(new CompanyDto(document.getString("companyName"), document.get("companyCountry", Document.class).getString("countryName")));
		}
		return companyList;
	}

	public void insertCountryDocs(List<Document> countryDocs) {

		countryCollection.insertMany(countryDocs);

	}

	public void insertCompanyDocs(List<Document> companyDocs) {

		companyCollection.insertMany(companyDocs);
	}

	public void insertPlaneDocs(List<Document> planeDocs) {
		planeCollection.insertMany(planeDocs);
	}

	public void insertTripDocs(List<Document> tripDocs) {
		tripCollection.insertMany(tripDocs);
	}
}
