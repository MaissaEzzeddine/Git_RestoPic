package tn.codeit.restopic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public  class MainActivity extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        if (!session.checkLogin()) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new LogInFragment()).commit();
            }
        }
        else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new ClientFragment()).commit();
            }
        }
    }

    public void passCreate(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreateAccountFragment()).addToBackStack(null).commit();
    }

    public void ResetPassword (View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ResetPasswordFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

}