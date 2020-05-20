package Model;

public class Plane extends IdentifierEntity {
	private String modification;
	private int capacity;
	private long company_id;

	public Plane() {
	}

	public Plane(long id) {
		this.setId(id);
	}

	@Override
	public String getEntityName() {
		return modification;
	}

	public Plane(String modification, int capacity, long company_id) {
		this.modification = modification;
		this.capacity = capacity;
		this.company_id = company_id;
	}

	public String getModification() {
		return modification;
	}

	public void setModification(String modification) {
		this.modification = modification;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public long getCompany_id() {
		return company_id;
	}

	public void setCompany_id(long company_id) {
		this.company_id = company_id;
	}
}
