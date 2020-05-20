import DAO.MongoDAO;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MongoTest {

    private MongoDAO mongoDAO;

    @Before
    public void setUp(){
        mongoDAO = new MongoDAO();
    }

    @Test
    public void testOne(){
        //System.out.println("First test");
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1904, mongoDAO.getTripsByCountry("Ukraine"));
    }

    @Test
    public void testTwo() throws Exception {
        //System.out.println("Second test");
        int oldCountPlane = mongoDAO.getPlaneCollectionSize();
        int oldCountCompany = mongoDAO.getCompanyCollectionSize();
        mongoDAO.addNewCountry("TestCountry");
        mongoDAO.addNewPlaneFunc("TestPlane", 2228, "TestCompany");
        Assert.assertEquals("The plane hasn't created!", oldCountPlane+1, mongoDAO.getPlaneCollectionSize());
        Assert.assertEquals("The TestCompany hasn't created!", oldCountCompany+1, mongoDAO.getCompanyCollectionSize());
        Assert.assertEquals("There is incorrect number of planes in the TestCompany!", 1, mongoDAO.getCompanyPlaneCount("TestCompany"));
        mongoDAO.getCountryCollection().findOneAndDelete(Filters.eq("countryName", "TestCountry"));
        mongoDAO.getPlaneCollection().findOneAndDelete(Filters.eq("planeName", "TestPlane"));
        mongoDAO.getCompanyCollection().findOneAndDelete(Filters.eq("companyName", "TestCompany"));
    }

    @After
    public void deleteAllTestRecords(){

    }
}
