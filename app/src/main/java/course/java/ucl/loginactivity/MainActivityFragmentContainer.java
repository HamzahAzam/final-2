package course.java.ucl.loginactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class MainActivityFragmentContainer extends FragmentActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conatiner);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (fragment == null)
        {
            fragment = new RegistereUsersList();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

}
