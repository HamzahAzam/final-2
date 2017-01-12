package course.java.ucl.loginactivity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuInflater;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import static android.R.attr.data;


public class QuizActivity extends Fragment
{

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    Button mTrueButton;
    Button mFalseButton;
    Button mCheatButton;
    Button mskipButton;
    TextView m_userName;
        int position=0;

    static boolean mIsCheater=false;
    static int cheaterPosition = -1;
    boolean mAnswerIsTrue = true;

    TextView mQuestionTextView;

    int mCurrentIndex = 0;

    public static final String EXTRA_ANSWER_IS_TRUE = "tfquiz.ANSWER_IS_TRUE";
    public static final String EXTRA_ANSWER_SHOWN = "tfquiz.ANSWER_SHOWN";

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        getActivity().setResult(Activity.RESULT_OK, data);
    }


    private void updateQuestion() {
        String question = questionsList.getAnswerKey().get(position).getQuestion();
        mQuestionTextView.setText(question);
    }

     public void checkAnswer(boolean userPressedTrue)
     {
        boolean answerIsTrue = questionsList.getAnswerKey().get(position).isTrueQuestion();

        int messageResId = 0;

        if (mIsCheater) {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.judgment_toast;

            } else {
                messageResId = R.string.incorrect_judgement_toast;
            }
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }


        questionsList.attemptAddedToList = false;
        questionsList.lastAnswerCorrect = (userPressedTrue == answerIsTrue);

        if(mIsCheater) {
            questionsList.lastAnswerCorrect = false;
        }
        questionsList.positionLastAttempt = position;
        Toast.makeText(getActivity(), "Your answer has been registered, please attempt another question", Toast.LENGTH_SHORT)
                .show();
        getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final EditText et_Username, et_Password, et_Email;

        View rootView = inflater.inflate(R.layout.activity_quiz,
                container, false);

        if (savedInstanceState == null) {
            // first startup, so the answer must not
            // be shown yet
            setAnswerShownResult(false);

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActivity().getActionBar();
            actionBar.setSubtitle("TFFTT");
        }*/

            if (cheaterPosition != position) mIsCheater = false;
            mQuestionTextView = (TextView) rootView.findViewById(R.id.question_text_view);

            String userName = getArguments().getString("USER");
            position = getArguments().getInt("position");

            m_userName = (TextView) rootView.findViewById(R.id.quiz_user);
            m_userName.setText("You are taking this quiz as " + userName);

            mTrueButton = (Button) rootView.findViewById(R.id.true_button);
            mTrueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(true);
                    getFragmentManager().popBackStackImmediate();
                }
            });

            mFalseButton = (Button) rootView.findViewById(R.id.false_button);
            mFalseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(false);
                    getFragmentManager().popBackStackImmediate();
                }
            });

            mCheatButton = (Button) rootView.findViewById(R.id.cheat_button);
            mCheatButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    Log.d(TAG, "cheat button clicked");
                    cheaterPosition = position;
                    mIsCheater = true;

                    if (mAnswerIsTrue) {
                        Toast.makeText(getActivity(), "The answer was true!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "The answer was false!", Toast.LENGTH_LONG).show();
                    }
                    setAnswerShownResult(true);

                    checkAnswer(true);

                    Log.d(TAG, "cheat button clicked");
                    getFragmentManager().popBackStackImmediate();
                }
            });

            mskipButton = (Button) rootView.findViewById(R.id.skip_button);
            mskipButton.setOnClickListener(new View.OnClickListener() {
                FragmentManager manager = getFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_container);

                @Override
                public void onClick(View v) {
                    Log.d(TAG, "cheat button clicked");
                    cheaterPosition = position;
                    mIsCheater = true;

                    setAnswerShownResult(true);

                    checkAnswer(true);

                    Log.d(TAG, "cheat button clicked");
                    getFragmentManager().popBackStackImmediate();

                }
            });

            if (savedInstanceState != null) {
                mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            }

            updateQuestion();
            return rootView;
        }
        return rootView;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_quiz, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}