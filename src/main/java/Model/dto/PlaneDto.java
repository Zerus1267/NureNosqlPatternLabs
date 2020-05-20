package Model.dto;

public class PlaneDto {

	public long id;
	public long companyId;
	public String planeName;
	public int planeCapacity;
	public String companyName;

	private PlaneDto() {
	}

	public PlaneDto(String planeName, int planeCapacity, String companyName) {
		this.planeName = planeName;
		this.planeCapacity = planeCapacity;
		this.companyName = companyName;
	}

	public PlaneDto(long id, long companyId, String planeName, int planeCapacity) {
		this.id = id;
		this.companyId = companyId;
		this.planeName = planeName;
		this.planeCapacity = planeCapacity;
	}

	public static Builder newBuilder() {
		return new PlaneDto().new Builder();
	}

	public class Builder {

		private Builder() {
		}

		public Builder setId(long id) {
			PlaneDto.this.id = id;
			return this;
		}

		public Builder setCompanyId(long companyId) {
			PlaneDto.this.companyId = companyId;
			return this;
		}

		public Builder setPlaneName(String planeName) {
			PlaneDto.this.planeName = planeName;
			return this;
		}

		public Builder setPlaneCapacity(int planeCapacity) {
			PlaneDto.this.planeCapacity = planeCapacity;
			return this;
		}

		public Builder setCompanyName(String companyName) {
			PlaneDto.this.companyName = companyName;
			return this;
		}

		public PlaneDto build() {
			PlaneDto planeDto = new PlaneDto(PlaneDto.this.id, PlaneDto.this.companyId, PlaneDto.this.planeName, PlaneDto.this.planeCapacity);
			planeDto.companyName = PlaneDto.this.companyName;
			return planeDto;
		}
	}
}
