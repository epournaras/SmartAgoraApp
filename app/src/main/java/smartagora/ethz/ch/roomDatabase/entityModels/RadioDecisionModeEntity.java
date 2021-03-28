package smartagora.ethz.ch.roomDatabase.entityModels;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Entity Declaring the RadioDecisionMode Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "RadioDecisionMode",
        foreignKeys = {

                /*
                   @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id from the parent Table
                 *  childColumns specify Foreign Key in the Table
                 */
                @ForeignKey(entity = RadioButtonEntity.class,
                        parentColumns = "id",
                        childColumns = "radioId",
                        onDelete = ForeignKey.CASCADE)
        },

        /*
         * @Index index a column for faster searching of it.
         * radioId is a foreign key in Table
        */
        indices = @Index(value = "radioId")
)


public class RadioDecisionModeEntity {
    /**
     * RadioDecisionMode Entity class ia a modal that represents the RadioDecisionMode Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "radioId")
    private int radioId;
    @ColumnInfo(name = "nextQuestionId")
    private int nextQuestionId;
    @ColumnInfo(name = "nextAssignmentId")
    private String nextAssignmentId;
    @ColumnInfo(name = "credits")
    private String credits;


    /**
     * Instantiates a new Radio decision mode entity.
     */
    public RadioDecisionModeEntity() {
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
     * Gets radio id.
     *
     * @return the radio id
     */
    public int getRadioId() {
        return radioId;
    }

    /**
     * Sets radio id.
     *
     * @param radioId the radio id
     */
    public void setRadioId(int radioId) {
        this.radioId = radioId;
    }


    /**
     * Gets next question id.
     *
     * @return the next question id
     */
    public int getNextQuestionId() {
        return nextQuestionId;
    }

    /**
     * Sets next question id.
     *
     * @param nextQuestionId the next question id
     */
    public void setNextQuestionId(int nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }

    /**
     * Gets next assignment id.
     *
     * @return the next assignment id
     */
    public String getNextAssignmentId() {
        return nextAssignmentId;
    }

    /**
     * Sets next assignment id.
     *
     * @param nextAssignmentId the next assignment id
     */
    public void setNextAssignmentId(String nextAssignmentId) {
        this.nextAssignmentId = nextAssignmentId;
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
