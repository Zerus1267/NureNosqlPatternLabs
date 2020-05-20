package Model;

public class Company extends IdentifierEntity {
	private String name;
	private long country_id;

	public Company() {
	}

	@Override
	public String getEntityName() {
		return name;
	}

	public Company(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCountry_id() {
		return country_id;
	}

	public void setCountry_id(long country_id) {
		this.country_id = country_id;
	}
}
