package migration;

import DAO.MongoDAO;
import Model.Country;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToNosqlMigrationService {

	public static List<Document> processCountryList(List<Country> countryList) {
		List<Document> countryDocs = new ArrayList<>();

		countryList.forEach(country -> countryDocs.add(new Document("countryName", country.getName())));

		return countryDocs;
	}

	public static List<Document> processPlaneList(List<PlaneDto> planeDtoList) {
		List<Document> planeDocs = new ArrayList<>();
		return planeDocs;
	}

	public static List<Document> processCompanyList(List<CompanyDto> companyDtoList, Map<Long, String> countryMap) {
		List<Document> companyDocs = new ArrayList<>();
		companyDtoList.forEach(companyDto -> {
			companyDto.companyCountry = countryMap.get(companyDto.countryId);
			companyDocs.add(new Document("companyName", companyDto.companyName).append("companyCountry", new Document("countryName", companyDto.companyCountry)));
		});
		return companyDocs;
	}

	public static List<Document> processPlaneList(List<PlaneDto> planeDtoList, Map<Long, String> companyMap) {
		List<Document> planeDocs = new ArrayList<>();
		planeDtoList.forEach(planeDto -> {
			planeDto.companyName = companyMap.get(planeDto.companyId);
			planeDocs.add(new Document("planeName", planeDto.planeName).append("planeCapacity", planeDto.planeCapacity).append("planeCompany", new Document("companyName", planeDto.companyName)));
		});
		return planeDocs;
	}

	public static List<Document> processTripList(List<TripDto> tripDtoList, Map<Long, String> countryMap, Map<Long, String> companyMap, Map<Long, Long> tripCompanyRelation) {
		List<Document> tripDocs = new ArrayList<>();

		tripDtoList.forEach(tripDto -> {
			tripDto.tripCompanyId = tripCompanyRelation.get(tripDto.tripId);
			tripDto.countryIn = countryMap.get(tripDto.countryInId);
			tripDto.countryOut = countryMap.get(tripDto.countryOutId);
			tripDto.tripCompany = companyMap.get(tripDto.tripCompanyId);
			tripDocs.add(new Document("countryIn", new Document("countryName", tripDto.countryIn))
					.append("countryOut", new Document("countryName", tripDto.countryOut))
					.append("tripMinDuration", tripDto.minDuration)
					.append("tripMinPrice", tripDto.minPrice)
					.append("tripCompany", new Document("companyName", tripDto.tripCompany)));
		});
		return tripDocs;
	}

	public static void migrateCountryData(List<Document> countryDocs, MongoDAO mongoDao) {
		mongoDao.insertCountryDocs(countryDocs);
	}

	public static void migrateCompanyData(List<Document> companyDocs, MongoDAO mongoDAO) {
		mongoDAO.insertCompanyDocs(companyDocs);
	}

	public static void migratePlaneData(List<Document> planeDocs, MongoDAO mongoDAO) {
		mongoDAO.insertPlaneDocs(planeDocs);
	}

	public static void migrateTripData(List<Document> tripDocs, MongoDAO mongoDAO) {
		mongoDAO.insertTripDocs(tripDocs);
	}
}
