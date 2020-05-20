import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import DAO.DAO;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MysqlWriteTest {

    private DAO dao;
    private List<Document> documents;

    @Before
    public void setDao(){
        dao = new DAO();
        documents = new ArrayList<>();
        int i = 0;
        do {
            documents.add(new Document("planeName", "PlaneForTest").append("planeCapacity", 200+i));
            i++;
        }
        while (i < 100000);
    }

    @Test
    public void addNewPlaneTest(){
        documents.forEach(document -> {
            try {
                dao.addNewPlane(document.getString("planeName"), document.getInteger("planeCapacity"), 16);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @After
    public void deleteAllPlanes() throws SQLException {
        System.out.println("Enter AFTER method");
        dao.getConnection().prepareStatement("delete from plane where plane_modification like 'PlaneForTest'").execute();
    }
}
