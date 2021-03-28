package smartagora.ethz.ch.uiActivities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.utils.ActivityStackManager;
/**
 * SplashActivity is the main starting point activity within the Survey app.
 * This activity starts when the app is launched. This activity has loader
 * image which is displayed when app launched
 */

public class Splash extends AppCompatActivity {

    private ActivityStackManager mStackManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mStackManager = ActivityStackManager.getInstance();
        mStackManager.startLocationService(this);
        SplashTime();
    }


    private void SplashTime() {
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mStackManager.startMainHomeScreen(Splash.this);
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.rightout, R.anim.leftout);
    }
}
