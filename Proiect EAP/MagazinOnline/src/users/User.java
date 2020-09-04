package users;

import db.DBFunctions;

import java.sql.Connection;

public abstract class User {
    private String user_name;
    private String user_password;
    private String nume;
    private String prenume;
    private String email;
    private String money;

    public User(String user_name, String user_password, String nume, String prenume, String email, String money) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.money=money;
    }

    public User(String user_name, String user_password) {
        this(user_name,user_password,"","","","");
    }
    public User(){
        this("","","","","","");
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoney() {
        return money;
    }
    // change the user_password in DB
    public void changeUserPassword(final Connection connection, final String newPassword){
        this.user_password=newPassword;
        String[] columnsToUpdate={"user_password"};
        String[] newVals={newPassword};
        String[] filterArgs={"user_name = '"+this.user_name+"'"};
        DBFunctions.updateTable(connection,"user",columnsToUpdate,newVals,filterArgs);
    }
}
