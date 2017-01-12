package course.java.ucl.loginactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.Fragment;




    /**
     * A placeholder fragment containing a simple view.
     */
    public class RegisterNewUser extends Fragment {

        private static final String TAG = "COMP211P";
        public RegisterNewUser() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

           final EditText et_Username, et_Password, et_Email;

            View rootView = inflater.inflate(R.layout.fragment_register_new_user,
                    container, false);
            Button b2 = (Button) rootView.findViewById(R.id.btn_cancel);
            Button btn = (Button) rootView.findViewById(R.id.btn_register2);
            et_Username = (EditText)rootView.findViewById(R.id.et_login_Username);
            et_Email = (EditText)rootView.findViewById(R.id.et_login_Password);
            et_Password = (EditText)rootView.findViewById(R.id.et_Password);

            final DBHandler db = new DBHandler(getActivity());

            btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String username = String.valueOf(et_Username.getText());
                        // Stores Password
                        String password = String.valueOf(et_Password.getText());
                        String email = String.valueOf(et_Email.getText());

                        Toast.makeText(getActivity(), "Added user!",Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Inserting users...");
                        db.addUser(new User(username, email, password));
                        FragmentManager manager = getFragmentManager();
                        Fragment fragment = new LoginMenu();
                        Bundle b = new Bundle();
                        b.putString("USER",username);
                        fragment.setArguments(b);
                        manager.beginTransaction().replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                    }
                });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStackImmediate();
                }
            });

            return rootView;
        }

    }