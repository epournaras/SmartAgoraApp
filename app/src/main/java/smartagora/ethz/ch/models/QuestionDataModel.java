package smartagora.ethz.ch.models;

import java.util.List;

/**
 * Questions data model to store the questions data
 */

public class QuestionDataModel {

    private String Id;               //question id {Attribute}
    public String Question;         // questions
    private  String Latitude;         // questions latitude
    private  String Longitude;        // questions longitude
    private List<SensorModel> Sensor;

    private  String Time;
    private String Frequency;
    private  String Sequence;
    public String Type;
    private  String Visibility;
    private  String Mandatory;
    public List<OptionModel> Option;

    //used for push notification xml
    private  String StopName;         //Stop Name for notification
    private  String QId;              //QID for notification

    private  String Vicinity;

    public String getVicinity() {
        return Vicinity;
    }

    public void setVicinity(String vicinity) {
        this.Vicinity = vicinity;
    }


    public String getSequence() {
        return Sequence;
    }

    public void setSequence(String sequence) {
        this.Sequence = sequence;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        this.Frequency = frequency;
    }


    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }
    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        this.Question = question;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getVisibility() {
        return Visibility;
    }

    public void setVisibility(String visibility) {
        this.Visibility = visibility;
    }

    public String getMandatory() {
        return Mandatory;
    }

    public void setMandatory(String mandatory) {
        this.Mandatory = mandatory;
    }

    public List<OptionModel> getOption() {
        return Option;
    }

    public void setOption(List<OptionModel> option) {
        this.Option = option;
    }

    public List<SensorModel> getSensorsList() {
        return Sensor;
    }

    public void setSensorsList(List<SensorModel> Sensor) {
        this.Sensor = Sensor;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getId() {
        return this.Id;
    }

    public void setStopName(String stopName) {
        this.StopName = stopName;
    }

    public String getStopName() {
        return this.StopName;
    }

    public void setQId(String qId) {
        this.QId = qId;
    }

    public String getQId() {
        return this.QId;
    }
}