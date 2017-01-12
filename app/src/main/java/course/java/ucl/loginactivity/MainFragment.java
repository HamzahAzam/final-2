package course.java.ucl.loginactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Adapter;


public class MainFragment extends Fragment
{
    Button b1,b3;

    int selection = 0;

    int LoggedIn = 0;

    Bundle bundle = new Bundle();

    public int getSelect() { return selection; }

    private static final String TAG = "COMP211P";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {


       final View rootView = inflater.inflate(R.layout.activity_main,
                container, false);

        b1 = (Button) rootView.findViewById(R.id.btn_main_login);
        b3 = (Button)rootView.findViewById(R.id.btn_start_quiz);



        final DBHandler db = new DBHandler(getContext());

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                FragmentManager manager = getFragmentManager();
                //Fragment fragment = manager.findFragmentById(R.id.fragment_container);

                bundle.putInt("loggedin",LoggedIn);
                Fragment fragment = new LoginMenu();
                fragment.setArguments(bundle);
                manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });

        b3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                FragmentManager manager = getFragmentManager();
                Fragment fragment;
                /*
                    Start the quiz session for each logged in user...

                 */

                ArrayList<String> loggedInuser = LoginMenu.getLoggedInUsers();

                if(loggedInuser.isEmpty())
                {
                    Toast.makeText(getActivity(),
                            "Please Login First...", Toast.LENGTH_SHORT).show();
                }
                else {
                    for (String userName : loggedInuser) {

                        Bundle b = new Bundle();
                        b.putString("USER",userName);
                        fragment = new LoggedInUsersList();
                        fragment.setArguments(b);
                        manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    }
              }
            }
        });
        return rootView;
    }
}
