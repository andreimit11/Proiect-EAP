package MainApp;

import db.DBConnector;
import db.DBFunctions;
import products.Produs;
import users.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MagazinOnline {
    private JFrame logInFrame;
    private JFrame createAccountFrame;
    private JFrame managerFrame;
    private JFrame livratorFrame;
    private JFrame cumparatorFrame;
    private User user;
    private DBConnector dbConnector;

    public MagazinOnline() {
        buildCreateAccountFrame();
        buildLoginFrame();
    }

    public void buildLoginFrame(){
        logInFrame=new JFrame("Magazin Online");
        JPanel leftPanel=new JPanel();
        leftPanel.setBounds(0,0,200,640);
        leftPanel.setBackground(Color.gray);
        JPanel loginPanel=new JPanel();
        loginPanel.setBounds(200,0,824,640);
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.black);

        JLabel user_nameLabel=new JLabel("Username:");
        user_nameLabel.setBounds(220,160,80,30);
        user_nameLabel.setForeground(Color.white);
        JTextField user_nameTextField=new JTextField();
        user_nameTextField.setBounds(300,160,100,30);
        user_nameTextField.setToolTipText("Enter your username.");

        JLabel passwordLabel=new JLabel("Password:");
        passwordLabel.setBounds(220,200,80,30);
        passwordLabel.setForeground(Color.white);
        JPasswordField passwordField=new JPasswordField();
        passwordField.setBounds(300,200,100,30);
        passwordField.setToolTipText("Enter your password.");

        JLabel label=new JLabel();
        label.setBounds(220,360,400,100);
        label.setForeground(Color.red);

        JButton logInButton=new JButton("Login");
        logInButton.setBounds(300,240,80,30);
        logInButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dbConnector=new DBConnector("jdbc:mysql://localhost:3306/magazin_online");
                String user_name=user_nameTextField.getText();
                String user_password=new String(passwordField.getPassword());
                if(DBFunctions.logIn(dbConnector.getConnection(),user_name, user_password)){
                    user_nameTextField.setText(null);
                    passwordField.setText(null);
                    label.setText(null);
                    user=DBFunctions.getUserFromDB(dbConnector.getConnection(),user_name);
                    if(user instanceof Manager){
                        buildManagerFrame();
                        logInFrame.setVisible(false);
                    }else if(user instanceof Livrator){
                        buildLivratorFrame();
                        logInFrame.setVisible(false);
                    }else if(user instanceof Cumparator){
                        buildCumparatorFrame();
                        logInFrame.setVisible(false);
                    }
                }else {
                    label.setText("Wrong username or password!");
                    user_nameTextField.setText(null);
                    passwordField.setText(null);
                    try { dbConnector.getConnection().close(); }catch (SQLException ex){ System.out.println(ex); }
                }
            }
        });

        JButton createAnAccountButton=new JButton("Create an account");
        createAnAccountButton.setBounds(220,280,180,30);
        createAnAccountButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccountFrame.setVisible(true);
                logInFrame.setVisible(false);
            }
        });

        loginPanel.add(user_nameLabel); loginPanel.add(user_nameTextField);
        loginPanel.add(passwordLabel); loginPanel.add(passwordField);
        loginPanel.add(logInButton); loginPanel.add(createAnAccountButton); loginPanel.add(label);

        logInFrame.add(leftPanel); logInFrame.add(loginPanel);

        logInFrame.setSize(1024,640);
        logInFrame.setResizable(false);
        logInFrame.setLayout(null);
        logInFrame.setVisible(true);
        logInFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void buildCreateAccountFrame(){
        createAccountFrame=new JFrame("Magazin Online");
        JPanel leftPanel=new JPanel();
        leftPanel.setBounds(0,0,200,640);
        leftPanel.setBackground(Color.gray);
        JPanel createAccountPanel=new JPanel();
        createAccountPanel.setBounds(200,0,824,640);
        createAccountPanel.setLayout(null);
        createAccountPanel.setBackground(Color.black);

        JLabel user_nameLabel=new JLabel("Username:");
        user_nameLabel.setBounds(220,40,80,30);
        user_nameLabel.setForeground(Color.white);
        JTextField user_nameTextField=new JTextField();
        user_nameTextField.setBounds(300,40,150,30);

        JLabel passwordLabel=new JLabel("Password:");
        passwordLabel.setBounds(220,80,80,30);
        passwordLabel.setForeground(Color.white);
        JPasswordField passwordField=new JPasswordField();
        passwordField.setBounds(300,80,150,30);

        JPanel moneyPanel=new JPanel();
        moneyPanel.setBounds(220,280,230,30);
        moneyPanel.setBackground(Color.black);
        moneyPanel.setLayout(null);
        JLabel moneyLabel=new JLabel("Money:");
        moneyLabel.setBounds(0,0,80,30);
        moneyLabel.setForeground(Color.white);
        JTextField moneyTextField=new JTextField();
        moneyTextField.setBounds(80,0,150,30);

        JLabel userTypeLabel=new JLabel("User type:");
        userTypeLabel.setBounds(220,120,80,30);
        userTypeLabel.setForeground(Color.white);
        String userTypes[]={UserFactory.LIVRATOR,UserFactory.CUMPARATOR,UserFactory.MANAGER};
        JComboBox userTypeComboBox=new JComboBox(userTypes);
        userTypeComboBox.setBounds(300,120,150,30);
        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(userTypeComboBox.getItemAt(userTypeComboBox.getSelectedIndex()).equals(UserFactory.CUMPARATOR)){
                    moneyPanel.setVisible(true);
                }else {
                    moneyPanel.setVisible(false);
                }
            }
        });

        JLabel numeLabel=new JLabel("Nume:");
        numeLabel.setBounds(220,160,80,30);
        numeLabel.setForeground(Color.white);
        JTextField numeTextField=new JTextField();
        numeTextField.setBounds(300,160,150,30);

        JLabel prenumeLabel=new JLabel("Prenume:");
        prenumeLabel.setBounds(220,200,80,30);
        prenumeLabel.setForeground(Color.white);
        JTextField prenumeTextField=new JTextField();
        prenumeTextField.setBounds(300,200,150,30);

        JLabel emailLabel=new JLabel("Email:");
        emailLabel.setBounds(220,240,80,30);
        emailLabel.setForeground(Color.white);
        JTextField emailTextField=new JTextField();
        emailTextField.setBounds(300,240,150,30);

        JButton createAccountButton=new JButton("Create account");
        createAccountButton.setBounds(220,400,180,30);
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user_name=user_nameTextField.getText();
                if(user_name==null || user_name.equals("")){
                    JOptionPane.showMessageDialog(createAccountFrame,"User_name cannot be null!","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String user_password=new String(passwordField.getPassword());
                if(user_password.equals("")){
                    JOptionPane.showMessageDialog(createAccountFrame,"Password cannot be null!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String user_type=userTypeComboBox.getItemAt(userTypeComboBox.getSelectedIndex()).toString();
                String nume=numeTextField.getText();
                String prenume=prenumeTextField.getText();
                String email=emailTextField.getText();
                String money="";
                if(user_type.equals(UserFactory.CUMPARATOR)){
                    money=moneyTextField.getText();
                    try{
                        Double m=Double.parseDouble(money);
                    }catch (NumberFormatException nfe){
                        JOptionPane.showMessageDialog(createAccountFrame,"Money value must be a number!",
                                "WARNING", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                User user=UserFactory.createUser(user_name,user_password,user_type,nume,prenume,email,money);
                DBConnector dbConnector=new DBConnector("jdbc:mysql://localhost:3306/magazin_online");
                if(DBFunctions.createAccount(dbConnector.getConnection(),user)){
                    JOptionPane.showMessageDialog(createAccountFrame,"Account created with success!");
                    user_nameTextField.setText(null); passwordField.setText(null); numeTextField.setText(null);
                    prenumeTextField.setText(null); emailTextField.setText(null);
                    if(userTypeComboBox.getItemAt(userTypeComboBox.getSelectedIndex()).equals(UserFactory.CUMPARATOR)){
                        moneyTextField.setText(null);
                    }
                    logInFrame.setVisible(true);
                    createAccountFrame.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(createAccountFrame,"Account already exists","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                    user_nameTextField.setText(null); passwordField.setText(null); numeTextField.setText(null);
                    prenumeTextField.setText(null); emailTextField.setText(null);
                    if(userTypeComboBox.getItemAt(userTypeComboBox.getSelectedIndex()).equals(UserFactory.CUMPARATOR)){
                        moneyTextField.setText(null);
                    }
                    logInFrame.setVisible(true);
                    createAccountFrame.setVisible(false);
                    try { dbConnector.getConnection().close(); }catch (SQLException ex){ System.out.println(ex); }
                }
            }
        });

        JButton cancelButton=new JButton("Cancel");
        cancelButton.setBounds(420,400,80,30);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                user_nameTextField.setText(null); passwordField.setText(null); numeTextField.setText(null);
                prenumeTextField.setText(null); emailTextField.setText(null);
                if(userTypeComboBox.getItemAt(userTypeComboBox.getSelectedIndex()).equals(UserFactory.CUMPARATOR)){
                    moneyTextField.setText(null);
                }
                logInFrame.setVisible(true);
                createAccountFrame.setVisible(false);
            }
        });

        moneyPanel.add(moneyLabel); moneyPanel.add(moneyTextField);

        createAccountPanel.add(user_nameLabel); createAccountPanel.add(user_nameTextField);
        createAccountPanel.add(passwordLabel); createAccountPanel.add(passwordField);
        createAccountPanel.add(userTypeLabel); createAccountPanel.add(userTypeComboBox);
        createAccountPanel.add(numeLabel); createAccountPanel.add(numeTextField);
        createAccountPanel.add(prenumeLabel); createAccountPanel.add(prenumeTextField);
        createAccountPanel.add(emailLabel); createAccountPanel.add(emailTextField);
        createAccountPanel.add(moneyPanel); createAccountPanel.add(createAccountButton);
        createAccountPanel.add(cancelButton);

        moneyPanel.setVisible(false);

        createAccountFrame.add(leftPanel); createAccountFrame.add(createAccountPanel);

        createAccountFrame.setSize(1024,640);
        createAccountFrame.setResizable(false);
        createAccountFrame.setLayout(null);
        createAccountFrame.setVisible(false);
        createAccountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void buildManagerFrame(){
        managerFrame=new JFrame("Magazin Online");
        JPanel leftPanel=new JPanel();
        leftPanel.setBounds(0,0,200,640);
        leftPanel.setLayout(null);
        leftPanel.setBackground(Color.black);
        JPanel rightPanel=new JPanel();
        rightPanel.setBounds(200,0,824,640);
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.gray.brighter());

        JPanel listAllProductsPanel=buildDisplayProductsPanel();
        JPanel comenziInCursDeLivrarePanel=buildComenziInCursDeLivrarePanel();
        JPanel addNewProductPanel=new JPanel();
        JPanel deleteProductPanel=new JPanel();
        JPanel editProductPanel=new JPanel();
        JPanel comenziPlasatePanel=new JPanel();
        JPanel readTimeIntervalPanel=new JPanel();

        JPanel changePasswordPanel=new JPanel();
        changePasswordPanel.setBounds(200,100,320,300);
        changePasswordPanel.setBackground(Color.gray.brighter());
        changePasswordPanel.setLayout(null);
        JLabel l1=new JLabel("Old password:");
        l1.setBounds(0,0,100,30);
        JPasswordField p1=new JPasswordField();
        p1.setBounds(100,0,100,30);
        JLabel l2=new JLabel("New password:");
        l2.setBounds(0,40,100,30);
        JPasswordField p2=new JPasswordField();
        p2.setBounds(100,40,100,30);
        JButton b1=new JButton("Change password");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword=new String(p1.getPassword());
                String newPassword=new String(p2.getPassword());
                if(newPassword.equals("")){
                    JOptionPane.showMessageDialog(managerFrame,"New password cannot be null!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    p1.setText(null); p2.setText(null);
                    changePasswordPanel.setVisible(false);
                    return;
                }
                if(oldPassword.equals(user.getUser_password())){
                    int a = JOptionPane.showConfirmDialog(managerFrame, "Your password will be changed!" +
                            "Are you sure?");
                    if (a == JOptionPane.YES_OPTION) {
                        user.changeUserPassword(dbConnector.getConnection(), newPassword);
                        p1.setText(null); p2.setText(null);
                        JOptionPane.showMessageDialog(managerFrame,"Password changed!");
                        logInFrame.setVisible(true);
                        managerFrame.setVisible(false);
                    }
                }else{
                    JOptionPane.showMessageDialog(managerFrame,"Wrong old password!","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                }
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        b1.setBounds(0,200,200,30);
        JButton b2=new JButton("Cancel");
        b2.setBounds(220,200,100,30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        changePasswordPanel.add(l1); changePasswordPanel.add(p1); changePasswordPanel.add(l2);
        changePasswordPanel.add(p2); changePasswordPanel.add(b1); changePasswordPanel.add(b2);

        JMenuBar menuBar=new JMenuBar();
        JLabel userLabel=new JLabel(user.getPrenume()+" "+user.getNume()+" ("+user.getUser_name()+")");
        userLabel.setBorder(BorderFactory.createCompoundBorder(userLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userLabel.setForeground(Color.white);
        JLabel userTypeLabel=new JLabel("Manager");
        userTypeLabel.setBorder(BorderFactory.createCompoundBorder(userTypeLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userTypeLabel.setForeground(Color.white);
        JMenu accountMenu=new JMenu("Account");
        accountMenu.setForeground(Color.white);
        accountMenu.setBorder(BorderFactory.createCompoundBorder(accountMenu.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        JMenuItem changePassword=new JMenuItem("Change password");
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                changePasswordPanel.setVisible(true);
            }
        });
        JMenuItem deleteAccount=new JMenuItem("Delete account");
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(managerFrame, "Your account will be deleted!" +
                        "Are you sure?");
                if (a == JOptionPane.YES_OPTION) {
                    DBFunctions.deleteAccount(dbConnector.getConnection(), user);
                    JOptionPane.showMessageDialog(managerFrame,"Account deleted with success!");
                    logInFrame.setVisible(true);
                    managerFrame.setVisible(false);
                }
            }
        });
        JMenuItem logOut=new JMenuItem("LogOut");
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInFrame.setVisible(true);
                managerFrame.setVisible(false);
            }
        });
        accountMenu.add(changePassword); accountMenu.add(deleteAccount); accountMenu.add(logOut);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.setBackground(Color.black);
        menuBar.add(userLabel); menuBar.add(userTypeLabel); menuBar.add(accountMenu);

        addNewProductPanel.setBounds(0,0,824,640);
        addNewProductPanel.setBackground(Color.gray.brighter());
        addNewProductPanel.setLayout(null);
        JLabel id_produsLabel=new JLabel("id_produs:");
        id_produsLabel.setBounds(200,80,140,30);
        JTextField id_produsTextField=new JTextField();
        id_produsTextField.setBounds(340,80,140,30);
        JLabel numeProdusLabel=new JLabel("Nume produs:");
        numeProdusLabel.setBounds(200,120,140,30);
        JTextField numeProdusTextField=new JTextField();
        numeProdusTextField.setBounds(340,120,140,30);
        JLabel pretProdusLabel=new JLabel("Pret:");
        pretProdusLabel.setBounds(200,160,140,30);
        JTextField pretProdusTextField=new JTextField();
        pretProdusTextField.setBounds(340,160,140,30);
        JLabel cantitateProdusLabel=new JLabel("Cantitate(nr.buc.):");
        cantitateProdusLabel.setBounds(200,200,140,30);
        JTextField cantitateProdusTextField=new JTextField();
        cantitateProdusTextField.setBounds(340,200,140,30);
        JButton addProductButton=new JButton("Add product");
        addProductButton.setBounds(200,320,140,30);
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_produs=id_produsTextField.getText();
                String nume_produs=numeProdusTextField.getText();
                if(id_produs==null || id_produs.equals("")){
                    JOptionPane.showMessageDialog(managerFrame,"ID_produs cannot be null!","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(nume_produs.equals("")){
                    JOptionPane.showMessageDialog(managerFrame,"Nume produs cannot be null!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Double pret_produs;
                try{
                    pret_produs=Double.parseDouble(pretProdusTextField.getText());
                }catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(managerFrame,"Pret_produs value must be a number!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Integer cantitate_produs;
                try{
                    cantitate_produs=Integer.parseInt(cantitateProdusTextField.getText());
                }catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(managerFrame,"Cantitate_produs value must integer!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Produs produs=new Produs(id_produs,nume_produs,pret_produs,cantitate_produs);
                if((user instanceof Manager) && ((Manager) user).addProductToDB(dbConnector.getConnection(),produs)){
                    JOptionPane.showMessageDialog(managerFrame,"Product added to DB.");
                    id_produsTextField.setText(null); numeProdusTextField.setText(null);
                    pretProdusTextField.setText(null); cantitateProdusTextField.setText(null);
                    addNewProductPanel.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(managerFrame,"Product already exists!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                id_produsTextField.setText(null); numeProdusTextField.setText(null);
                pretProdusTextField.setText(null); cantitateProdusTextField.setText(null);
            }
        });
        JButton c=new JButton("Cancel");
        c.setBounds(380,320,100,30);
        c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id_produsTextField.setText(null); numeProdusTextField.setText(null);
                pretProdusTextField.setText(null); cantitateProdusTextField.setText(null);
                addNewProductPanel.setVisible(false);
            }
        });
        addNewProductPanel.add(id_produsLabel); addNewProductPanel.add(id_produsTextField);
        addNewProductPanel.add(numeProdusLabel); addNewProductPanel.add(numeProdusTextField);
        addNewProductPanel.add(pretProdusLabel); addNewProductPanel.add(pretProdusTextField);
        addNewProductPanel.add(cantitateProdusLabel); addNewProductPanel.add(cantitateProdusTextField);
        addNewProductPanel.add(addProductButton); addNewProductPanel.add(c);

        /////////////////////////////////////
        deleteProductPanel.setBounds(0,0,600,400);
        deleteProductPanel.setBackground(Color.gray.brighter());
        deleteProductPanel.setLayout(null);
        JLabel idDeleteProdLabel=new JLabel("ID produs:");
        idDeleteProdLabel.setBounds(20,20,100,30);
        JTextField idDeleteProdTextField=new JTextField();
        idDeleteProdTextField.setBounds(120,20,100,30);
        JButton deleteProdButton=new JButton("Delete");
        deleteProdButton.setBounds(20,100,100,30);
        deleteProdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_produs=idDeleteProdTextField.getText();
                ((Manager)user).deleteProduct(dbConnector.getConnection(),id_produs);
                idDeleteProdTextField.setText(null);
                deleteProductPanel.setVisible(false);
            }
        });
        JButton deleteProdCancelButton=new JButton("Cancel");
        deleteProdCancelButton.setBounds(140,100,80,30);
        deleteProdCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idDeleteProdTextField.setText(null);
                deleteProductPanel.setVisible(false);
            }
        });
        deleteProductPanel.add(idDeleteProdLabel); deleteProductPanel.add(idDeleteProdTextField);
        deleteProductPanel.add(deleteProdButton); deleteProductPanel.add(deleteProdCancelButton);
        //////////////////////////////////////

        //////////////////////////////////////////////////////
        editProductPanel.setBounds(100,20,400,400);
        editProductPanel.setBackground(Color.gray.brighter());
        editProductPanel.setLayout(null);
        JLabel idProdEditLabel=new JLabel("ID produs:");
        idProdEditLabel.setBounds(10,10,100,30);
        JTextField idProdEditTextField=new JTextField();
        idProdEditTextField.setBounds(110,10,100,30);
        JLabel newPriceEditLabel=new JLabel("New price:");
        newPriceEditLabel.setBounds(10,50,100,30);
        JTextField newPriceEditTextField=new JTextField();
        newPriceEditTextField.setBounds(110,50,100,30);
        JButton editProdButton=new JButton("Edit");
        editProdButton.setBounds(10,90,100,30);
        editProdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_produs=idProdEditTextField.getText();
                Double newPrice;
                try{
                    newPrice=Double.parseDouble(newPriceEditTextField.getText());
                }catch (NumberFormatException nfe){
                    JOptionPane.showMessageDialog(managerFrame,"Pret_produs value must be a number!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if(((Manager)user).productExistsInDB(dbConnector.getConnection(),new Produs(id_produs,"",0.0,0))){
                    ((Manager)user).changeProductPrice(dbConnector.getConnection(),id_produs,newPrice);
                    JOptionPane.showMessageDialog(managerFrame,"Product updated.");
                    idProdEditTextField.setText(null); newPriceEditTextField.setText(null);
                    editProductPanel.setVisible(false);
                }else {
                    JOptionPane.showMessageDialog(managerFrame,"Product doesn't exists in DB!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                }
                idProdEditTextField.setText(null); newPriceEditTextField.setText(null);
            }
        });
        JButton cancelEditProdButton=new JButton("Cancel");
        cancelEditProdButton.setBounds(130,90,80,30);
        cancelEditProdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idProdEditTextField.setText(null); newPriceEditTextField.setText(null);
                editProductPanel.setVisible(false);
            }
        });
        editProductPanel.add(idProdEditLabel); editProductPanel.add(idProdEditTextField);
        editProductPanel.add(newPriceEditLabel); editProductPanel.add(newPriceEditTextField);
        editProductPanel.add(editProdButton); editProductPanel.add(cancelEditProdButton);
        /////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////
        readTimeIntervalPanel.setBounds(20,20,400,400);
        readTimeIntervalPanel.setBackground(Color.gray.brighter());
        readTimeIntervalPanel.setLayout(null);
        ///////////////////////////////////////////////////////////

        JButton displayProducts=new JButton("List all products");
        displayProducts.setBounds(0,0,200,30);
        displayProducts.setBackground(Color.white);
        displayProducts.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                listAllProductsPanel.setVisible(true);
            }
        });

        JButton editProduct=new JButton("Edit a product");
        editProduct.setBounds(0,30,200,30);
        editProduct.setBackground(Color.white);
        editProduct.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                editProductPanel.setVisible(true);
            }
        });

        JButton addNewProduct=new JButton("Add a new product");
        addNewProduct.setBounds(0,60,200,30);
        addNewProduct.setBackground(Color.white);
        addNewProduct.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                addNewProductPanel.setVisible(true);
            }
        });

        JButton deleteProduct=new JButton("Delete a product");
        deleteProduct.setBounds(0,90,200,30);
        deleteProduct.setBackground(Color.white);
        deleteProduct.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                deleteProductPanel.setVisible(true);
            }
        });

        JLabel managerRapoarte=new JLabel("Manager rapoarte");
        managerRapoarte.setBounds(0,120,200,30);
        managerRapoarte.setForeground(Color.white);

        JButton comenziPlasate=new JButton("Comenzi plasate");
        comenziPlasate.setBounds(0,150,200,30);
        comenziPlasate.setBackground(Color.white);
        comenziPlasate.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(comenziInCursDeLivrarePanel.isVisible()){
                    comenziInCursDeLivrarePanel.setVisible(false);
                }
                readTimeIntervalPanel.setVisible(true);
            }
        });

        JButton comenziInCursDeLivrare=new JButton("Comenzi in curs de livrare");
        comenziInCursDeLivrare.setBounds(0,180,200,30);
        comenziInCursDeLivrare.setBackground(Color.white);
        comenziInCursDeLivrare.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(listAllProductsPanel.isVisible()){
                    listAllProductsPanel.setVisible(false);
                }
                if(addNewProductPanel.isVisible()){
                    addNewProductPanel.setVisible(false);
                }
                if(deleteProductPanel.isVisible()){
                    deleteProductPanel.setVisible(false);
                }
                if(editProductPanel.isVisible()){
                    editProductPanel.setVisible(false);
                }
                if(readTimeIntervalPanel.isVisible()){
                    readTimeIntervalPanel.setVisible(false);
                }
                comenziInCursDeLivrarePanel.setVisible(true);
            }
        });

        leftPanel.add(displayProducts); leftPanel.add(editProduct); leftPanel.add(addNewProduct);
        leftPanel.add(deleteProduct); leftPanel.add(managerRapoarte); leftPanel.add(comenziPlasate);
        leftPanel.add(comenziInCursDeLivrare);

        rightPanel.add(changePasswordPanel); changePasswordPanel.setVisible(false);

        rightPanel.add(listAllProductsPanel); listAllProductsPanel.setVisible(false);

        rightPanel.add(comenziInCursDeLivrarePanel); comenziInCursDeLivrarePanel.setVisible(false);

        rightPanel.add(addNewProductPanel); addNewProductPanel.setVisible(false);

        rightPanel.add(deleteProductPanel); deleteProductPanel.setVisible(false);

        rightPanel.add(editProductPanel); editProductPanel.setVisible(false);

        rightPanel.add(readTimeIntervalPanel); readTimeIntervalPanel.setVisible(false);

        managerFrame.add(leftPanel); managerFrame.add(rightPanel);

        managerFrame.setJMenuBar(menuBar);
        managerFrame.setSize(1024,640);
        managerFrame.setLayout(null);
        managerFrame.setVisible(true);
        managerFrame.setResizable(false);
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void buildLivratorFrame(){
        livratorFrame=new JFrame("Magazin Online");
        JPanel leftPanel=new JPanel();
        leftPanel.setBounds(0,0,200,640);
        leftPanel.setLayout(null);
        leftPanel.setBackground(Color.black);
        JPanel rightPanel=new JPanel();
        rightPanel.setBounds(200,0,824,640);
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.gray.brighter());

        JPanel comenziGataDeLivrarePanel=new JPanel();
        JPanel preiaComandaPanel=new JPanel();

        JPanel changePasswordPanel=new JPanel();
        changePasswordPanel.setBounds(200,100,320,300);
        changePasswordPanel.setBackground(Color.gray.brighter());
        changePasswordPanel.setLayout(null);
        JLabel l1=new JLabel("Old password:");
        l1.setBounds(0,0,100,30);
        JPasswordField p1=new JPasswordField();
        p1.setBounds(100,0,100,30);
        JLabel l2=new JLabel("New password:");
        l2.setBounds(0,40,100,30);
        JPasswordField p2=new JPasswordField();
        p2.setBounds(100,40,100,30);
        JButton b1=new JButton("Change password");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword=new String(p1.getPassword());
                String newPassword=new String(p2.getPassword());
                if(newPassword.equals("")){
                    JOptionPane.showMessageDialog(livratorFrame,"New password cannot be null!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    p1.setText(null); p2.setText(null);
                    changePasswordPanel.setVisible(false);
                    return;
                }
                if(oldPassword.equals(user.getUser_password())){
                    int a = JOptionPane.showConfirmDialog(livratorFrame, "Your password will be changed!" +
                            "Are you sure?");
                    if (a == JOptionPane.YES_OPTION) {
                        user.changeUserPassword(dbConnector.getConnection(), newPassword);
                        p1.setText(null); p2.setText(null);
                        JOptionPane.showMessageDialog(livratorFrame,"Password changed!");
                        logInFrame.setVisible(true);
                        livratorFrame.setVisible(false);
                    }
                }else{
                    JOptionPane.showMessageDialog(livratorFrame,"Wrong old password!","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                }
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        b1.setBounds(0,200,200,30);
        JButton b2=new JButton("Cancel");
        b2.setBounds(220,200,100,30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        changePasswordPanel.add(l1); changePasswordPanel.add(p1); changePasswordPanel.add(l2);
        changePasswordPanel.add(p2); changePasswordPanel.add(b1); changePasswordPanel.add(b2);

        JMenuBar menuBar=new JMenuBar();
        JLabel userLabel=new JLabel(user.getPrenume()+" "+user.getNume()+" ("+user.getUser_name()+")");
        userLabel.setBorder(BorderFactory.createCompoundBorder(userLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userLabel.setForeground(Color.white);
        JLabel userTypeLabel=new JLabel("Livrator");
        userTypeLabel.setBorder(BorderFactory.createCompoundBorder(userTypeLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userTypeLabel.setForeground(Color.white);
        JMenu accountMenu=new JMenu("Account");
        accountMenu.setForeground(Color.white);
        accountMenu.setBorder(BorderFactory.createCompoundBorder(accountMenu.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        JMenuItem changePassword=new JMenuItem("Change password");
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comenziGataDeLivrarePanel.isVisible()){
                    comenziGataDeLivrarePanel.setVisible(false);
                }
                if(preiaComandaPanel.isVisible()){
                    preiaComandaPanel.setVisible(false);
                }
                changePasswordPanel.setVisible(true);
            }
        });
        JMenuItem deleteAccount=new JMenuItem("Delete account");
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(livratorFrame, "Your account will be deleted!" +
                        "Are you sure?");
                if (a == JOptionPane.YES_OPTION) {
                    DBFunctions.deleteAccount(dbConnector.getConnection(), user);
                    JOptionPane.showMessageDialog(livratorFrame,"Account deleted with success!");
                    logInFrame.setVisible(true);
                    livratorFrame.setVisible(false);
                }
            }
        });
        JMenuItem logOut=new JMenuItem("LogOut");
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInFrame.setVisible(true);
                livratorFrame.setVisible(false);
            }
        });
        accountMenu.add(changePassword); accountMenu.add(deleteAccount); accountMenu.add(logOut);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.setBackground(Color.black);
        menuBar.add(userLabel); menuBar.add(userTypeLabel); menuBar.add(accountMenu);

        //////////////////////////////////////////////
        preiaComandaPanel.setBounds(0,0,400,400);
        preiaComandaPanel.setBackground(Color.gray.brighter());
        preiaComandaPanel.setLayout(null);
        JLabel idComandaLabel=new JLabel("ID comanda:");
        idComandaLabel.setBounds(10,10,100,30);
        JTextField idComandaTextField=new JTextField();
        idComandaTextField.setBounds(110,10,100,30);
        JButton preiaComandaButton=new JButton("Preia comanda");
        preiaComandaButton.setBounds(10,60,140,30);
        preiaComandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id_comanda=idComandaTextField.getText();
                if(((Livrator)user).areAlteComenziInCursDeLivrare(dbConnector.getConnection())){
                    JOptionPane.showMessageDialog(livratorFrame,"Nu poti prelua alta comanda " +
                            "pana nu o livrezi pe cea existenta!", "WARNING", JOptionPane.WARNING_MESSAGE);
                    idComandaTextField.setText(null);
                    preiaComandaPanel.setVisible(false);
                    return;
                }
                if(((Livrator)user).isGataDeLivrare(dbConnector.getConnection(),id_comanda)){
                    ((Livrator)user).preiaComanda(dbConnector.getConnection(),id_comanda);
                    JOptionPane.showMessageDialog(livratorFrame,"Comanda preluata!");
                    idComandaTextField.setText(null);
                    preiaComandaPanel.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(livratorFrame,"ID comanda invalid!Comanda nu exista " +
                            "sau nu are STATUS = gata de livrare!", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton cancelPreiaComanda=new JButton("Cancel");
        cancelPreiaComanda.setBounds(160,60,80,30);
        cancelPreiaComanda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idComandaTextField.setText(null);
                preiaComandaPanel.setVisible(false);
            }
        });
        preiaComandaPanel.add(idComandaLabel); preiaComandaPanel.add(idComandaTextField);
        preiaComandaPanel.add(preiaComandaButton); preiaComandaPanel.add(cancelPreiaComanda);
        /////////////////////////////////////////////

        comenziGataDeLivrarePanel.setBounds(40,20,700,500);
        comenziGataDeLivrarePanel.setLayout(new BorderLayout());
        String[][] comenzi=DBFunctions.getComenziGataDeLivrareFromDB(dbConnector.getConnection());
        String coloane[]={"ID","ID Cos","Adresa","Data crearii","STATUS","Livrator"};
        JTable comenziTable=new JTable();
        DefaultTableModel defaultTableModel=new DefaultTableModel(comenzi,coloane){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        comenziTable.setModel(defaultTableModel);
        JScrollPane scrollPaneComenzi=new JScrollPane(comenziTable);
        comenziGataDeLivrarePanel.add(scrollPaneComenzi, BorderLayout.CENTER);

        JButton comenziGataDeLivrare=new JButton("Comenzi gata de livrare");
        comenziGataDeLivrare.setBounds(0,0,200,30);
        comenziGataDeLivrare.setBackground(Color.white);
        comenziGataDeLivrare.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(preiaComandaPanel.isVisible()){
                    preiaComandaPanel.setVisible(false);
                }
                comenziGataDeLivrarePanel.setVisible(true);
            }
        });

        JButton preiaComanda=new JButton("Preia o comanda");
        preiaComanda.setBounds(0,30,200,30);
        preiaComanda.setBackground(Color.white);
        preiaComanda.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(changePasswordPanel.isVisible()){
                    changePasswordPanel.setVisible(false);
                }
                if(comenziGataDeLivrarePanel.isVisible()){
                    comenziGataDeLivrarePanel.setVisible(false);
                }
                preiaComandaPanel.setVisible(true);
            }
        });

        JButton comandaLivrataCuSucces=new JButton("Comanda livrata cu succes");
        comandaLivrataCuSucces.setBounds(0,60,200,30);
        comandaLivrataCuSucces.setBackground(Color.white);
        comandaLivrataCuSucces.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("comanda livrata cu succes!");
            }
        });

        leftPanel.add(comenziGataDeLivrare); leftPanel.add(preiaComanda); leftPanel.add(comandaLivrataCuSucces);

        rightPanel.add(changePasswordPanel); changePasswordPanel.setVisible(false);

        rightPanel.add(comenziGataDeLivrarePanel); comenziGataDeLivrarePanel.setVisible(false);

        rightPanel.add(preiaComandaPanel); preiaComandaPanel.setVisible(false);

        livratorFrame.add(leftPanel); livratorFrame.add(rightPanel);

        livratorFrame.setJMenuBar(menuBar);
        livratorFrame.setSize(1024,640);
        livratorFrame.setLayout(null);
        livratorFrame.setVisible(true);
        livratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void buildCumparatorFrame(){
        cumparatorFrame=new JFrame("Magazin Online");
        JPanel leftPanel=new JPanel();
        leftPanel.setBounds(0,0,200,640);
        leftPanel.setLayout(null);
        leftPanel.setBackground(Color.black);
        JPanel rightPanel=new JPanel();
        rightPanel.setBounds(200,0,824,640);
        rightPanel.setLayout(null);
        rightPanel.setBackground(Color.gray.brighter());

        JPanel changePasswordPanel=new JPanel();
        changePasswordPanel.setBounds(200,100,320,300);
        changePasswordPanel.setBackground(Color.gray.brighter());
        changePasswordPanel.setLayout(null);
        JLabel l1=new JLabel("Old password:");
        l1.setBounds(0,0,100,30);
        JPasswordField p1=new JPasswordField();
        p1.setBounds(100,0,100,30);
        JLabel l2=new JLabel("New password:");
        l2.setBounds(0,40,100,30);
        JPasswordField p2=new JPasswordField();
        p2.setBounds(100,40,100,30);
        JButton b1=new JButton("Change password");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword=new String(p1.getPassword());
                String newPassword=new String(p2.getPassword());
                if(newPassword.equals("")){
                    JOptionPane.showMessageDialog(cumparatorFrame,"New password cannot be null!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                    p1.setText(null); p2.setText(null);
                    changePasswordPanel.setVisible(false);
                    return;
                }
                if(oldPassword.equals(user.getUser_password())){
                    int a = JOptionPane.showConfirmDialog(cumparatorFrame, "Your password will be changed!" +
                            "Are you sure?");
                    if (a == JOptionPane.YES_OPTION) {
                        user.changeUserPassword(dbConnector.getConnection(), newPassword);
                        p1.setText(null); p2.setText(null);
                        JOptionPane.showMessageDialog(cumparatorFrame,"Password changed!");
                        logInFrame.setVisible(true);
                        cumparatorFrame.setVisible(false);
                    }
                }else{
                    JOptionPane.showMessageDialog(cumparatorFrame,"Wrong old password!","WARNING",
                            JOptionPane.WARNING_MESSAGE);
                }
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        b1.setBounds(0,200,200,30);
        JButton b2=new JButton("Cancel");
        b2.setBounds(220,200,100,30);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                p1.setText(null); p2.setText(null);
                changePasswordPanel.setVisible(false);
            }
        });
        changePasswordPanel.add(l1); changePasswordPanel.add(p1); changePasswordPanel.add(l2);
        changePasswordPanel.add(p2); changePasswordPanel.add(b1); changePasswordPanel.add(b2);

        JMenuBar menuBar=new JMenuBar();
        JLabel userLabel=new JLabel(user.getPrenume()+" "+user.getNume()+" ("+user.getUser_name()+")");
        userLabel.setBorder(BorderFactory.createCompoundBorder(userLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userLabel.setForeground(Color.white);
        JLabel userTypeLabel=new JLabel("Cumparator");
        userTypeLabel.setBorder(BorderFactory.createCompoundBorder(userTypeLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        userTypeLabel.setForeground(Color.white);
        JLabel moneyLabel=new JLabel("Money: "+user.getMoney());
        moneyLabel.setBorder(BorderFactory.createCompoundBorder(moneyLabel.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        moneyLabel.setForeground(Color.white);
        JMenu accountMenu=new JMenu("Account");
        accountMenu.setForeground(Color.white);
        accountMenu.setBorder(BorderFactory.createCompoundBorder(accountMenu.getBorder(),
                BorderFactory.createEmptyBorder(0, 50, 0, 50)));
        JMenuItem changePassword=new JMenuItem("Change password");
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePasswordPanel.setVisible(true);
            }
        });
        JMenuItem deleteAccount=new JMenuItem("Delete account");
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(cumparatorFrame, "Your account will be deleted!" +
                        "Are you sure?");
                if (a == JOptionPane.YES_OPTION) {
                    DBFunctions.deleteAccount(dbConnector.getConnection(), user);
                    JOptionPane.showMessageDialog(cumparatorFrame,"Account deleted with success!");
                    logInFrame.setVisible(true);
                    cumparatorFrame.setVisible(false);
                }
            }
        });
        JMenuItem logOut=new JMenuItem("LogOut");
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logInFrame.setVisible(true);
                cumparatorFrame.setVisible(false);
            }
        });
        accountMenu.add(changePassword); accountMenu.add(deleteAccount); accountMenu.add(logOut);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.setBackground(Color.black);
        menuBar.add(userLabel); menuBar.add(userTypeLabel); menuBar.add(moneyLabel); menuBar.add(accountMenu);

        JButton listAllProducts=new JButton("List all products");
        listAllProducts.setBounds(0,0,200,30);
        listAllProducts.setBackground(Color.white);
        listAllProducts.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("List all products!");
            }
        });

        JButton adaugaProdusInCos=new JButton("Adauga produs in cos");
        adaugaProdusInCos.setBounds(0,30,200,30);
        adaugaProdusInCos.setBackground(Color.white);
        adaugaProdusInCos.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Adauga produs in cos!");
            }
        });

        JButton costTotalCos=new JButton("Cost total cos");
        costTotalCos.setBounds(0,60,200,30);
        costTotalCos.setBackground(Color.white);
        costTotalCos.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cost total cos!");
            }
        });

        JButton afiseazaProduseleDinCos=new JButton("Afiseaza produsele din cos");
        afiseazaProduseleDinCos.setBounds(0,90,200,30);
        afiseazaProduseleDinCos.setBackground(Color.white);
        afiseazaProduseleDinCos.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Afiseaza produsele din cos1");
            }
        });

        JButton eliminaProdusDinCos=new JButton("Elimina produs din cos");
        eliminaProdusDinCos.setBounds(0,120,200,30);
        eliminaProdusDinCos.setBackground(Color.white);
        eliminaProdusDinCos.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Elimina un produs din cos.");
            }
        });

        JButton modificaCantitateProdusDinCos=new JButton("Modifica cantitatea unui produs din cos");
        modificaCantitateProdusDinCos.setBounds(0,150,200,30);
        modificaCantitateProdusDinCos.setBackground(Color.white);
        modificaCantitateProdusDinCos.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Modifica cantitatea unui produs din cos");
            }
        });

        JButton plaseazaComanda=new JButton("Plaseaza comanda");
        plaseazaComanda.setBounds(0,180,200,30);
        plaseazaComanda.setBackground(Color.white);
        plaseazaComanda.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Plaseaza comanda!");
            }
        });

        JButton listeazaComenzileEfectuate=new JButton("Listeaza comenzile efectuate");
        listeazaComenzileEfectuate.setBounds(0,210,200,30);
        listeazaComenzileEfectuate.setBackground(Color.white);
        listeazaComenzileEfectuate.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Listeaza comenzile efectuate!");
            }
        });

        leftPanel.add(listAllProducts); leftPanel.add(adaugaProdusInCos); leftPanel.add(costTotalCos);
        leftPanel.add(afiseazaProduseleDinCos); leftPanel.add(eliminaProdusDinCos);
        leftPanel.add(modificaCantitateProdusDinCos); leftPanel.add(plaseazaComanda);
        leftPanel.add(listeazaComenzileEfectuate);

        rightPanel.add(changePasswordPanel);
        changePasswordPanel.setVisible(false);

        cumparatorFrame.add(leftPanel); cumparatorFrame.add(rightPanel);

        cumparatorFrame.setJMenuBar(menuBar);
        cumparatorFrame.setSize(1024,640);
        cumparatorFrame.setLayout(null);
        cumparatorFrame.setVisible(true);
        cumparatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel buildDisplayProductsPanel(){
        JPanel listAllProductsPanel=new JPanel();
        listAllProductsPanel.setBounds(40,20,700,500);
        listAllProductsPanel.setLayout(new BorderLayout());
        String[][] data=DBFunctions.getProducts(dbConnector.getConnection());
        String columns[]={"ID","Name","Price","Quantity"};
        JTable listProductsTable=new JTable();
        DefaultTableModel tableModel=new DefaultTableModel(data,columns){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listProductsTable.setModel(tableModel);
        JScrollPane scrollPane=new JScrollPane(listProductsTable);
        listAllProductsPanel.add(scrollPane, BorderLayout.CENTER);
        return listAllProductsPanel;
    }

    public JPanel buildComenziInCursDeLivrarePanel(){
        JPanel comenziInCursDeLivrarePanel=new JPanel();
        comenziInCursDeLivrarePanel.setBounds(40,20,700,500);
        comenziInCursDeLivrarePanel.setLayout(new BorderLayout());
        String[][] comenzi=DBFunctions.getComenziInCursDeLivrareFromDB(dbConnector.getConnection());
        String coloane[]={"ID","ID Cos","Adresa","Data crearii","STATUS","Livrator"};
        JTable comenziInCursTable=new JTable();
        DefaultTableModel defaultTableModel=new DefaultTableModel(comenzi,coloane){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        comenziInCursTable.setModel(defaultTableModel);
        JScrollPane scrollPaneComenzi=new JScrollPane(comenziInCursTable);
        comenziInCursDeLivrarePanel.add(scrollPaneComenzi, BorderLayout.CENTER);
        return comenziInCursDeLivrarePanel;
    }
}
