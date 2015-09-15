package tn.codeit.restopic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity   {

    private static int SPLASH_SCREEN_DELAY = 6000;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        button = (Button) findViewById(R.id.try_run);
        button.setVisibility(View.INVISIBLE);
        TextView Logo=(TextView) findViewById(R.id.app_name);
        Typeface face= Typeface.createFromAsset(getAssets(), "font/font.ttf");
        Logo.setTypeface(face);

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet(); // true or false
        String testInternet = isInternetPresent.toString();
        Toast.makeText(getApplicationContext(), testInternet, Toast.LENGTH_LONG).show();

        if ( isInternetPresent == true ) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }
        else{
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(SplashScreen.this, SplashScreen.class);
                    startActivity(i);
                    finish();
                }

            });

            Toast.makeText(getApplicationContext(), "L'aplication ne peut se lancer que lorsque vous activer l'internet", Toast.LENGTH_LONG).show();
        }


    }
}
