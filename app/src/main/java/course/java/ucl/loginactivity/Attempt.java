package course.java.ucl.loginactivity;

public class Attempt
{
    private String userName;
    private int score;


    public Attempt() {}

    public Attempt(String userName, int score)
    {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
