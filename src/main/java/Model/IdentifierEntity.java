package Model;

public abstract class IdentifierEntity {

	private long id;

	public IdentifierEntity() {
	}

	public abstract String getEntityName();

	public IdentifierEntity(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
