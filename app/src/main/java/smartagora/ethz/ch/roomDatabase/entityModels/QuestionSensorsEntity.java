package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * @Entity Declaring the QuestionSensors Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@SuppressWarnings("ALL")
@Entity(tableName = "QuestionSensors",
        foreignKeys = {

                /**
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the source keys from the parent Table
                 *  childColumns specify Foreign Key in the Table
                 */
                @ForeignKey(entity = SensorEntity.class,
                        parentColumns = "id",
                        childColumns = "sensorId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = QuestionEntity.class,
                        parentColumns = {"id", "assignmentId"},
                        childColumns = {"questionId", "assignmentId"},
                        onDelete = ForeignKey.CASCADE)
        },

        /**
         * @Index index a column for faster searching of it.
         * questionId and assignmentId are the composite foreign keys in Table
         * sensorId is a foreign key in Table
        */
        indices = {@Index(value = "sensorId"), @Index(value = {"questionId", "assignmentId"})}

)
/**
 * QuestionSensors Entity class ia a modal that represents the QuestionSensors Table
 * It is used to create a table in Room Persistent Database.
 * @ColumnInfo Specify the custom name of column in Table
 */

public class QuestionSensorsEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "sensorId")
    private int sensorId;
    @ColumnInfo(name = "questionId")
    private int questionId;
    @ColumnInfo(name = "assignmentId")
    private String assignmentId;

    /**
     * Instantiates a new Question sensors entity.
     */
    public QuestionSensorsEntity() {
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
     * Gets sensor id.
     *
     * @return the sensor id
     */
    public int getSensorId() {
        return sensorId;
    }

    /**
     * Sets sensor id.
     *
     * @param sensorId the sensor id
     */
    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    /**
     * Gets question id.
     *
     * @return the question id
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * Sets question id.
     *
     * @param questionId the question id
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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

}

