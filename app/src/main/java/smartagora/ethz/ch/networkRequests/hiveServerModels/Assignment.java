package smartagora.ethz.ch.networkRequests.hiveServerModels;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartagora.ethz.ch.models.StaticModel;

/**
 * Assignment Class is a JSON Model which is created in Hive Server.
 * Assignment is used to represent the Assignment attributes, Asset and their corresponding Completed Assignment Data/Information
 * Assignment has a Asset and submittedData attributes which defines the two different JSONs
 */
@SuppressWarnings("unused")
public class Assignment {

    @SerializedName("Id")
    private String assignmentId;
    @SerializedName("Project")
    private String projectName;
    @SerializedName("Task")
    private String task;
    @SerializedName("User")
    private String userName;
    @SerializedName("State")
    private String state;
    @SerializedName("Asset")
    private Asset asset;
    @SerializedName("SubmittedData")
    private AssignmentSubmittedData submittedData;


    /**
     * Instantiates a new Assignment.
     */
    public Assignment() {
    }

// All setters and getters of above variables


    /**
     * Gets assignmentId.
     *
     * @return the assignmentid
     */
    public String getAssignmentid() {
        return assignmentId;
    }

    /**
     * Sets assignmentId.
     *
     * @param assignmentid the assignmentid
     */
    public void setAssignmentId(String assignmentid) {
        assignmentId = assignmentid;
    }

    /**
     * Gets project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets project name.
     *
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets task.
     *
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * Sets task.
     *
     * @param task the task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets state.
     *
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets asset.
     *
     * @return the asset
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * Sets asset.
     *
     * @param asset the asset
     */
    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    /**
     * Gets submitted data.
     *
     * @return the submitted data
     */
    public AssignmentSubmittedData getSubmittedData() {
        return submittedData;
    }

    /**
     * Sets submitted data.
     *
     * @param submittedData the submitted data
     */
    public void setSubmittedData(AssignmentSubmittedData submittedData) {
        this.submittedData = submittedData;
    }


    /**
     * Answer class is a JSON Model which is used in Assignment SubmittedData.
     * It is used to represent the answer of each question.
     * It includes the unique id of each answer, answer , file name to represent the Assignment Name,
     * location of answer, question which is answered , question type and answering time of question
     */
    @SuppressWarnings("unused")
    public static class Answer {

        @SerializedName("Id")
        private int id;
        @SerializedName("Answer")
        private String answer;
        @SerializedName("Files_name")
        private String files_name;
        @SerializedName("Latitude")
        private String latitude;
        @SerializedName("Longitude")
        private String longitude;
        @SerializedName("Question")
        private String question;
        @SerializedName("Type")
        private String type;
        @SerializedName("TimeAtAnswering")
        private String timeAtAnswering;

        // All setters and getters of above variables


        /**
         * Gets id.
         *
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * Sets id.
         *
         * @param id the id
         */
        public void setId(int id) {
            this.id = id;
        }

        /**
         * Gets answer.
         *
         * @return the answer
         */
        public String getAnswer() {
            return answer;
        }

        /**
         * Sets answer.
         *
         * @param answer the answer
         */
        public void setAnswer(String answer) {
            this.answer = answer;
        }

        /**
         * Gets files name.
         *
         * @return the files name
         */
        public String getFiles_name() {
            return files_name;
        }

        /**
         * Sets files name.
         *
         * @param files_name the files name
         */
        public void setFiles_name(String files_name) {
            this.files_name = files_name;
        }

        /**
         * Gets latitude.
         *
         * @return the latitude
         */
        public String getLatitude() {
            return latitude;
        }

        /**
         * Sets latitude.
         *
         * @param latitude the latitude
         */
        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        /**
         * Gets longitude.
         *
         * @return the longitude
         */
        public String getLongitude() {
            return longitude;
        }

        /**
         * Sets longitude.
         *
         * @param longitude the longitude
         */
        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        /**
         * Gets question.
         *
         * @return the question
         */
        public String getQuestion() {
            return question;
        }

        /**
         * Sets question.
         *
         * @param question the question
         */
        public void setQuestion(String question) {
            this.question = question;
        }

        /**
         * Gets type.
         *
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * Sets type.
         *
         * @param type the type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * Gets time at answering.
         *
         * @return the time at answering
         */
        public String getTimeAtAnswering() {
            return timeAtAnswering;
        }

        /**
         * Sets time at answering.
         *
         * @param timeAtAnswering the time at answering
         */
        public void setTimeAtAnswering(String timeAtAnswering) {
            this.timeAtAnswering = timeAtAnswering;
        }
    }

    /**
     * SensorReading class is a JSON Model which is used in Assignment SubmittedData.
     * It is used to represent the reading of each sensor
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public static class SensorReading {


        @SerializedName("Id")
        private String id;
        @SerializedName("AssignmentId")
        private String assignmentId;
        @SerializedName("Question")
        private String question;
        @SerializedName("QuestionId")
        private String questionId;
        @SerializedName("TimeAtSensoring")
        private String timeAtSensoring;
        @SerializedName("Answer Value")
        private String answerValue;
        @SerializedName("Acceleration")
        private String acceleration;
        @SerializedName("Frequency")
        private String frequency;
        @SerializedName("Gyroscope")
        private String gyroscope;
        @SerializedName("Light")
        private String light;
        @SerializedName("Location")
        private String location;
        @SerializedName("Noise")
        private String noise;
        @SerializedName("Proximity")
        private String proximity;


        /**
         * Instantiates a new Sensor reading.
         * It is used to identify the question sensor then get the reading of that sensor.
         *
         * @param sensorReading   the readings of Sensor against sensor name
         * @param sensorNamesList the list of sensor names
         * @param frequency       the frequency of Question
         * @param question        the question
         * @param questionId      the question id
         * @param assignmentId    the assignment id
         * @param timeAtSensoring the time at sensoring
         */
        public SensorReading(HashMap<String, String> sensorReading, List<String> sensorNamesList, String frequency, String question, String questionId, String assignmentId, String timeAtSensoring) {
            if (sensorNamesList != null) {
                for (int i = 0; i < sensorNamesList.size(); i++) {
                    if (sensorReading.get(sensorNamesList.get(i)) != null) {
                        switch (sensorNamesList.get(i)) {
                            case StaticModel.LOCATION:
                                setLocation(sensorReading.get(sensorNamesList.get(i)));
                                break;
                            case StaticModel.ACCEL_SENSOR:
                                setAcceleration(sensorReading.get(sensorNamesList.get(i)));
                                break;
                            case StaticModel.GYRO_SENSOR:
                                setGyroscope(sensorReading.get(sensorNamesList.get(i)));
                                break;
                            case StaticModel.LIGHT_SENSOR:
                                setLight(sensorReading.get(sensorNamesList.get(i)));
                                break;
                            case StaticModel.NOISE_SENSOR:
                                setNoise(sensorReading.get(sensorNamesList.get(i)));
                                break;
                            case StaticModel.PROXIMITY_SENSOR:
                                setProximity(sensorReading.get(sensorNamesList.get(i)));
                                break;
                        }
                    }
                }
                setFrequency(frequency);
                setQuestion(question);
                setQuestionId(questionId);
                setAssignmentId(assignmentId);
                setTimeAtSensoring(timeAtSensoring);
            }
        }

        /**
         * Gets id.
         *
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Sets id.
         *
         * @param id the id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets assignment id.
         *
         * @return the assignment id
         */
        public String getAssignmentId() {
            return assignmentId;
        }

        /**
         * Sets assignment id.
         *
         * @param assignmentId the assignment id
         */
        public void setAssignmentId(String assignmentId) {
            this.assignmentId = assignmentId;
        }

        /**
         * Gets question.
         *
         * @return the question
         */
        public String getQuestion() {
            return question;
        }

        /**
         * Sets question.
         *
         * @param question the question
         */
        public void setQuestion(String question) {
            this.question = question;
        }

        /**
         * Gets question id.
         *
         * @return the question id
         */
        public String getQuestionId() {
            return questionId;
        }

        /**
         * Sets question id.
         *
         * @param questionId the question id
         */
        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        /**
         * Gets time at sensoring.
         *
         * @return the time at sensoring
         */
        public String getTimeAtSensoring() {
            return timeAtSensoring;
        }

        /**
         * Sets time at sensoring.
         *
         * @param timeAtSensoring the time at sensoring
         */
        public void setTimeAtSensoring(String timeAtSensoring) {
            this.timeAtSensoring = timeAtSensoring;
        }

        /**
         * Gets answer value.
         *
         * @return the answer value
         */
        public String getAnswerValue() {
            return answerValue;
        }

        /**
         * Sets answer value.
         *
         * @param answerValue the answer value
         */
        public void setAnswerValue(String answerValue) {
            this.answerValue = answerValue;
        }

        /**
         * Gets acceleration.
         *
         * @return the acceleration
         */
        public String getAcceleration() {
            return acceleration;
        }

        /**
         * Sets acceleration.
         *
         * @param acceleration the acceleration
         */
        public void setAcceleration(String acceleration) {
            this.acceleration = acceleration;
        }

        /**
         * Gets frequency.
         *
         * @return the frequency
         */
        public String getFrequency() {
            return frequency;
        }

        /**
         * Sets frequency.
         *
         * @param frequency the frequency
         */
        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        /**
         * Gets gyroscope.
         *
         * @return the gyroscope
         */
        public String getGyroscope() {
            return gyroscope;
        }

        /**
         * Sets gyroscope.
         *
         * @param gyroscope the gyroscope
         */
        public void setGyroscope(String gyroscope) {
            this.gyroscope = gyroscope;
        }

        /**
         * Gets light.
         *
         * @return the light
         */
        public String getLight() {
            return light;
        }

        /**
         * Sets light.
         *
         * @param light the light
         */
        public void setLight(String light) {
            this.light = light;
        }

        /**
         * Gets location.
         *
         * @return the location
         */
        public String getLocation() {
            return location;
        }

        /**
         * Sets location.
         *
         * @param location the location
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         * Gets noise.
         *
         * @return the noise
         */
        public String getNoise() {
            return noise;
        }

        /**
         * Sets noise.
         *
         * @param noise the noise
         */
        public void setNoise(String noise) {
            this.noise = noise;
        }

        /**
         * Gets proximity.
         *
         * @return the proximity
         */
        public String getProximity() {
            return proximity;
        }

        /**
         * Sets proximity.
         *
         * @param proximity the proximity
         */
        public void setProximity(String proximity) {
            this.proximity = proximity;
        }
    }


    /**
     * AssignmentSubmittedData Class is a JSON Model used in SubmittedData which is submitted
     * in the Hive Server when assignment is completed.
     * Submitted data include Answers of all questions and list of sensor readings while answering
     */
    @SuppressWarnings("unused")
    public static class AssignmentSubmittedData {

        @SerializedName("SubmittedData")
        private List<Answer> answer;
        /**
         * The SensorReadings.
         */
        @SerializedName("SensorData")
        List<Map<String, List<SensorReading>>> sensorreadings;


        /**
         * Gets answer.
         *
         * @return the answer
         */
        public List<Answer> getAnswer() {
            return answer;
        }

        /**
         * Sets answer.
         *
         * @param answer the answer
         */
        public void setAnswer(List<Answer> answer) {
            this.answer = answer;
        }


        /**
         * Gets sensorReadings.
         *
         * @return the sensorreadings
         */
        public List<Map<String, List<SensorReading>>> getSensorreadings() {
            return sensorreadings;
        }

        /**
         * Sets sensorReadings.
         *
         * @param sensorreadings the sensorreadings
         */
        public void setSensorreadings(List<Map<String, List<SensorReading>>> sensorreadings) {
            this.sensorreadings = sensorreadings;
        }
    }

}
