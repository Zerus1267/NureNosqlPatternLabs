import DAO.MongoDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MongoReadTest {

    private MongoDAO mongoDAO;

    @Before
    public void setUp(){
        mongoDAO = new MongoDAO();
    }

    @Test
    public void readTripsByCountryTest(){
        //System.out.println("First test");
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1904, mongoDAO.getTripsByCountry("Ukraine"));
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1928, mongoDAO.getTripsByCountry("Italy"));
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1974, mongoDAO.getTripsByCountry("Poland"));
    }

}
