package smartagora.ethz.ch.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.services.LocationService;


public class ActivityStackManager {

    private static final ActivityStackManager mActivityStack = new ActivityStackManager();
    private static boolean IsLocationServiceStarted = false;
    private boolean flag=false;

    private ActivityStackManager() {

    }

    public static ActivityStackManager getInstance() {
        return mActivityStack;
    }

    public void startMainHomeScreen(Context mContext) {
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra("New", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

    public void startLocationService(Context mContext) {
        Log.d("TESTING", "Location Service Started");
        if (!IsLocationServiceStarted) {
            Intent intent = new Intent(mContext, LocationService.class);
            mContext.startService(intent);
            IsLocationServiceStarted = true;
            flag=true;
        }
    }
    public boolean returnLocationFlag(){
        return flag;
    }
    public void stopLocationService(Context mContext) {
        Log.d("TESTING", "Location Service Stopped");
        if (IsLocationServiceStarted) {
            mContext.stopService(new Intent(mContext, LocationService.class));
            IsLocationServiceStarted = false;
            flag=false;
        }
    }
}
