package smartagora.ethz.ch.roomDatabase.entityModels;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;


/**
 * @Entity Declaring the Question Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "Question",

        /*
         * Specify the composite primary key in Table by using the primaryKeys attribute
         * id , AssignmentId are Composite Keys
        */
        primaryKeys = {"id", "assignmentId"},

        /*
         *  Specify the foreign keys in Table by using the foreignKeys attribute
         *  @ForeignKey creates a foreign key in Table
         *  entity specify the Entity class of parent Table
         *  parentColumns specify the id from the parent Table
         *  childColumns specify Foreign Key in the Table
        */

        foreignKeys = @ForeignKey(entity = AssignmentEntity.class,
                parentColumns = "id",
                childColumns = "assignmentId",
                onDelete = ForeignKey.CASCADE),

        /*
         * @Index index a column for faster searching of it.
         * id and assignmentId are the composite primary key in Table
        */

        indices = {@Index(value = "assignmentId"), @Index(value = ("id")),
                @Index(value = {"id", "assignmentId"}, unique = true)
        }
)



public class QuestionEntity {
    /**
     * Question Entity class ia a modal that represents the Question Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */


    @ColumnInfo(name = "id")
    private int id;
    @NonNull
    @ColumnInfo(name = "assignmentId")
    private String assignmentId = "";
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "question")
    private String question;
    @ColumnInfo(name = "latitude")
    private String latitude;
    @ColumnInfo(name = "longitude")
    private String longitude;
    @ColumnInfo(name = "frequency")
    private String frequency;
    @ColumnInfo(name = "mandatory")
    private String mandatory;
    @ColumnInfo(name = "visibility")
    private String visibility;
    @ColumnInfo(name = "sequence")
    private String sequence;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "vicinity")
    private String vicinity;


    /**
     * Instantiates a new Question entity.
     */
    public QuestionEntity() {
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
     * Gets assignment id.
     *
     * @return the assignment id
     */
    @NonNull
    public String getAssignmentId() {
        return assignmentId;
    }

    /**
     * Sets assignment id.
     *
     * @param assignmentId the assignment id
     */
    public void setAssignmentId(@NonNull String assignmentId) {
        this.assignmentId = assignmentId;
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
