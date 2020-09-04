package users;

import db.DBFunctions;
import products.Produs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Manager extends User {
    public Manager() {
    }

    public Manager(String user_name, String user_password, String nume, String prenume, String email, String money) {
        super(user_name, user_password, nume, prenume, email, money);
    }

    public Manager(String user_name, String user_password) {
        super(user_name, user_password);
    }

    public boolean productExistsInDB(final Connection connection, final Produs produs){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from produs where id_produs='"+produs.getId_produs()+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                statement.close();
                return true;
            }else {
                statement.close();
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // true if product is added to DB and false if the product already exists
    public boolean addProductToDB(final Connection connection, Produs produs){
        if(productExistsInDB(connection,produs)){
            return false;
        }
        String[] userData={produs.getId_produs(),produs.getNume_produs(), produs.getPret_produs().toString(),
                produs.getCantitate_produs().toString()};
        DBFunctions.insertIntoTable(connection,"produs",userData);
        return true;
    }

    public void deleteProduct(final Connection connection, final String id_produs){
        String[] filter={"id_produs = '"+ id_produs+"'"};
        DBFunctions.deleteFromTable(connection,"produs",filter);
    }

    public void changeProductPrice(final Connection connection, String id_produs, Double newPrice){
        String[] columnsToUpdate={"pret_produs"};
        String[] newVals={newPrice.toString()};
        String[] filterArgs={"id_produs = '"+id_produs+"'"};
        DBFunctions.updateTable(connection,"produs",columnsToUpdate,newVals,filterArgs);
    }

}
