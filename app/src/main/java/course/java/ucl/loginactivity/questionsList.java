package course.java.ucl.loginactivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class questionsList extends ListFragment{
    private static ArrayList<TrueFalse> qKeyList;
    static boolean attemptAddedToList =true;
    static int positionLastAttempt=0;
    static boolean lastAnswerCorrect = false;
    public int m_Score;
    String m_UserName;

    Button finishBtn;


    // Store Questions attempted and correct answers
    static int index;

    private HashMap<Integer, Boolean> answeredQuestions = new HashMap<Integer, Boolean>();

    public void setQuestionsAttempted(int questionNo, boolean result)
    {
        answeredQuestions.put(questionNo, result);
    }

    void addAnswerToAttemptedQuestions()
    {
        if(!attemptAddedToList)
        {
            if(!answeredQuestions.containsKey(positionLastAttempt))
            {
                answeredQuestions.put(positionLastAttempt, lastAnswerCorrect);
                attemptAddedToList = true;
            }
            attemptAddedToList = true;
        }
    }
    private void setIndex(int index) {
        this.index = index;
    }

    public static ArrayList <TrueFalse> getAnswerKey() {
        return qKeyList;
    }

    static int currentIndex() {
        return index;
    }

    public questionsList() {
        index = 0;
    }

    private String loadJsonFromAsset(String filename, Context context)
    {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex)
        {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    public ArrayList<TrueFalse> getQKeysFromFile(String filename, Context context)
    {
        final ArrayList<TrueFalse> qKeyList = new ArrayList<TrueFalse>();
        try
        {
            // Load data
            String jsonString = loadJsonFromAsset(filename, context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray questionKeys = json.getJSONArray("questions");

            // Get Recipe objects from data
            for (int i = 0; i < questionKeys.length(); i++)
            {
                TrueFalse qKey = new TrueFalse();
                qKey.setQuestion(questionKeys.getJSONObject(i).getString("question"));
                qKey.setTrueQuestion(questionKeys.getJSONObject(i).getBoolean("answer"));
                qKeyList.add(qKey);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return qKeyList;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        qKeyList = getQKeysFromFile("questions.json", getContext());
        QuestionAdapter adapter = new QuestionAdapter(qKeyList);
        setListAdapter(adapter);
        m_UserName  = getArguments().getString("USER");
        m_Score = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ((QuestionAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class QuestionAdapter extends ArrayAdapter<TrueFalse>
    {

        public QuestionAdapter(ArrayList<TrueFalse> qKey)
        {
            super(getActivity(), android.R.layout.simple_list_item_1, qKey);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_question, null);
            }

            finishBtn = (Button) convertView.findViewById(R.id.finishBtn);
            finishBtn.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    getFragmentManager().popBackStackImmediate();
                }
            }
            );

            // configure the view for this question
            TrueFalse qk = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.question_list_title);
            titleTextView.setText(qk.getQuestion());
            titleTextView.setTag(position);
            addAnswerToAttemptedQuestions();
            // start count from top of list-- check how many are correct
            if(position==0){
                m_Score=0;
            }

            if(answeredQuestions.containsKey(position))
            {
                boolean result = answeredQuestions.get(position);
                if (result) {
                    ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.recipe_list_thumbnail);
                    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.tick);
                    thumbnailImageView.setImageBitmap(icon);
                    convertView.setBackgroundColor(Color.GREEN);
                    m_Score++;

                }
                else {
                    ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.recipe_list_thumbnail);
                    Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.incorrect);
                    thumbnailImageView.setImageBitmap(icon);
                    convertView.setBackgroundColor(Color.RED);
                }
            }
            convertView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.setBackgroundColor(Color.BLUE);
                    return false;
                }
            });

            convertView.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    FragmentManager manager = getFragmentManager();
                    Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                    v.setBackgroundColor(Color.GREEN);
                    Bundle b = new Bundle();
                    TextView titleTextView = (TextView) v.findViewById(R.id.question_list_title);
                    int position = (int) titleTextView.getTag();
                    b.putInt("position",position);
                    b.putString("USER",m_UserName);
                    fragment = new QuizActivity();
                    fragment.setArguments(b);
                    manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            });
            return convertView;
        }
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Attempt thisAttempt = new Attempt();
        thisAttempt.setScore(m_Score);
        thisAttempt.setUserName(m_UserName);
        DBHandler db = new DBHandler(getActivity());
        db.addAttempt(thisAttempt);

    }
}