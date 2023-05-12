package sokrous.rtracker.model;

import java.io.IOException;
import java.io.Serializable;

public class User implements Serializable {

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
