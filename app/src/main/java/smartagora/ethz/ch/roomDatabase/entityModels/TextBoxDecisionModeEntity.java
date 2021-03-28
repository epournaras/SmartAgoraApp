package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;


/**
 * @Entity Declaring the TextBoxDecisionMode Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "TextBoxDecisionMode",
        foreignKeys = {

                /*
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id , assignmentId from the parent Table
                 *  childColumns specify Foreign Keys in the Table
                 */
                @ForeignKey(entity = TextBoxEntity.class,
                        parentColumns = {"id"},
                        childColumns = {"textBoxId"},
                        onDelete = ForeignKey.CASCADE)
        },

        /*
          @Index index a column for faster searching of it.
         * questionId and assignmentId are the composite foreign keys in Table
        */
        indices = {@Index(value = {"textBoxId"})}

)


public class TextBoxDecisionModeEntity {
    /**
     * TextBoxDecisionMode Entity class ia a modal that represents the TextBoxDecisionMode Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "textBoxId")
    private int textBoxId;
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
     * Gets text box id.
     *
     * @return the text box id
     */
    public int getTextBoxId() {
        return textBoxId;
    }

    /**
     * Sets text box id.
     *
     * @param textBoxId the text box id
     */
    public void setTextBoxId(int textBoxId) {
        this.textBoxId = textBoxId;
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
