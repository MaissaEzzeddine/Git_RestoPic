package tn.codeit.restopic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity   {

    private static int SPLASH_SCREEN_DELAY = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        TextView Logo=(TextView) findViewById(R.id.app_name);

        Typeface face= Typeface.createFromAsset(getAssets(), "font/font.ttf");
        Logo.setTypeface(face);
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
}