package Model;

public class Country extends IdentifierEntity {
	private String name;

	public Country() {
	}

	@Override
	public String getEntityName() {
		return name;
	}

	public Country(String name) {
		this.name = name;
	}

	public Country(long id, String name) {
		this.setId(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
