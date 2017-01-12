package course.java.ucl.loginactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



public class HighScores extends ListFragment
{
    private static ArrayList<TrueFalse> qKeyList;


    private static HashMap<String, UserHistory> userandHistory = new HashMap<String, UserHistory>();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final DBHandler db = new DBHandler(getActivity());
        ArrayList<User> regUSers = db.getAllUsers();
        ArrayList<Attempt> attempts = db.getAllAttempts();

        if (attempts == null)
        {
            attempts = new ArrayList<Attempt>();

            Attempt atmpt = new Attempt();
            atmpt.setUserName("azam");
            atmpt.setScore(0);
            attempts.add(atmpt);
        }




        for (User user : regUSers)
        {
            int highestScore = 0;
            int totalAttempts = 0;
            UserHistory hist = new UserHistory();
            for (Attempt attempt : attempts)
            {
                if (attempt.getUserName().equals(user.getUserName()))
                {
                    totalAttempts++;
                    if (highestScore < attempt.getScore()) highestScore = attempt.getScore();
                }
            }
            hist.set(highestScore, totalAttempts);
            userandHistory.put(user.getUserName(), hist);
        }
        UserDetailsAdapter adapter = new UserDetailsAdapter(attempts);
        setListAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        ((UserDetailsAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private class UserDetailsAdapter extends ArrayAdapter<Attempt>
    {
        public UserDetailsAdapter(ArrayList<Attempt> loggedIn)
        {
            super(getActivity(), android.R.layout.simple_list_item_1, loggedIn);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // if we weren't given a view, inflate one
            final FragmentManager manager = getFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.fragment_container);

            if (null == convertView && position != 0)
            {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_loggedin_users, null);
            }

            else if (position == 0)
            {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.header_loggedin, null);
                Button finishBtn = (Button) convertView.findViewById(R.id.backBtn);
                finishBtn.setOnClickListener
                        (new View.OnClickListener()
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
                titleTextView.setText("High Scores!");
                return convertView;
            }

            ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.users_list_thumbnail);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person);
            thumbnailImageView.setImageBitmap(icon);

            // configure the view for this question
            Attempt attempt = getItem(position);
            final TextView titleTextView = (TextView) convertView.findViewById(R.id.users_list_title);
            titleTextView.setText(attempt.getUserName());
            TextView textViewScore = (TextView) convertView.findViewById(R.id.current_score);
            textViewScore.setText(Integer.toString(attempt.getScore()));
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


            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                    fragment = new LoginMenu();
                    v.setBackgroundColor(Color.GREEN);
                    Bundle b = new Bundle();
                    int position = (int) titleTextView.getTag();
                    b.putInt("position", position);
                    b.putString("USER", titleTextView.getText().toString());
                    fragment.setArguments(b);
                    manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            }
            );
            return convertView;
        }
    }
}
