package Model.dto;

public class TripDto {

	public long tripId;
	public long countryInId;
	public long countryOutId;
	public String countryIn;
	public String countryOut;
	public int minPrice;
	public int minDuration;
	public String tripCompany;
	public long tripCompanyId;

	private TripDto() {
	}

	public TripDto(String countryIn, String countryOut, int minPrice, int minDuration, String tripCompany) {
		this.countryIn = countryIn;
		this.countryOut = countryOut;
		this.minPrice = minPrice;
		this.minDuration = minDuration;
		this.tripCompany = tripCompany;
	}

	public TripDto(long tripId, long countryInId, long countryOutId, int minPrice, int minDuration) {
		this.tripId = tripId;
		this.countryInId = countryInId;
		this.countryOutId = countryOutId;
		this.minPrice = minPrice;
		this.minDuration = minDuration;
	}

	public static Builder newBuilder(){
		return new TripDto().new Builder();
	}

	@Override
	public String toString() {
		return "TripDto{" +
				"tripId=" + tripId +
				", countryInId=" + countryInId +
				", countryOutId=" + countryOutId +
				", countryIn='" + countryIn + '\'' +
				", countryOut='" + countryOut + '\'' +
				", minPrice=" + minPrice +
				", minDuration=" + minDuration +
				", tripCompany='" + tripCompany + '\'' +
				", tripCompanyId=" + tripCompanyId +
				'}';
	}

	public class Builder {

		private Builder() {
		}

		public Builder setTripId(long tripId) {
			TripDto.this.tripId = tripId;
			return this;
		}

		public Builder setCountryInId(long countryInId) {
			TripDto.this.countryInId = countryInId;
			return this;
		}

		public Builder setCountryOutId(long countryOutId) {
			TripDto.this.countryOutId = countryOutId;
			return this;
		}

		public Builder setCountryIn(String countryIn) {
			TripDto.this.countryIn = countryIn;
			return this;
		}

		public Builder setCountryOut(String countryOut) {
			TripDto.this.countryOut = countryOut;
			return this;
		}

		public Builder setMinPrice(int minPrice) {
			TripDto.this.minPrice = minPrice;
			return this;
		}

		public Builder setMinDuration(int minDuration) {
			TripDto.this.minDuration = minDuration;
			return this;
		}

		public Builder setTripCompany(String tripCompany) {
			TripDto.this.tripCompany = tripCompany;
			return this;
		}

		public Builder setTripCompanyId(long tripCompanyId) {
			TripDto.this.tripCompanyId = tripCompanyId;
			return this;
		}

		public TripDto build() {
			TripDto tripDto = new TripDto(TripDto.this.countryIn, TripDto.this.countryOut, TripDto.this.minPrice, TripDto.this.minDuration, TripDto.this.tripCompany);
			tripDto.tripCompanyId = TripDto.this.tripCompanyId;
			tripDto.tripId = TripDto.this.tripId;
			tripDto.countryInId = TripDto.this.countryInId;
			tripDto.countryOutId = TripDto.this.countryOutId;
			return tripDto;
		}
	}
}
