package db;

import comanda.Comanda;
import cos.Cos;
import products.Produs;
import users.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DBFunctions {

    public static void insertIntoTable(final Connection connection, final String table_name,final String[] lineVals){
        try{
            final Statement statement=connection.createStatement();
            // split lineVals to strings and concatenate them, separated by commas
            final String columns=Stream.of(lineVals).collect(Collectors.joining("', '"));
            final String query=String.format("insert into "+table_name+" values ( '"+columns+"')");
            statement.execute(query);
            statement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void getColumnValuePairs(final List<String> columnsList, final List<String> valuesList,
                                           final List<String> columnValueList){
        int i=0, j=0;
        while (( i<columnsList.size() ) && (j<valuesList.size())){
            String column=columnsList.get(i); String value=valuesList.get(j);
            columnValueList.add(String.format("%s = '%s'", column, value));
            ++i; ++j;
        }

    }
    public static void updateTable(final Connection connection, final String tableName, final String[] columnsToUpdate,
                                   final String[] newValues, final String[] filterArguments) {
        try {
            final Statement statement = connection.createStatement();
            final List<String> columnsList = Arrays.asList(columnsToUpdate);
            final List<String> valuesList = Arrays.asList(newValues);
            final List<String> columnValueList = new ArrayList<>();
            getColumnValuePairs(columnsList,valuesList,columnValueList);
            final String columnValueString = Stream.of(columnValueList.toArray(new String[0])).collect(Collectors.joining(","));
            final String filterArgumentsString = Stream.of(filterArguments).collect(Collectors.joining(" and "));
            final String query = String.format("UPDATE %s SET %s WHERE %s", tableName, columnValueString, filterArgumentsString);
            statement.execute(query);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFromTable(final Connection connection, final String tableName, final String[] filterArguments) {
        try {
            final Statement statement = connection.createStatement();
            String whereKeyWord;
            if (filterArguments.length == 0) {
                whereKeyWord = "";
            } else {
                whereKeyWord = "WHERE";
            }
            // split filterArguments to strings and concatenate them, separated by ' and '
            final String filterArgumentsString = Stream.of(filterArguments).collect(Collectors.joining(" and "));
            final String query = String.format("DELETE FROM %s %s %s", tableName, whereKeyWord, filterArgumentsString);
            statement.execute(query);
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean userExistsInDB(final Connection connection, User user){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from user where user_name='"+user.getUser_name()+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
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

    public static String getUserType(final User user){
        if(user instanceof Manager){
            return UserFactory.MANAGER;
        }
        if(user instanceof Livrator){
            return UserFactory.LIVRATOR;
        }
        if(user instanceof Cumparator){
            return UserFactory.CUMPARATOR;
        }
        return null;
    }
    // return true if the account is created with success and false if the user_name already exists
    public static boolean createAccount(final Connection connection, User user){
        if(userExistsInDB(connection,user)){
            return false;
        }
        String user_type=getUserType(user);
        String[] userData={user.getUser_name(),user.getUser_password(),user_type,user.getNume(),
                user.getPrenume(),user.getEmail(),user.getMoney()};
        insertIntoTable(connection,"user",userData);
        return true;
    }

    public static void deleteAccount(final Connection connection, final User user){
        if(user instanceof Cumparator){
            Cos cos=loadCosFromDB(connection,user.getUser_name());
            if(cos!=null){
                Comanda comanda=loadComandaFromDB(connection,cos.getId_cos());
                if(comanda!=null){
                    deleteComanda(connection,cos.getId_cos());
                }
            }
            deleteCos(connection,user.getUser_name());
        }
        String[] filter={"user_name = '"+ user.getUser_name()+"'"};
        DBFunctions.deleteFromTable(connection,"user",filter);
    }

    public static boolean logIn(final Connection connection, final String user_name, final String user_password) {
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from user where user_name='"+user_name+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next() && resultSet.getString(2).equals(user_password)) {
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

    public static void displayAllProductsFromDB(final Connection con) {
        try {
            Statement statement=con.createStatement();
            final String query=String.format( "select * from produs ;");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1)+"  "+resultSet.getString(2)+"  "
                        +resultSet.getString(3)+ " " +resultSet.getString(4));
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    public static List<String[]> getProductsList(final Connection connection){
        List<String[]> produsList=new ArrayList<>();
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select * from produs ;");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String[] val=new String[4];
                for (int i = 1; i <= 4; i++) {
                    val[i-1]=resultSet.getString(i);
                }
                produsList.add(val);
            }
            return produsList;
        }catch (Exception e) {
            System.out.println(e);
        }
        return produsList;
    }

    public static String[][] getProducts(final Connection connection){
        List<String[]> produsList=getProductsList(connection);
        String[][] data=new String[produsList.size()][4];
        for (int i = 0; i < produsList.size(); i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j]=produsList.get(i)[j];
            }
        }
        return data;
    }

    public static Produs getProdusFromDB(final Connection connection, final String id_produs){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from produs where id_produs='"+id_produs+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()){
                return new Produs(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getDouble(3),resultSet.getInt(4));
            }else {
                statement.close();
                return null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCos(final Connection connection, final String proprietar){
        String[] filter={"proprietar = '"+ proprietar+"'"};
        DBFunctions.deleteFromTable(connection,"cos",filter);
    }

    public static Cos loadCosFromDB(final Connection connection, final String proprietar){
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select * from cos where proprietar = '"+proprietar+"' ;");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String id_cos = resultSet.getString(1);
                String produs_cantitate = resultSet.getString(3);
                Double cost_total = resultSet.getDouble(4);
                return new Cos(id_cos,proprietar,produs_cantitate,cost_total);
            }
            return null;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static boolean cosExistsInDB(final Connection connection, final String id_cos){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from cos where id_cos='"+id_cos+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
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

    public static void updateCosInDB(final Connection connection, final Cos cos){
        String[] columnsToUpdate={"produs_cantitate","cost_total"};
        String[] newVals={cos.getProdus_cantitate(), cos.getCost_total().toString()};
        String[] filterArgs={"id_cos = '"+cos.getId_cos()+"'"};
        DBFunctions.updateTable(connection,"cos",columnsToUpdate,newVals,filterArgs);
    }

    // return false if the cos already exists and true otherwise
    public static boolean addCosToDB(final Connection connection, final Cos cos){
        if(cosExistsInDB(connection,cos.getId_cos())){
            return false;
        }
        String[] userData={cos.getId_cos(),cos.getProprietar(),cos.getProdus_cantitate(),
                cos.getCost_total().toString()};
        insertIntoTable(connection,"cos",userData);
        return true;
    }

    public static Comanda loadComandaFromDB(final Connection connection, final String id_cos){
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select * from comanda where id_cos = '"+id_cos+"' ;");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String id_comanda = resultSet.getString(1);
                String adresa=resultSet.getString(3);
                String data_crearii=resultSet.getString(4);
                String status=resultSet.getString(5);
                String livrator=resultSet.getString(6);
                return new Comanda(id_comanda,id_cos,adresa,data_crearii,status,livrator);
            }
            return null;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void deleteComanda(final Connection connection,final String id_cos){
        String[] filter={"id_cos = '"+ id_cos+"'"};
        DBFunctions.deleteFromTable(connection,"comanda",filter);
    }

    // scadem cantitatea produsului cand clientul plaseaza comanda !
    public static void setCantitate_produsInDB(final Connection connection,final String id_produs,
                                               final Integer newCantitate){
        String[] columnsToUpdate={"cantitate_produs"};
        String[] newVals={newCantitate.toString()};
        String[] filterArgs={"id_produs = '"+id_produs+"'"};
        DBFunctions.updateTable(connection,"produs",columnsToUpdate,newVals,filterArgs);
    }

    public static List<Comanda> getComenziGataDeLivrare(final Connection connection){
        List<Comanda> comenziEfectuate=new ArrayList<>();
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where status='"+Comanda.GATA_DE_LIVRARE+"';");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                comenziEfectuate.add(new Comanda(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6)));
            }
            if(comenziEfectuate.size()!=0){
                return comenziEfectuate;
            }
            return null;
        }catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static String[][] getComenziGataDeLivrareFromDB(final Connection connection){
        List<Comanda> comenziGataDeLivrare=getComenziGataDeLivrare(connection);
        if(comenziGataDeLivrare==null){
            return null;
        }
        String[][] data=new String[comenziGataDeLivrare.size()][6];
        for (int i = 0; i < comenziGataDeLivrare.size(); i++) {
            data[i][0]=comenziGataDeLivrare.get(i).getId_comanda();
            data[i][1]=comenziGataDeLivrare.get(i).getId_cos();
            data[i][2]=comenziGataDeLivrare.get(i).getAdresa();
            data[i][3]=comenziGataDeLivrare.get(i).getData_crearii();
            data[i][4]=comenziGataDeLivrare.get(i).getStatus();
            data[i][5]=comenziGataDeLivrare.get(i).getLivrator();
        }
        return data;
    }

    public static boolean dataIsInInterval(LocalDateTime a, LocalDateTime b, LocalDateTime data_crearii){
        if(data_crearii==null){
            return false;
        }
        return (data_crearii.compareTo(a) > 0 && data_crearii.compareTo(b) < 0);
    }

    // comenzi plasate in intervalul [a, b]
    public static List<Comanda> getComenziEfectuateInInterval(final Connection connection, LocalDateTime a, LocalDateTime b){
        List<Comanda> comenziEfectuate=new ArrayList<>();
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda ;");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                LocalDateTime data_crearii= LocalDateTime.parse(resultSet.getString(4));
                if(dataIsInInterval(a,b,data_crearii)){
                    comenziEfectuate.add(new Comanda(resultSet.getString(1),resultSet.getString(2),
                            resultSet.getString(3),resultSet.getString(4),
                            resultSet.getString(5),resultSet.getString(6)));
                }
            }
            if(comenziEfectuate.size()!=0){
                return comenziEfectuate;
            }
            return null;
        }catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static LocalDateTime readDateTime(final Scanner scanner){
        System.out.print("Read DateTime.\nYear: "); Integer year=scanner.nextInt();
        System.out.print("Month: "); Integer month=scanner.nextInt();
        System.out.print("DayOfMonth: "); Integer dayOfMonth=scanner.nextInt();
        System.out.print("Hour: "); Integer hour=scanner.nextInt();
        System.out.print("Minute: "); Integer minute=scanner.nextInt();
        System.out.print("Second: "); Integer second=scanner.nextInt();
        LocalDateTime time=LocalDateTime.of(year,month,dayOfMonth,hour,minute,second);
        return time;
    }

    public static List<Comanda> getComenziInCursDeLivrare(final Connection connection){
        List<Comanda> comenziEfectuate=new ArrayList<>();
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where status='"+Comanda.LIVRARE_IN_CURS+"';");
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                comenziEfectuate.add(new Comanda(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6)));
            }
            if(comenziEfectuate.size()!=0){
                return comenziEfectuate;
            }
            return null;
        }catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static String[][] getComenziInCursDeLivrareFromDB(final Connection connection){
        List<Comanda> comenziInCursDeLivrare=getComenziInCursDeLivrare(connection);
        String[][] data=new String[comenziInCursDeLivrare.size()][6];
        for (int i = 0; i < comenziInCursDeLivrare.size(); i++) {
            data[i][0]=comenziInCursDeLivrare.get(i).getId_comanda();
            data[i][1]=comenziInCursDeLivrare.get(i).getId_cos();
            data[i][2]=comenziInCursDeLivrare.get(i).getAdresa();
            data[i][3]=comenziInCursDeLivrare.get(i).getData_crearii();
            data[i][4]=comenziInCursDeLivrare.get(i).getStatus();
            data[i][5]=comenziInCursDeLivrare.get(i).getLivrator();
        }
        return data;
    }

    public static User getUserFromDB(final Connection connection, final String user_name){
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select * from user where user_name = '"+user_name+"' ;");
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return UserFactory.createUser(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getString(4),
                        resultSet.getString(5),resultSet.getString(6),
                        resultSet.getString(7));
            }
            return null;
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
