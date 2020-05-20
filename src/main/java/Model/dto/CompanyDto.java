package Model.dto;

public class CompanyDto {

	public long id;
	public long countryId;
	public String companyName;
	public String companyCountry;

	private CompanyDto() {
	}

	public CompanyDto(String companyName, String companyCountry) {
		this.companyName = companyName;
		this.companyCountry = companyCountry;
	}

	public CompanyDto(long id, long countryId, String companyName) {
		this.id = id;
		this.countryId = countryId;
		this.companyName = companyName;
	}

	public static Builder newBuilder() {
		return new CompanyDto().new Builder();
	}

	@Override
	public String toString() {
		return "CompanyDto{" +
				"id=" + id +
				", countryId=" + countryId +
				", companyName='" + companyName + '\'' +
				", companyCountry='" + companyCountry + '\'' +
				'}';
	}

	public class Builder {
		private Builder() {

		}

		public Builder setId(long id) {
			CompanyDto.this.id = id;
			return this;
		}

		public Builder setCountryId(long countryId) {
			CompanyDto.this.countryId = countryId;
			return this;
		}

		public Builder setName(String name) {
			CompanyDto.this.companyName = name;
			return this;
		}

		public Builder setCountryName(String name) {
			CompanyDto.this.companyCountry = name;
			return this;
		}

		public CompanyDto build() {
			CompanyDto companyDto = new CompanyDto();
			companyDto.id = CompanyDto.this.id;
			companyDto.countryId = CompanyDto.this.countryId;
			companyDto.companyName = CompanyDto.this.companyName;
			companyDto.companyCountry = CompanyDto.this.companyCountry;
			return companyDto;
		}

	}
}
