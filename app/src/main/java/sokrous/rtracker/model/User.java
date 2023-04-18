package sokrous.rtracker.model;

public class User {

    public String userName;
    public String userPassword;
    public String mobileNo;
    public String email;

    public User(){

    }

    public User (String userName, String password, String mobileNo, String email){
        this.userName = userName;
        this.userPassword = password;
        this.mobileNo = mobileNo;
        this.email = email;
    }
}
