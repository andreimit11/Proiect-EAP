package users;

import comanda.Comanda;
import cos.Cos;
import db.DBConnector;
import db.DBFunctions;
import products.Produs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cumparator extends User {
    public Cumparator(String user_name, String user_password, String nume, String prenume, String email, String money) {
        super(user_name, user_password, nume, prenume, email, money);
    }

    public Cumparator() {
    }

    public Cumparator(String user_name, String user_password) {
        super(user_name, user_password);
    }

    public String getMoneyFromDB(final Connection connection){
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select money from user where user_name='"+this.getUser_name()+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                return resultSet.getString(1);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }

    public boolean produsExistentInCos(Cos cos, Produs produs){
        try{
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                if(prod_cantitate.get(i).equals(produs.getId_produs())){
                    return true;
                }
            }
        }catch(Exception e){ System.out.println(e);}
        return false;
    }

    public void addProdusExistentInCos(Cos cos, Produs produs, Integer cantitate){
        try{
            if(produs==null){
                return;
            }
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String newProdusCantiate="";
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                if(! (prod_cantitate.get(i).equals(produs.getId_produs()))){
                    newProdusCantiate+=","+prod_cantitate.get(i)+"-"+prod_cantitate.get(i+1);
                }else{
                    Integer newCantitate=Integer.parseInt(prod_cantitate.get(i+1))+cantitate;
                    newProdusCantiate+=","+prod_cantitate.get(i)+"-"+newCantitate.toString();
                }
            }
            cos.setProdus_cantitate(newProdusCantiate);
            cos.setCost_total(cos.getCost_total()+ produs.getPret_produs()*cantitate);
        }catch(Exception e){ System.out.println(e);}
    }

    // product is valid
    public void adaugaProdusInCos(Cos cos, Produs produs, Integer cantitate){
        if(produsExistentInCos(cos,produs)){
            addProdusExistentInCos(cos,produs,cantitate);
        }else {
            cos.setProdus_cantitate(cos.getProdus_cantitate()+","+produs.getId_produs()+"-"+cantitate.toString());
            cos.setCost_total(cos.getCost_total()+(produs.getPret_produs()*cantitate));
        }
    }

    public void afiseazaProduseleDinCos(final Connection connection, Cos cos){
        try{
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                Produs produs = DBFunctions.getProdusFromDB(connection,prod_cantitate.get(i));
                System.out.println(produs.getId_produs()+" "+produs.getNume_produs()+"\t\tPret produs: " +
                        produs.getPret_produs()+ "\t\tNr bucati: "+prod_cantitate.get(i+1));
            }
        }catch(Exception e){ System.out.println(e);}
    }

    public void eliminaProdusDinCos(final Connection connection, Cos cos, final String id_produs){
        try{
            Produs produs=DBFunctions.getProdusFromDB(connection,id_produs);
            if(produs==null){
                return;
            }
            Double cantitate_produs_din_cos=0.0;
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String newProdusCantiate="";
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                if(! (prod_cantitate.get(i).equals(id_produs))){
                    newProdusCantiate+=","+prod_cantitate.get(i)+"-"+prod_cantitate.get(i+1);
                }else{
                    cantitate_produs_din_cos=Double.parseDouble(prod_cantitate.get(i+1));
                }
            }
            cos.setProdus_cantitate(newProdusCantiate);
            cos.setCost_total(cos.getCost_total()-produs.getPret_produs()*cantitate_produs_din_cos);
        }catch(Exception e){ System.out.println(e);}
    }

    public void updateProdusExistentInCos(Cos cos, Produs produs, Integer newCantitate){
        try{
            if(produs==null){
                return;
            }
            Integer cantitate_prod_existenta=0;
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String newProdusCantiate="";
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                if(! (prod_cantitate.get(i).equals(produs.getId_produs()))){
                    newProdusCantiate+=","+prod_cantitate.get(i)+"-"+prod_cantitate.get(i+1);
                }else {
                    cantitate_prod_existenta=Integer.parseInt(prod_cantitate.get(i+1));
                    if(!newCantitate.equals(0)){
                        newProdusCantiate+=","+prod_cantitate.get(i)+"-"+newCantitate.toString();
                    }
                }
            }
            cos.setProdus_cantitate(newProdusCantiate);
            cos.setCost_total(cos.getCost_total()-produs.getPret_produs()*cantitate_prod_existenta
                    + produs.getPret_produs()*newCantitate);
        }catch(Exception e){ System.out.println(e);}
    }

    public boolean comandaExista(final Connection connection, final String id_comanda){
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where id_comanda='"+id_comanda+"';");
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

    public void updateCantitateProdusInDB(final Connection connection, final Cos cos){
        try{
            if(cos==null){
                return;
            }
            List<String> prod_cantitate=new ArrayList<>();
            String produs_cantitate=cos.getProdus_cantitate();
            String[] vals=produs_cantitate.split(",");
            for(String v:vals){
                String[] p = v.split("-");
                for(String str : p ){
                    String[] s=str.split("\\s+");
                    for(String a:s){
                        if(!a.equals("")) { prod_cantitate.add(a); }
                    }
                }
            }
            for (int i = 0; i < prod_cantitate.size(); i+=2) {
                String id_produs=prod_cantitate.get(i);
                Produs produs=DBFunctions.getProdusFromDB(connection,id_produs);
                Integer newCantitate=produs.getCantitate_produs()-Integer.parseInt(prod_cantitate.get(i+1));
                DBFunctions.setCantitate_produsInDB(connection,id_produs,newCantitate);
            }

        }catch(Exception e){ System.out.println(e);}
    }

    public void setMoneyInDB(final Connection connection, final String newMoney){
        String[] columnsToUpdate={"money"};
        String[] newVals={newMoney};
        String[] filterArgs={"user_name = '"+this.getUser_name()+"'"};
        DBFunctions.updateTable(connection,"user",columnsToUpdate,newVals,filterArgs);
    }
    // id_comanda is valid
    public void plaseazaComanda(final Connection connection, final String id_comanda,
                                final Cos cos, final String adresa){
        if(comandaExista(connection,id_comanda)){
            return;
        }
        LocalDateTime now=LocalDateTime.now();
        String data_crearii=now.toString();
        String[] userData={id_comanda,cos.getId_cos(),adresa,data_crearii,Comanda.GATA_DE_LIVRARE,""};
        DBFunctions.insertIntoTable(connection,"comanda",userData);
        updateCantitateProdusInDB(connection,cos);
        Double newMoney=Double.parseDouble(this.getMoneyFromDB(connection))-cos.getCost_total();
        this.setMoneyInDB(connection,newMoney.toString());
        cos.setProdus_cantitate("");
        cos.setCost_total(0.0);
    }

    public List<Comanda> afiseazaComenzileEfectuate(final Connection connection, final Cos cos){
        List<Comanda> comenziEfectuate=new ArrayList<>();
        try {
            Statement statement= connection.createStatement();
            final String query=String.format( "select * from comanda where id_cos='"+cos.getId_cos()+"';");
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
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
