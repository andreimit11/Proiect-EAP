package users;


public class UserFactory {
    public static final String MANAGER="manager";
    public static final String LIVRATOR="livrator";
    public static final String CUMPARATOR="cumparator";
    // factory design pattern
    public static User createUser(final String user_name, final String user_password, final String user_type){
        return createUser(user_name,user_password,user_type,"","","","");
    }

    public static User createUser(final String user_name, final String user_password, final String user_type,
                                  final String nume, final String preunume, final String email, final String money){
        if(user_type.equals(MANAGER)){
            return new Manager(user_name,user_password,nume,preunume,email,money);
        }
        if(user_type.equals(LIVRATOR)){
            return new Livrator(user_name,user_password,nume,preunume,email,money);
        }
        if(user_type.equals(CUMPARATOR)){
            return new Cumparator(user_name,user_password,nume,preunume,email,money);
        }
        return null;
    }
}
