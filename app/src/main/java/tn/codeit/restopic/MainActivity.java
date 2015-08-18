package tn.codeit.restopic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import tn.codeit.restopic.webservice.JSONParser;

@SuppressWarnings("ALL")
public  class MainActivity extends AppCompatActivity  {

    SessionManager session;
    ViewPager Tab;
    TabsAdapter TabAdapter;
    ActionBar actionBar;
    int code = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    Uri fileUri ;
    int session_id ;
    String timeStampName , timeStamp , CurrentPhotoPath , session_name;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_FAIL = "error";
    JSONObject json ;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());
        session_name = session.getName() ;
        if (session_name != "empty") {
            session_id = Integer.parseInt(session_name);
        }

        if (!session.checkLogin()) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new LogInFragment()).commit();
            }
        }

        else {
            if (savedInstanceState == null) {
                TabAdapter = new TabsAdapter(getSupportFragmentManager());
                Tab = (ViewPager)findViewById(R.id.pager);
                Tab.setOnPageChangeListener(
                        new ViewPager.SimpleOnPageChangeListener() {
                            @Override
                            public void onPageSelected(int position) {
                                actionBar = getSupportActionBar();
                                actionBar.setSelectedNavigationItem(position);
                            }
                        });

                Tab.setAdapter(TabAdapter);
                actionBar = getSupportActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                ActionBar.TabListener tabListener = new ActionBar.TabListener(){

                    @Override
                    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                        Tab.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    }

                    @Override
                    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                    }};

                actionBar.addTab(actionBar.newTab().setText("Mes photos").setTabListener(tabListener));
                actionBar.addTab(actionBar.newTab().setText("Toutes les photos").setTabListener(tabListener));

                getSupportFragmentManager().beginTransaction().add(R.id.container, new ClientFragment()).commit();
            }
        }
        this.registerReceiver(mBroadcastReceiver, new IntentFilter("start.fragment.action"));
    }

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String url = intent.getExtras().getString("url");
            Bundle bundle = new Bundle();
            bundle.putString( "url" , url);
            CouponFragment couponFragment = new CouponFragment();
            couponFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, couponFragment).addToBackStack(null).commit();
        }
    };

    public void passCreate(View view){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CreateAccountFragment()).addToBackStack(null).commit();
    }

    public void ResetPassword (View view)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ForgotPasswordFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem acceuilItem = menu.findItem(R.id.acceuil);
        MenuItem okItem = menu.findItem(R.id.ok);
        acceuilItem.setVisible(false);
        okItem.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {

            case R.id.deconnexion:
                logOut();
                session.logoutUser();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new LogInFragment()).addToBackStack(null).commit();
                return true;
            case R.id.capture:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createPicture();
                    } catch (IOException ex) {}
                    if (photoFile != null) {
                        fileUri = Uri.fromFile(photoFile) ;
                        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(i, code);
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private File createPicture() throws IOException
    {
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        timeStampName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String user_id = "" + session_id +"_" ;
        File storageDir = Environment.getExternalStorageDirectory()  ;
        File image = new File(storageDir + "/" + user_id + timeStampName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        CurrentPhotoPath = image.getAbsolutePath();
        Log.e(TAG, "photo path = " + CurrentPhotoPath);
        return image;
    }

    private void launchPartagerPhoto(boolean isImage ,  String path)
    {
        Bundle bundle = new Bundle();
        bundle.putString("filePath" , path);
        bundle.putString("datePrise", timeStamp);
        bundle.putBoolean("isImage", isImage);
        bundle.putInt("id", session_id);
        PartagePhotoFragment partagePhotoFragment = new PartagePhotoFragment();
        partagePhotoFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, partagePhotoFragment).addToBackStack(null).commit();
    }

    public  void  onActivityResult( int requestCode, int resultCode , Intent data )
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==code && resultCode == Activity.RESULT_OK) {
            launchPartagerPhoto(true , CurrentPhotoPath);
        }
    }

    public void logOut()
    {
        AccessToken.setCurrentAccessToken(null);
        Profile.setCurrentProfile(null);
    }
}