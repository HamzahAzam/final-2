package course.java.ucl.loginactivity;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

/**
 * Created by azam1 on 30/12/2016.
 */

public class LoggedInUsersList extends ListFragment
{
    private static ArrayList<TrueFalse> qKeyList;

    String m_Name;

    private static HashMap<String, questionsList> quizStarted = new HashMap<String, questionsList>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        m_Name  = getArguments().getString("USER");

        if(!quizStarted.containsKey(m_Name))
        {
            Fragment fragment = new questionsList();
            quizStarted.put(m_Name, (questionsList)fragment);
        }

        ArrayList<String> loggedInList =new ArrayList<String>(quizStarted.keySet());
        ArrayList<String> list = new ArrayList<String>();
        list.add("");
        list.addAll(loggedInList);
        UserDetailsAdapter adapter = new UserDetailsAdapter(list);
        setListAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ((UserDetailsAdapter)getListAdapter()).notifyDataSetChanged();
    }


    private class UserDetailsAdapter extends ArrayAdapter<String>
    {

        public UserDetailsAdapter(ArrayList<String> loggedIn)
        {
            super(getActivity(), android.R.layout.simple_list_item_1, loggedIn);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // if we weren't given a view, inflate one
            final FragmentManager manager = getFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.fragment_container);

            if (null == convertView && position!=0)
            {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_loggedin_users, null);
            }
            else if (position==0)
            {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.header_loggedin, null);
                Button finishBtn = (Button) convertView.findViewById(R.id.backBtn);
                finishBtn.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        getFragmentManager().popBackStackImmediate();
                    }
                }
                );

                TextView titleTextView =
                        (TextView) convertView.findViewById(R.id.listHeader);
                titleTextView.setText("Select from users currently logged in to start quiz:");
                return convertView;
            }

            ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.users_list_thumbnail);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person);
            thumbnailImageView.setImageBitmap(icon);

            // configure the view for this question
            String user = getItem(position);
            final TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.users_list_title);
            titleTextView.setText(user);

            fragment = null;
            if(quizStarted.containsKey(titleTextView.getText().toString()))
            {
                fragment = quizStarted.get(titleTextView.getText().toString());
            }
            if(fragment==null){
                fragment = new questionsList();
                quizStarted.put(titleTextView.getText().toString(), (questionsList)fragment);
            }
            questionsList quiz = (questionsList) fragment;
            TextView textViewScore  =
                    (TextView) convertView.findViewById(R.id.current_score);
            textViewScore.setText(Integer.toString(quiz.m_Score));
            titleTextView.setTag(position);
            convertView.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent)
                {
                    view.setBackgroundColor(Color.BLUE);
                    return false;
                }
            }
            );


            convertView.setOnClickListener( new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                    fragment = null;

                    v.setBackgroundColor(Color.GREEN);
                    Bundle b = new Bundle();
                    TextView titleTextView = (TextView) v.findViewById(R.id.users_list_title);
                    int position = (int) titleTextView.getTag();
                    b.putInt("position",position);
                    b.putString("USER",m_Name);

                    if(quizStarted.containsKey(titleTextView.getText().toString()))
                    {
                        fragment = quizStarted.get(titleTextView.getText().toString());
                    }

                    if(fragment==null)
                    {
                        fragment = new questionsList();
                        quizStarted.put(titleTextView.getText().toString(), (questionsList)fragment);
                    }
                    fragment.setArguments(b);
                    manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            }
            );
            return convertView;
        }
    }
}