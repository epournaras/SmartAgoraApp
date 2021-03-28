package smartagora.ethz.ch.prefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.maps.model.LatLng;


/**
 * The purpose of this class is to store the data. This class store the data in shared preferences.
 */
public class AppPreferences {

    //save user current location
    public static void saveLocation(Context context, LatLng location) {
        if (location.latitude == 0.0 || location.longitude == 0.0)
            return;

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.LATITUDE, location.latitude + "")
                .putString(PreferencesKeys.LONGITUDE, location.longitude + "")
                .apply();
    }

    public static void saveAccuracy(Context context, double accuracy) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.ACCURACY, String.valueOf(accuracy))
                .apply();
    }

    public static double getAccuracy(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.parseDouble(sp.getString(PreferencesKeys.ACCURACY, "0.0"));
    }

    // get user current latitude
    public static double getLatitude(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.parseDouble(sp.getString(PreferencesKeys.LATITUDE, "0.0"));
    }

    // get user current longitude
    public static double getLongitude(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return Double.parseDouble(sp.getString(PreferencesKeys.LONGITUDE, "0.0"));
    }

    public static void saveDataScientistId(String dataScientistId, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.DATA_SCIENTIST_ID, dataScientistId)
                .apply();
    }

    public static String getDataScientistId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PreferencesKeys.DATA_SCIENTIST_ID, "");
    }

    public static void saveHelpForProject(String help, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.HELP, help)
                .apply();
    }

    public static String getHelp(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PreferencesKeys.HELP, "");
    }

    public static void saveProjectId(String projectId, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.PROJECT_ID, projectId)
                .apply();
    }

    public static String getProjectId(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PreferencesKeys.PROJECT_ID, "");
    }

    public static void saveAuto(String auto, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PreferencesKeys.AUTO_ASSIGNMENT, String.valueOf(auto))
                .apply();
    }

    public static String getAuto(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PreferencesKeys.AUTO_ASSIGNMENT, "");
    }

}
