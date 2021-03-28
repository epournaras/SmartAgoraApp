package smartagora.ethz.ch.models;

import java.util.List;

public class StaticModel {


    public static List<Integer> skippedQuestions;
    public static List<Integer> questionsQueue;
    public static List<Integer> questionsSensorsQueue;
    public static List<Integer> shownQuestionsQueue;
    public static boolean XMLLoaded = false;


    //************************************************************************************************
    //************************************************************************************************
    //**                                 Database Handler variables                                 **
    //************************************************************************************************
    //************************************************************************************************
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "questions";

    // Questions table name
    public static final String TABLE_QUESTIONS_DATA = "questions_data";

    // Answers table name
    public static final String TABLE_ANSWERS_DATA = "answers_data";

    // Start and destination table name
    public static final String TABLE_START_AND_DESTINATION = "start_and_destination";

    // sensor data table
    public static final String TABLE_SENSOR_DATA = "sensor_data";

    // Table Columns names
    public static final String FILESNAME = "Files_Name";
    public static final String ASSIGNMENTSTATE = "Assignment_State";
    public static final String ID = "Id";
    public static final String QUESTIONID = "QuestionID";
    public static final String QUESTION = "Question";
    public static final String NOISE_SENSOR = "Noise";
    public static final String LIGHT_SENSOR = "Light";
    public static final String ACCEL_SENSOR = "Accelerometer";
    public static final String PROXIMITY_SENSOR = "Proximity";
    public static final String GYRO_SENSOR = "Gyroscope";
    public static final String LOCATION = "Location";
    public static final String TIME = "Time";
    public static final String FREQUENCY = "Frequency";
    public static final String SEQUENCE = "Sequence";
    public static final String VISIBILITY = "Visibility";
    public static final String MANDATORY = "Mandatory";
    public static final String VICINITY = "Vicinity";
    public static final String TYPE = "Type";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String OPTIONS = "Options";
    public static final String ANSWERS = "Answer";
    public static final String START_LATITUDE = "Start_Latitude";
    public static final String START_LONGITUDE = "Start_Longitude";
    public static final String DESTINATION_LATITUDE = "Destination_Latitude";
    public static final String DESTINATION_LONGITUDE = "Destination_Longitude";
    public static final String CAR = "Car";
    public static final String TRAIN = "Train";
    public static final String CYCLE = "Cycle";
    public static final String WALKING = "Walking";
    public static final String MODE = "Mode";
    public static final String QID = "QID";
    public static final String STOPNAME = "Stop_Name";
    public static final String DEFAULTCREDIT = "Default_Credit";
    public static final String COMBINATION = "Combination";
    public static final String ASSIGNMENTID = "Assignment_Id";
    public static final String TIMEATANSWERING = "TimeAtAnswering";
    public static final String TIMEATSENSORING = "TimeAtSensoring";
    public static final String CREDIT = "Credit";


    // time in ms to get a sensor value
    public static final int LOW_TIME_INTERVAL = 2000;
    public static final int MED_TIME_INTERVAL = 250;
    public static final int HIGH_TIME_INTERVAL = 200;


}
