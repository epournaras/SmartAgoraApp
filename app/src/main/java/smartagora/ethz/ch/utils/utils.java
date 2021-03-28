package smartagora.ethz.ch.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.CRC32;

import smartagora.ethz.ch.uiActivities.HomeActivity;

/**
 * utility class for loading files from file chooser, converting data from file to string
 */

public class utils {


    public static boolean FileWritingStatus = false;
    public static String fileName = "";
    public static String loadedFileName = "";
    public static boolean assignmentSubmissionStatus = false; //false means not submitted, true means assignment is submitted


    public static void showFileChooser(Activity context, int FILE_SELECT_CODE) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/xml");             //XML file only
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            context.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(context.getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();

            ExceptionalHandling.logException(ex);
        }
    }

    public static String getDataFromFile(final Context context, Intent data) {
        String xmlData = "";
        try {

            String filePath = GetFilePathFromDevice.getPath(context, data.getData());

            if(filePath == null){
                Log.e("utils:getDataFromFile*", "The filepath was null");
                return xmlData;
            }

            fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            File textFile = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            StringBuilder textBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                textBuilder.append(line);
                textBuilder.append("\n");
            }
            xmlData = textBuilder.toString();

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
        return xmlData;
    }

    public static boolean haveNetworkConnection(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm == null){
            Log.e("utils:haveNetworkCone*", "The connectivity manager was null");
            return false;
        }

        int netInfoType = cm.getActiveNetworkInfo().getType();
        return netInfoType == ConnectivityManager.TYPE_WIFI || netInfoType == ConnectivityManager.TYPE_MOBILE;
    }

    public static void closeSensorInfoFile(Context context) {
        String filename = utils.fileName + ".json";
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File sensorInfoFolder = contextWrapper.getExternalFilesDir("Sensor Information"); //Creating an internal dir;
        File sensorInfoFile = null;

        if(sensorInfoFolder == null){
            Log.e("utils:closeSensorInfoF*", "Could not create an internal directory called 'Sensor Information'");
            return;
        }

        File[] files = sensorInfoFolder.listFiles();

        for (File file : files) {
            if (file.getName().equals(filename)) {
                sensorInfoFile = file;
            }
        }

        if(sensorInfoFile == null){
            Log.e("utils:closeSensorInfoF*", "Did not find a file with filename '" + filename + "' in directory called 'Sensor Information'");
            return;
        }

        try {
            FileWriter fileWriter;
            fileWriter = new FileWriter(sensorInfoFile, true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.append("]");
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getSensorData(Context context) {
        StringBuilder data = new StringBuilder();
        String filename = utils.fileName + ".json";
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File sensorInfoFolder = contextWrapper.getExternalFilesDir("Sensor Information"); //Creating an internal dir;
        File sensorInfoFile = null;

        if(sensorInfoFolder == null){
            Log.e("utils:getSensorData", "Could not create an internal directory called 'Sensor Information'");
            return data.toString();
        }

        File[] files = sensorInfoFolder.listFiles();
        for (File file : files) {
            if (file.getName().equals(filename)) {
                sensorInfoFile = file;
            }
        }

        try {
            if (sensorInfoFile != null) {
                FileReader fileReader;
                fileReader = new FileReader(sensorInfoFile);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();

    }

    public static ProgressDialog initializeProgressDialog(Context context) {
        ProgressDialog loadingProgressDialog = new ProgressDialog(context);
        loadingProgressDialog.setIndeterminate(true);
        loadingProgressDialog.setTitle("Please Wait.");

        SpannableString ssMsg = new SpannableString("Syncing...");
        ssMsg.setSpan(new RelativeSizeSpan(1f), 0, ssMsg.length(), 0);
        loadingProgressDialog.setMessage(ssMsg);
        loadingProgressDialog.setCancelable(false);
        return loadingProgressDialog;
    }

    public static SensorManager registerSensorListener(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager == null)
            return null;
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);

        return sensorManager;
    }

    public static void updateRegisterSensorListener(SensorManager sensorManager, Context context) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            sensorManager.registerListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static void unregisterSensorListener(SensorManager sensorManager, Context context) {
        if (sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null)
            sensorManager.unregisterListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            sensorManager.unregisterListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null)
            sensorManager.unregisterListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
            sensorManager.unregisterListener((SensorEventListener) context, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));

    }

    public static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Enable GPS to use this application")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }


    public static String makeURL(double sourceLat, double sourceLog, double destLat, double destLog, String travelMode) {
        return "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" +                       // from
                sourceLat +
                "," +
                sourceLog +
                "&destination=" +                  // to
                destLat +
                "," +
                destLog +
                "&sensor=false&mode=" + travelMode + "&alternatives=true" +
                "&key=AIzaSyD4svI1DTicSeMI-9wT13UMz1CRoVLzXrU";
    }

    public static void getDiasCheckPointLeaveStatus(final LatLng checkPointLatLng, final LatLng myLatLng) {
        try {
            final long period = 2000; // time in milliseconds between successive task executions.
            final long delay = 0;//delay in milliseconds before task is to be executed.
            Timer timer = new Timer();
            final int distanceLimit = 30;
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    HomeActivity.isLeaveDiasCheckPoint = distance(checkPointLatLng.latitude, checkPointLatLng.longitude, myLatLng.latitude, myLatLng.longitude) > distanceLimit;
                }
            };
            timer.schedule(timerTask, delay, period);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1000;
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static int getUniqueCheckpointId(String lat, String lng) {
        CRC32 hash = new CRC32();
        hash.update((lat + lng).getBytes());
        return (int) hash.getValue();
    }


}

