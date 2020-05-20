import DAO.MongoDAO;
import com.mongodb.client.model.Filters;
import javafx.util.Pair;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MongoWriteTest {

	private MongoDAO mongoDAO;
	List<Document> documents;

	@Before
	public void setMongoDAO() {
		mongoDAO = new MongoDAO();
		documents = new ArrayList<>();
		int i = 0;
		do {
			documents.add(new Document("planeName", "PlaneForTest" + i).append("planeCapacity", 200 + i));
			i++;
		}
		while (i < 100000);
	}

	@Test
	public void insertTest() throws InterruptedException {
		int k = 0;
		for(Document document : documents){
			int i = 0;
			try {
				if(k == 10000) {
					System.out.println("Pause on 10k rows!");
				}
				insertSingleDocument(document, i);
			} catch (RuntimeException runtimeException){
				runtimeException.printStackTrace();
			} finally {
				k++;
			}
		}
		/*documents.forEach(document -> {
			if (document.getInteger("planeCapacity") == 2001) System.out.println("Has reached 2001 rows");
			mongoDAO.addNewPlane(document.getString("planeName"), document.getInteger("planeCapacity"));
		});*/
	}

	private void insertSingleDocument(Document document, int index) throws InterruptedException {
		try{
			mongoDAO.addNewPlane(document.getString("planeName"), document.getInteger("planeCapacity"));
		} catch (Exception e){
			if(index > 3){
				e.printStackTrace();
				System.out.println("It's too long to wait a response from server");
				throw new RuntimeException("It's too long waiting server response");
			}
			TimeUnit.MINUTES.sleep(1);
			insertSingleDocument(document, index++);
		}
	}


	@After
	public void deletePlanes() {
		System.out.println("enter the AFTER method");
		System.out.println("Plane collection count after insertions = " + mongoDAO.getPlaneCollection().countDocuments());
		//mongoDAO.getPlaneCollection().deleteMany(Filters.eq("planeName", Pattern.compile("PlaneForTest")));
	}
}
