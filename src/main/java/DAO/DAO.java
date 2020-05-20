package DAO;

import DAO.observer.EventManager;
import DAO.observer.listeners.EntityReadListener;
import DAO.observer.listeners.EntityWriteListener;
import Model.Company;
import Model.Country;
import Model.Plane;
import Model.Trip;
import Model.dto.CompanyDto;
import Model.dto.PlaneDto;
import Model.dto.TripDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO implements IMyDAO {
	private String conString = "jdbc:mysql://localhost:3306/airtransSmall";
	private String user = "skrekoza";
	private String password = "855492";
	private Connection connection;
	private EventManager eventManager;

	public Connection getConnection() {
		return connection;
	}

	public DAO() {
		eventManager = new EventManager("read","write");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(conString, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getTripsByCountry(String country) {
		int i = 0;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select t.trip_id, c1.country_name as country_out, c2.country_name as country_in, t.trip_min_duration, t.trip_min_price from trip t" +
					"    join country c1 on t.trip_country_out = c1.country_id" +
					"    join country c2 on t.trip_country_in = c2.country_id" +
					"    where c1.country_name = ?");
			preparedStatement.setString(1, country);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				i++;
				//System.out.println("Country out: " + resultSet.getString("country_out") + "; " + "Country in: " + resultSet.getString("country_in") + "; " + "Price: " + resultSet.getInt("trip_min_price") + "; " + "Duration: " + resultSet.getInt("trip_min_duration"));
				//System.out.println(resultSet.getInt("trip_id") + resultSet.getString("country_out") + resultSet.getString("country_in") + resultSet.getInt("trip_min_duration") + resultSet.getInt("trip_min_price"))
			}
			//return resultSet.getFetchSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public void addNewPlaneFunc(String name, int capacity, String company) throws Exception {
		BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));
		if (hasCompany(company)) {
			addNewPlane(name, capacity, getCompanyId(company));
		} else {
			System.out.println("Enter country name of your new company");
			String country = reader.readLine();
			addNewCompanyFunc(company, country);
			addNewPlane(name, capacity, getCompanyId(company));
		}
	}

	private int addNewCompanyFunc(String company, String country) throws SQLException {
		if (hasCountry(country)) {
			return addNewCompany(company, country);
		} else {
			addNewCountry(country);
			return addNewCompany(company, country);
		}
	}

	private void addNewCountry(String country) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("insert into country (country_name) VALUE (?)");
		preparedStatement.setString(1, country);
		preparedStatement.execute();
	}

	private int addNewCompany(String company, String country) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("insert into company (company_name, company_country_id) VALUE (?,?)", Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, company);
		PreparedStatement preparedStatement1 = connection.prepareStatement("select country_id from country where country_name = ?");
		preparedStatement1.setString(1, country);

		connection.setAutoCommit(false);
		try {
			ResultSet resultSet = preparedStatement1.executeQuery();
			System.out.println(resultSet.next());
			int countryId = resultSet.getInt(1);
			preparedStatement.setInt(2, countryId);
			preparedStatement.executeUpdate();
			connection.commit();
			ResultSet RS = preparedStatement.getGeneratedKeys();
			if (RS.next()) {
				return RS.getInt(1);
			} else return 0;
		} catch (SQLException e) {
			connection.rollback();
		} finally {
			connection.setAutoCommit(true);
		}
		return 0;
	}

	private boolean hasCountry(String country) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from country where country_name = ?");
		preparedStatement.setString(1, country);
		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet.next();
	}

	private boolean hasCompany(String name) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select * from company where company_name = ?");
		preparedStatement.setString(1, name);
		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet.next();
	}

	private int getCompanyId(String name) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select company_id from company where company_name = ?");
		preparedStatement.setString(1, name);
		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt(1);
	}

	public void addNewPlane(String name, int capacity, int company_id) throws SQLException {
		eventManager.subscribe("write", new EntityWriteListener("plane"));
		PreparedStatement preparedStatement = connection.prepareStatement("insert into plane (plane_modification, plane_capacity, plane_company_id) VALUE (?,?,?)");
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, capacity);
		preparedStatement.setInt(3, company_id);
		connection.setAutoCommit(false);
		try {
			preparedStatement.execute();
			//System.out.println("Successful adding new plane");
			connection.commit();
			eventManager.notify("write", new Plane(name, capacity, company_id));
		} catch (SQLException e) {
			//System.out.println("Couldn't insert new plane. Partially");
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.setAutoCommit(true);
		}
	}

	private int getPlaneId(String plane_name) throws SQLException {
		eventManager.subscribe("read", new EntityReadListener("plane"));
		PreparedStatement preparedStatement = connection.prepareStatement("select plane_id from plane where plane_modification = ?");
		preparedStatement.setString(1, plane_name);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			long id = resultSet.getLong(1);
			eventManager.notify("read", new Plane(id));
			return (int) id;
		} else return 0;
	}

	private int getCountryId(String name) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("select country_id from country where country_name = ?");
		preparedStatement.setString(1, name);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			return resultSet.getInt(1);
		} else return 0;
	}

	public void deletePlaneByName(String plane_name) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("delete from plane where plane_modification = ?");
		preparedStatement.setString(1, plane_name);
		connection.setAutoCommit(false);
		try {
			preparedStatement.execute();
			connection.commit();
			System.out.println("Successful delete");
		} catch (SQLException e) {
			System.out.println("Something went wrong");
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.setAutoCommit(true);
		}
	}

	public void updateCompanyPlaneByNames(String company_name, String plane_name) throws SQLException {
		if (this.hasCompany(company_name)) {
			connection.setAutoCommit(false);
			int company_id = this.getCompanyId(company_name);
			int plane_id = this.getPlaneId(plane_name);
			if (plane_id != 0) {
				PreparedStatement preparedStatement = connection.prepareStatement("update plane set plane_company_id = ? where plane_id = ?");
				int i = 1;
				preparedStatement.setInt(i++, company_id);
				preparedStatement.setInt(i, plane_id);
				try {
					preparedStatement.execute();
					connection.commit();
					System.out.println("Successfully updated plane's company");
				} catch (SQLException e) {
					connection.rollback();
					e.printStackTrace();
				} finally {
					connection.setAutoCommit(true);
				}
			} else {
				System.out.println("Entered plane's name isn't in a database");
				connection.setAutoCommit(true);
			}
		} else System.out.println("Entered Company doesn't exist!");
	}

	public void addNewTrip(String countryIn, String countryOut, int minDuration, int minPrice, String company) throws SQLException, IOException {
		connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		connection.setAutoCommit(false);
		if (this.hasCountry(countryIn)) {
			if (this.hasCountry(countryOut)) {
				if (this.hasCompany(company)) {
					int compID = this.getCompanyId(company);
					addNewTripFunc(countryIn, countryOut, minDuration, minPrice, compID);
				} else {
					BufferedReader reader =
							new BufferedReader(new InputStreamReader(System.in));
					System.out.println("Enter the company's country please:");
					String compCountry = reader.readLine();
					int compID = addNewCompanyFunc(company, compCountry);
					System.out.println("Generated id for company:" + compID);
					addNewTripFunc(countryIn, countryOut, minDuration, minPrice, compID);
				}
			} else {
				this.addNewCountry(countryOut);
				this.addNewTrip(countryIn, countryOut, minDuration, minPrice, company);
			}
		} else {
			this.addNewCountry(countryIn);
			this.addNewTrip(countryIn, countryOut, minDuration, minPrice, company);
		}
	}

	public void insertCountryList(List<Country> countryList) throws SQLException {
		createCountryTableIfNotExists();
		StringBuilder sqlInsert = new StringBuilder();
		sqlInsert.append("insert into country(country_name) values ");
		countryList.forEach(country -> sqlInsert.append("('").append(country.getName()).append("'), "));
		sqlInsert.replace(sqlInsert.length() - 2, sqlInsert.length(), "");
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert.toString(), Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		int i = 0;
		while (rs.next()) {
			countryList.get(i).setId(rs.getLong(1));
			i++;
		}
		return;
	}

	private boolean createCountryTableIfNotExists() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists country (country_id int auto_increment primary key, country_name varchar(50) not null)");
		return preparedStatement.execute();
	}

	public void insertCompanyList(List<Company> companies) throws SQLException {
		createCompanyTableIfNotExists();
		StringBuilder sqlInsert = new StringBuilder();
		sqlInsert.append("insert into company(company_name, company_country_id) values ");
		companies.forEach(company -> sqlInsert.append("('").append(company.getName()).append("',").append(company.getCountry_id()).append("), "));
		sqlInsert.replace(sqlInsert.length() - 2, sqlInsert.length(), "");
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert.toString(), Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		int i = 0;
		while (rs.next()) {
			companies.get(i).setId(rs.getLong(1));
			i++;
		}
	}

	private void createCompanyTableIfNotExists() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists company " +
				"(company_id int auto_increment primary key," +
				"company_name varchar(50) not null," +
				"company_country_id int not null," +
				"constraint company_country_country_id_fk foreign key (company_country_id) references country (country_id));");
		preparedStatement.execute();
	}

	public void insertPlaneList(List<Plane> planeList) throws SQLException {
		createPlaneTableIfNotExists();
		StringBuilder sqlInsert = new StringBuilder();
		sqlInsert.append("insert into plane (plane_modification, plane_capacity, plane_company_id) VALUES ");
		planeList.forEach(plane -> sqlInsert.append("('").append(plane.getModification()).append("',").append(plane.getCapacity()).append(",").append(plane.getCompany_id()).append("), "));
		sqlInsert.replace(sqlInsert.length() - 2, sqlInsert.length(), "");
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert.toString(), Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		int i = 0;
		while (rs.next()) {
			planeList.get(i).setId(rs.getLong(1));
			i++;
		}
	}

	private void createPlaneTableIfNotExists() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists plane(plane_id int auto_increment primary key," +
				"plane_modification varchar(255) charset utf8 not null," +
				"plane_capacity int not null,plane_company_id int null," +
				"constraint plane_company_company_id_fk foreign key (plane_company_id) references company (company_id));");
		preparedStatement.execute();
	}

	public void insertTripList(List<Trip> tripList) throws SQLException {
		createTripTableIfNotExists();
		StringBuilder sqlInsert = new StringBuilder();
		sqlInsert.append("insert into trip (trip_country_out, trip_country_in, trip_min_duration, trip_min_price) VALUES ");
		tripList.forEach(trip -> sqlInsert.append("(").append(trip.getC_out()).append(",").append(trip.getC_in()).append(",").append(trip.getMin_duration()).append(",").append(trip.getMin_price()).append("), "));
		sqlInsert.replace(sqlInsert.length() - 2, sqlInsert.length(), "");
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert.toString(), Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		int i = 0;
		while (rs.next()) {
			tripList.get(i).setId(rs.getLong(1));
			i++;
		}
	}

	@Override
	public List<Country> getCountryList() {
		List<Country> countryList = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement("select country_id, country_name from country");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				countryList.add(new Country(rs.getLong("country_id"), rs.getString("country_name")));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return countryList;
	}

	@Override
	public List<PlaneDto> getPlaneList() {
		List<PlaneDto> planeDtoList = new ArrayList<>();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select plane_id, plane_modification, plane_capacity, plane_company_id from plane;");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				planeDtoList.add(new PlaneDto(rs.getLong("plane_id"),
						rs.getLong("plane_company_id"),
						rs.getString("plane_modification"),
						rs.getInt("plane_capacity")));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return planeDtoList;
	}

	@Override
	public List<CompanyDto> getCompanyList() {
		List<CompanyDto> companyDtoList = new ArrayList<>();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select company_id, company_name, company_country_id from company;");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				companyDtoList.add(new CompanyDto(rs.getLong("company_id"),
						rs.getLong("company_country_id"),
						rs.getString("company_name")));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return companyDtoList;
	}

	@Override
	public List<TripDto> getTripList() {
		List<TripDto> tripDtoList = new ArrayList<>();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select trip_id, trip_country_out, trip_country_in, trip_min_duration, trip_min_price from trip;");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tripDtoList.add(new TripDto(rs.getLong("trip_id"),
						rs.getLong("trip_country_in"),
						rs.getLong("trip_country_out"),
						rs.getInt("trip_min_duration"),
						rs.getInt("trip_min_price")));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		return tripDtoList;
	}

	public Map<Long, Long> getTripCompanyRelation() {
		Map<Long, Long> tripCompanyMap = new HashMap<>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("select * from company_trip");
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				tripCompanyMap.put(rs.getLong("trip_id"), rs.getLong("company_id"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return tripCompanyMap;
	}

	private void createTripTableIfNotExists() throws SQLException {

		PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists trip (\n" +
				"trip_id int auto_increment primary key," +
				"trip_country_out int not null," +
				"trip_country_in int not null," +
				"trip_min_duration int not null," +
				"trip_min_price int not null," +
				"constraint trip_country_country_id_fk foreign key (trip_country_in) references country (country_id)," +
				"constraint trip_country_country_id_fk_2 foreign key (trip_country_out) references country (country_id))");
		preparedStatement.execute();
	}

	public void insertCompanyTripMap(Map<Long, Long> companyTripMap) throws SQLException {
		createCompanyTripTableIfNotExists();
		StringBuilder sqlInsert = new StringBuilder();
		sqlInsert.append("insert into company_trip (company_id, trip_id) VALUES ");
		companyTripMap.forEach((tripId, companyId) -> sqlInsert.append("(").append(companyId).append(",").append(tripId).append("), "));
		sqlInsert.replace(sqlInsert.length() - 2, sqlInsert.length(), "");
		PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert.toString());
		preparedStatement.execute();
	}

	private void createCompanyTripTableIfNotExists() throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists company_trip (" +
				"company_id int not null," +
				"trip_id int not null," +
				"primary key (company_id, trip_id)," +
				"constraint company_trip_company_company_id_fk foreign key (company_id) references company (company_id)," +
				"constraint company_trip_trip_trip_id_fk foreign key (trip_id) references trip (trip_id));");
		preparedStatement.execute();
	}

	private void addNewTripFunc(String countryIn, String countryOut, int minDuration, int minPrice, int companyId) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("insert into trip(trip_country_out, trip_country_in, trip_min_duration, trip_min_price) value(?,?,?,?);",
				Statement.RETURN_GENERATED_KEYS);
		int i = 1;
		connection.setAutoCommit(false);
		preparedStatement.setInt(i++, this.getCountryId(countryOut));
		preparedStatement.setInt(i++, this.getCountryId(countryIn));
		preparedStatement.setInt(i++, minDuration);
		preparedStatement.setInt(i, minPrice);
		try {
			preparedStatement.executeUpdate();
			ResultSet RS = preparedStatement.getGeneratedKeys();
			int tripID = 0;
			if (RS.next()) {
				tripID = RS.getInt(1);
			}
			PreparedStatement preparedStatement1 = connection.prepareStatement("insert into company_trip value (?,?)");
			preparedStatement1.setInt(1, companyId);
			preparedStatement1.setInt(2, tripID);
			preparedStatement1.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.setAutoCommit(true);
			connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		}
	}


}
