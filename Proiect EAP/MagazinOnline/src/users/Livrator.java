package users;

import comanda.Comanda;
import db.DBFunctions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Livrator extends User {
    public Livrator() {
    }

    public Livrator(String user_name, String user_password, String nume, String prenume, String email, String money) {
        super(user_name, user_password, nume, prenume, email, money);
    }

    public Livrator(String user_name, String user_password) {
        super(user_name, user_password);
    }

    public boolean areAlteComenziInCursDeLivrare(final Connection connection){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where status='"+Comanda.LIVRARE_IN_CURS+"'" +
                    " and livrator='"+this.getUser_name()+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                statement.close();
                return true;
            }
            return false;
        }catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean isGataDeLivrare(final Connection connection, final String id_comanda){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where status='"+Comanda.GATA_DE_LIVRARE+"';");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString(1).equals(id_comanda)){
                    return true;
                }
            }
            return false;
        }catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public void preiaComanda(final Connection connection, final String id_comanda){
        if( ! isGataDeLivrare(connection,id_comanda)){
            return;
        }
        if(areAlteComenziInCursDeLivrare(connection)){
            return;
        }
        String[] columnsToUpdate={"status","livrator"};
        String[] newVals={Comanda.LIVRARE_IN_CURS, this.getUser_name()};
        String[] filterArgs={"id_comanda = '"+id_comanda+"'"};
        DBFunctions.updateTable(connection,"comanda",columnsToUpdate,newVals,filterArgs);
    }

    public Comanda getComandaPreluata(final Connection connection){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where status='"+Comanda.LIVRARE_IN_CURS+"'" +
                    " and livrator='"+this.getUser_name()+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return new Comanda(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6));
            }
            return null;
        }catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void setComandaLivrataCuSuccess(final Connection connection){
        Comanda comanda=getComandaPreluata(connection);
        if(comanda==null){
            return;
        }
        String[] columnsToUpdate={"status"};
        String[] newVals={Comanda.LIVRATA_CU_SUCCES};
        String[] filterArgs={"id_comanda = '"+comanda.getId_comanda()+"'"};
        DBFunctions.updateTable(connection,"comanda",columnsToUpdate,newVals,filterArgs);
    }
}
