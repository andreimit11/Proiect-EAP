package TestApp;

import com.mysql.cj.xdevapi.DbDoc;
import comanda.Comanda;
import cos.Cos;
import db.DBConnector;
import db.DBFunctions;
import products.Produs;
import users.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.ServiceConfigurationError;

public class AppWithoutGui {
    public static String chooseUserType(String op){
        if(op.equals("1")){
            return UserFactory.MANAGER;
        }else if(op.equals("2")){
            return UserFactory.LIVRATOR;
        }else if(op.equals("3")){
            return UserFactory.CUMPARATOR;
        }
        return null;
    }
    public static String findUserType(final User user){
        if(user instanceof Manager){
            return UserFactory.MANAGER;
        }else if(user instanceof Livrator){
            return UserFactory.LIVRATOR;
        }else if(user instanceof Cumparator){
            return UserFactory.CUMPARATOR;
        }
        return null;
    }
    public static String findUserTypeInDB(final Connection connection, final String user_name){
        try {
            Statement statement=connection.createStatement();
            final String query=String.format( "select user_type from user where user_name='"+user_name+"';");
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                return resultSet.getString(1);
            }
        }catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public static void getUserOptionPane(final Connection connection, final User user){
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.print("Magazin Online\t\t\t\t\t\t  User: "+user.getUser_name()+"\t("+findUserType(user)+")");
        if(user instanceof Cumparator){
            System.out.println("\t\tMoney: "+((Cumparator)user).getMoneyFromDB(connection));
        }else System.out.println();
        System.out.println("\t\t\t\t\t\t\t\t\t  1 - change password ");
        System.out.println("\t\t\t\t\t\t\t\t\t  2 - delete account ");
        System.out.println("\t\t\t\t\t\t\t\t\t  3 - LogOut ");
    }
    public static boolean basicAccountOptions(final Connection connection, final User user,
                                              final String option,Scanner scanner){
        if(option.equals("1")){
            System.out.print("Change password.\nNew password: "); String newPassword=scanner.next();
            user.changeUserPassword(connection, newPassword); System.out.println("Password changed!");
        }else if(option.equals("2")){
            while (true){
                System.out.print("Your account will be deleted! Are you sure?(1-Yes/2-No): "); String op=scanner.next();
                if(op.equals("1")){
                    DBFunctions.deleteAccount(connection, user); System.out.println("Account deleted");
                    return true;
                }else if(op.equals("2")){
                    break;
                } else System.out.println("Invalid option.");
            }
        }else if(option.equals("3")){
            return true;
        }
        return false;
    }
    public static void getManagerView(final Connection connection, final User user){
        while (true){
            getUserOptionPane(connection,user);
            System.out.println("4 - List all products.");
            System.out.println("5 - Change price of a product.");
            System.out.println("6 - Add a new product.");
            System.out.println("7 - Delete a product.");
            System.out.println("Manager rapoarte.");
            System.out.println("8 - Afiseaza comenzi plasate intr-un interval de timp dat. ");
            System.out.println("9 - Afiseaza comenzi in curs de livrare. ");
            Scanner scanner=new Scanner(System.in);
            System.out.print("Option : "); String option=scanner.next();
            boolean logOut = basicAccountOptions(connection,user,option,scanner);
            if(logOut){
                return;
            }
            if(option.equals("4")){ DBFunctions.displayAllProductsFromDB(connection); }
            else if(option.equals("5")){
                System.out.print("Change product price.\nId_produs: "); String id_produs=scanner.next();
                System.out.print("New Price: "); Double newPrice=scanner.nextDouble();
                if(((Manager)user).productExistsInDB(connection,new Produs(id_produs,"",0.0,0))){
                    ((Manager)user).changeProductPrice(connection,id_produs,newPrice);
                    System.out.println("Price changed!");
                }else System.out.println("Product doesn't exists in DB!");
            }else if(option.equals("6")){
                System.out.println("Add new product.");
                System.out.print("id_produs: "); String id_produs=scanner.next();
                System.out.print("nume_produs: "); String nume_produs=scanner.next();
                System.out.print("pret_produs: "); Double pret_produs=scanner.nextDouble();
                System.out.print("cantitate_produs: "); Integer cantitate_produs=scanner.nextInt();
                Produs produs=new Produs(id_produs,nume_produs,pret_produs,cantitate_produs);
                if((user instanceof Manager) && ((Manager) user).addProductToDB(connection,produs)){
                    System.out.println("The product was added to DB");
                } else System.out.println("Product already exists!");
            }else if(option.equals("7")){
                System.out.print("Delete a product.\nid_produs: "); String id_produs=scanner.next();
                ((Manager)user).deleteProduct(connection,id_produs);
            }else if(option.equals("8")){
                System.out.println("Read startDateTime: "); LocalDateTime a = DBFunctions.readDateTime(scanner);
                System.out.println("Read endDateTime: "); LocalDateTime b = DBFunctions.readDateTime(scanner);
                List<Comanda> comenzi=DBFunctions.getComenziEfectuateInInterval(connection,a,b);
                if(comenzi!=null){
                    for(Comanda c : comenzi){
                        System.out.println("ID: "+c.getId_comanda()+" Id_cos: "+c.getId_cos()+" Adresa: "+c.getAdresa()+
                                " Data crearii: "+c.getData_crearii()+" STATUS: "+c.getStatus()+
                                " Livrator: "+c.getLivrator());
                    }
                }else {
                    System.out.println("Nu sunt comenzi plasate in intervalul dat!");
                }
            }else if(option.equals("9")){
                List<Comanda> comenzi=DBFunctions.getComenziInCursDeLivrare(connection);
                if(comenzi!=null){
                    for(Comanda c : comenzi){
                        System.out.println("ID: "+c.getId_comanda()+" Id_cos: "+c.getId_cos()+" Adresa: "+c.getAdresa()+
                                " Data crearii: "+c.getData_crearii()+" STATUS: "+c.getStatus()+
                                " Livrator: "+c.getLivrator());
                    }
                }else {
                    System.out.println(("Nu exista comenzi in curs de livrare!"));
                }
            }
        }
    }
    public static void getLivratorView(final Connection connection, final User user){
        while (true){
            getUserOptionPane(connection,user);
            System.out.println("4 - Afiseaza lista de comenzi gata de livrare. ");
            System.out.println("5 - Preia o comanda. ");
            System.out.println("6 - Set comanda livrata cu succes. ");
            Scanner scanner=new Scanner(System.in);
            System.out.print("Option : "); String option=scanner.next();
            boolean logOut = basicAccountOptions(connection,user,option,scanner);
            if(logOut){
                return;
            }
            if(option.equals("4")){
                List<Comanda> comenziGataDeLivrare=DBFunctions.getComenziGataDeLivrare(connection);
                if(comenziGataDeLivrare!=null){
                    for(Comanda c : comenziGataDeLivrare){
                        System.out.println("ID: "+c.getId_comanda()+" Id_cos: "+c.getId_cos()+" Adresa: "+
                                c.getAdresa()+" Data_crearii: "+c.getData_crearii()+"\t\tSTATUS: "+c.getStatus());
                    }
                }
            }else if(option.equals("5")){
                System.out.println("Preia o comanda. ");
                String id_comanda;
                while (true){
                    System.out.print("id_comanda: "); id_comanda=scanner.next();
                    if(((Livrator)user).isGataDeLivrare(connection,id_comanda)){
                        break;
                    }else {
                        System.out.println("id_comanda invalid! Comanda nu exista sau nu are " +
                                "STATUS = gata de livrare!");
                    }
                }
                if(((Livrator)user).areAlteComenziInCursDeLivrare(connection)){
                    System.out.println("Nu poti prelua alta comanda pana nu o livrezi pe cea existenta!");
                }else {
                    if(id_comanda!=null){
                        ((Livrator)user).preiaComanda(connection,id_comanda);
                        System.out.println("Comanda preluata!");
                    }
                }
            }else if(option.equals("6")){
                ((Livrator)user).setComandaLivrataCuSuccess(connection);
                System.out.println("Comanda livrata cu succes!");
            }
        }
    }
    public static void getCumparatorView(final Connection connection, final User user){
        Cos cos = DBFunctions.loadCosFromDB(connection,user.getUser_name());
        while (true){
            getUserOptionPane(connection,user);
            System.out.println("4 - List all products.");
            System.out.println("5 - Adauga produs in cos.");
            System.out.println("6 - Cost total cos. ");
            System.out.println("7 - Afiseaza produsele din cos. ");
            System.out.println("8 - Elimina un produs din cos. ");
            System.out.println("9 - Modifica cantitatea unui produs din cos. ");
            System.out.println("10 - Plaseaza comanda. ");
            System.out.println("11 - Listeaza comenzile efectuate. ");
            Scanner scanner=new Scanner(System.in);
            System.out.print("Option : "); String option=scanner.next();
            boolean logOut = basicAccountOptions(connection,user,option,scanner);
            if(logOut){
                // option 2 = Delete Account
                if(cos!=null && (! option.equals("2")) ){
                    if(DBFunctions.addCosToDB(connection,cos)){
                        System.out.println("Cos nou adaugat in DB.");
                    }else {
                        DBFunctions.updateCosInDB(connection,cos);
                    }
                }
                return;
            }
            if(option.equals("4")){ DBFunctions.displayAllProductsFromDB(connection); }
            else if(option.equals("5")){
                if(cos == null){
                    System.out.println("Cos inexistent. Creaza un cos nou.");
                    String id_cos;
                    while (true){
                        System.out.print("id_cos: "); id_cos=scanner.next();
                        if(DBFunctions.cosExistsInDB(connection,id_cos) ){
                            System.out.println("Invalid id_cos! Try again.");
                        } else{
                            break;
                        }
                    }
                    cos=new Cos(id_cos,user.getUser_name(),"",0.0);
                    System.out.println("Cos creat!");
                }
                System.out.print("Adauga produs in cos.\nid_produs: "); String id_produs=scanner.next();
                System.out.print("cantitate: "); Integer cantitate=scanner.nextInt();
                Produs produs = DBFunctions.getProdusFromDB(connection,id_produs);
                if(produs!=null && (cantitate <= produs.getCantitate_produs())){
                    ((Cumparator)user).adaugaProdusInCos(cos, produs,cantitate);
                    System.out.println("Produs adaugat.");
                }else{
                    System.out.println("id_produs invalid sau cantiate > cantitatea disponibila!\n" +
                                "Listati toate produsele pentru a introduce date valide.");
                    }
            }else if(option.equals("6")){
                System.out.println("Cost total cos: "+((cos==null)? 0.0 : (cos.getCost_total().toString())));
            }else if(option.equals("7")){
                if(cos!=null){
                    System.out.println("Produse adaugate in cos:");
                    ((Cumparator)user).afiseazaProduseleDinCos(connection,cos);
                }else {
                    System.out.println("Nu exista produse in cos!");
                }
            }else if(option.equals("8")){
                if(cos!=null){
                    System.out.print("Elimina un produs din cos.\nid_produs: "); String id_produs=scanner.next();
                    ((Cumparator)user).eliminaProdusDinCos(connection,cos,id_produs);
                }
            }else if (option.equals("9")){
                System.out.println("Modifica cantitatea unui produs din cos.");
                System.out.print("id_produs: "); String id_produs=scanner.next();
                System.out.print("cantitate: "); Integer cantitate=scanner.nextInt();
                Produs produs= DBFunctions.getProdusFromDB(connection,id_produs);
                ((Cumparator)user).updateProdusExistentInCos(cos,produs,cantitate);
            }else if(option.equals("10")){
                if(cos!=null){
                    String id_comanda=null;
                    while (true){
                        System.out.print("id_comanda: "); id_comanda=scanner.next();
                        if(((Cumparator)user).comandaExista(connection,id_comanda)){
                            System.out.println("id_comanda invalid!");
                        }else {
                            break;
                        }
                    }
                    System.out.print("Adresa: "); String adresa=scanner.next();
                    if(id_comanda!=null){
                        if(DBFunctions.addCosToDB(connection,cos)){
                            System.out.println("Cos nou adaugat in DB.");
                        }else {
                            DBFunctions.updateCosInDB(connection,cos);
                        }
                        ((Cumparator)user).plaseazaComanda(connection,id_comanda,cos,adresa);
                    }
                }
            }else if(option.equals("11")){
                if(cos!=null){
                    List<Comanda> comenziEfectuate=((Cumparator)user).afiseazaComenzileEfectuate(connection,cos);
                    for(Comanda c:comenziEfectuate){
                        System.out.println("ID: "+c.getId_comanda()+" Adresa: "+c.getAdresa()
                                +" Data_crearii: "+c.getData_crearii()+"\t\tSTATUS: "+c.getStatus());
                    }
                }
            }
        }
    }
    public static void getUserView(final Connection connection, final User user){
        if(user instanceof Manager){
            getManagerView(connection,user);
        }
        if(user instanceof Livrator){
            getLivratorView(connection,user);
        }
        if(user instanceof Cumparator){
            getCumparatorView(connection,user);
        }
    }
    public static void LogIn_Frame(){
        DBConnector dbConnector=new DBConnector("jdbc:mysql://localhost:3306/magazin_online");
        Scanner scanner=new Scanner(System.in);
        System.out.println("-----------------------------------------------------------------------");
        System.out.print("Username: ");   String user_name=scanner.next();
        System.out.print("Password: ");   String user_password=scanner.next();
        if(DBFunctions.logIn(dbConnector.getConnection(),user_name, user_password)){
                System.out.println("LogIn successful!");
                String user_type=findUserTypeInDB(dbConnector.getConnection(),user_name);
                User user= UserFactory.createUser(user_name,user_password,user_type);
                getUserView(dbConnector.getConnection(),user);
        }else {
            System.out.println("Wrong username or password!");
            try { dbConnector.getConnection().close(); }catch (SQLException e){ System.out.println(e); }
        }
    }
    public static void createAnAccountFrame(){
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Create an account");
        Scanner scanner=new Scanner(System.in);
        System.out.print("Username: "); String user_name=scanner.next();
        System.out.print("Password: "); String user_password=scanner.next();
        System.out.print("Select User_type(1-manager 2-livrator 3-cumparator): ");
        String op=scanner.next(); String user_type=chooseUserType(op);
        System.out.print("Nume: "); String nume=scanner.next();
        System.out.print("Prenume: "); String prenume=scanner.next();
        System.out.print("Email: "); String email=scanner.next();
        String money="";
        if(user_type.equals(UserFactory.CUMPARATOR)){
            System.out.print("Money in account: "); money=scanner.next();
        }
        User user=UserFactory.createUser(user_name,user_password,user_type,nume,prenume,email,money);
        DBConnector dbConnector=new DBConnector("jdbc:mysql://localhost:3306/magazin_online");
        if(DBFunctions.createAccount(dbConnector.getConnection(),user)){
            System.out.println("Account Created with success");
        }else{
            System.out.println("Account already exists");
            try { dbConnector.getConnection().close(); }catch (SQLException e){ System.out.println(e); }
        }
    }

    public static void main(String[] args) {

        while (true){
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("Magazin Online\n1-Log In\n2-Create an account\n3-Exit");
            Scanner scanner=new Scanner(System.in);
            String option = scanner.next();
            if(option.equals("3")){ break;  }
            if(option.equals("1")){
                LogIn_Frame();
            }else if(option.equals("2")){
                createAnAccountFrame();
            } else System.out.println("Invalid option!");
        }
    }
}
