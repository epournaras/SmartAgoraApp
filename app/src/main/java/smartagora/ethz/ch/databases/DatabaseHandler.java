package smartagora.ethz.ch.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import smartagora.ethz.ch.models.OptionModel;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.models.SensorModel;
import smartagora.ethz.ch.models.StartAndDestinationModel;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.utils;

/**
 * DatabaseHandler is used to store and retrieve data locally.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context) {
        super(context, StaticModel.DATABASE_NAME, null, StaticModel.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_QAS_TABLE = "CREATE TABLE " + StaticModel.TABLE_QUESTIONS_DATA + "(" +
                    StaticModel.ID + " INTEGER PRIMARY KEY," + StaticModel.QUESTION + " TEXT," + StaticModel.LATITUDE + " TEXT," + StaticModel.LONGITUDE + " TEXT," +
                    StaticModel.TYPE + " TEXT," + StaticModel.OPTIONS + " TEXT," + StaticModel.FILESNAME + " TEXT," + StaticModel.NOISE_SENSOR + " TEXT," +
                    StaticModel.LIGHT_SENSOR + " TEXT," + StaticModel.ACCEL_SENSOR + " TEXT," + StaticModel.GYRO_SENSOR + " TEXT," + StaticModel.PROXIMITY_SENSOR + " TEXT," +
                    StaticModel.LOCATION + " TEXT," + StaticModel.TIME + " TEXT," + StaticModel.FREQUENCY + " TEXT," + StaticModel.SEQUENCE + " TEXT," +
                    StaticModel.VISIBILITY + " TEXT," + StaticModel.MANDATORY + " TEXT, " + StaticModel.QUESTIONID + " TEXT, " + StaticModel.QID + " TEXT, " +
                    StaticModel.STOPNAME + " TEXT," + StaticModel.COMBINATION + " TEXT," + StaticModel.VICINITY+ " TEXT" + ")";
            db.execSQL(CREATE_QAS_TABLE);

            String CREATE_QUE_ANSWERS = "CREATE TABLE " + StaticModel.TABLE_ANSWERS_DATA + "(" +
                    StaticModel.ID + " INTEGER PRIMARY KEY," + StaticModel.QUESTION + " TEXT," + StaticModel.LATITUDE + " TEXT," + StaticModel.LONGITUDE + " TEXT," +
                    StaticModel.FILESNAME + " TEXT," + StaticModel.ANSWERS + " TEXT, " + StaticModel.QUESTIONID + " TEXT, " + StaticModel.CREDIT + " TEXT, "+StaticModel.QID + " TEXT, " +
                    StaticModel.STOPNAME + " TEXT," +
                    StaticModel.TIMEATANSWERING + " TEXT" + ")";
            db.execSQL(CREATE_QUE_ANSWERS);

            String CREATE_START_AND_DESTINATION_TABLE = "CREATE TABLE " + StaticModel.TABLE_START_AND_DESTINATION + "(" + StaticModel.ID + " INTEGER PRIMARY KEY," + " TEXT," +
                    StaticModel.START_LATITUDE + " TEXT," + StaticModel.START_LONGITUDE + " TEXT," + StaticModel.DESTINATION_LATITUDE + " TEXT," +
                    StaticModel.DESTINATION_LONGITUDE + " TEXT," + StaticModel.CAR + " TEXT," + StaticModel.TRAIN + " TEXT," + StaticModel.CYCLE + " TEXT," +
                    StaticModel.WALKING + " TEXT," + StaticModel.FILESNAME + " TEXT," + StaticModel.VICINITY + " TEXT," + StaticModel.MODE + " TEXT," + StaticModel.DEFAULTCREDIT + " TEXT," + StaticModel.ASSIGNMENTID + " TEXT," + StaticModel.ASSIGNMENTSTATE + " TEXT "+")";
            db.execSQL(CREATE_START_AND_DESTINATION_TABLE);

            String CREATE_SENSORS_DATA_TABLE = "CREATE TABLE " + StaticModel.TABLE_SENSOR_DATA + "(" + StaticModel.ID + " INTEGER PRIMARY KEY," + " TEXT," + StaticModel.FILESNAME + " TEXT,"+
                    StaticModel.ACCEL_SENSOR + " TEXT," + StaticModel.LIGHT_SENSOR + " TEXT," + StaticModel.NOISE_SENSOR + " TEXT," + StaticModel.GYRO_SENSOR + " TEXT," +
                    StaticModel.PROXIMITY_SENSOR + " TEXT," + StaticModel.LOCATION + " TEXT," + StaticModel.FREQUENCY + " TEXT," + StaticModel.QUESTION + " TEXT, " + StaticModel.QUESTIONID + " TEXT," + StaticModel.TIMEATSENSORING + " Text "+")";
            db.execSQL(CREATE_SENSORS_DATA_TABLE);

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);

        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Create tables again
        onCreate(db);
    }


    public void saveQuestionDataTransition(List<QuestionDataModel> sampleDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            Gson gson = new Gson();

            db.beginTransaction();

            for (int j = 0; j < sampleDataModel.size(); j++) {

                QuestionDataModel current = sampleDataModel.get(j);

                ContentValues values = new ContentValues();
                String options = gson.toJson(current.getOption());
                values.put(StaticModel.QUESTION, current.getQuestion());
                values.put(StaticModel.LATITUDE, current.getLatitude());
                values.put(StaticModel.LONGITUDE, current.getLongitude());
                values.put(StaticModel.TYPE,current.getType());
                values.put(StaticModel.OPTIONS, options);

                values.put(StaticModel.FILESNAME, utils.fileName);
                QuestionsActivity.FileFromList = utils.fileName;

                for (int i = 0; i < current.getSensorsList().size(); i++) {
                    String sensorName = current.getSensorsList().get(i).getName();
                    switch (sensorName) {
                        case "Light":
                            values.put(StaticModel.LIGHT_SENSOR, sensorName);
                            break;
                        case "Gyroscope":
                            values.put(StaticModel.GYRO_SENSOR, sensorName);
                            break;
                        case "Proximity":
                            values.put(StaticModel.PROXIMITY_SENSOR, sensorName);
                            break;
                        case "Accelerometer":
                            values.put(StaticModel.ACCEL_SENSOR, sensorName);
                            break;
                        case "Location":
                            values.put(StaticModel.LOCATION, sensorName);
                            break;
                        case "Noise":
                            values.put(StaticModel.NOISE_SENSOR, sensorName);
                            break;
                    }
                }
                values.put(StaticModel.TIME, current.getTime());
                values.put(StaticModel.FREQUENCY, current.getFrequency());
                values.put(StaticModel.SEQUENCE, current.getSequence());
                values.put(StaticModel.VISIBILITY, current.getVisibility());
                values.put(StaticModel.MANDATORY, current.getMandatory());
                values.put(StaticModel.QUESTIONID, current.getId());
                values.put(StaticModel.QID, current.getQId());
                values.put(StaticModel.STOPNAME, current.getStopName());
                values.put(StaticModel.VICINITY, current.getVicinity());

                // Inserting Row
                db.insert(StaticModel.TABLE_QUESTIONS_DATA, null, values);
            }
            db.setTransactionSuccessful();

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        } finally {
            db.endTransaction();
            db.close(); // Closing database connection
        }
    }

    /**
     * Store start and destination valued into database.
     *
     * @param startAndDestinationValue the start and destination value
     */
    public void saveStartAndDestinationData(StartAndDestinationModel startAndDestinationValue) {

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(StaticModel.START_LATITUDE, startAndDestinationValue.getStartLatitude());
            values.put(StaticModel.START_LONGITUDE, startAndDestinationValue.getStartLongitude());
            values.put(StaticModel.DESTINATION_LATITUDE, startAndDestinationValue.getDestinationLatitude());
            values.put(StaticModel.DESTINATION_LONGITUDE, startAndDestinationValue.getDestinationLongitude());
            values.put(StaticModel.FILESNAME, utils.fileName);
            values.put(StaticModel.MODE, startAndDestinationValue.getMode());
            values.put(StaticModel.DEFAULTCREDIT, startAndDestinationValue.getDefaultCredit());

            // Inserting Row
            db.insert(StaticModel.TABLE_START_AND_DESTINATION, null, values);
            db.close(); // Closing database connection
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Returns questions data from database.
     */
    public List<QuestionDataModel> getFilesData() {

        Gson gson = new Gson();
        List<QuestionDataModel> sampleDataModels = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + StaticModel.TABLE_QUESTIONS_DATA + " WHERE " + StaticModel.FILESNAME + " = '" + QuestionsActivity.FileFromList + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    QuestionDataModel sampleDataModel = new QuestionDataModel();

                    sampleDataModel.setQuestion(cursor.getString(1));
                    sampleDataModel.setLatitude(cursor.getString(2));
                    sampleDataModel.setLongitude(cursor.getString(3));
                    sampleDataModel.setType(cursor.getString(4));

                    // storing sensor information
                    for (int i = 7; i <= 12; i++) {
                        if (cursor.getString(i) != null) {
                            SensorModel sensorModel = new SensorModel(cursor.getString(i));
                            if (sampleDataModel.getSensorsList() == null) {
                                sampleDataModel.setSensorsList(new ArrayList<SensorModel>());
                            }
                            sampleDataModel.getSensorsList().add(sensorModel);
                        }
                    }
                    sampleDataModel.setTime(cursor.getString(13));
                    sampleDataModel.setFrequency(cursor.getString(14));
                    sampleDataModel.setSequence(cursor.getString(15));
                    sampleDataModel.setVisibility(cursor.getString(16));
                    sampleDataModel.setMandatory(cursor.getString(17));
                    sampleDataModel.setId(cursor.getString(18));
                    sampleDataModel.setQId(cursor.getString(19));
                    sampleDataModel.setStopName(cursor.getString(20));
                    sampleDataModel.setVicinity(cursor.getString(22));
                    String options = cursor.getString(5);
                    Type optionModelType = new TypeToken<List<OptionModel>>() {
                    }.getType();
                    List<OptionModel> option = gson.fromJson(options, optionModelType);
                    sampleDataModel.setOption(option);

                    //If combinations provided in case of QuestionType: Checkbox
                    sampleDataModels.add(sampleDataModel);

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }
        return sampleDataModels;
    }

    /**
     * Returns all file names stored in database.
     */
    public ArrayList<String> getFilesName() {

        ArrayList<String> name = new ArrayList<>();
        String selectQuery = "SELECT " + StaticModel.FILESNAME + " FROM " + StaticModel.TABLE_START_AND_DESTINATION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try {
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    name.add(cursor.getString(cursor.getColumnIndex(StaticModel.FILESNAME)));

                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        }
        return name;
    }

    /**
     * Delete a specific file selected by user from database.
     */
    public void deleteFile(String filename) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + StaticModel.TABLE_START_AND_DESTINATION + " WHERE " + StaticModel.FILESNAME + " = '" + filename + "'");
            db.execSQL("DELETE FROM " + StaticModel.TABLE_QUESTIONS_DATA + " WHERE " + StaticModel.FILESNAME + " = '" + filename + "'");

            QuestionsActivity.FileFromList = "";
            db.close();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

}