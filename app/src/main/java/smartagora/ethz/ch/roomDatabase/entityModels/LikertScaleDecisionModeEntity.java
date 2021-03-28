package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * @Entity Declaring the LikertScaleDecisionMode Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "LikertScaleDecisionMode",
        foreignKeys = {

                /*
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id , assignmentId from the parent Table
                 *  childColumns specify Foreign Keys in the Table
                 */
                @ForeignKey(entity = LikertScaleEntity.class,
                        parentColumns = {"id"},
                        childColumns = {"likertScaleId"},
                        onDelete = ForeignKey.CASCADE)
        },

        /*
         * @Index index a column for faster searching of it.
         * questionId and assignmentId are the composite foreign keys in Table
        */
        indices = {@Index(value = {"likertScaleId"})}

)


public class LikertScaleDecisionModeEntity {
    /**
     * LikertScaleDecisionMode Entity class ia a modal that represents the LikertScaleDecisionMode Table
     * It is used to create a table in Room Persistent Database.
     *
     * @ColumnInfo Specify the custom name of column in Table
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "likertScaleId")
    private int likertScaleId;
    @ColumnInfo(name = "value")
    private int value;
    @ColumnInfo(name = "nextQuestionId")
    private int nextQuestionId;
    @ColumnInfo(name = "nextAssignmentId")
    private String nextAssignmentId;
    @ColumnInfo(name = "credits")
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
     * Gets likert scale id.
     *
     * @return the likert scale id
     */
    public int getLikertScaleId() {
        return likertScaleId;
    }

    /**
     * Sets likert scale id.
     *
     * @param likertScaleId the likert scale id
     */
    public void setLikertScaleId(int likertScaleId) {
        this.likertScaleId = likertScaleId;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
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
