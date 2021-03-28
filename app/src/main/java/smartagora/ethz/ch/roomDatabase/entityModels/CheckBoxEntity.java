package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Entity Declaring the CheckBox Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "CheckBox",
        foreignKeys = {
                /*
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify source keys from the parent Table
                 *  childColumns specify Foreign Keys in the Table
                 */

                @ForeignKey(entity = QuestionEntity.class,
                        parentColumns = {"id", "assignmentId"},
                        childColumns = {"questionId", "assignmentId"},
                        onDelete = ForeignKey.CASCADE)
        },

        /*
         * @Index index a column for faster searching of it.
         * questionId and assignmentId are the composite foreign keys in Table
        */
        indices = {
                @Index(value = {"questionId", "assignmentId"})
        }
)


public class CheckBoxEntity {
    /**
     * CheckBox Entity class ia a modal that represents the CheckBox Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "questionId")
    private int questionId;
    @ColumnInfo(name = "assignmentId")
    private String assignmentId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "credits")
    private String credits;


    /**
     * Instantiates a new Check box entity.
     */
    public CheckBoxEntity() {
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
