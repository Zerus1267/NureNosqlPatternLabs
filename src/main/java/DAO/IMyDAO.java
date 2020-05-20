package DAO;

import Model.Country;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface IMyDAO {

	public int getTripsByCountry(String country);
	public void addNewPlaneFunc(String name, int capacity, String company) throws Exception;
	public void deletePlaneByName(String plane_name) throws SQLException;
	public void updateCompanyPlaneByNames(String companyName, String planeName) throws SQLException;
	public void addNewTrip(String countryIn, String countryOut, int minDuration, int minPrice, String companyName) throws SQLException, IOException;
	public List<Country> getCountryList();
	public List<PlaneDto> getPlaneList();
	public List<CompanyDto> getCompanyList();
	public List<TripDto> getTripList();

}
