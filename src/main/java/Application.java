import DAO.DAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import DAO.*;

public class Application {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world");
        //System.out.println("Boolean test " + TypeDAO.MySQL.equals(TypeDAO.MySQL));
        IMyDAO dao = DAOFactory.getDaoInstance(TypeDAO.MySQL);
        //DAO dao = new DAO();
        //IMyDAO dao = new DAO();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Choose what you want to do");
            System.out.println("1 - Add new plane to DB");
            System.out.println("2 - Select trips from certain country");
            System.out.println("3 - Delete plane by it's name");
            System.out.println("4 - Update owning company of a plane");
            System.out.println("5 - Add new trip to BD");
            switch (reader.readLine()) {
                case "1":
                    System.out.println("Enter the name of your plane");
                    String name = reader.readLine();
                    System.out.println("Enter the capacity of your new plane");
                    String capacity = reader.readLine();
                    System.out.println("Enter the air company name that owes this plane");
                    String company = reader.readLine();
                    dao.addNewPlaneFunc(name, Integer.parseInt(capacity), company);
                    break;
                case "2":
                    System.out.println("Enter country name please");
                    String country_name = reader.readLine();
                    dao.getTripsByCountry(country_name);
                    break;
                case "3":
                    System.out.println("Please enter the plane's modification name to delete it");
                    String plane_name = reader.readLine();
                    dao.deletePlaneByName(plane_name);
                    break;
                case "4":
                    System.out.println("Please enter the plane's modification name");
                    String plane_update_name = reader.readLine();
                    System.out.println("Please enter the name of company that will own this plane");
                    String company_update_name = reader.readLine();
                    dao.updateCompanyPlaneByNames(company_update_name, plane_update_name);
                    break;
                case "5":
                    System.out.println("Enter the country OUT");
                    String countryOut = reader.readLine();
                    System.out.println("Enter the country IN");
                    String countryIn = reader.readLine();
                    System.out.println("Enter the company name that will service this trip");
                    String companyName = reader.readLine();
                    System.out.println("Enter trip minimal duration");
                    int minDuration = Integer.parseInt(reader.readLine());
                    System.out.println("En5ter trip minimal price");
                    int minPrice = Integer.parseInt(reader.readLine());
                    dao.addNewTrip(countryIn, countryOut, minDuration, minPrice, companyName);
            }
        }
    }
}
