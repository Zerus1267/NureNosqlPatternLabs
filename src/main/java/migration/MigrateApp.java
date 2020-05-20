package migration;

import DAO.DAO;
import DAO.MongoDAO;
import Model.Company;
import Model.Country;
import Model.Plane;
import Model.Trip;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static migration.ToNosqlMigrationService.migrateCompanyData;
import static migration.ToNosqlMigrationService.migrateCountryData;
import static migration.ToNosqlMigrationService.migratePlaneData;
import static migration.ToNosqlMigrationService.migrateTripData;
import static migration.ToNosqlMigrationService.processCompanyList;
import static migration.ToNosqlMigrationService.processCountryList;
import static migration.ToNosqlMigrationService.processPlaneList;
import static migration.ToNosqlMigrationService.processTripList;
import static migration.ToSqlMigrationService.getCompanyMap;
import static migration.ToSqlMigrationService.getCountryMap;
import static migration.ToSqlMigrationService.migrateCompanyData;
import static migration.ToSqlMigrationService.migrateCompanyTripData;
import static migration.ToSqlMigrationService.migrateCountryData;
import static migration.ToSqlMigrationService.migratePlaneData;
import static migration.ToSqlMigrationService.migrateTripData;

public class MigrateApp {

	public static void main(String[] args) {
		MongoDAO mongoDAO = new MongoDAO();
		DAO dao = new DAO();
		mongoDAO.getCountryList().forEach(country -> System.out.println(country.getName()));
		migrateNoSQLtoMySQL(mongoDAO);
		migrateMySQLtoNoSQL(dao, mongoDAO);
	}

	private static void migrateMySQLtoNoSQL(DAO dao, MongoDAO mongoDAO) {
		List<Country> countryList = dao.getCountryList();
		List<PlaneDto> planeDtoList = dao.getPlaneList();
		List<CompanyDto> companyDtoList = dao.getCompanyList();
		List<TripDto> tripDtoList = dao.getTripList();
		Map<Long, Long> tripCompanyRelation = dao.getTripCompanyRelation();
		migrateCountryData(processCountryList(countryList), mongoDAO);
		Map<Long, String> countryReversedMap = getCountryReversedMap(countryList);
		migrateCompanyData(processCompanyList(companyDtoList, countryReversedMap), mongoDAO);
		Map<Long, String> companyReversedMap = getCompanyReversedMap(companyDtoList);
		migratePlaneData(processPlaneList(planeDtoList, companyReversedMap), mongoDAO);

		migrateTripData(processTripList(tripDtoList, countryReversedMap, companyReversedMap, tripCompanyRelation), mongoDAO);

		System.out.println(countryList + " " + planeDtoList + " " + companyDtoList + " " + tripDtoList);
	}

	private static Map<Long, String> getCompanyReversedMap(List<CompanyDto> companyDtoList) {
		Map<Long, String> companyMap = new HashMap<>();
		companyDtoList.forEach(companyDto -> companyMap.put(companyDto.id, companyDto.companyName));
		return companyMap;
	}

	private static Map<Long, String> getCountryReversedMap(List<Country> countryList) {
		Map<Long, String> countryMap = new HashMap<>();
		countryList.forEach(country -> countryMap.put(country.getId(), country.getName()));
		return countryMap;
	}

	private static void migrateNoSQLtoMySQL(MongoDAO mongoDAO) {
		DAO dao = new DAO();
		List<Country> countryList = mongoDAO.getCountryList();
		migrateCountryData(countryList, dao);

		List<CompanyDto> companyDtoList = mongoDAO.getCompanyList();
		List<Company> companies = new ArrayList<>();
		companyDtoList.forEach(companyDto -> companies.add(new Company(companyDto.companyName)));
		Map<String, Long> countryMap = getCountryMap(countryList);
		migrateCompanyData(companyDtoList, countryMap, companies, dao);

		List<PlaneDto> planeDtoList = mongoDAO.getPlaneList();
		List<Plane> planeList = new ArrayList<>();
		planeDtoList.forEach(planeDto -> planeList.add(new Plane(planeDto.planeName, planeDto.planeCapacity, -1L)));
		Map<String, Long> companyMap = getCompanyMap(companies);
		migratePlaneData(planeDtoList, companyMap, planeList, dao);

		List<TripDto> tripDtos = mongoDAO.getTripList();
		List<Trip> tripList = new ArrayList<>();
		tripDtos.forEach(dto -> tripList.add(new Trip(dto.minDuration, dto.minPrice)));
		migrateTripData(tripDtos, tripList, dao, countryMap);

		migrateCompanyTripData(tripList, tripDtos, companyMap, dao);
	}
}
