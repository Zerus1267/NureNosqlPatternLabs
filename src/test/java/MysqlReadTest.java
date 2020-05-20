import DAO.DAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MysqlReadTest {

    private DAO dao;

    @Before
    public void setDao(){
        dao = new DAO();
    }

    @Test
    public void readTripsByCountryTest(){
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1874, dao.getTripsByCountry("Ukraine"));
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 2077, dao.getTripsByCountry("Italy"));
        Assert.assertEquals("The number of trips from Ukraine is incorrect!", 1983, dao.getTripsByCountry("Poland"));
    }
}
