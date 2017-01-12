package course.java.ucl.loginactivity;

public class TrueFalse {
    private String mQuestion;
    // this variable will hold a resource ID for a string

    private boolean mTrueQuestion;
    
    public TrueFalse(String question, boolean trueQuestion) {
		mQuestion = question;
		mTrueQuestion = trueQuestion;
	}

	public TrueFalse() {
		mQuestion = "";
		mTrueQuestion = false;
	}


	public String getQuestion() {
		return mQuestion;
	}

	public void setQuestion(String question) {
		mQuestion = question;
	}

	public boolean isTrueQuestion() {
		return mTrueQuestion;
	}

	public void setTrueQuestion(boolean trueQuestion) {
		mTrueQuestion = trueQuestion;
	}

}
