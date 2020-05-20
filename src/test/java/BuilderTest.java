import Model.dto.CompanyDto;
import Model.dto.TripDto;
import org.junit.Assert;
import org.junit.Test;

public class BuilderTest {

	@Test
	public void testBuilder(){
		CompanyDto companyDto = CompanyDto.newBuilder().setCountryId(10).setCountryName("Ukrainer").setName("BohdanCompany").setId(-1).build();

		Assert.assertEquals("Company name doesn't match!", "BohdanCompany" ,companyDto.companyName);
		System.out.println(companyDto.toString());

		TripDto tripDto = TripDto.newBuilder().setTripCompany("BohdanCompany").setMinDuration(60).setMinPrice(4000).build();
		System.out.println(tripDto);
	}

}
