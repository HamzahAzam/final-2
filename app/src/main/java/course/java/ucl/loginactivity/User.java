package course.java.ucl.loginactivity;


public class User {
    private String userName;
    private String email;
    private String password;
    private int loggedIn;

    public User() {
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public int getLogged() { return loggedIn; }
    public void setLoggedIn(int loggedIn) { this.loggedIn = loggedIn; }


    public void setPassword(String password) {
        this.password = password;
    }
}
