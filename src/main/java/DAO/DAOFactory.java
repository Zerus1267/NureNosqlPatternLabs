package DAO;

public class DAOFactory {

    private static IMyDAO dao;

    public static IMyDAO getDaoInstance(TypeDAO typeDAO){
        if(typeDAO == TypeDAO.MySQL){
            if(dao == null){
                System.out.println("Return new DAO");
                dao = new DAO();
                return dao;
            }
            else {
                System.out.println("Return previous dao");
                return dao;
            }
        }
        else if(typeDAO == typeDAO.MongoDB){
            if(dao == null){
                dao = new MongoDAO();
                return dao;
            }
            else{
                return dao;
            }
        }
        return null;
    }
}
