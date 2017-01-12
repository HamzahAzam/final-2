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
 *
 */
class UserHistory {
  int highestScore;
  int totalAttempts;

    /**
     *
     */
    UserHistory() {
        highestScore = 0;
        totalAttempts = 0;
    }

    /**
     *
     */
    void set(int hscor, int attempts) {
        highestScore = hscor;
        totalAttempts = attempts;
    }

}

/**
 * Created by azam1 on 30/12/2016.
 */
public class RegistereUsersList extends ListFragment{
    private static ArrayList<TrueFalse> qKeyList;


    private static HashMap<String, UserHistory> userandHistory = new HashMap<String, UserHistory>();
    // Store Questions attempted and correct answers

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DBHandler db = new DBHandler(getActivity());
        ArrayList<User> regUSers = db.getAllUsers();
        ArrayList<Attempt> attempts = db.getAllAttempts();

               if (regUSers ==null)
        {
            regUSers = new ArrayList<User>();
        }

        User usr = new User();
        usr.setUserName("azam");
        usr.setPassword("azam");
        usr.setEmail("a@a");
        regUSers.add(usr);

        for(User user   :regUSers) {
            int highestScore = 0;
            int totalAttempts = 0;
            UserHistory hist = new UserHistory();
            for(Attempt attempt: attempts) {
                if(attempt.getUserName().equals(user.getUserName())){
                    totalAttempts++;
                    if (highestScore <  attempt.getScore()) highestScore = attempt.getScore();
                }
            }
            hist.set(highestScore, totalAttempts);
            userandHistory.put(user.getUserName(), hist);
        }
        UserDetailsAdapter adapter = new UserDetailsAdapter(regUSers);
        setListAdapter(adapter);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((UserDetailsAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class UserDetailsAdapter extends ArrayAdapter<User> {

        public UserDetailsAdapter(ArrayList<User> loggedIn) {
            super(getActivity(), android.R.layout.simple_list_item_1, loggedIn);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            final FragmentManager manager = getFragmentManager();
            Fragment fragment = manager.findFragmentById(R.id.fragment_container);

            if (null == convertView && position!=0) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_registered_users, null);
            }
            else if (position==0) {
                final View innerView = getActivity().getLayoutInflater()
                        .inflate(R.layout.header, null);
                TextView titleTextView =
                        (TextView) innerView.findViewById(R.id.listHeader);
                titleTextView.setText("Select a user to login");
                final Button btn_RegisterNewUser = (Button)innerView.findViewById(R.id.btn_register_user);
                btn_RegisterNewUser.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                        fragment = new RegisterNewUser();
                        v.setBackgroundColor(Color.GREEN);
                        Bundle b = new Bundle();
                        manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    }

                });

                return innerView;
            }

            ImageView thumbnailImageView = (ImageView) convertView.findViewById(R.id.users_list_thumbnail);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.person);
            thumbnailImageView.setImageBitmap(icon);

            // configure the view for this question
            User user = getItem(position);
            final TextView titleTextView = (TextView) convertView.findViewById(R.id.users_list_title);
            titleTextView.setText(user.getUserName());
            UserHistory history = userandHistory.get(user.getUserName());
            TextView textViewScore  = (TextView) convertView.findViewById(R.id.current_score);
            textViewScore.setText(Integer.toString(history.highestScore));
            titleTextView.setTag(position);
            convertView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.setBackgroundColor(Color.BLUE);
                    return false;
                }
            });


            convertView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = manager.findFragmentById(R.id.fragment_container);
                    fragment = new LoginMenu();
                    v.setBackgroundColor(Color.GREEN);
                    Bundle b = new Bundle();
                    int position = (int) titleTextView.getTag();
                    b.putInt("position",position);
                    b.putString("USER",titleTextView.getText().toString());
                    fragment.setArguments(b);
                    manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                }
            });
            return convertView;
        }
    }
}