package smartagora.ethz.ch.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

@TargetApi(23)
public class Permissions {

    public static boolean hasLocationPermissions(Context context) {
        String permission1 = "android.permission.ACCESS_FINE_LOCATION";
        String permission2 = "android.permission.ACCESS_COARSE_LOCATION";
        int res1 = context.checkCallingOrSelfPermission(permission1);
        int res2 = context.checkCallingOrSelfPermission(permission2);
        return (res1 == PackageManager.PERMISSION_GRANTED ||
                res2 == PackageManager.PERMISSION_GRANTED);
    }

    public static void getLocationPermissions(Context context){
        String[] permissions = { "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION" };
        ((Activity)context).requestPermissions(permissions, 12);
    }

    public static boolean hasStorageReadPermissions(Context context) {
        String permission = "android.permission.READ_EXTERNAL_STORAGE";
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static void getStorageReadPermissions(Context context,int requestCode){
        String[] permissions = { "android.permission.READ_EXTERNAL_STORAGE" };
        ((Activity)context).requestPermissions(permissions, requestCode);
    }

}