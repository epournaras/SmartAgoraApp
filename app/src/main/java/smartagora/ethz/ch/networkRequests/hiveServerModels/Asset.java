package smartagora.ethz.ch.networkRequests.hiveServerModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Asset Class is a JSON representation of Asset which is created in Hive Server.
 * Asset class is used to represent the starting and ending location of each Assignment and their corresponding questions
 * Asset has a Metadata, and Counts attributes which is also representing two different JSONs
 * MetaData have a record which included startAndDestination of Asset and SampleDataQuestions
 * which included all questions of the Asset
 */
@SuppressWarnings("ALL")
public class Asset {

    @SerializedName("Id")
    private String id;
    @SerializedName("Project")
    private String project;
    @SerializedName("Name")
    private String name;
    @SerializedName("Url")
    private String url;
    @SerializedName("Metadata")
    private Metadata metadata;
    @SerializedName("Favorited")
    private boolean Favorited;
    @SerializedName("Verified")
    private boolean Verified;
    @SerializedName("Counts")
    private Counts counts;


    // All setters and getters of above variables

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
     * Gets project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets metadata.
     *
     * @return the metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Sets metadata.
     *
     * @param metadata the metadata
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }


    /**
     * Gets counts.
     *
     * @return the counts
     */
    public Counts getCounts() {
        return counts;
    }

    /**
     * Sets counts.
     *
     * @param counts the counts
     */
    public void setCounts(Counts counts) {
        this.counts = counts;
    }

    /**
     * Is favorited boolean.
     *
     * @return the boolean
     */
    public boolean isFavorited() {
        return Favorited;
    }

    /**
     * Sets favorited.
     *
     * @param favorited the favorited
     */
    public void setFavorited(boolean favorited) {
        Favorited = favorited;
    }

    /**
     * Is verified boolean.
     *
     * @return the boolean
     */
    public boolean isVerified() {
        return Verified;
    }

    /**
     * Sets verified.
     *
     * @param verified the verified
     */
    public void setVerified(boolean verified) {
        Verified = verified;
    }

    // All Classes used for creating an Asset

    /**
     * Metadata Class is a JSON Model which is an attribute of the Asset.
     * It includes the record JSON Model to store the startAndDestination and sampleDataQuestions of the Asset.
     */
    public class Metadata {
        @SerializedName("record")
        private Record record;

        // All setters and getters of above variables


        /**
         * Instantiates a new Metadata.
         */
        public Metadata() {
        }

        /**
         * Gets record.
         *
         * @return the record
         */
        public Record getRecord() {
            return record;
        }

        /**
         * Sets record.
         *
         * @param record the record
         */
        public void setRecord(Record record) {
            this.record = record;
        }

    }

    /**
     * Counts class is a JSON Model which is a attribute of Asset.
     * It is used to represent all the skipped, finished, unfinished and total number of Assignments in Asset.
     */
    public class Counts {

        @SerializedName("skipped")
        private int skipped;
        @SerializedName("unfinished")
        private int unfinished;
        @SerializedName("finished")
        private int finished;
        @SerializedName("Assignments")
        private int Assignments;

        // All setters and getters of above variables


        /**
         * Gets skipped.
         *
         * @return the skipped
         */
        public int getSkipped() {
            return skipped;
        }

        /**
         * Sets skipped.
         *
         * @param skipped the skipped
         */
        public void setSkipped(int skipped) {
            this.skipped = skipped;
        }

        /**
         * Gets unfinished.
         *
         * @return the unfinished
         */
        public int getUnfinished() {
            return unfinished;
        }

        /**
         * Sets unfinished.
         *
         * @param unfinished the unfinished
         */
        public void setUnfinished(int unfinished) {
            this.unfinished = unfinished;
        }

        /**
         * Gets finished.
         *
         * @return the finished
         */
        public int getFinished() {
            return finished;
        }

        /**
         * Sets finished.
         *
         * @param finished the finished
         */
        public void setFinished(int finished) {
            this.finished = finished;
        }

        /**
         * Gets assignments.
         *
         * @return the assignments
         */
        public int getAssignments() {
            return Assignments;
        }

        /**
         * Sets assignments.
         *
         * @param assignments the assignments
         */
        public void setAssignments(int assignments) {
            Assignments = assignments;
        }
    }

    /**
     * Record class is a JSON Model in MetaData.
     * Record class is used to represent the record of each Asset which includes starting and ending location
     * and list of questions in Asset.
     */
    public static class Record {

        @SerializedName("StartAndDestinationModel")
        private StartAndDestination startAndDestinationModel;
        @SerializedName("SampleDataModel")
        private List<SampleDataModel> sampleDataModel;

        // All setters and getters of above variables

        /**
         * Gets start and destination model.
         *
         * @return the start and destination model
         */
        public StartAndDestination getStartAndDestinationModel() {
            return startAndDestinationModel;
        }

        /**
         * Sets start and destination model.
         *
         * @param startAndDestinationModel the start and destination model
         */
        public void setStartAndDestinationModel(StartAndDestination startAndDestinationModel) {
            this.startAndDestinationModel = startAndDestinationModel;
        }

        /**
         * Gets sample data model.
         *
         * @return the sample data model
         */
        public List<SampleDataModel> getSampleDataModel() {
            return sampleDataModel;
        }

        /**
         * Sets sample data model.
         *
         * @param sampleDataModel the sample data model
         */
        public void setSampleDataModel(List<SampleDataModel> sampleDataModel) {
            this.sampleDataModel = sampleDataModel;
        }

    }

    /**
     * StartAndDestination class is a JSON Model in Record.
     * StartAndDestination class is used to represent the starting and ending location of each Asset.
     */
    public class StartAndDestination {

        @SerializedName("StartLatitude")
        private String startLatitude;
        @SerializedName("StartLongitude")
        private String startLongitude;
        @SerializedName("DestinationLatitude")
        private String destinationLatitude;
        @SerializedName("DestinationLongitude")
        private String destinationLongitude;
        @SerializedName("Mode")
        private String mode;
        @SerializedName("DefaultCredit")
        private String defaultCredit;


        // All setters and getters of above variables

        /**
         * Gets start latitude.
         *
         * @return the start latitude
         */
        public String getStartLatitude() {
            return startLatitude;
        }

        /**
         * Sets start latitude.
         *
         * @param startLatitude the start latitude
         */
        public void setStartLatitude(String startLatitude) {
            this.startLatitude = startLatitude;
        }

        /**
         * Gets start longitude.
         *
         * @return the start longitude
         */
        public String getStartLongitude() {
            return startLongitude;
        }

        /**
         * Sets start longitude.
         *
         * @param startLongitude the start longitude
         */
        public void setStartLongitude(String startLongitude) {
            this.startLongitude = startLongitude;
        }

        /**
         * Gets destination latitude.
         *
         * @return the destination latitude
         */
        public String getDestinationLatitude() {
            return destinationLatitude;
        }

        /**
         * Sets destination latitude.
         *
         * @param destinationLatitude the destination latitude
         */
        public void setDestinationLatitude(String destinationLatitude) {
            this.destinationLatitude = destinationLatitude;
        }

        /**
         * Gets destination longitude.
         *
         * @return the destination longitude
         */
        public String getDestinationLongitude() {
            return destinationLongitude;
        }

        /**
         * Sets destination longitude.
         *
         * @param destinationLongitude the destination longitude
         */
        public void setDestinationLongitude(String destinationLongitude) {
            this.destinationLongitude = destinationLongitude;
        }

        /**
         * Gets mode.
         *
         * @return the mode
         */
        public String getMode() {
            return mode;
        }

        /**
         * Sets mode.
         *
         * @param mode the mode
         */
        public void setMode(String mode) {
            this.mode = mode;
        }

        /**
         * Gets default credit.
         *
         * @return the default credit
         */
        public String getDefaultCredit() {
            return defaultCredit;
        }

        /**
         * Sets default credit.
         *
         * @param defaultCredit the default credit
         */
        public void setDefaultCredit(String defaultCredit) {
            this.defaultCredit = defaultCredit;
        }
    }

    /**
     * SampleDataModel class is a JSON Model in Record.
     * SampleDataModel class is used to represent the each question information/data.
     * It contains question details which include unique id, question, question type, location of question,
     * all sensors, all options, all combinations, question frequency, sequence & vicinity
     */
    public static class SampleDataModel {

        @SerializedName("id")
        private int id;
        @SerializedName("Question")
        private String question;
        @SerializedName("Type")
        private String type;
        @SerializedName("Latitude")
        private String latitude;
        @SerializedName("Longitude")
        private String longitude;
        @SerializedName("Sensor")
        private List<Sensor> Sensor;
        @SerializedName("Time")
        private String time;
        @SerializedName("Frequency")
        private String frequency;
        @SerializedName("Sequence")
        private String sequence;
        @SerializedName("Visibility")
        private String visibility;
        @SerializedName("Mandatory")
        private String mandatory;
        @SerializedName("Option")
        private List<Option> Option;
        @SerializedName("Combination")
        private List<Combination> combination;
        @SerializedName("Vicinity")
        private String vicinity;


        // All setter getters of above variables


        /**
         * Instantiates a new Sample data model.
         */
        public SampleDataModel() {
        }

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
         * Gets sensor.
         *
         * @return the sensor
         */
        public List<Asset.Sensor> getSensor() {
            return Sensor;
        }

        /**
         * Sets sensor.
         *
         * @param sensor the sensor
         */
        public void setSensor(List<Asset.Sensor> sensor) {
            Sensor = sensor;
        }

        /**
         * Gets time.
         *
         * @return the time
         */
        public String getTime() {
            return time;
        }

        /**
         * Sets time.
         *
         * @param time the time
         */
        public void setTime(String time) {
            this.time = time;
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
         * Gets sequence.
         *
         * @return the sequence
         */
        public String getSequence() {
            return sequence;
        }

        /**
         * Sets sequence.
         *
         * @param sequence the sequence
         */
        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        /**
         * Gets visibility.
         *
         * @return the visibility
         */
        public String getVisibility() {
            return visibility;
        }

        /**
         * Sets visibility.
         *
         * @param visibility the visibility
         */
        public void setVisibility(String visibility) {
            this.visibility = visibility;
        }

        /**
         * Gets mandatory.
         *
         * @return the mandatory
         */
        public String getMandatory() {
            return mandatory;
        }

        /**
         * Sets mandatory.
         *
         * @param mandatory the mandatory
         */
        public void setMandatory(String mandatory) {
            this.mandatory = mandatory;
        }

        /**
         * Gets option.
         *
         * @return the option
         */
        public List<Asset.Option> getOption() {
            return Option;
        }

        /**
         * Sets option.
         *
         * @param option the option
         */
        public void setOption(List<Asset.Option> option) {
            Option = option;
        }

        /**
         * Gets combination.
         *
         * @return the combination
         */
        public List<Combination> getCombination() {
            return combination;
        }

        /**
         * Sets combination.
         *
         * @param combination the combination
         */
        public void setCombination(List<Combination> combination) {
            this.combination = combination;
        }

        /**
         * Gets vicinity.
         *
         * @return the vicinity
         */
        public String getVicinity() {
            return vicinity;
        }

        /**
         * Sets vicinity.
         *
         * @param vicinity the vicinity
         */
        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }
    }

    /**
     * Option class is a JSON Model which is used in SampleDataModel.
     * It contains all options of each question and each option has a unique Id, option name,
     * next question against option and its credit
     */
    public class Option {

        @SerializedName("id")
        private int id;
        @SerializedName("Name")
        private String name;
        @SerializedName("NextQuestion")
        private String nextQuestion;
        @SerializedName("Credits")
        private String credits;


        // All setter and getters of above variables


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
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Gets next question.
         *
         * @return the next question
         */
        public String getNextQuestion() {
            return nextQuestion;
        }

        /**
         * Sets next question.
         *
         * @param nextQuestion the next question
         */
        public void setNextQuestion(String nextQuestion) {
            this.nextQuestion = nextQuestion;
        }

        /**
         * Gets credits.
         *
         * @return the credits
         */
        public String getCredits() {
            return credits;
        }

        /**
         * Sets credits.
         *
         * @param credits the credits
         */
        public void setCredits(String credits) {
            this.credits = credits;
        }

    }

    /**
     * Sensor class is the JSON Model which is used in SampleDataModel.
     * It contains the unique id of each sensor and sensor name.
     */
    public class Sensor {
        @SerializedName("id")
        private int id;

        @SerializedName("Name")
        private String name;

        // All setter and getters of above variables


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
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets name.
         *
         * @param name the name
         */
        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * Combination class is a JSON Model which is used in SampleDataModel.
     * It contains all combinations of checkboxes and each combination has a unique Id,
     * list of orders (i-e all possible combinations of checkboxes), next question and its credit
     */
    public class Combination {

        @SerializedName("id")
        private int id;
        @SerializedName("Selected")
        private List<Order> selected;
        @SerializedName("NextQuestion")
        private String nextQuestion;
        @SerializedName("Credits")
        private String credits;


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
         * Gets selected.
         *
         * @return the selected
         */
        public List<Order> getSelected() {
            return selected;
        }

        /**
         * Sets selected.
         *
         * @param selected the selected
         */
        public void setSelected(List<Order> selected) {
            this.selected = selected;
        }

        /**
         * Gets next question.
         *
         * @return the next question
         */
        public String getNextQuestion() {
            return nextQuestion;
        }

        /**
         * Sets next question.
         *
         * @param nextQuestion the next question
         */
        public void setNextQuestion(String nextQuestion) {
            this.nextQuestion = nextQuestion;
        }

        /**
         * Gets credits.
         *
         * @return the credits
         */
        public String getCredits() {
            return credits;
        }

        /**
         * Sets credits.
         *
         * @param credits the credits
         */
        public void setCredits(String credits) {
            this.credits = credits;
        }
    }

    /**
     * Order class is a JSON Model of Combination.
     * It includes id and order attributes. ID is the unique representation of each order
     * and order attribute is the all sequences/combinations of checkboxes. e.g 2 checkboxes order is 1, 2, 1 2
     */
    public class Order {

        @SerializedName("Order")
        private String order;

        @SerializedName("id")
        private int id;

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
         * Gets order.
         *
         * @return the order
         */
        public String getOrder() {
            return order;
        }

        /**
         * Sets order.
         *
         * @param order the order
         */
        public void setOrder(String order) {
            this.order = order;
        }
    }


}


