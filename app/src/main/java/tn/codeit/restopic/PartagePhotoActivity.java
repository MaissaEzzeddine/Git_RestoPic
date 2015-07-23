package tn.codeit.restopic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PartagePhotoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partage);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.partage_container, new PartagePhotoFragment()).commit();
        }
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}