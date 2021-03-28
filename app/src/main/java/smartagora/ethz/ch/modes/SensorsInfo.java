package smartagora.ethz.ch.modes;

import android.content.Context;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
import android.os.CountDownTimer;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import smartagora.ethz.ch.networkRequests.hiveServerModels.Assignment;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.QuestionSensorsThread;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorReadingEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.FileHandling;
import smartagora.ethz.ch.utils.utils;
//import smartagora.ethz.ch.uiActivities.HomeActivity;

/**
 * SensorsInfo class has responsibility to save the sensors reading of each Answer.
 */
public class SensorsInfo {

    private final Context context;
    private List<SensorEntity> sensors;
    private final List<String> sensorsNamesList;
    private final HashMap<String, String> mSensorReading;
    private String frequency;


    /**
     * Instantiates a SensorsInfo.
     *
     * @param context          the context
     * @param sensors          the sensors
     * @param sensorsNamesList the sensors names list
     * @param mSensorReading   the m sensor reading
     * @param frequency        the frequency
     */
    public SensorsInfo(Context context, List<SensorEntity> sensors, List<String> sensorsNamesList, HashMap<String, String> mSensorReading, String frequency) {
        this.context = context;
        this.sensors = sensors;
        this.sensorsNamesList = sensorsNamesList;
        this.mSensorReading = mSensorReading;
        this.frequency = frequency;
    }

    /**
     * Save sensors reading against answer value.
     *
     * @param index       the index
     * @param answerValue the answer value
     */
    void saveSensorsValue(int index, String answerValue) {
        try {

            sensorsNamesList.clear();
            sensors.clear();

            int questionId = QuestionsActivity.assignmentQuestions.get(index).getId();
            String assignmentId = QuestionsActivity.assignmentQuestions.get(index).getAssignmentId();

            QuestionSensorsThread questionSensorsThread = new QuestionSensorsThread(context);
            sensors = questionSensorsThread.getAllSensorsFromAssignmentQuestion(assignmentId, questionId);

            for (int i = 0; i < sensors.size(); i++) {
                String name = sensors.get(i).getName();

                if (name.equalsIgnoreCase("Accelerometer")) {
                    sensorsNamesList.add(StaticModel.ACCEL_SENSOR);
                } else if (name.equalsIgnoreCase("Light")) {
                    sensorsNamesList.add(StaticModel.LIGHT_SENSOR);
                } else if (name.equalsIgnoreCase("Gyroscope")) {
                    sensorsNamesList.add(StaticModel.GYRO_SENSOR);
                } else if (name.equalsIgnoreCase("Proximity")) {
                    sensorsNamesList.add(StaticModel.PROXIMITY_SENSOR);
                } else if (name.equalsIgnoreCase("Location")) {
                    sensorsNamesList.add(StaticModel.LOCATION);
                } else if (name.equalsIgnoreCase("Noise")) {
                    sensorsNamesList.add(StaticModel.NOISE_SENSOR);
                    mSensorReading.put(StaticModel.NOISE_SENSOR, String.valueOf(new DecimalFormat("##.##").format(getNoiseLevel())));
                }
            }

            if (QuestionsActivity.assignmentQuestions.get(index).getTime() != null && QuestionsActivity.assignmentQuestions.get(index).getFrequency() != null) {
                frequency = QuestionsActivity.assignmentQuestions.get(index).getFrequency();
                if (frequency.equalsIgnoreCase("low")) {
                    saveSensorDataAtIntervals(index, StaticModel.LOW_TIME_INTERVAL, answerValue);
                } else if (frequency.equalsIgnoreCase("medium")) {
                    saveSensorDataAtIntervals(index, StaticModel.MED_TIME_INTERVAL, answerValue);
                } else if (frequency.equalsIgnoreCase("high")) {
                    saveSensorDataAtIntervals(index, StaticModel.HIGH_TIME_INTERVAL, answerValue);
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Save sensor reading at intervals.
     *
     * @param index        the index
     * @param timeInterval the time interval
     * @param answerValue  the answer value
     */
    private  void saveSensorDataAtIntervals(final int index, int timeInterval, String answerValue) {
        try {
            final String ansVal = answerValue;
            utils.FileWritingStatus = false;
            int timeSeconds = Integer.parseInt(QuestionsActivity.assignmentQuestions.get(index).getTime());
            int timeMilliSeconds = timeSeconds * 1000;
            final List<String> jsonList = new ArrayList<>();
            final Gson gson = new Gson();
            CountDownTimer timer = new CountDownTimer(timeMilliSeconds, timeInterval) {
                @Override
                public void onTick(long l) {
                    Date d = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    String timeAtSensoring = format.format(d);

                    if (sensorsNamesList.contains(StaticModel.LOCATION))
                        mSensorReading.put(StaticModel.LOCATION, AppPreferences.getLatitude(context) + "," + AppPreferences.getLongitude(context));

                    if (sensorsNamesList.contains(StaticModel.NOISE_SENSOR))
                        mSensorReading.put(StaticModel.NOISE_SENSOR, String.valueOf(new DecimalFormat("##.##").format(getNoiseLevel())));

                    for (int i = 0; i < sensors.size(); i++) {
                        SensorReadingEntity sensorReadingEntity = new SensorReadingEntity();
                        sensorReadingEntity.setSensorId(sensors.get(i).getId());
                        sensorReadingEntity.setReadingTime(timeAtSensoring);
                        sensorReadingEntity.setReadingValue(mSensorReading.get(sensors.get(i).getName()));
                        RoomDatabaseHandler.getAppDatabase(context).getSensorReadingDao().insert(sensorReadingEntity);
                    }

                    Assignment.SensorReading sensorReading = new Assignment.SensorReading(mSensorReading, sensorsNamesList, frequency, QuestionsActivity.assignmentQuestions.get(index).getQuestion(), String.valueOf(QuestionsActivity.assignmentQuestions.get(index).getId()), QuestionsActivity.assignmentQuestions.get(index).getAssignmentId(), timeAtSensoring);

                    String json = gson.toJson(sensorReading);
                    jsonList.add(json);

                    mSensorReading.clear();
                }

                @Override
                public void onFinish() {
                    FileHandling fl = new FileHandling();
                    fl.saveDataToFile(context, jsonList, String.valueOf(QuestionsActivity.assignmentQuestions.get(index).getId()), ansVal);
                }
            };
            timer.start();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Gets noise level.
     *
     * @return the noise level
     */
    private  double getNoiseLevel() {
        return -1;
        /*try {
            int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);

            //making the buffer bigger....
            bufferSize = bufferSize * 4;
            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    44100, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

            short[] data = new short[bufferSize];
            double average = 0.0;
            recorder.startRecording();

            //recording data;
            recorder.read(data, 0, bufferSize);
            recorder.stop();
            for (short s : data) {
                if (s > 0) {
                    average += Math.abs(s);
                } else {
                    bufferSize--;
                }
            }
            double x = average / bufferSize;
            recorder.release();
            // calculating the pascal pressure based on the idea that the max amplitude (between 0 and 32767) is
            // relative to the pressure
            double pressure = x / 51805.5336; //the value 51805.5336 can be derived from assuming that x=32767=0.6325 Pa and x=1 = 0.00002 Pa (the reference value)
            return (20 * Math.log10(pressure / HomeActivity.REFERENCE));
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
            return -1;
        }*/
    }


}
