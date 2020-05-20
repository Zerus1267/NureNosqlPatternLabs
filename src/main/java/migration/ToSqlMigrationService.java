package migration;

import DAO.DAO;
import DAO.IMyDAO;
import Model.Company;
import Model.Country;
import Model.Plane;
import Model.Trip;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToSqlMigrationService {

	public static void migrateCompanyTripData(List<Trip> tripList, List<TripDto> tripDtos, Map<String, Long> companyMap, DAO dao) {
		Iterator<TripDto> dtoIterator = tripDtos.iterator();
		Iterator<Trip> tripIterator = tripList.iterator();
		Map<Long, Long> companyTripMap = new HashMap<>();
		while (dtoIterator.hasNext()) {
			companyTripMap.put(tripIterator.next().getId(), companyMap.get(dtoIterator.next().tripCompany));
		}
		try {
			dao.insertCompanyTripMap(companyTripMap);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void migrateTripData(List<TripDto> tripDtos, List<Trip> tripList, DAO dao, Map<String, Long> countryMap) {

		Iterator<TripDto> dtoIterator = tripDtos.iterator();
		Iterator<Trip> tripIterator = tripList.iterator();

		while (dtoIterator.hasNext()) {
			Trip currentTrip = tripIterator.next();
			TripDto currentDto = dtoIterator.next();
			currentTrip.setC_in(countryMap.get(currentDto.countryIn));
			currentTrip.setC_out(countryMap.get(currentDto.countryOut));
		}

		try {
			dao.insertTripList(tripList);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

	}


	public static void migratePlaneData(List<PlaneDto> planeDtoList, Map<String, Long> companyMap, List<Plane> planeList, DAO dao) {
		Iterator<PlaneDto> dtoIterator = planeDtoList.iterator();
		Iterator<Plane> planeIterator = planeList.iterator();
		while (dtoIterator.hasNext()) {
			planeIterator.next().setCompany_id(companyMap.get(dtoIterator.next().companyName));
		}
		try {
			dao.insertPlaneList(planeList);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static Map<String, Long> getCompanyMap(List<Company> companyList) {
		Map<String, Long> companyMap = new HashMap<>();
		companyList.forEach(company -> companyMap.put(company.getName(), company.getId()));
		return companyMap;

	}

	public static Map<String, Long> getCountryMap(List<Country> countryList) {
		Map<String, Long> countryMap = new HashMap<>();
		countryList.forEach(country -> countryMap.put(country.getName(), country.getId()));
		return countryMap;
	}

	public static void migrateCompanyData(List<CompanyDto> companyDtoList, Map<String, Long> countryMap, List<Company> companies, DAO dao) {
		Iterator<CompanyDto> dtoIterator = companyDtoList.iterator();
		Iterator<Company> companyIterator = companies.iterator();
		while (dtoIterator.hasNext()) {
			companyIterator.next().setCountry_id(countryMap.get(dtoIterator.next().companyCountry));
		}
		try {
			dao.insertCompanyList(companies);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static void migrateCountryData(List<Country> countries, DAO dao) {
		try {
			dao.insertCountryList(countries);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

}
