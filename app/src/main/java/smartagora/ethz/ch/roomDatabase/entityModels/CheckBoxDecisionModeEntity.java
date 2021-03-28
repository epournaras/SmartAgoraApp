package smartagora.ethz.ch.roomDatabase.entityModels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @Entity Declaring the Assignment Entity class as a Room Entity
 * Specify the custom name of Table in Room Database by using tableName attribute
 * Specify the foreign keys in Table by using the foreignKeys attribute
 * Specify the indices in Table by using the indices attribute
 */
@Entity(tableName = "CheckBoxDecisionMode",
        foreignKeys = {

                /*
                 *  @ForeignKey creates a foreign key in Table
                 *  entity specify the Entity class of parent Table
                 *  parentColumns specify the id from the parent Table
                 *  childColumns specify Foreign Keys in the Table
                 */
                @ForeignKey(entity = CombinationEntity.class,
                        parentColumns = "id",
                        childColumns = "combinationId",
                        onDelete = ForeignKey.CASCADE)
        },

        /*
         * @Index index a column for faster searching of it.
         * checkBoxId are the composite foreign keys in Table
        */
        indices = {
                @Index(value = "combinationId")

        }
)



public class CheckBoxDecisionModeEntity {
    /**
     * CheckBoxDecisionMode Entity class ia a modal that represents the CheckBoxDecisionMode Table
     * It is used to create a table in Room Persistent Database.
     * @ColumnInfo Specify the custom name of column in Table
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "combinationId")
    private String combinationId;
    @ColumnInfo(name = "nextQuestionId")
    private int nextQuestionId;
    @ColumnInfo(name = "nextAssignmentId")
    private String nextAssignmentId;
    @ColumnInfo(name = "credits")
    private String credits;


    /**
     * Instantiates a new Check box decision mode entity.
     */
    public CheckBoxDecisionModeEntity() {
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
     * Gets combinationId.
     *
     * @return the combinationId
     */

    public String getCombinationId() {
        return combinationId;
    }

    /**
     * Sets combinationId.
     *
     * @param combinationId the id
     */
    public void setCombinationId(String combinationId) {
        this.combinationId = combinationId;
    }

    /**
     * Gets nextQuestionId.
     *
     * @return the nextQuestionId
     */

    public int getNextQuestionId() {
        return nextQuestionId;
    }

    /**
     * Sets nextQuestionId
     *
     * @param nextQuestionId the id
     */
    public void setNextQuestionId(int nextQuestionId) {
        this.nextQuestionId = nextQuestionId;
    }

    /**
     * Gets nextAssignmentId.
     *
     * @return the nextAssignmentId
     */

    public String getNextAssignmentId() {
        return nextAssignmentId;
    }

    /**
     * Sets nextAssignmentId
     *
     * @param nextAssignmentId the id
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
